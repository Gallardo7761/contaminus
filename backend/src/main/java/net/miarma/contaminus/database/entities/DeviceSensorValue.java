package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_sensor_values")
public class DeviceSensorValue {
	private Integer sensorId;
	private Integer deviceId;
	private String sensorType;
	private String unit;
	private Integer sensorStatus;
	private Float temperature;
	private Float humidity;
	private Float carbonMonoxide;
	private Float lat;
	private Float lon;
	private Long timestamp;
	
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

	public DeviceSensorValue(Integer sensorId, Integer deviceId, String sensorType, String unit, Integer sensorStatus,
			Float temperature, Float humidity, Float carbonMonoxide, Float lat, Float lon, Long timestamp) {
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

	public Integer getSensorId() {
		return sensorId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public String getUnit() {
		return unit;
	}

	public Integer getSensorStatus() {
		return sensorStatus;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

	public Float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public Float getLat() {
		return lat;
	}

	public Float getLon() {
		return lon;
	}

	public Long getTimestamp() {
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
		return Objects.equals(carbonMonoxide, other.carbonMonoxide) && Objects.equals(deviceId, other.deviceId)
				&& Objects.equals(humidity, other.humidity) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(sensorId, other.sensorId)
				&& Objects.equals(sensorStatus, other.sensorStatus) && Objects.equals(sensorType, other.sensorType)
				&& Objects.equals(temperature, other.temperature) && Objects.equals(timestamp, other.timestamp)
				&& Objects.equals(unit, other.unit);
	}

	@Override
	public String toString() {
		return "DeviceSensorValue [sensorId=" + sensorId + ", deviceId=" + deviceId + ", sensorType=" + sensorType
				+ ", unit=" + unit + ", sensorStatus=" + sensorStatus + ", temperature=" + temperature + ", humidity="
				+ humidity + ", carbonMonoxide=" + carbonMonoxide + ", lat=" + lat + ", lon=" + lon + ", timestamp="
				+ timestamp + "]";
	}

	
}
