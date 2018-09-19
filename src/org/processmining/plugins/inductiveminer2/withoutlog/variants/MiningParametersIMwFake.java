package org.processmining.plugins.inductiveminer2.withoutlog.variants;

import java.util.ArrayList;
import java.util.List;

import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.efficienttree.InlineTree;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfo;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIM;
import org.processmining.plugins.inductiveminer2.withoutlog.MinerStateWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.MiningParametersWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd;
import org.processmining.plugins.inductiveminer2.withoutlog.fallthroughs.FallThroughWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.postprocessors.PostProcessorWithoutLog;

import gnu.trove.set.TIntSet;

public class MiningParametersIMwFake implements MiningParametersWithoutLog {

	public float getNoiseThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasNoise() {
		return false;
	}

	public boolean isDebug() {
		return false;
	}

	public Probabilities getSatProbabilities() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isUseMultithreading() {
		return false;
	}

	public IMLog2IMLogInfo getLog2LogInfo() {
		return null;
	}

	public List<BaseCaseFinderWithoutLog> getBaseCaseFinders() {
		List<BaseCaseFinderWithoutLog> result = new ArrayList<>();
		result.add(new BaseCaseFinderWithoutLog() {
			public EfficientTree findBaseCases(DfgMsd graph, MinerStateWithoutLog minerState) {
				return InlineTree.leaf("test");
			}
		});
		return result;
	}

	public List<CutFinderWithoutLog> getCutFinders() {
		return new ArrayList<>();
	}

	public List<FallThroughWithoutLog> getFallThroughs() {
		return new ArrayList<>();
	}

	public List<PostProcessorWithoutLog> getPostProcessors() {
		return new ArrayList<>();
	}

	public EfficientTreeReduceParameters getReduceParameters() {
		return MiningParametersIM.basicReduceParameters;
	}

	public DfgMsd[] splitGraphConcurrent(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public DfgMsd[] splitGraphInterleaved(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public DfgMsd[] splitGraphLoop(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public DfgMsd[] splitGraphOr(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public DfgMsd[] splitGraphSequence(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		// TODO Auto-generated method stub
		return null;
	}

	public DfgMsd[] splitGraphXor(DfgMsd graph, List<TIntSet> partition, MinerStateWithoutLog minerState) {
		// TODO Auto-generated method stub
		return null;
	}

}
