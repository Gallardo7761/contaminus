package net.miarma.contaminus.database.entities;

import java.util.Objects;

import io.vertx.sqlclient.Row;
import net.miarma.contaminus.common.Table;
import net.miarma.contaminus.util.DateParser;

@Table("co_values")
public class COValue {

    private Integer valueId;
    private Integer sensorId;
    private Float value;
    private Long timestamp;
    
    public COValue() {}
    
    public COValue(Row row) {
        this.valueId = row.getInteger("valueId");
        this.sensorId = row.getInteger("sensorId");
        this.value = row.getFloat("value");
        this.timestamp = DateParser.parseDate(row.getLocalDateTime("timestamp"));
    }

	public COValue(Integer valueId, Integer sensorId, Float value, Long timestamp) {
		super();
		this.valueId = valueId;
		this.sensorId = sensorId;
		this.value = value;
		this.timestamp = timestamp;
	}

	public Integer getValueId() {
		return valueId;
	}

	public void setValueId(Integer valueId) {
		this.valueId = valueId;
	}

	public Integer getSensorId() {
		return sensorId;
	}

	public void setSensorId(Integer sensorId) {
		this.sensorId = sensorId;
	}

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public Long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Long timestamp) {
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
		return Objects.equals(sensorId, other.sensorId) && Objects.equals(timestamp, other.timestamp)
				&& Objects.equals(value, other.value) && Objects.equals(valueId, other.valueId);
	}

	@Override
	public String toString() {
		return "COValue [valueId=" + valueId + ", sensorId=" + sensorId + ", value=" + value + ", timestamp="
				+ timestamp + "]";
	}

	

    
}