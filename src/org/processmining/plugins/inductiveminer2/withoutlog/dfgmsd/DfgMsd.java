package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import org.processmining.plugins.inductiveminer2.helperclasses.IntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.graphs.IntGraph;

public interface DfgMsd extends IntDfg {

	public IntGraph getMinimumSelfDistanceGraph();

	public String getActivityOfIndex(int value);

	public DfgMsd clone();
}