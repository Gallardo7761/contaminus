package net.miarma.contaminus.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_sensor_values")
public class ViewSensorValue {
	private Integer sensorId;
	private String deviceId;
	private String sensorType;
	private String unit;
	private Integer sensorStatus;
	private Float temperature;
	private Float humidity;
	private Float pressure;
	private Float carbonMonoxide;
	private Float lat;
	private Float lon;
	private Long timestamp;
	
	public ViewSensorValue() {}

	public ViewSensorValue(Row row) {
		this.sensorId = row.getInteger("sensorId");
		this.deviceId = row.getString("deviceId");
		this.sensorType = row.getString("sensorType");
		this.unit = row.getString("unit");
		this.sensorStatus = row.getInteger("sensorStatus");
		this.temperature = row.getFloat("temperature");
		this.humidity = row.getFloat("humidity");
		this.pressure = row.getFloat("pressure");
		this.carbonMonoxide = row.getFloat("carbonMonoxide");
		this.lat = row.getFloat("lat");
		this.lon = row.getFloat("lon");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public ViewSensorValue(Integer sensorId, String deviceId, String sensorType, String unit, Integer sensorStatus,
			Float temperature, Float humidity, Float pressure, Float carbonMonoxide, Float lat, Float lon, Long timestamp) {
		super();
		this.sensorId = sensorId;
		this.deviceId = deviceId;
		this.sensorType = sensorType;
		this.unit = unit;
		this.sensorStatus = sensorStatus;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.carbonMonoxide = carbonMonoxide;
		this.lat = lat;
		this.lon = lon;
		this.timestamp = timestamp;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public String getDeviceId() {
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
	
	public Float getPressure() {
		return pressure;
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
	
	

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setSensorStatus(Integer sensorStatus) {
		this.sensorStatus = sensorStatus;
	}

	public void setTemperature(Float temperature) {
		this.temperature = temperature;
	}

	public void setHumidity(Float humidity) {
		this.humidity = humidity;
	}
	
	public void setPressure(Float pressure) {
		this.pressure = pressure;
	}

	public void setCarbonMonoxide(Float carbonMonoxide) {
		this.carbonMonoxide = carbonMonoxide;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carbonMonoxide, deviceId, humidity, lat, lon, pressure, sensorId, sensorStatus, sensorType,
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
		ViewSensorValue other = (ViewSensorValue) obj;
		return Objects.equals(carbonMonoxide, other.carbonMonoxide) && Objects.equals(deviceId, other.deviceId)
				&& Objects.equals(humidity, other.humidity) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(pressure, other.pressure)
				&& Objects.equals(sensorId, other.sensorId) && Objects.equals(sensorStatus, other.sensorStatus)
				&& Objects.equals(sensorType, other.sensorType) && Objects.equals(temperature, other.temperature)
				&& Objects.equals(timestamp, other.timestamp) && Objects.equals(unit, other.unit);
	}

	@Override
	public String toString() {
		return "ViewSensorValue [sensorId=" + sensorId + ", deviceId=" + deviceId + ", sensorType=" + sensorType
				+ ", unit=" + unit + ", sensorStatus=" + sensorStatus + ", temperature=" + temperature + ", humidity="
				+ humidity + ", pressure=" + pressure + ", carbonMonoxide=" + carbonMonoxide + ", lat=" + lat + ", lon="
				+ lon + ", timestamp=" + timestamp + "]";
	}

	
	
}
