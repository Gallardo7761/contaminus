package net.miarma.contaminus.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_sensor_history_by_device")
public class ViewSensorHistory {
	private String deviceId;
	private String deviceName;
	private Float value;
	private String valueType;
	private Long timestamp;
	
	public ViewSensorHistory() {}
	
	public ViewSensorHistory(Row row) {
		this.deviceId = row.getString("deviceId");
		this.deviceName = row.getString("deviceName");
		this.value = row.getFloat("value");
		this.valueType = row.getString("valueType");
		this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
	}

	public ViewSensorHistory(String deviceId, String deviceName, Float value, String valueType, Long timestamp) {
		super();
		this.deviceId = deviceId;
		this.deviceName = deviceName;
		this.value = value;
		this.valueType = valueType;
		this.timestamp = timestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public Float getValue() {
		return value;
	}

	public String getValueType() {
		return valueType;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public void setValueType(String valueType) {
		this.valueType = valueType;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
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
		ViewSensorHistory other = (ViewSensorHistory) obj;
		return Objects.equals(deviceId, other.deviceId) && Objects.equals(deviceName, other.deviceName)
				&& Objects.equals(timestamp, other.timestamp) && Objects.equals(value, other.value)
				&& Objects.equals(valueType, other.valueType);
	}

	@Override
	public String toString() {
		return "DeviceSensorHistory [deviceId=" + deviceId + ", deviceName=" + deviceName + ", value=" + value
				+ ", valueType=" + valueType + ", timestamp=" + timestamp + "]";
	}

	
}
