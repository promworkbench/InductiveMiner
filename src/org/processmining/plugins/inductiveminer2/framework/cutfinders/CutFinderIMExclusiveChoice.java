package org.processmining.plugins.inductiveminer2.framework.cutfinders;

import java.util.List;

import org.processmining.plugins.InductiveMiner.mining.cuts.Cut.Operator;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntConnectedComponents;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormaliserInt;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.mining.MinerState;

import gnu.trove.set.TIntSet;

public class CutFinderIMExclusiveChoice implements CutFinder {

	public Cut findCut(final IMLog log, final IMLogInfo logInfo, final MinerState minerState) {
		return findCut(logInfo.getDfg(), logInfo.getNormaliser());
	}

	public static Cut findCut(NormalisedIntDfg dfg, NormaliserInt normaliser) {
		List<TIntSet> connectedComponents = NormalisedIntConnectedComponents.compute(dfg.getDirectlyFollowsGraph(),
				normaliser.getNumberOfActivities());
		return new Cut(Operator.xor, normaliser.deNormalise(connectedComponents));
	}
}
