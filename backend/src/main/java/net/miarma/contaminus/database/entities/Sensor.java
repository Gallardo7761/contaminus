package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("sensors")
public class Sensor {

    private Integer sensorId;
    private Integer deviceId;
    private String sensorType;
    private String unit;
    private Integer status;
    private Long timestamp;

    public Sensor() {}
    
    public Sensor(Row row) {
        this.sensorId = row.getInteger("sensorId");
        this.deviceId = row.getInteger("deviceId");
        this.sensorType = row.getString("sensorType");
        this.unit = row.getString("unit");
        this.status = row.getInteger("status");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }
    
	public Sensor(Integer sensorId, Integer deviceId, String sensorType, String unit, Integer status, Long timestamp) {
		super();
		this.sensorId = sensorId;
		this.deviceId = deviceId;
		this.sensorType = sensorType;
		this.unit = unit;
		this.status = status;
		this.timestamp = timestamp;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public Integer getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(Integer deviceId) {
		this.deviceId = deviceId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public void setSensorType(String sensorType) {
		this.sensorType = sensorType;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
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
		return Objects.hash(deviceId, sensorId, sensorType, status, timestamp, unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Sensor other = (Sensor) obj;
		return Objects.equals(deviceId, other.deviceId) && Objects.equals(sensorId, other.sensorId)
				&& Objects.equals(sensorType, other.sensorType) && Objects.equals(status, other.status)
				&& Objects.equals(timestamp, other.timestamp) && Objects.equals(unit, other.unit);
	}

	@Override
	public String toString() {
		return "Sensor [sensorId=" + sensorId + ", deviceId=" + deviceId + ", sensorType=" + sensorType + ", unit="
				+ unit + ", status=" + status + ", timestamp=" + timestamp + "]";
	}

	    
}
