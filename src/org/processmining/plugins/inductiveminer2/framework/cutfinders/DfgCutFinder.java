package org.processmining.plugins.inductiveminer2.framework.cutfinders;

import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormaliserInt;
import org.processmining.plugins.inductiveminer2.mining.MinerState;

public interface DfgCutFinder {

	Cut findCut(NormalisedIntDfg dfg, NormaliserInt normaliser, MinerState minerState);

}
