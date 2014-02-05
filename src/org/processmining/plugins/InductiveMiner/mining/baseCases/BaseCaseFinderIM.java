package org.processmining.plugins.InductiveMiner.mining.baseCases;

import org.processmining.plugins.InductiveMiner.mining.LogInfo;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.filteredLog.IMLog;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractTask;

public class BaseCaseFinderIM implements BaseCaseFinder {

	public Node findBaseCases(IMLog log, LogInfo logInfo, ProcessTree tree, MiningParameters parameters) {
		
		if (logInfo.getActivities().size() == 1) {
			Node node = new AbstractTask.Manual(logInfo.getActivities().iterator().next().toString());
			node.setProcessTree(tree);
			return node;
		} else if (logInfo.getActivities().size() == 0) {
			Node node = new AbstractTask.Automatic("");
			node.setProcessTree(tree);
			return node;
		}
		
		return null;
	}
	
}
