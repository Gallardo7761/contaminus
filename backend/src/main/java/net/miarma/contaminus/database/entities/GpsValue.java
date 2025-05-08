package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("gps_values")
public class GpsValue {

    private Integer valueId;
    private String deviceId;
    private Integer sensorId;
    private Float lat;
    private Float lon;
    private Long timestamp;

    public GpsValue() {}
    
    public GpsValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.deviceId = row.getString("deviceId");
        this.sensorId = row.getInteger("sensorId");
        this.lat = row.getFloat("lat");
        this.lon = row.getFloat("lon");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public GpsValue(Integer valueId, String deviceId, Integer sensorId, Float lat, Float lon, Long timestamp) {
		super();
		this.valueId = valueId;
		this.deviceId = deviceId;
		this.sensorId = sensorId;
		this.lat = lat;
		this.lon = lon;
		this.timestamp = timestamp;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}
	
	public String getDeviceId() {
		return deviceId;
	}
	
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public Float getLat() {
		return lat;
	}

	public void setLat(Float lat) {
		this.lat = lat;
	}

	public Float getLon() {
		return lon;
	}

	public void setLon(Float lon) {
		this.lon = lon;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(deviceId, lat, lon, sensorId, timestamp, valueId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GpsValue other = (GpsValue) obj;
		return Objects.equals(deviceId, other.deviceId) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(sensorId, other.sensorId)
				&& Objects.equals(timestamp, other.timestamp) && Objects.equals(valueId, other.valueId);
	}

	@Override
	public String toString() {
		return "GpsValue [valueId=" + valueId + ", deviceId=" + deviceId + ", sensorId=" + sensorId + ", lat=" + lat
				+ ", lon=" + lon + ", timestamp=" + timestamp + "]";
	}

	public static GpsValue fromPayload(DevicePayload payload) {
		return new GpsValue(null, payload.getDeviceId(), payload.getSensorId(), payload.getLat(), payload.getLon(),
				payload.getTimestamp());
	}
    
}