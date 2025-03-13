package net.miarma.contaminus.database.entities;

public class GpsValue {
	
	private int valueId;
	private int sensorId;
	private float lat;
	private float lon;
	private long timestamp;
	
	public GpsValue(int valueId, int sensorId, float lat, float lon, long timestamp) {
		this.valueId = valueId;
		this.sensorId = sensorId;
		this.lat = lat;
		this.lon = lon;
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

	public float getLat() {
		return lat;
	}

	public void setLat(float lat) {
		this.lat = lat;
	}

	public float getLon() {
		return lon;
	}

	public void setLon(float lon) {
		this.lon = lon;
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
		result = prime * result + Float.floatToIntBits(lat);
		result = prime * result + Float.floatToIntBits(lon);
		result = prime * result + sensorId;
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
		GpsValue other = (GpsValue) obj;
		if (Float.floatToIntBits(lat) != Float.floatToIntBits(other.lat)) {
			return false;
		}
		if (Float.floatToIntBits(lon) != Float.floatToIntBits(other.lon)) {
			return false;
		}
		if (sensorId != other.sensorId) {
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
		return "GpsValue [valueId=" + valueId + ", sensorId=" + sensorId + ", lat=" + lat + ", lon=" + lon
				+ ", timestamp=" + timestamp + "]";
	}

}
