package org.processmining.plugins.InductiveMiner.mining.fallthrough;

import org.processmining.plugins.InductiveMiner.mining.IMLog;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;

public class FallThroughETM implements FallThrough {
	
	public Node fallThrough(IMLog log, IMLogInfo logInfo, ProcessTree tree, MiningParameters parameters) {
		/*
		private static PluginContext context = new CLIPluginContext(new CLIContext(), "");
		ETMParam etmParameters = ETMParamFactory.buildStandardParam(log.toXLog(), context);
		
		//set maximum duration
		ETMParamFactory.removeTerminationConditionIfExists(etmParameters, ElapsedTime.class);
		etmParameters.addTerminationCondition(new ElapsedTime(2000));
		
		ETM miner = new org.processmining.plugins.etm.ETM(etmParameters);
		miner.run();
		
		NAryTree internalTree = miner.getResult();
		
		ProcessTree tree1 = NAryTreeToProcessTree.convert(internalTree);
		*/
		
		return null;
	}

}
