package net.miarma.contaminus.database.models;

import java.sql.Timestamp;

public class Sensor {
    private int id;
    private String sensorType;
    private float value;
    private float lat;
    private float lon;
    private Timestamp timestamp;

    public Sensor() {}

    public Sensor(int id, String sensorType, float value, float lat, float lon, Timestamp timestamp) {
        this.id = id;
        this.sensorType = sensorType;
        this.value = value;
        this.lat = lat;
        this.lon = lon;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", sensorType='" + sensorType + '\'' +
                ", value=" + value +
                ", lat=" + lat +
                ", lon=" + lon +
                ", timestamp=" + timestamp +
                '}';
    }
}
