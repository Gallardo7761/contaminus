package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("co_values")
public class COValue {

    private int valueId;
    private int sensorId;
    private float value;
    private long timestamp;
    
    public COValue() {}
    
    public COValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.sensorId = row.getInteger("sensorId");
        this.value = row.getFloat("value");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public COValue(int valueId, int sensorId, float value, long timestamp) {
		super();
		this.valueId = valueId;
		this.sensorId = sensorId;
		this.value = value;
		this.timestamp = timestamp;
	}

	public int getValueId() {
		return valueId;
	}

	public void setValueId(int valueId) {
		this.valueId = valueId;
	}

	public int getSensorId() {
		return sensorId;
	}

	public void setSensorId(int sensorId) {
		this.sensorId = sensorId;
	}

	public float getValue() {
		return value;
	}

	public void setValue(float value) {
		this.value = value;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	@Override
	public int hashCode() {
		return Objects.hash(sensorId, timestamp, value, valueId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		COValue other = (COValue) obj;
		return sensorId == other.sensorId
				&& Objects.equals(timestamp, other.timestamp)
				&& Float.floatToIntBits(value) == Float.floatToIntBits(other.value) && valueId == other.valueId;
	}

	@Override
	public String toString() {
		return "COValue [valueId=" + valueId + ", sensorId=" + sensorId + ", value=" + value + ", timestamp="
				+ timestamp + "]";
	}

    
}