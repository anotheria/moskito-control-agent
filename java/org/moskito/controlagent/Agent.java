package org.moskito.controlagent;

import net.anotheria.moskito.core.threshold.ThresholdRepository;
import org.apache.log4j.Logger;
import org.configureme.ConfigurationManager;

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
		if (agentConfig.includeAll()){
			tsh.setStatus(repository.getWorstStatus());
		}else{
			if (agentConfig.getIncludedProducersList().size()>0){
				tsh.setStatus(repository.getWorstStatus(agentConfig.getIncludedProducersList()));
			}else{
				log.warn("repository.getWorstStatusWithout is not yet implemented, skipping!");
				//tsh.setStatus(repository.getWorstStatusWithout(agentConfig.getExcludedProducersList()));)
			}
		}

		return tsh;
	}

	private static class AgentInstanceHolder{
		static final Agent instance = new Agent();
	}

}