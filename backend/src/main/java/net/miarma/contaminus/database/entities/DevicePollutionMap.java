package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_pollution_map")
public class DevicePollutionMap {
	int deviceId;
	String deviceName;
	float lat;
	float lon;
	float carbonMonoxide;
	long timestamp;
	
	public DevicePollutionMap() {}
	
	public DevicePollutionMap(Row row) {
		this.deviceId = row.getInteger("deviceId");
		this.deviceName = row.getString("deviceName");
		this.lat = row.getFloat("lat");
		this.lon = row.getFloat("lon");
		this.carbonMonoxide = row.getFloat("carbonMonoxide");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DevicePollutionMap(int deviceId, String deviceName, float lat, float lon, float carbonMonoxide,
			long timestamp) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.lat = lat;
		this.lon = lon;
		this.carbonMonoxide = carbonMonoxide;
		this.timestamp = timestamp;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public long getTimestamp() {
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
		return Float.floatToIntBits(carbonMonoxide) == Float.floatToIntBits(other.carbonMonoxide)
				&& deviceId == other.deviceId && Objects.equals(deviceName, other.deviceName)
				&& Float.floatToIntBits(lat) == Float.floatToIntBits(other.lat)
				&& Float.floatToIntBits(lon) == Float.floatToIntBits(other.lon) && timestamp == other.timestamp;
	}

	@Override
	public String toString() {
		return "DevicePollutionMap [deviceId=" + deviceId + ", deviceName=" + deviceName + ", lat=" + lat + ", lon="
				+ lon + ", carbonMonoxide=" + carbonMonoxide + ", timestamp=" + timestamp + "]";
	}
}