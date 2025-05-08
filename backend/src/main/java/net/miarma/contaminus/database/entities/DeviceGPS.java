package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_gps_by_device")
public class DeviceGPS {
	private String deviceId;
	private Float lat;
	private Float lon;
	private Long timestamp;
	
	public DeviceGPS() {}
	
	public DeviceGPS(Row row) {
		this.deviceId = row.getString("deviceId");
		this.lat = row.getFloat("lat");
		this.lon = row.getFloat("lon");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DeviceGPS(String deviceId, Float lat, Float lon) {
		super();
		this.deviceId = deviceId;
		this.lat = lat;
		this.lon = lon;
	}

	public String getDeviceId() {
		return deviceId;
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
		return Objects.equals(deviceId, other.deviceId) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "DeviceGPS [deviceId=" + deviceId + ", lat=" + lat + ", lon=" + lon + ", timestamp=" + timestamp + "]";
	}

	
}
