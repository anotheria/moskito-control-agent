package org.anotheria.moskito.controlagent;

import net.anotheria.moskito.core.threshold.ThresholdStatus;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 15.04.13 22:28
 */
public class ThresholdStatusHolder {
	private ThresholdStatus status = ThresholdStatus.GREEN;

	public ThresholdStatus getStatus() {
		return status;
	}

	public void setStatus(ThresholdStatus status) {
		this.status = status;
	}
}
