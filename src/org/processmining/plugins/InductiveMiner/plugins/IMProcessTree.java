package org.processmining.plugins.InductiveMiner.plugins;

import org.deckfour.xes.model.XLog;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.plugins.InductiveMiner.mining.IMLog;
import org.processmining.plugins.InductiveMiner.mining.Miner;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.processtree.ProcessTree;

@Plugin(name = "Mine Process Tree with Inductive Miner", returnLabels = { "Process Tree" }, returnTypes = { ProcessTree.class }, parameterLabels = {
		"Log" }, userAccessible = false)
public class IMProcessTree {
	
	public ProcessTree mineProcessTree(PluginContext context, XLog xlog) {
		return mineProcessTree(xlog);
	}
	
	public static ProcessTree mineProcessTree(XLog xlog) {
		return mineProcessTree(xlog, new MiningParametersIM());
	}
	
	public static ProcessTree mineProcessTree(XLog xlog, MiningParameters parameters) {
		//prepare the log
		IMLog log = new IMLog(xlog, parameters.getClassifier());
		return mineProcessTree(log, parameters);
	}
	
	public static ProcessTree mineProcessTree(IMLog log, MiningParameters parameters) {
		return Miner.mine(log, parameters);
	}
}
