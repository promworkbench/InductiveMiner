package org.processmining.plugins.inductiveminer2.framework.cutfinders;

import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormaliserInt;
import org.processmining.plugins.inductiveminer2.mining.MinerState;

public class DfgCutFinderSimple implements DfgCutFinder {

	public Cut findCut(NormalisedIntDfg dfg, NormaliserInt normaliser, MinerState minerState) {
		Cut cut = CutFinderIMExclusiveChoice.findCut(dfg, normaliser);

		if (cut != null) {
			return cut;
		}

		if (minerState.isCancelled()) {
			return null;
		}

		cut = CutFinderIMSequence.findCut(dfg, normaliser, minerState);

		if (cut != null) {
			return cut;
		}

		if (minerState.isCancelled()) {
			return null;
		}

		cut = CutFinderIMConcurrent.findCut(dfg, normaliser, null);

		if (cut != null) {
			return cut;
		}

		if (minerState.isCancelled()) {
			return null;
		}

		return CutFinderIMLoop.findCut(dfg, normaliser);
	}

}
