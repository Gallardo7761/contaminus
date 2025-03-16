package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("actuators")
public class Actuator {

    private Integer actuatorId;
    private Integer deviceId;
    private Integer status;
    private Long timestamp;

    public Actuator() {}
    
    public Actuator(Row row) {
        this.actuatorId = row.getInteger("actuatorId");
        this.deviceId = row.getInteger("deviceId");
        this.status = row.getInteger("status");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public Actuator(Integer actuatorId, Integer deviceId, Integer status, Long timestamp) {
		super();
		this.actuatorId = actuatorId;
		this.deviceId = deviceId;
		this.status = status;
		this.timestamp = timestamp;
	}

	public Integer getActuatorId() {
		return actuatorId;
	}

	public void setActuatorId(Integer actuatorId) {
		this.actuatorId = actuatorId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
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