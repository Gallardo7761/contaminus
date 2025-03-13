package net.miarma.contaminus.database.entities;

public class COValue {
	
	private int valueId;
	private int sensorId;
	private float value;
	private long timestamp;
	
	public COValue(int valueId, int sensorId, float value, long timestamp) {
		this.valueId = valueId;
		this.sensorId = sensorId;
		this.value = value;
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

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
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
		result = prime * result + sensorId;
		result = prime * result + (int) (timestamp ^ (timestamp >>> 32));
		result = prime * result + Float.floatToIntBits(value);
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
		COValue other = (COValue) obj;
		if (sensorId != other.sensorId) {
			return false;
		}
		if (timestamp != other.timestamp) {
			return false;
		}
		if (Float.floatToIntBits(value) != Float.floatToIntBits(other.value)) {
			return false;
		}
		if (valueId != other.valueId) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "COValue [valueId=" + valueId + ", sensorId=" + sensorId + ", value=" + value + ", timestamp="
				+ timestamp + "]";
	}

}
