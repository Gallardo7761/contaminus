package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_sensor_values")
public class DeviceSensorValue {
	int sensorId;
	int deviceId;
	String sensorType;
	String unit;
	int sensorStatus;
	float temperature;
	float humidity;
	float carbonMonoxide;
	float lat;
	float lon;
	long timestamp;
	
	public DeviceSensorValue() {}

	public DeviceSensorValue(Row row) {
		this.sensorId = row.getInteger("sensorId");
		this.deviceId = row.getInteger("deviceId");
		this.sensorType = row.getString("sensorType");
		this.unit = row.getString("unit");
		this.sensorStatus = row.getInteger("sensorStatus");
		this.temperature = row.getFloat("temperature");
		this.humidity = row.getFloat("humidty");
		this.carbonMonoxide = row.getFloat("carbonMonoxide");
		this.lat = row.getFloat("lat");
		this.lon = row.getFloat("lon");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DeviceSensorValue(int sensorId, int deviceId, String sensorType, String unit, int sensorStatus,
			float temperature, float humidity, float carbonMonoxide, float lat, float lon, long timestamp) {
		super();
		this.sensorId = sensorId;
		this.deviceId = deviceId;
		this.sensorType = sensorType;
		this.unit = unit;
		this.sensorStatus = sensorStatus;
		this.temperature = temperature;
		this.humidity = humidity;
		this.carbonMonoxide = carbonMonoxide;
		this.lat = lat;
		this.lon = lon;
		this.timestamp = timestamp;
	}

	public int getSensorId() {
		return sensorId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public String getUnit() {
		return unit;
	}

	public int getSensorStatus() {
		return sensorStatus;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carbonMonoxide, deviceId, humidity, lat, lon, sensorId, sensorStatus, sensorType,
				temperature, timestamp, unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceSensorValue other = (DeviceSensorValue) obj;
		return Float.floatToIntBits(carbonMonoxide) == Float.floatToIntBits(other.carbonMonoxide)
				&& deviceId == other.deviceId && Float.floatToIntBits(humidity) == Float.floatToIntBits(other.humidity)
				&& Float.floatToIntBits(lat) == Float.floatToIntBits(other.lat)
				&& Float.floatToIntBits(lon) == Float.floatToIntBits(other.lon) && sensorId == other.sensorId
				&& sensorStatus == other.sensorStatus && Objects.equals(sensorType, other.sensorType)
				&& Float.floatToIntBits(temperature) == Float.floatToIntBits(other.temperature)
				&& timestamp == other.timestamp && Objects.equals(unit, other.unit);
	}

	@Override
	public String toString() {
		return "DeviceSensorValue [sensorId=" + sensorId + ", deviceId=" + deviceId + ", sensorType=" + sensorType
				+ ", unit=" + unit + ", sensorStatus=" + sensorStatus + ", temperature=" + temperature + ", humidity="
				+ humidity + ", carbonMonoxide=" + carbonMonoxide + ", lat=" + lat + ", lon=" + lon + ", timestamp="
				+ timestamp + "]";
	}
}
