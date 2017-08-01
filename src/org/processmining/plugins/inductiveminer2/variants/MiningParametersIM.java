package org.processmining.plugins.inductiveminer2.variants;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMEmptyLog;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMEmptyTraces;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMSemiFlowerModel;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMSingleActivity;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.Cut;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMConcurrent;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMConcurrentWithMinimumSelfDistance;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMExclusiveChoice;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMLoop;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMSequence;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThroughFlowerModel;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitter;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitterConcurrent;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitterLoop;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitterSequenceFiltering;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitterXorFiltering;
import org.processmining.plugins.inductiveminer2.helperclasses.XLifeCycleClassifierIgnore;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfo;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfoDefault;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.logs.IMLogImpl;
import org.processmining.plugins.inductiveminer2.mining.MinerState;
import org.processmining.plugins.inductiveminer2.mining.MiningParametersAbstract;

public class MiningParametersIM extends MiningParametersAbstract {

	public MiningParametersIM() {
		baseCaseFinders.add(new BaseCaseFinderIMSingleActivity());
		baseCaseFinders.add(new BaseCaseFinderIMSemiFlowerModel());
		baseCaseFinders.add(new BaseCaseFinderIMEmptyLog());
		baseCaseFinders.add(new BaseCaseFinderIMEmptyTraces());

		cutFinders.add(new CutFinderIMExclusiveChoice());
		cutFinders.add(new CutFinderIMSequence());
		cutFinders.add(new CutFinderIMConcurrentWithMinimumSelfDistance());
		cutFinders.add(new CutFinderIMConcurrent());
		cutFinders.add(new CutFinderIMLoop());

		logSplitter = new LogSplitter() {
			public IMLog[] split(IMLog log, IMLogInfo logInfo, Cut cut, MinerState minerState) {
				switch (cut.getOperator()) {
					case concurrent :
						return LogSplitterConcurrent.split(log, cut.getPartition(), minerState);
					case interleaved :
						break;
					case loop :
						return LogSplitterLoop.split(log, cut.getPartition(), minerState);
					case maybeInterleaved :
						break;
					case or :
						break;
					case sequence :
						return LogSplitterSequenceFiltering.split(log, cut.getPartition(), minerState);
					case xor :
						return LogSplitterXorFiltering.split(log, cut.getPartition(), minerState);
					default :
						break;
				}
				throw new RuntimeException("not available");
			}
		};

		fallThroughs.add(new FallThroughFlowerModel());

		getReduceParameters().setReduceToOr(false);
	}

	public Probabilities getSatProbabilities() {
		return null;
	}

	public IMLog2IMLogInfo getLog2LogInfo() {
		return new IMLog2IMLogInfoDefault();
	}

	public boolean isRepairLifeCycle() {
		return false;
	}

	public boolean isProcessStartEndComplete() {
		return false;
	}

	@Override
	public XLifeCycleClassifier getLifeCycleClassifier() {
		/**
		 * We disable life cycle transitions by treating each event as
		 * 'complete', using a special life cycle classifier.
		 */

		return new XLifeCycleClassifierIgnore();
	}

	public IMLog getIMLog(XLog xLog) {
		return new IMLogImpl(xLog, getClassifier(), getLifeCycleClassifier());
	}

}
