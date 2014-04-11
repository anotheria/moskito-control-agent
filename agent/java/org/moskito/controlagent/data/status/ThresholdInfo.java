package org.moskito.controlagent.data.status;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 14.06.13 11:52
 */
public class ThresholdInfo {
	private String threshold;

	private String value;

	private String message;

	public String getThreshold() {
		return threshold;
	}

	public void setThreshold(String threshold) {
		this.threshold = threshold;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
