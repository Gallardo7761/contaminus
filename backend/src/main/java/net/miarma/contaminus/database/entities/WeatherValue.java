package net.miarma.contaminus.database.entities;

public class WeatherValue {
	
	private int valueId;
	private int sensorId;
	private float temperature;
	private float humidity;
	private long timestamp;
	
	public WeatherValue(int valueId, int sensorId, float temperature, float humidity, long timestamp) {
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
		final int prime = 31;
		int result = 1;
		result = prime * result + Float.floatToIntBits(humidity);
		result = prime * result + sensorId;
		result = prime * result + Float.floatToIntBits(temperature);
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + valueId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		WeatherValue other = (WeatherValue) obj;
		if (Float.floatToIntBits(humidity) != Float.floatToIntBits(other.humidity)) {
			return false;
		}
		if (sensorId != other.sensorId) {
			return false;
		}
		if (Float.floatToIntBits(temperature) != Float.floatToIntBits(other.temperature)) {
			return false;
		}
		if (timestamp != other.timestamp) {
			return false;
		}
		if (valueId != other.valueId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "WeatherValue [valueId=" + valueId + ", sensorId=" + sensorId + ", temperature=" + temperature
				+ ", humidity=" + humidity + ", timestamp=" + timestamp + "]";
	}

}
