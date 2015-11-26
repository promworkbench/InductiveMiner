package org.processmining.plugins.InductiveMiner.mining.fallthrough;

import org.deckfour.xes.classification.XEventClass;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XLog;
import org.deckfour.xes.model.XTrace;
import org.deckfour.xes.model.impl.XAttributeMapImpl;
import org.deckfour.xes.model.impl.XLogImpl;
import org.deckfour.xes.model.impl.XTraceImpl;
import org.processmining.framework.packages.PackageManager.Canceller;
import org.processmining.plugins.InductiveMiner.MultiSet;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.Miner;
import org.processmining.plugins.InductiveMiner.mining.MinerState;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.logs.IMTrace;
import org.processmining.plugins.InductiveMiner.mining.logs.LifeCycles.Transition;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock.XorLoop;
import org.processmining.processtree.impl.AbstractTask.Automatic;

public class FallThroughTauLoop implements FallThrough {

	private final boolean useLifeCycle;

	public FallThroughTauLoop(boolean useLifeCycle) {
		this.useLifeCycle = useLifeCycle;
	}

	public Node fallThrough(IMLog log, IMLogInfo logInfo, ProcessTree tree, MinerState minerState, Canceller canceller) {

		if (logInfo.getActivities().toSet().size() > 1) {

			//try to find a tau loop
			XLog sublog = new XLogImpl(new XAttributeMapImpl());

			for (IMTrace trace : log) {
				filterTrace(log, sublog, trace, logInfo.getStartActivities(), useLifeCycle);
			}

			if (sublog.size() > log.size()) {
				Miner.debug(" fall through: tau loop", minerState);
				//making a tau loop split makes sense
				Block loop = new XorLoop("");
				Miner.addNode(tree, loop);

				{
					Node body = Miner.mineNode(new IMLog(sublog, log.getClassifier()), tree, minerState, canceller);
					loop.addChild(body);
				}

				{
					Node redo = new Automatic("tau");
					Miner.addNode(tree, redo);
					loop.addChild(redo);
				}

				{
					Node exit = new Automatic("tau");
					Miner.addNode(tree, exit);
					loop.addChild(exit);
				}

				return loop;
			}
		}

		return null;
	}

	public static void filterTrace(IMLog log, XLog sublog, IMTrace trace, MultiSet<XEventClass> startActivities,
			boolean useLifeCycle) {
		boolean first = true;
		XTrace partialTrace = new XTraceImpl(new XAttributeMapImpl());

		MultiSet<XEventClass> openActivityInstances = new MultiSet<>();

		for (XEvent event : trace) {

			XEventClass activity = log.classify(event);

			if (!first && startActivities.contains(activity)) {
				//we discovered a transition body -> body
				//check whether there are no open activity instances
				if (!useLifeCycle || openActivityInstances.size() == 0) {
					sublog.add(partialTrace);
					partialTrace = new XTraceImpl(new XAttributeMapImpl());
					first = true;
				}
			}

			if (useLifeCycle) {
				if (log.getLifeCycle(event) == Transition.complete) {
					if (openActivityInstances.getCardinalityOf(activity) > 0) {
						openActivityInstances.remove(activity, 1);
					}
				} else if (log.getLifeCycle(event) == Transition.start) {
					openActivityInstances.add(log.classify(event));
				}
			}

			partialTrace.add(event);
			first = false;
		}
		sublog.add(partialTrace);
	}
}
