package org.moskito.controlagent;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 18.06.13 09:25
 */
public class AccumulatorDataItem {
	private long timestamp;
	private String value;

	public AccumulatorDataItem(){

	}

	public AccumulatorDataItem(long aTimestamp, String aValue){
		timestamp = aTimestamp;
		value = aValue;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override public String toString(){
		return getTimestamp()+", "+getValue();
	}
}
