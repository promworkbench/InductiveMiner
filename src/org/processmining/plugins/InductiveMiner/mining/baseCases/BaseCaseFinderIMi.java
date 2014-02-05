package org.processmining.plugins.InductiveMiner.mining.baseCases;

import org.processmining.plugins.InductiveMiner.mining.LogInfo;
import org.processmining.plugins.InductiveMiner.mining.Miner2;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.filteredLog.IMLog;
import org.processmining.plugins.InductiveMiner.mining.filteredLog.IMTrace;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock;
import org.processmining.processtree.impl.AbstractTask;

public class BaseCaseFinderIMi implements BaseCaseFinder {

	public Node findBaseCases(IMLog log, LogInfo logInfo, ProcessTree tree, MiningParameters parameters) {

		if (logInfo.getNumberOfEvents() == 0) {
			//empty log, return tau

			Node node = new AbstractTask.Automatic("");
			node.setProcessTree(tree);

			return node;
		}

		if (logInfo.getActivities().size() == 1) {
			//the log contains just one activity

			//assuming application of the activity follows a geometric distribution, we estimate parameter ^p

			//calculate the event-per-trace size of the log
			double p = logInfo.getNumberOfTraces() / ((logInfo.getNumberOfEvents() + logInfo.getNumberOfTraces()) * 1.0);

			if (0.5 - parameters.getNoiseThreshold() <= p && p <= 0.5 + parameters.getNoiseThreshold()) {
				//^p is close enough to 0.5, consider it as a single activity

				Node node = new AbstractTask.Manual(logInfo.getActivities().iterator().next().toString());
				node.setProcessTree(tree);
				return node;
			}
			//else, the probability to stop is too low or too high, and we better output a flower model
		}
		
		//this clause is not proven in the paper
		if (logInfo.getNumberOfEpsilonTraces() != 0) {
			//the log contains empty traces

			if (logInfo.getNumberOfEpsilonTraces() < logInfo.getLengthStrongestTrace() * parameters.getNoiseThreshold()) {
				//there are not enough empty traces, the empty traces are considered noise

				//filter the empty traces from the log and recurse
				log.remove(new IMTrace());

				return Miner2.mineNode(log, tree, parameters);

			} else {
				//There are too many empty traces to consider them noise.
				//Mine an xor(tau, ..) and recurse.
				Block newNode = new AbstractBlock.Xor("");
				newNode.setProcessTree(tree);
				
				//add tau
				Node tau = new AbstractTask.Automatic("");
				tau.setProcessTree(tree);
				newNode.addChild(tau);
				
				//filter empty traces
				log.remove(new IMTrace());
				
				//recurse
				Node child = Miner2.mineNode(log, tree, parameters);
				newNode.addChild(child);

				return newNode;
			}
		}
		
		return null;
	}
}
