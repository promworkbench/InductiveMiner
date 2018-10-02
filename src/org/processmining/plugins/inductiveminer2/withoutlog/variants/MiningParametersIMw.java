package org.processmining.plugins.inductiveminer2.withoutlog.variants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.mining.cuts.Cut.Operator;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfo;
import org.processmining.plugins.inductiveminer2.withoutlog.MinerStateWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.MiningParametersWithoutLogAbstract;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLogEmptyLog;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLogEmptyTraces;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLogSemiFlowerModel;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLogSingleActivity;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLogIMConcurrentWithMinimumSelfDistance;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLogIMExclusiveChoice;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLogIMLoop;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLogIMSequence;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd;
import org.processmining.plugins.inductiveminer2.withoutlog.fallthroughs.FallThroughWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.fallthroughs.FallThroughWithoutLogFlowerWithoutEpsilon;
import org.processmining.plugins.inductiveminer2.withoutlog.graphsplitters.SimpleDfgMsdSplitter;
import org.processmining.plugins.inductiveminer2.withoutlog.postprocessors.PostProcessorWithoutLog;

import gnu.trove.set.TIntSet;

public class MiningParametersIMw extends MiningParametersWithoutLogAbstract implements InductiveMinerWithoutLogVariant {

	public static final List<BaseCaseFinderWithoutLog> basicBaseCaseFinders = Collections
			.unmodifiableList(Arrays.asList(new BaseCaseFinderWithoutLog[] { //
					new BaseCaseFinderWithoutLogSingleActivity(), //
					new BaseCaseFinderWithoutLogSemiFlowerModel(), //
					new BaseCaseFinderWithoutLogEmptyLog(), //
					new BaseCaseFinderWithoutLogEmptyTraces() }));

	public static final List<CutFinderWithoutLog> basicCutFinders = Collections
			.unmodifiableList(Arrays.asList(new CutFinderWithoutLog[] { //
					new CutFinderWithoutLogIMExclusiveChoice(), //
					new CutFinderWithoutLogIMSequence(), //
					new CutFinderWithoutLogIMConcurrentWithMinimumSelfDistance(), // 
					new CutFinderWithoutLogIMLoop() }));

	public static final List<FallThroughWithoutLog> basicFallThroughs = Collections
			.unmodifiableList(Arrays.asList(new FallThroughWithoutLog[] { //
					new FallThroughWithoutLogFlowerWithoutEpsilon(), //
			//TODO: finish
	}));

	public boolean hasNoise() {
		return false;
	}

	public Probabilities getSatProbabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	public IMLog2IMLogInfo getLog2LogInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<BaseCaseFinderWithoutLog> getBaseCaseFinders() {
		return basicBaseCaseFinders;
	}

	public List<CutFinderWithoutLog> getCutFinders() {
		return basicCutFinders;
	}

	public List<FallThroughWithoutLog> getFallThroughs() {
		return basicFallThroughs;
	}

	public List<PostProcessorWithoutLog> getPostProcessors() {
		// TODO Auto-generated method stub
		return null;
	}

	public EfficientTreeReduceParameters getReduceParameters() {
		// TODO Auto-generated method stub
		return null;
	}

	public DfgMsd[] splitGraphConcurrent(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		return SimpleDfgMsdSplitter.split2(graph, partition, Operator.concurrent, minerState);
	}

	public DfgMsd[] splitGraphInterleaved(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		return SimpleDfgMsdSplitter.split2(graph, partition, Operator.interleaved, minerState);
	}

	public DfgMsd[] splitGraphLoop(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		return SimpleDfgMsdSplitter.split2(graph, partition, Operator.loop, minerState);
	}

	public DfgMsd[] splitGraphOr(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		return SimpleDfgMsdSplitter.split2(graph, partition, Operator.or, minerState);
	}

	public DfgMsd[] splitGraphSequence(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		return SimpleDfgMsdSplitter.split2(graph, partition, Operator.sequence, minerState);
	}

	public DfgMsd[] splitGraphXor(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		return SimpleDfgMsdSplitter.split2(graph, partition, Operator.xor, minerState);
	}

	public boolean hasFitness() {
		return false;
	}

	public boolean noNoiseImpliesFitness() {
		return false;
	}

	public MiningParametersWithoutLogAbstract getMiningParameters() {
		return this;
	}

	public int getWarningThreshold() {
		return -1;
	}

	public String getDoi() {
		return "https://doi.org/10.1007/s10270-016-0545-x";
	}

}
