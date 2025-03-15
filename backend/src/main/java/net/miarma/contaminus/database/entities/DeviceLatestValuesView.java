package net.miarma.contaminus.database.entities;
import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("v_latest_values")
public class DeviceLatestValuesView {
	int deviceId;
	int sensorId;
	String sensorType;
	String unit;
	int sensorStatus;
	long sensorTimestamp;
	float temperature;
	float humidity;
	float carbonMonoxide;
	float lat;
	float lon;
	long airValuesTimestamp;
	
    public DeviceLatestValuesView() {}
    
    public DeviceLatestValuesView(Row row) {
    	this.deviceId = row.getInteger("deviceId");
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
	
	public DeviceLatestValuesView(int deviceId, int sensorId, String sensorType, String unit, int sensorStatus,
			long sensorTimestamp, float temperature, float humidity, float carbonMonoxide, float lat, float lon,
			long airValuesTimestamp) {
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

	public int getDeviceId() {
		return deviceId;
	}

	public int getSensorId() {
		return sensorId;
	}

	public String getSensorType() {
		return sensorType;
	}

	public String getUnit() {
		return unit;
	}

	public int getSensorStatus() {
		return sensorStatus;
	}

	public long getSensorTimestamp() {
		return sensorTimestamp;
	}

	public float getTemperature() {
		return temperature;
	}

	public float getHumidity() {
		return humidity;
	}

	public float getCarbonMonoxide() {
		return carbonMonoxide;
	}

	public float getLat() {
		return lat;
	}

	public float getLon() {
		return lon;
	}

	public long getAirValuesTimestamp() {
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
		return airValuesTimestamp == other.airValuesTimestamp
				&& Float.floatToIntBits(carbonMonoxide) == Float.floatToIntBits(other.carbonMonoxide)
				&& deviceId == other.deviceId && Float.floatToIntBits(humidity) == Float.floatToIntBits(other.humidity)
				&& Float.floatToIntBits(lat) == Float.floatToIntBits(other.lat)
				&& Float.floatToIntBits(lon) == Float.floatToIntBits(other.lon) && sensorId == other.sensorId
				&& sensorStatus == other.sensorStatus && sensorTimestamp == other.sensorTimestamp
				&& Objects.equals(sensorType, other.sensorType)
				&& Float.floatToIntBits(temperature) == Float.floatToIntBits(other.temperature)
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
