package org.moskito.controlagent.endpoints.rmi;

import net.anotheria.anoprise.metafactory.Service;
import org.moskito.controlagent.data.status.ThresholdStatusHolder;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;

import java.util.List;

/**
 * TODO comment this class
 *
 * @author lrosenberg
 * @since 09.04.14 13:50
 */
public interface AgentService extends Service{
	public ThresholdStatusHolder getThresholdStatus();

	public List<ThresholdDataItem> getThresholds();


}
