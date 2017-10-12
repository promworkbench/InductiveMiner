package org.processmining.plugins.inductiveminer2.mining;

import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.InductiveMiner.mining.logs.LifeCycleClassifier;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinder;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinder;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThrough;
import org.processmining.plugins.inductiveminer2.framework.postprocessor.PostProcessor;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfo;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;
import org.processmining.plugins.inductiveminer2.logs.IMLog;

import gnu.trove.set.TIntSet;

public interface MiningParameters {

	public static final XEventClassifier defaultClassifier = new XEventNameClassifier();
	public static final XLifeCycleClassifier defaultLifeCycleClassifier = new LifeCycleClassifier();
	public static final float defaultNoiseThreshold = 0.2f;
	public static final boolean defaultIsDebug = true;
	public static final boolean defaultIsUseMultiThreading = true;
	public static final EfficientTreeReduceParameters defaultReduceParameters = new EfficientTreeReduceParameters(
			false);

	public IMLog getIMLog(XLog xLog);

	public XEventClassifier getClassifier();

	public XLifeCycleClassifier getLifeCycleClassifier();

	public float getNoiseThreshold();

	public boolean isDebug();

	public Probabilities getSatProbabilities();

	public boolean isUseMultithreading();

	public IMLog2IMLogInfo getLog2LogInfo();

	public List<BaseCaseFinder> getBaseCaseFinders();

	public List<CutFinder> getCutFinders();

	public List<FallThrough> getFallThroughs();

	public boolean isRepairLifeCycle();

	List<PostProcessor> getPostProcessors();

	public EfficientTreeReduceParameters getReduceParameters();

	public IMLog[] splitLogConcurrent(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState);

	public IMLog[] splitLogInterleaved(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState);

	public IMLog[] splitLogLoop(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState);

	public IMLog[] splitLogOr(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState);

	public IMLog[] splitLogSequence(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState);

	public IMLog[] splitLogXor(IMLog log, IMLogInfo logInfo, List<TIntSet> partition, MinerState minerState);

}
