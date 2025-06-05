#include "main.hpp"

const uint32_t DEVICE_ID = getChipID();
const String mqttId = "CUS-" + String(DEVICE_ID, HEX);
int GROUP_ID = -1;

TaskTimer_t globalTimer{0, 30000};
TaskTimer_t mqttTimer{0, 2000};

#if DEVICE_ROLE == ACTUATOR
TaskTimer_t matrixTimer{0, 25};
TaskTimer_t displayTimer{0, 1000};
String currentMessage = "";
String lastMessage = "";
extern MD_Parola display;
#endif

extern HTTPClient httpClient;
String response;

#if DEVICE_ROLE == SENSOR
MQ7Data_t mq7Data;
BME280Data_t bme280Data;
GPSData_t gpsData;
#endif

static bool mqttStarted = false;

void setup()
{
    Serial.begin(115200);

#ifdef DEBUG
    Serial.println("Starting...");
#endif

    WiFi_Init();

    try
    {

#if DEVICE_ROLE == SENSOR
        BME280_Init();
        Serial.println("BME280 initialized");
        GPS_Init();
        Serial.println("GPS initialized");
        MQ7_Init();
        Serial.println("MQ7 initialized");
#endif

#if DEVICE_ROLE == ACTUATOR
        MAX7219_Init();
        Serial.println("Display initialized");
        writeMatrix(currentMessage.c_str());
#endif
    }
    catch (const char *e)
    {
        Serial.println(e);
    }
}

void loop()
{
    WiFi_Handle();

    if (!WiFi_IsConnected())
    {
        return;
    }

    if (GROUP_ID == -1)
    {
        GROUP_ID = getGroupId(DEVICE_ID);
    }

    if (!mqttStarted && GROUP_ID != -1)
    {
        MQTT_Init(MQTT_URI, MQTT_PORT);
        mqttStarted = true;
    }

#if DEVICE_ROLE == ACTUATOR
    if (currentMessage.isEmpty() && GROUP_ID != -1)
    {
        String url = String(API_URI) + "groups/" + GROUP_ID + "/devices/" + String(DEVICE_ID, HEX) + "/actuators/" + MAX7219_ID + "/status";
        getRequest(url, response);
        MAX7219Status_t statusData = deserializeActuatorStatus(httpClient, httpClient.GET());
        currentMessage = statusData.actuatorStatus;
    }
#endif

    uint32_t now = millis();

#if DEVICE_ROLE == ACTUATOR
    if (now - matrixTimer.lastRun >= matrixTimer.interval)
    {
        if (MAX7219_Animate())
        {
            MAX7219_ResetAnimation();
        }
        matrixTimer.lastRun = now;
    }

    if (now - displayTimer.lastRun >= displayTimer.interval)
    {
        if (currentMessage != lastMessage)
        {
            writeMatrix(currentMessage.c_str());
            lastMessage = currentMessage;
#ifdef DEBUG
            Serial.println("Display has been updated");
#endif
        }
        displayTimer.lastRun = now;
    }
#endif

    if (now - globalTimer.lastRun >= globalTimer.interval)
    {

#if DEVICE_ROLE == SENSOR
        readBME280();
        readGPS();
        readMQ7();

#ifdef DEBUG
        printAllData();
#endif

        sendSensorData();
#endif

        globalTimer.lastRun = now;
    }

    if (now - mqttTimer.lastRun >= mqttTimer.interval)
    {
        MQTT_Handle(mqttId.c_str());
        mqttTimer.lastRun = now;
    }
}

#if DEVICE_ROLE == ACTUATOR
void writeMatrix(const char *message)
{
#ifdef DEBUG
    Serial.print("Writing message: ");
    Serial.println(message);
#endif
    MAX7219_DisplayText(message, PA_LEFT, 50, 0);
}
#endif

#if DEVICE_ROLE == SENSOR
void readMQ7()
{
    const float CO_THRESHOLD = 100.0f;
    mq7Data = MQ7_Read_Fake();
}

void readBME280()
{
    bme280Data = BME280_Read();
}

void readGPS()
{
    gpsData = GPS_Read_Fake();
}

void printAllData()
{
    Serial.println("---------------------");
    Serial.println("📦 Measured batch:");
    Serial.println("---------------------");

    Serial.print("ID: ");
    Serial.println(DEVICE_ID, HEX);

    Serial.print("Pressure: ");
    Serial.print(bme280Data.pressure);
    Serial.println(" hPa");
    Serial.print("Temperature: ");
    Serial.print(bme280Data.temperature);
    Serial.println(" °C");
    Serial.print("Humidity: ");
    Serial.print(bme280Data.humidity);
    Serial.println(" %");

    Serial.print("Latitude: ");
    Serial.println(gpsData.lat);
    Serial.print("Longitude: ");
    Serial.println(gpsData.lon);

    Serial.print("CO: ");
    Serial.println(mq7Data.co);
    Serial.print("D0: ");
    Serial.println(mq7Data.threshold);

    Serial.println("---------------------");
}

void sendSensorData()
{
    const String deviceId = String(DEVICE_ID, HEX);

    bool gpsValid = gpsData.lat != 0.0f && gpsData.lon != 0.0f;
    bool weatherValid = bme280Data.temperature != 0.0f &&
                        bme280Data.humidity != 0.0f &&
                        bme280Data.pressure != 0.0f;
    bool coValid = mq7Data.co >= 0.0f;

    if (!gpsValid || !weatherValid || !coValid)
    {
#ifdef DEBUG
        Serial.println("❌ Invalid batch. Won't be stored.");
#endif
        return;
    }

    String json = serializeSensorValue(GROUP_ID, deviceId,
                                       GPS_ID, BME280_ID, MQ7_ID,
                                       bme280Data, mq7Data, gpsData);

#ifdef DEBUG
    Serial.println("📤 Sending batch to server...");
#endif

    postRequest(String(API_URI) + "/batch", json, response);

#ifdef DEBUG
    Serial.println("📥 Server response:");
    Serial.println(response);
#endif
}
#endif

uint32_t getChipID()
{
    uint32_t chipId = 0;
    for (int i = 0; i < 17; i += 8)
    {
        chipId |= ((ESP.getEfuseMac() >> (40 - i)) & 0xff) << i;
    }
#ifdef DEBUG
    Serial.print("Chip ID: ");
    Serial.println(chipId, HEX);
#endif
    return chipId;
}

int getGroupId(int deviceId)
{
    String url = String(RAW_API_URI) + "devices/" + String(DEVICE_ID, HEX) + "/my-group";
    getRequest(url, response);
    return deserializeGroupId(httpClient, httpClient.GET());
}