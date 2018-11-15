package org.processmining.plugins.inductiveminer2.withoutlog.cutfinders;

import org.processmining.plugins.inductiveminer2.framework.cutfinders.Cut;
import org.processmining.plugins.inductiveminer2.withoutlog.MinerStateWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd;

public class CutFinderWithoutLogIMLoopWithMinimumSelfDistance implements CutFinderWithoutLog {

	public Cut findCut(DfgMsd graph, MinerStateWithoutLog minerState) {

		//first, check whether each activity has an outgoing msd-edge
		for (int node : graph.getActivities()) {
			if (!graph.getMinimumSelfDistanceGraph().getOutgoingEdgesOf(node).iterator().hasNext()) {
				return null;
			}
		}

		return org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMLoop.findCut(graph);
	}

}
