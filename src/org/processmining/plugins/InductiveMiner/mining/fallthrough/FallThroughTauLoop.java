package org.processmining.plugins.InductiveMiner.mining.fallthrough;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.mining.IMLog;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.IMTrace;
import org.processmining.plugins.InductiveMiner.mining.Miner;
import org.processmining.plugins.InductiveMiner.mining.MinerState;
import org.processmining.plugins.InductiveMiner.mining.metrics.MinerMetrics;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock.XorLoop;
import org.processmining.processtree.impl.AbstractTask.Automatic;

public class FallThroughTauLoop implements FallThrough {

	public Node fallThrough(IMLog log, IMLogInfo logInfo, ProcessTree tree, MinerState minerState) {

		if (logInfo.getActivities().toSet().size() > 1) {

			//try to find a tau loop
			IMLog sublog = new IMLog();
			long numberOfTimesTauTaken = 0;

			for (IMTrace trace : log) {
				numberOfTimesTauTaken += filterTrace(sublog, trace, log.getCardinalityOf(trace),
						logInfo.getStartActivities());
			}

			if (sublog.size() > log.size()) {
				Miner.debug(" fall through: tau loop", minerState);
				//making a tau loop split makes sense
				Block loop = new XorLoop("");
				loop.setProcessTree(tree);

				Node body = Miner.mineNode(sublog, tree, minerState);
				loop.addChild(body);

				Node redo = new Automatic("tau");
				redo.setProcessTree(tree);
				loop.addChild(redo);
				MinerMetrics.attachNumberOfTracesRepresented(redo, (int) numberOfTimesTauTaken);

				Node exit = new Automatic("tau");
				exit.setProcessTree(tree);
				loop.addChild(exit);
				MinerMetrics.attachNumberOfTracesRepresented(exit, logInfo);

				return MinerMetrics.attachNumberOfTracesRepresented(loop, logInfo);
			}
		}

		return null;
	}

	public static long filterTrace(IMLog sublog, IMTrace trace, int cardinality, MultiSet<XEventClass> startActivities) {
		boolean first = true;
		long numberOfTimesTauTaken = 0;
		IMTrace partialTrace = new IMTrace();
		for (XEventClass event : trace) {

			if (!first && startActivities.contains(event)) {
				//we discovered a transition body -> body
				sublog.add(partialTrace, cardinality);
				partialTrace = new IMTrace();
				first = true;

				numberOfTimesTauTaken += cardinality;
			}

			partialTrace.add(event);
			first = false;
		}
		sublog.add(partialTrace, cardinality);
		return numberOfTimesTauTaken;
	}
}
