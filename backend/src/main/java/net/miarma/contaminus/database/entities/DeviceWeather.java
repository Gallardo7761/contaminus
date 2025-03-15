package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_weather_by_device")
public class DeviceWeather {
	int deviceId;
	float temperature;
	float humidity;
	long timestamp;
	
	public DeviceWeather() {}
	
	public DeviceWeather(Row row) {
		this.deviceId = row.getInteger("deviceId");
		this.temperature = row.getFloat("temperature");
		this.humidity = row.getFloat("humidity");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DeviceWeather(int deviceId, float temperature, float humidity, long timestamp) {
		super();
		this.deviceId = deviceId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.timestamp = timestamp;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId, humidity, temperature, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceWeather other = (DeviceWeather) obj;
		return deviceId == other.deviceId && Float.floatToIntBits(humidity) == Float.floatToIntBits(other.humidity)
				&& Float.floatToIntBits(temperature) == Float.floatToIntBits(other.temperature)
				&& timestamp == other.timestamp;
	}

	@Override
	public String toString() {
		return "DeviceWeather [deviceId=" + deviceId + ", temperature=" + temperature + ", humidity=" + humidity
				+ ", timestamp=" + timestamp + "]";
	}
}