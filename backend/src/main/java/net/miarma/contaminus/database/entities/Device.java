package net.miarma.contaminus.database.entities;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;

@Table("devices")
public class Device {

    private Integer deviceId;
    private Integer groupId;
    private String deviceName;

    public Device() {}
    
    public Device(Row row) {
        this.deviceId = row.getInteger("deviceId");
        this.groupId = row.getInteger("groupId");
        this.deviceName = row.getString("deviceName");
    }

	public Device(Integer deviceId, Integer groupId, String deviceName) {
		super();
		this.deviceId = deviceId;
		this.groupId = groupId;
		this.deviceName = deviceName;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
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

