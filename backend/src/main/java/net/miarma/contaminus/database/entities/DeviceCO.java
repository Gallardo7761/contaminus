package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("v_co_by_device")
public class DeviceCO {
	int deviceId;
	float carbonMonoxide;
	long timestamp;
	
	public DeviceCO() {}
	
	public DeviceCO(Row row) {
		this.deviceId = row.getInteger("deviceId");
		this.carbonMonoxide = row.getFloat("carbonMonoxide");
		this.timestamp = row.getLong("timestamp");
	}
	
	public DeviceCO(int deviceId, float carbonMonoxide, long timestamp) {
		super();
		this.deviceId = deviceId;
		this.carbonMonoxide = carbonMonoxide;
		this.timestamp = timestamp;
	}

	public int getDeviceId() {
		return deviceId;
	}
	
	public float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(carbonMonoxide, deviceId, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceCO other = (DeviceCO) obj;
		return Float.floatToIntBits(carbonMonoxide) == Float.floatToIntBits(other.carbonMonoxide)
				&& deviceId == other.deviceId && timestamp == other.timestamp;
	}

	@Override
	public String toString() {
		return "DeviceCO [deviceId=" + deviceId + ", carbonMonoxide=" + carbonMonoxide + ", timestamp=" + timestamp
				+ "]";
	}
}