package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("weather_values")
public class WeatherValue {

    private Integer valueId;
    private String deviceId;
    private Integer sensorId;
    private Float temperature;
    private Float humidity;
    private Float pressure;
    private Long timestamp;

    public WeatherValue() {}
    
    public WeatherValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.deviceId = row.getString("deviceId");
        this.sensorId = row.getInteger("sensorId");
        this.temperature = row.getFloat("temperature");
        this.humidity = row.getFloat("humidity");
        this.pressure = row.getFloat("pressure");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public WeatherValue(Integer valueId, String deviceId, Integer sensorId, Float temperature, Float humidity, Float pressure, Long timestamp) {
		super();
		this.valueId = valueId;
		this.deviceId = deviceId;
		this.sensorId = sensorId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.pressure = pressure;
		this.timestamp = timestamp;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
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
	
	public void setPressure(Float pressure) {
		this.pressure = pressure;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId, humidity, pressure, sensorId, temperature, timestamp, valueId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WeatherValue other = (WeatherValue) obj;
		return Objects.equals(deviceId, other.deviceId) && Objects.equals(humidity, other.humidity)
				&& Objects.equals(pressure, other.pressure) && Objects.equals(sensorId, other.sensorId)
				&& Objects.equals(temperature, other.temperature) && Objects.equals(timestamp, other.timestamp)
				&& Objects.equals(valueId, other.valueId);
	}

	@Override
	public String toString() {
		return "WeatherValue [valueId=" + valueId + ", deviceId=" + deviceId + ", sensorId=" + sensorId
				+ ", temperature=" + temperature + ", humidity=" + humidity + ", pressure=" + pressure + ", timestamp="
				+ timestamp + "]";
	}

	public static WeatherValue fromPayload(DevicePayload payload) {
		return new WeatherValue(null, payload.getDeviceId(), payload.getSensorId(), payload.getTemperature(),
				payload.getHumidity(), payload.getPressure(), payload.getTimestamp());
	}
    
}