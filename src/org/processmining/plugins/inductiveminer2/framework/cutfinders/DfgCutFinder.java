package org.processmining.plugins.inductiveminer2.framework.cutfinders;

import org.processmining.plugins.inductiveminer2.helperclasses.IntDfg;
import org.processmining.plugins.inductiveminer2.mining.MinerState;

public interface DfgCutFinder {

	Cut findCut(IntDfg dfg, MinerState minerState);

}
