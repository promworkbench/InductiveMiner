package org.processmining.plugins.inductiveminer2.withoutlog.variants;

import java.util.List;

import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMc.probabilities.Probabilities;
import org.processmining.plugins.inductiveminer2.loginfo.IMLog2IMLogInfo;
import org.processmining.plugins.inductiveminer2.withoutlog.MinerStateWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.MiningParametersWithoutLogAbstract;
import org.processmining.plugins.inductiveminer2.withoutlog.basecases.BaseCaseFinderWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.cutfinders.CutFinderWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd;
import org.processmining.plugins.inductiveminer2.withoutlog.fallthroughs.FallThroughWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.postprocessors.PostProcessorWithoutLog;

import gnu.trove.set.TIntSet;

public class MiningParametersIMw extends MiningParametersWithoutLogAbstract implements InductiveMinerWithoutLogVariant {

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
		// TODO Auto-generated method stub
		return null;
	}

	public List<CutFinderWithoutLog> getCutFinders() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<FallThroughWithoutLog> getFallThroughs() {
		// TODO Auto-generated method stub
		return null;
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
