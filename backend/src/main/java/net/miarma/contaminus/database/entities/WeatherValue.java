package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("weather_values")
public class WeatherValue {

    private int valueId;
    private int sensorId;
    private float temperature;
    private float humidity;
    private long timestamp;

    public WeatherValue() {}
    
    public WeatherValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.sensorId = row.getInteger("sensorId");
        this.temperature = row.getFloat("temperature");
        this.humidity = row.getFloat("humidity");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public WeatherValue(int valueId, int sensorId, float temperature, float humidity, long timestamp) {
		super();
		this.valueId = valueId;
		this.sensorId = sensorId;
		this.temperature = temperature;
		this.humidity = humidity;
		this.timestamp = timestamp;
	}

	public int getValueId() {
		return valueId;
	}

	public void setValueId(int valueId) {
		this.valueId = valueId;
	}

	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public float getTemperature() {
		return temperature;
	}

	public void setTemperature(float temperature) {
		this.temperature = temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public void setHumidity(float humidity) {
		this.humidity = humidity;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
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
		return Float.floatToIntBits(humidity) == Float.floatToIntBits(other.humidity) && sensorId == other.sensorId
				&& Float.floatToIntBits(temperature) == Float.floatToIntBits(other.temperature)
				&& timestamp == other.timestamp && valueId == other.valueId;
	}

	@Override
	public String toString() {
		return "WeatherValue [valueId=" + valueId + ", sensorId=" + sensorId + ", temperature=" + temperature
				+ ", humidity=" + humidity + ", timestamp=" + timestamp + "]";
	}

	
    
}