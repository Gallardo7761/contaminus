package net.miarma.contaminus.entities;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("devices")
public class Device {

    private String deviceId;
    private Integer groupId;
    private String deviceName;

    public Device() {}
    
    public Device(Row row) {
        this.deviceId = row.getString("deviceId");
        this.groupId = row.getInteger("groupId");
        this.deviceName = row.getString("deviceName");
    }

	public Device(String deviceId, Integer groupId, String deviceName) {
		super();
		this.deviceId = deviceId;
		this.groupId = groupId;
		this.deviceName = deviceName;
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

	

    
    
}

