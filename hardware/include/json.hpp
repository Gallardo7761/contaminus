#include <ArduinoJson.h>
#include <HTTPClient.h>

String serializeSensorValue (
    int sensorId,
    int deviceId,
    String sensorType,
    String unit, 
    int sensorStatus,
    float temperature,
    float humidity,
    float carbonMonoxide,
    float lat,
    float lon,
    long timestamp
);

String serializeActuatorStatus (
    int actuatorId, 
    int deviceId, 
    int status, 
    long timestamp
);

String serializeDevice (
    int sensorId, 
    int deviceId, 
    String sensorType, 
    int status, 
    long timestamp
);

void deserializeSensorValue (
    HTTPClient* http,
    int httpResponseCode
);

void deserializeActuatorStatus (
    HTTPClient* http,
    int httpResponseCode
);

void deserializeDevice (
    HTTPClient* http,
    int httpResponseCode
);