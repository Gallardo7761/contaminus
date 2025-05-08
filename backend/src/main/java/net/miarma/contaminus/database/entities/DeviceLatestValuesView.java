package net.miarma.contaminus.database.entities;
import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_latest_values")
public class DeviceLatestValuesView {
	private String deviceId;
	private Integer sensorId;
	private String sensorType;
	private String unit;
	private Integer sensorStatus;
	private Long sensorTimestamp;
	private Float temperature;
	private Float humidity;
	private Float carbonMonoxide;
	private Float lat;
	private Float lon;
	private Long airValuesTimestamp;
	
    public DeviceLatestValuesView() {}
    
    public DeviceLatestValuesView(Row row) {
    	this.deviceId = row.getString("deviceId");
    	this.sensorId = row.getInteger("sensorId");
    	this.sensorType = row.getString("sensorType");
    	this.unit = row.getString("unit");
    	this.sensorStatus = row.getInteger("sensorStatus");
    	this.sensorTimestamp = DateParser.parseDate(row.getLocalDateTime("sensorTimestamp"));
    	this.temperature = row.getFloat("temperature");
    	this.humidity = row.getFloat("humidity");
    	this.carbonMonoxide = row.getFloat("carbonMonoxide");
    	this.lat = row.getFloat("lat");
    	this.lon = row.getFloat("lon");
    	this.airValuesTimestamp = DateParser.parseDate(row.getLocalDateTime("airValuesTimestamp"));
    }
	
	public DeviceLatestValuesView(String deviceId, Integer sensorId, String sensorType, String unit, Integer sensorStatus,
			Long sensorTimestamp, Float temperature, Float humidity, Float carbonMonoxide, Float lat, Float lon,
			Long airValuesTimestamp) {
		super();
		this.deviceId = deviceId;
		this.sensorId = sensorId;
		this.sensorType = sensorType;
		this.unit = unit;
		this.sensorStatus = sensorStatus;
		this.sensorTimestamp = sensorTimestamp;
		this.temperature = temperature;
		this.humidity = humidity;
		this.carbonMonoxide = carbonMonoxide;
		this.lat = lat;
		this.lon = lon;
		this.airValuesTimestamp = airValuesTimestamp;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public String getUnit() {
		return unit;
	}

	public Integer getSensorStatus() {
		return sensorStatus;
	}

	public Long getSensorTimestamp() {
		return sensorTimestamp;
	}

	public Float getTemperature() {
		return temperature;
	}

	public Float getHumidity() {
		return humidity;
	}

	public Float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public Float getLat() {
		return lat;
	}

	public Float getLon() {
		return lon;
	}

	public Long getAirValuesTimestamp() {
		return airValuesTimestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(airValuesTimestamp, carbonMonoxide, deviceId, humidity, lat, lon, sensorId, sensorStatus,
				sensorTimestamp, sensorType, temperature, unit);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DeviceLatestValuesView other = (DeviceLatestValuesView) obj;
		return Objects.equals(airValuesTimestamp, other.airValuesTimestamp)
				&& Objects.equals(carbonMonoxide, other.carbonMonoxide) && Objects.equals(deviceId, other.deviceId)
				&& Objects.equals(humidity, other.humidity) && Objects.equals(lat, other.lat)
				&& Objects.equals(lon, other.lon) && Objects.equals(sensorId, other.sensorId)
				&& Objects.equals(sensorStatus, other.sensorStatus)
				&& Objects.equals(sensorTimestamp, other.sensorTimestamp)
				&& Objects.equals(sensorType, other.sensorType) && Objects.equals(temperature, other.temperature)
				&& Objects.equals(unit, other.unit);
	}

	@Override
	public String toString() {
		return "DeviceLatestValuesView [deviceId=" + deviceId + ", sensorId=" + sensorId + ", sensorType=" + sensorType
				+ ", unit=" + unit + ", sensorStatus=" + sensorStatus + ", sensorTimestamp=" + sensorTimestamp
				+ ", temperature=" + temperature + ", humidity=" + humidity + ", carbonMonoxide=" + carbonMonoxide
				+ ", lat=" + lat + ", lon=" + lon + ", airValuesTimestamp=" + airValuesTimestamp + "]";
	}

	
	
	
}
