package org.anotheria.moskito.controlagent;

import org.configureme.annotations.Configure;
import org.configureme.annotations.ConfigureMe;

/**
 * Configuration of the agent.
 *
 * @author lrosenberg
 * @since 15.04.13 20:33
 */
@ConfigureMe
public class AgentConfig {
	/**
	 * Included producer names, comma separated or '*'.
	 */
	@Configure
	private String includedProducers;

	/**
	 * Excluded producer names, comma separated.
	 */
	@Configure
	private String excludedProducers;

	public String getIncludedProducers() {
		return includedProducers;
	}

	public void setIncludedProducers(String includedProducers) {
		this.includedProducers = includedProducers;
	}

	public String getExcludedProducers() {
		return excludedProducers;
	}

	public void setExcludedProducers(String excludedProducers) {
		this.excludedProducers = excludedProducers;
	}
}
