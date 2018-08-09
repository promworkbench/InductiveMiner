package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.plugins.InductiveMiner.dfgOnly.DfgImpl;
import org.processmining.plugins.InductiveMiner.graphs.Graph;

public class DfgMsdImpl extends DfgImpl implements DfgMsd {
	private Graph<XEventClass> minimumSelfDistanceGraph;

	public void addMinimumSelfDistanceEdge(XEventClass source, XEventClass target, long cardinality) {
		minimumSelfDistanceGraph.addEdge(source, target, cardinality);
	}

	public boolean containsMinimumSelfDistanceEdge(XEventClass source, XEventClass target) {
		return minimumSelfDistanceGraph.containsEdge(source, target);
	}

	public XEventClass getMinimumSelfDistanceEdgeSource(long edgeIndex) {
		return minimumSelfDistanceGraph.getEdgeSource(edgeIndex);
	}

	public XEventClass getMinimumSelfDistanceEdgeTarget(long edgeIndex) {
		return minimumSelfDistanceGraph.getEdgeTarget(edgeIndex);
	}
}
