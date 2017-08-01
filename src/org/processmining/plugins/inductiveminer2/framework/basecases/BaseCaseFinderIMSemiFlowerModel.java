package org.processmining.plugins.inductiveminer2.framework.basecases;

import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.efficienttree.InlineTree;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.mining.InductiveMiner;
import org.processmining.plugins.inductiveminer2.mining.MinerState;

public class BaseCaseFinderIMSemiFlowerModel implements BaseCaseFinder {

	public EfficientTree findBaseCases(IMLog log, IMLogInfo logInfo, MinerState minerState) {

		if (logInfo.getActivities().length == 1 && logInfo.getDfg().getNumberOfEmptyTraces() == 0) {
			//single activity in semi-flower model

			InductiveMiner.debug(" base case: IM single activity semi-flower model", minerState);

			EfficientTree activity = InlineTree.leaf(log.getActivity(logInfo.getActivities()[0]));
			return InlineTree.loop(activity, InlineTree.tau(), InlineTree.tau());
		}

		return null;
	}
}
