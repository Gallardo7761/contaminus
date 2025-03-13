package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("devices")
public class Device {

    private int deviceId;
    private int groupId;
    private String deviceName;

    public Device() {}
    
    public Device(Row row) {
        this.deviceId = row.getInteger("deviceId");
        this.groupId = row.getInteger("groupId");
        this.deviceName = row.getString("deviceName");
    }

	public Device(int deviceId, int groupId, String deviceName) {
		super();
		this.deviceId = deviceId;
		this.groupId = groupId;
		this.deviceName = deviceName;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId, deviceName, groupId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Device other = (Device) obj;
		return deviceId == other.deviceId
				&& Objects.equals(deviceName, other.deviceName)
				&& groupId == other.groupId;
	}

	@Override
	public String toString() {
		return "Device [deviceId=" + deviceId + ", groupId=" + groupId + ", deviceName=" + deviceName + "]";
	}

    
    
}

