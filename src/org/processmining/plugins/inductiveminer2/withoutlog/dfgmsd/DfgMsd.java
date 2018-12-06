package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import org.processmining.plugins.directlyfollowsmodel.DirectlyFollowsModel;
import org.processmining.plugins.inductiveminer2.helperclasses.graphs.IntGraph;

public interface DfgMsd extends DirectlyFollowsModel {

	public IntGraph getMinimumSelfDistanceGraph();

	public DfgMsd clone();

}