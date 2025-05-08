package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_pollution_map")
public class DevicePollutionMap {
	private String deviceId;
	private String deviceName;
	private Float lat;
	private Float lon;
	private Float carbonMonoxide;
	private Long timestamp;
	
	public DevicePollutionMap() {}
	
	public DevicePollutionMap(Row row) {
		this.deviceId = row.getString("deviceId");
		this.deviceName = row.getString("deviceName");
		this.lat = row.getFloat("lat");
		this.lon = row.getFloat("lon");
		this.carbonMonoxide = row.getFloat("carbonMonoxide");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DevicePollutionMap(String deviceId, String deviceName, Float lat, Float lon, Float carbonMonoxide,
			Long timestamp) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.lat = lat;
		this.lon = lon;
		this.carbonMonoxide = carbonMonoxide;
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public Float getLat() {
		return lat;
	}

	public Float getLon() {
		return lon;
	}

	public Float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carbonMonoxide, deviceId, deviceName, lat, lon, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DevicePollutionMap other = (DevicePollutionMap) obj;
		return Objects.equals(carbonMonoxide, other.carbonMonoxide) && Objects.equals(deviceId, other.deviceId)
				&& Objects.equals(deviceName, other.deviceName) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "DevicePollutionMap [deviceId=" + deviceId + ", deviceName=" + deviceName + ", lat=" + lat + ", lon="
				+ lon + ", carbonMonoxide=" + carbonMonoxide + ", timestamp=" + timestamp + "]";
	}

	
}