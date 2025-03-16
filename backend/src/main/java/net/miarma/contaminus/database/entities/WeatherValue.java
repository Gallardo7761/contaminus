package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("weather_values")
public class WeatherValue {

    private Integer valueId;
    private Integer sensorId;
    private Float temperature;
    private Float humidity;
    private Long timestamp;

    public WeatherValue() {}
    
    public WeatherValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.sensorId = row.getInteger("sensorId");
        this.temperature = row.getFloat("temperature");
        this.humidity = row.getFloat("humidity");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public WeatherValue(Integer valueId, Integer sensorId, Float temperature, Float humidity, Long timestamp) {
		super();
		this.valueId = valueId;
		this.sensorId = sensorId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.timestamp = timestamp;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
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

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(humidity, sensorId, temperature, timestamp, valueId);
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
		return Objects.equals(humidity, other.humidity) && Objects.equals(sensorId, other.sensorId)
				&& Objects.equals(temperature, other.temperature) && Objects.equals(timestamp, other.timestamp)
				&& Objects.equals(valueId, other.valueId);
	}

	@Override
	public String toString() {
		return "WeatherValue [valueId=" + valueId + ", sensorId=" + sensorId + ", temperature=" + temperature
				+ ", humidity=" + humidity + ", timestamp=" + timestamp + "]";
	}

		
    
}