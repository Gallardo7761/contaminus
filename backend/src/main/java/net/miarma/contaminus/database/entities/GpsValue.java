package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("gps_values")
public class GpsValue {

    private int valueId;
    private int sensorId;
    private float lat;
    private float lon;
    private long timestamp;

    public GpsValue() {}
    
    public GpsValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.sensorId = row.getInteger("sensorId");
        this.lat = row.getFloat("lat");
        this.lon = row.getFloat("lon");
        this.timestamp = row.getLong("timestamp");
    }

	public GpsValue(int valueId, int sensorId, float lat, float lon, long timestamp) {
		super();
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
		return Objects.hash(lat, lon, sensorId, timestamp, valueId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GpsValue other = (GpsValue) obj;
		return Float.floatToIntBits(lat) == Float.floatToIntBits(other.lat)
				&& Float.floatToIntBits(lon) == Float.floatToIntBits(other.lon)
				&& sensorId == other.sensorId && timestamp == other.timestamp && valueId == other.valueId;
	}

	@Override
	public String toString() {
		return "GpsValue [valueId=" + valueId + ", sensorId=" + sensorId + ", lat=" + lat + ", lon=" + lon
				+ ", timestamp=" + timestamp + "]";
	}

	
    
}