package org.moskito.controlagent;

import net.anotheria.moskito.core.threshold.ExtendedThresholdStatus;
import net.anotheria.moskito.core.threshold.ThresholdInStatus;
import net.anotheria.moskito.core.threshold.ThresholdRepository;
import net.anotheria.moskito.core.threshold.ThresholdStatus;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;

import java.util.List;

/**
 * Agent is the local center of everything.
 */
public class Agent{

	/**
	 * Instance of the threshold repository for lookup of the underlying status.
	 */
	private ThresholdRepository repository;
	/**
	 * Instance of the config.
	 */
	private AgentConfig agentConfig;
	/**
	 * Logger.
	 */
	private static Logger log = Logger.getLogger(Agent.class);

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

	public static Agent getInstance(){
		return AgentInstanceHolder.instance;
	}

	/**
	 * Returns the local threshold status.
	 * @return
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

	private static class AgentInstanceHolder{
		static final Agent instance = new Agent();
	}

}