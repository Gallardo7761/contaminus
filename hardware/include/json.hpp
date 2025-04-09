#include <ArduinoJson.h>

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
    
);

String serializeDevice (

);

void deserializeSensorValue (

);

