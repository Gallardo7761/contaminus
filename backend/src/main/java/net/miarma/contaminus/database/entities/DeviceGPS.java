package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("v_gps_by_device")
public class DeviceGPS {
	int deviceId;
	float lat;
	float lon;
	long timestamp;
	
	public DeviceGPS() {}
	
	public DeviceGPS(Row row) {
		this.deviceId = row.getInteger("deviceId");
		this.lat = row.getFloat("lat");
		this.lon = row.getFloat("lon");
		this.timestamp = row.getLong("timestamp");
	}

	public DeviceGPS(int deviceId, long lat, long lon) {
		super();
		this.deviceId = deviceId;
		this.lat = lat;
		this.lon = lon;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}
	
	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId, lat, lon, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceGPS other = (DeviceGPS) obj;
		return deviceId == other.deviceId && Float.floatToIntBits(lat) == Float.floatToIntBits(other.lat)
				&& Float.floatToIntBits(lon) == Float.floatToIntBits(other.lon) && timestamp == other.timestamp;
	}

	@Override
	public String toString() {
		return "DeviceGPS [deviceId=" + deviceId + ", lat=" + lat + ", lon=" + lon + ", timestamp=" + timestamp + "]";
	}
}
