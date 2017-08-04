package org.processmining.plugins.inductiveminer2.variants;

import java.util.List;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMEmptyLog;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMEmptyTraces;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMSemiFlowerModel;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinderIMSingleActivity;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMConcurrent;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMConcurrentWithMinimumSelfDistance;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMExclusiveChoice;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMLoop;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinderIMSequence;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThroughActivityConcurrent;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThroughActivityOncePerTraceConcurrent;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThroughFlowerWithoutEpsilon;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThroughTauLoop;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThroughTauLoopStrict;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitterConcurrent;
import org.processmining.plugins.inductiveminer2.framework.logsplitter.LogSplitterInterleavedFiltering;
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

import gnu.trove.set.TIntSet;

public class MiningParametersIM extends MiningParametersAbstract implements InductiveMinerVariant {

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

		fallThroughs.add(new FallThroughActivityOncePerTraceConcurrent(true));
		fallThroughs.add(new FallThroughActivityConcurrent());
		fallThroughs.add(new FallThroughTauLoopStrict());
		fallThroughs.add(new FallThroughTauLoop());
		fallThroughs.add(new FallThroughFlowerWithoutEpsilon());

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

	public IMLog[] splitLogConcurrent(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		return LogSplitterConcurrent.split(log, partition, minerState);
	}

	public IMLog[] splitLogInterleaved(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		return LogSplitterInterleavedFiltering.split(log, partition, minerState);
	}

	public IMLog[] splitLogLoop(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		return LogSplitterLoop.split(log, partition, minerState);
	}

	public IMLog[] splitLogOr(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		return LogSplitterConcurrent.split(log, partition, minerState);
	}

	public IMLog[] splitLogSequence(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		return LogSplitterSequenceFiltering.split(log, partition, minerState);
	}

	public IMLog[] splitLogXor(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState) {
		return LogSplitterXorFiltering.split(log, partition, minerState);
	}

	@Override
	public String toString() {
		return "Inductive Miner";
	}

	@Override
	public boolean hasNoise() {
		return false;
	}

	@Override
	public boolean noNoiseImpliesFitness() {
		return true;
	}

	@Override
	public MiningParametersAbstract getMiningParameters() {
		return this;
	}

	@Override
	public int getWarningThreshold() {
		return -1;
	}

	@Override
	public String getDoi() {
		return "http://dx.doi.org/10.1007/978-3-642-38697-8_17";
	}

	public boolean hasFitness() {
		return true;
	}
}
