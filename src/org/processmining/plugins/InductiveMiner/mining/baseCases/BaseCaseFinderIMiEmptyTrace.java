package org.processmining.plugins.InductiveMiner.mining.baseCases;

import java.util.Iterator;

import org.processmining.framework.packages.PackageManager.Canceller;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.Miner;
import org.processmining.plugins.InductiveMiner.mining.MinerState;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMTrace;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock;
import org.processmining.processtree.impl.AbstractTask;

public class BaseCaseFinderIMiEmptyTrace implements BaseCaseFinder {

	public Node findBaseCases(IMLog log, IMLogInfo logInfo, ProcessTree tree, MinerState minerState, Canceller canceller) {
		//this clause is not proven in the paper
		if (logInfo.getNumberOfEpsilonTraces() != 0) {
			//the log contains empty traces

			if (logInfo.getNumberOfEpsilonTraces() < log.size() * minerState.parameters.getNoiseThreshold()) {
				//there are not enough empty traces, the empty traces are considered noise

				Miner.debug(" base case: IMi empty traces filtered out", minerState);

				//filter the empty traces from the log and recurse
				Node newNode = Miner.mineNode(removeEpsilonTraces(log, canceller), tree, minerState, canceller);

				return newNode;

			} else {
				//There are too many empty traces to consider them noise.
				//Mine an xor(tau, ..) and recurse.

				Miner.debug(" base case: IMi xor(tau, ..)", minerState);

				Block newNode = new AbstractBlock.Xor("");
				Miner.addNode(tree, newNode);

				//add tau
				Node tau = new AbstractTask.Automatic("tau");
				Miner.addNode(tree, tau);
				newNode.addChild(tau);

				//filter empty traces
				IMLog sublog = removeEpsilonTraces(log, canceller);

				//recurse
				Node child = Miner.mineNode(sublog, tree, minerState, canceller);
				newNode.addChild(child);

				return newNode;
			}
		}
		return null;
	}

	public static IMLog removeEpsilonTraces(IMLog log, Canceller canceller) {
		IMLog sublog = new IMLog(log);
		for (Iterator<IMTrace> it = sublog.iterator(); it.hasNext();) {
			if (canceller.isCancelled()) {
				return sublog;
			}
			IMTrace t = it.next();
			if (t.isEmpty()) {
				it.remove();
			}
		}
		return sublog;
	}
}
