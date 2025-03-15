package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_sensor_history_by_device")
public class DeviceSensorHistory {
	int deviceId;
	String deviceName;
	float value;
	String valueType;
	long timestamp;
	
	public DeviceSensorHistory() {}
	
	public DeviceSensorHistory(Row row) {
		this.deviceId = row.getInteger("deviceId");
		this.deviceName = row.getString("deviceName");
		this.value = row.getFloat("value");
		this.valueType = row.getString("valueType");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public DeviceSensorHistory(int deviceId, String deviceName, float value, String valueType, long timestamp) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.value = value;
		this.valueType = valueType;
		this.timestamp = timestamp;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public float getValue() {
		return value;
	}

	public String getValueType() {
		return valueType;
	}

	public long getTimestamp() {
		return timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId, deviceName, timestamp, value, valueType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceSensorHistory other = (DeviceSensorHistory) obj;
		return deviceId == other.deviceId && Objects.equals(deviceName, other.deviceName)
				&& timestamp == other.timestamp && Float.floatToIntBits(value) == Float.floatToIntBits(other.value)
				&& Objects.equals(valueType, other.valueType);
	}

	@Override
	public String toString() {
		return "DeviceSensorHistory [deviceId=" + deviceId + ", deviceName=" + deviceName + ", value=" + value
				+ ", valueType=" + valueType + ", timestamp=" + timestamp + "]";
	}
}
