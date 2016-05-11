package org.processmining.plugins.InductiveMiner.mining;

import java.util.List;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;
import org.processmining.plugins.InductiveMiner.dfgOnly.log2logInfo.IMLog2IMLogInfo;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.mining.baseCases.BaseCaseFinder;
import org.processmining.plugins.InductiveMiner.mining.cuts.CutFinder;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.probabilities.Probabilities;
import org.processmining.plugins.InductiveMiner.mining.fallthrough.FallThrough;
import org.processmining.plugins.InductiveMiner.mining.logSplitter.LogSplitter;
import org.processmining.plugins.InductiveMiner.mining.postprocessor.PostProcessor;

public abstract class MiningParameters {
	private XEventClassifier classifier;
	private float noiseThreshold;
	private float incompleteThreshold;

	private boolean debug;
	private boolean repairLifeCycle;
	private boolean useMultiThreading;
	private Probabilities satProbabilities;

	private IMLog2IMLogInfo log2logInfo;
	private List<BaseCaseFinder> baseCaseFinders;
	private List<CutFinder> cutFinders;
	private LogSplitter logSplitter;
	private List<FallThrough> fallThroughs;
	private List<PostProcessor> postProcessors;
	
	private EfficientTreeReduceParameters reduceParameters;

	protected MiningParameters() {
		classifier = getDefaultClassifier();
		debug = false;
		repairLifeCycle = false;
		useMultiThreading = true;
		
		reduceParameters = new EfficientTreeReduceParameters(false);
	}

	public static XEventClassifier getDefaultClassifier() {
		return new XEventNameClassifier();
	}

	public void setClassifier(XEventClassifier classifier) {
		if (classifier != null) {
			this.classifier = classifier;
		}
	}

	public XEventClassifier getClassifier() {
		return this.classifier;
	}

	public float getNoiseThreshold() {
		return noiseThreshold;
	}

	public void setNoiseThreshold(float noiseThreshold) {
		this.noiseThreshold = noiseThreshold;
	}

	public boolean equals(Object object) {
		if (object instanceof MiningParameters) {
			MiningParameters parameters = (MiningParameters) object;
			if (classifier.equals(parameters.classifier)) {
				if (noiseThreshold == parameters.getNoiseThreshold()) {
					if (incompleteThreshold == parameters.getIncompleteThreshold()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public int hashCode() {
		return classifier.hashCode();
	}

	public float getIncompleteThreshold() {
		return incompleteThreshold;
	}

	public void setIncompleteThreshold(float incompleteThreshold) {
		this.incompleteThreshold = incompleteThreshold;
	}

	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public Probabilities getSatProbabilities() {
		return satProbabilities;
	}

	public void setSatProbabilities(Probabilities satProbabilities) {
		this.satProbabilities = satProbabilities;
	}

	public void setUseMultithreading(boolean useMultithreading) {
		this.useMultiThreading = useMultithreading;
	}
	
	public boolean isUseMultithreading() {
		return useMultiThreading;
	}

	public IMLog2IMLogInfo getLog2LogInfo() {
		return log2logInfo;
	}

	public void setLog2LogInfo(IMLog2IMLogInfo log2logInfo) {
		this.log2logInfo = log2logInfo;
	}
	
	public void setLogConverter(IMLog2IMLogInfo log2logInfo) {
		this.log2logInfo = log2logInfo;
	}

	public List<BaseCaseFinder> getBaseCaseFinders() {
		return baseCaseFinders;
	}

	public void setBaseCaseFinders(List<BaseCaseFinder> baseCaseFinders) {
		this.baseCaseFinders = baseCaseFinders;
	}

	public List<CutFinder> getCutFinders() {
		return cutFinders;
	}

	public void setCutFinder(List<CutFinder> cutFinders) {
		this.cutFinders = cutFinders;
	}

	public LogSplitter getLogSplitter() {
		return logSplitter;
	}

	public void setLogSplitter(LogSplitter logSplitter) {
		this.logSplitter = logSplitter;
	}

	public List<FallThrough> getFallThroughs() {
		return fallThroughs;
	}

	public void setFallThroughs(List<FallThrough> fallThroughs) {
		this.fallThroughs = fallThroughs;
	}

	public boolean isRepairLifeCycle() {
		return repairLifeCycle;
	}
	
	public void setRepairLifeCycle(boolean repairLifeCycle) {
		this.repairLifeCycle = repairLifeCycle;
	}

	public List<PostProcessor> getPostProcessors() {
		return postProcessors;
	}

	public void setPostProcessors(List<PostProcessor> postProcessors) {
		this.postProcessors = postProcessors;
	}

	public EfficientTreeReduceParameters getReduceParameters() {
		return reduceParameters;
	}

	/**
	 * Set to null if the tree is not to be reduced.
	 * @param reduceParameters
	 */
	public void setReduceParameters(EfficientTreeReduceParameters reduceParameters) {
		this.reduceParameters = reduceParameters;
	}
}