package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_weather_by_device")
public class DeviceWeather {
	private String deviceId;
	private Float temperature;
	private Float humidity;
	private Long timestamp;
	
	public DeviceWeather() {}
	
	public DeviceWeather(Row row) {
		this.deviceId = row.getString("deviceId");
		this.temperature = row.getFloat("temperature");
		this.humidity = row.getFloat("humidity");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DeviceWeather(String deviceId, Float temperature, Float humidity, Long timestamp) {
		super();
		this.deviceId = deviceId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

	public Long getTimestamp() {
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
		return Objects.equals(deviceId, other.deviceId) && Objects.equals(humidity, other.humidity)
				&& Objects.equals(temperature, other.temperature) && Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "DeviceWeather [deviceId=" + deviceId + ", temperature=" + temperature + ", humidity=" + humidity
				+ ", timestamp=" + timestamp + "]";
	}

	
}