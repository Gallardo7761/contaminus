package net.miarma.contaminus.entities;

import java.util.Objects;

public class DevicePayload {
	private String deviceId;
	private Integer sensorId;
	private Float temperature;
	private Float humidity;
	private Float pressure;
	private Float carbonMonoxide;
	private Float lat;
	private Float lon;
	private Long timestamp;
	
	public DevicePayload() {}
	
	public DevicePayload(String deviceId, Integer sensorId, String sensorType, String unit, Integer sensorStatus,
			Float temperature, Float humidity, Float pressure, Float carbonMonoxide, Float lat, Float lon, Long timestamp) {
		super();
		this.deviceId = deviceId;
		this.sensorId = sensorId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.carbonMonoxide = carbonMonoxide;
		this.lat = lat;
		this.lon = lon;
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public Float getTemperature() {
		return temperature;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}
	
	public Float getPressure() {
		return pressure;
	}

	public Float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public void setCarbonMonoxide(Float carbonMonoxide) {
		this.carbonMonoxide = carbonMonoxide;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carbonMonoxide, deviceId, humidity, lat, lon, pressure, sensorId, temperature, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevicePayload other = (DevicePayload) obj;
		return Objects.equals(carbonMonoxide, other.carbonMonoxide) && Objects.equals(deviceId, other.deviceId)
				&& Objects.equals(humidity, other.humidity) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(pressure, other.pressure)
				&& Objects.equals(sensorId, other.sensorId) && Objects.equals(temperature, other.temperature)
				&& Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "DevicePayload [deviceId=" + deviceId + ", sensorId=" + sensorId + ", temperature=" + temperature
				+ ", humidity=" + humidity + ", pressure=" + pressure + ", carbonMonoxide=" + carbonMonoxide + ", lat="
				+ lat + ", lon=" + lon + ", timestamp=" + timestamp + "]";
	}

		
	
}
