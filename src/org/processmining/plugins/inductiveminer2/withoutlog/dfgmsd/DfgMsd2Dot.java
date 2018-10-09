package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import java.util.Iterator;

import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.dot.DotEdge;
import org.processmining.plugins.graphviz.dot.DotNode;
import org.processmining.plugins.inductiveminer2.helperclasses.graphs.IntGraph;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

public class DfgMsd2Dot {

	public static Dot visualise(DfgMsd dfgMsd) {

		Dot result = new Dot();

		TIntObjectMap<DotNode> activity2dotNode = new TIntObjectHashMap<>(10, 0.5f, -1);
		for (Iterator<Integer> it = dfgMsd.getActivities().iterator(); it.hasNext();) {
			int activityIndex = it.next();
			activity2dotNode.put(activityIndex, result.addNode(dfgMsd.getActivityOfIndex(activityIndex)));
		}

		IntGraph dfg = dfgMsd.getDirectlyFollowsGraph();
		for (long edgeIndex : dfg.getEdges()) {
			int source = dfg.getEdgeSource(edgeIndex);
			int target = dfg.getEdgeTarget(edgeIndex);
			long weight = dfg.getEdgeWeight(edgeIndex);
			result.addEdge(activity2dotNode.get(source), activity2dotNode.get(target), weight + "");
		}

		IntGraph msd = dfgMsd.getMinimumSelfDistanceGraph();
		for (long edgeIndex : msd.getEdges()) {
			int source = msd.getEdgeSource(edgeIndex);
			int target = msd.getEdgeTarget(edgeIndex);
			long weight = msd.getEdgeWeight(edgeIndex);
			DotEdge edge = result.addEdge(activity2dotNode.get(source), activity2dotNode.get(target), weight + "");
			edge.setOption("style", "dashed");
		}

		return result;

	}

}
