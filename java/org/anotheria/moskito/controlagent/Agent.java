package org.anotheria.moskito.controlagent;

public class Agent{

	public static Agent getInstance(){
		return AgentInstanceHolder.instance;
	}

	public ThresholdStatusHolder getThresholdStatus(){
		return new ThresholdStatusHolder();
	}

	private static class AgentInstanceHolder{
		static final Agent instance = new Agent();
	}

}