package org.moskito.controlagent;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AgentConfigTest{
	@Test public void testEmptyConfig(){
		AgentConfig config = new AgentConfig();
		config.afterConfiguration();
		assertTrue(config.includeAll());
		assertEquals(0, config.getIncludedProducersList().size());
		assertEquals(0, config.getExcludedProducersList().size());

	}


}