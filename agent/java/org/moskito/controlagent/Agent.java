package org.moskito.controlagent;

import net.anotheria.moskito.core.threshold.ExtendedThresholdStatus;
import net.anotheria.moskito.core.threshold.Threshold;
import net.anotheria.moskito.core.threshold.ThresholdInStatus;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.configureme.ConfigurationManager;
import org.moskito.controlagent.data.status.ThresholdInfo;
import org.moskito.controlagent.data.status.ThresholdStatusHolder;
import org.moskito.controlagent.data.threshold.ThresholdDataItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Agent is the local center of everything.
 */
public class Agent{

	/**
	 * Instance of the threshold repository for lookup of the underlying status and thresholds.
	 */
	private ThresholdRepository repository;
	/**
	 * Instance of the config.
	 */
	private AgentConfig agentConfig;
	/**
	 * Logger.
	 */
	private static Logger log = LoggerFactory.getLogger(Agent.class);


	private Agent(){
		repository = ThresholdRepository.getInstance();
		agentConfig = new AgentConfig();
		try{
			ConfigurationManager.INSTANCE.configure(agentConfig);
		}catch(IllegalArgumentException e){
			log.info("No agent configuration found, consult MoSKito-Control-Agent documentation for details on configuring the agent.");
			//let it build internal lists.
			agentConfig.afterConfiguration();
		}

	}

    /**
     * @return {@link Agent} singleton instance.
     */
	public static Agent getInstance(){
		return AgentInstanceHolder.instance;
	}

    /**
     * {@link Agent} instance holder.
     */
    private static class AgentInstanceHolder{
        static final Agent instance = new Agent();
    }


	/**
	 * Returns the local threshold status.
     *
	 * @return status of component agent running on.
	 */
	public ThresholdStatusHolder getThresholdStatus(){
		//assuming you have no core.
		if (repository == null)
			return new ThresholdStatusHolder();

		ThresholdStatusHolder tsh = new ThresholdStatusHolder();
		ExtendedThresholdStatus thresholdStatus;

		if (agentConfig.includeAll()){
			thresholdStatus = repository.getExtendedWorstStatus(null);
		}else{
			if (agentConfig.getIncludedProducersList().size()>0){
				thresholdStatus = repository.getExtendedWorstStatus(agentConfig.getIncludedProducersList());
			}else{
				thresholdStatus = repository.getExtendedWorstStatusWithout(agentConfig.getExcludedProducersList());
			}
		}

		tsh.setStatus(thresholdStatus.getStatus());
		//only submit values for not-green status.
		if (thresholdStatus.getStatus()!= ThresholdStatus.GREEN){
			List<ThresholdInStatus> tisList = thresholdStatus.getThresholds();
			for (ThresholdInStatus tis : tisList){
				ThresholdInfo info = new ThresholdInfo();
				info.setValue(tis.getValue());
				info.setMessage(tis.getAdditionalMessage());
				info.setThreshold(tis.getThresholdName());
				tsh.addThresholdInfo(info);
			}
		}


		return tsh;
	}

    /**
     * Returns local thresholds.
     *
     * @return thresholds of component agent running on.
     */
    public List<ThresholdDataItem> getThresholds(){
        if (repository == null)
            return new LinkedList<ThresholdDataItem>();

        List<ThresholdDataItem> items = new LinkedList<ThresholdDataItem>();
        List<Threshold> thresholds = repository.getThresholds();
        for (Threshold threshold : thresholds){
            ThresholdDataItem item = new ThresholdDataItem();
            item.setName(threshold.getName());
            item.setStatus(threshold.getStatus());
            item.setLastValue(threshold.getLastValue());
            item.setStatusChangeTimestamp(threshold.getStatusChangeTimestamp());
            items.add(item);
        }

        return items;
    }

}