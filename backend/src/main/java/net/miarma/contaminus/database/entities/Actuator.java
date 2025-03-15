package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("actuators")
public class Actuator {

    private int actuatorId;
    private int deviceId;
    private int status;
    private long timestamp;

    public Actuator() {}
    
    public Actuator(Row row) {
        this.actuatorId = row.getInteger("actuatorId");
        this.deviceId = row.getInteger("deviceId");
        this.status = row.getInteger("status");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public Actuator(int actuatorId, int deviceId, int status, long timestamp) {
		super();
		this.actuatorId = actuatorId;
		this.deviceId = deviceId;
		this.status = status;
		this.timestamp = timestamp;
	}

	public int getActuatorId() {
		return actuatorId;
	}

	public void setActuatorId(int actuatorId) {
		this.actuatorId = actuatorId;
	}

	public int getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(int deviceId) {
		this.deviceId = deviceId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(actuatorId, deviceId, status, timestamp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Actuator other = (Actuator) obj;
		return actuatorId == other.actuatorId && deviceId == other.deviceId
				&& status == other.status && timestamp == other.timestamp;
	}

	@Override
	public String toString() {
		return "Actuator [actuatorId=" + actuatorId + ", deviceId=" + deviceId + ", status=" + status + ", timestamp="
				+ timestamp + "]";
	}
    
	
    
}