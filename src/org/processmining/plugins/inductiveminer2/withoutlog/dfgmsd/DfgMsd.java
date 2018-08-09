package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;

public interface DfgMsd extends Dfg {
	public void addMinimumSelfDistanceEdge(final XEventClass source, final XEventClass target, final long cardinality);

	public boolean containsMinimumSelfDistanceEdge(XEventClass source, XEventClass target);

	public XEventClass getMinimumSelfDistanceEdgeSource(long edgeIndex);

	public XEventClass getMinimumSelfDistanceEdgeTarget(long edgeIndex);
}
