package net.miarma.contaminus.entities;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("devices")
public class Device {

    private String deviceId;
    private Integer groupId;
    private String deviceName;
    private Integer deviceRole;

    public Device() {}
    
    public Device(Row row) {
        this.deviceId = row.getString("deviceId");
        this.groupId = row.getInteger("groupId");
        this.deviceName = row.getString("deviceName");
        this.deviceRole = row.getInteger("deviceRole");
    }

    public Device(String deviceId, Integer groupId, String deviceName, Integer deviceRole) {
		super();
		this.deviceId = deviceId;
		this.groupId = groupId;
		this.deviceName = deviceName;
		this.deviceRole = deviceRole;
		
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public String getDeviceName() {
		return deviceName;
	}

	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}

	public Integer getDeviceRole() {
		return deviceRole;
	}
	
    public void setDevice(Integer deviceRole) {
		this.deviceRole = deviceRole;
	}
	
}

