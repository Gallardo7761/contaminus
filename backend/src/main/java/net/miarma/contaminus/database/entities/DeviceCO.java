package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_co_by_device")
public class DeviceCO {
	private String deviceId;
	private Float carbonMonoxide;
	private Long timestamp;
	
	public DeviceCO() {}
	
	public DeviceCO(Row row) {
		this.deviceId = row.getString("deviceId");
		this.carbonMonoxide = row.getFloat("carbonMonoxide");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}
	
	public DeviceCO(String deviceId, Float carbonMonoxide, Long timestamp) {
		super();
		this.deviceId = deviceId;
		this.carbonMonoxide = carbonMonoxide;
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}
	
	public Float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public Long getTimestamp() {
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
		return Objects.equals(carbonMonoxide, other.carbonMonoxide) && Objects.equals(deviceId, other.deviceId)
				&& Objects.equals(timestamp, other.timestamp);
	}

	@Override
	public String toString() {
		return "DeviceCO [deviceId=" + deviceId + ", carbonMonoxide=" + carbonMonoxide + ", timestamp=" + timestamp
				+ "]";
	}

	
}