package org.processmining.plugins.inductiveminer2.mining;

import java.util.ArrayList;

import org.deckfour.xes.classification.XEventClassifier;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;
import org.processmining.plugins.inductiveminer2.framework.basecases.BaseCaseFinder;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.CutFinder;
import org.processmining.plugins.inductiveminer2.framework.fallthroughs.FallThrough;
import org.processmining.plugins.inductiveminer2.framework.postprocessor.PostProcessor;

public abstract class MiningParametersAbstract implements MiningParameters {

	protected XEventClassifier classifier = MiningParameters.defaultClassifier;
	protected XLifeCycleClassifier lifeCycleClassifier = MiningParameters.defaultLifeCycleClassifier;
	protected float noiseThreshold = MiningParameters.defaultNoiseThreshold;
	protected boolean isDebug = MiningParameters.defaultIsDebug;
	protected boolean isUseMultithreading = MiningParameters.defaultIsUseMultiThreading;

	protected ArrayList<BaseCaseFinder> baseCaseFinders = new ArrayList<>();
	protected ArrayList<CutFinder> cutFinders = new ArrayList<>();
	protected ArrayList<FallThrough> fallThroughs = new ArrayList<>();
	protected ArrayList<PostProcessor> postProcessors = new ArrayList<>();

	protected EfficientTreeReduceParameters reduceParameters = MiningParameters.defaultReduceParameters;

	@Override
	public XEventClassifier getClassifier() {
		return classifier;
	}

	public void setClassifier(XEventClassifier classifier) {
		this.classifier = classifier;
	}

	@Override
	public XLifeCycleClassifier getLifeCycleClassifier() {
		return lifeCycleClassifier;
	}

	public void setLifeCycleClassifier(XLifeCycleClassifier lifeCycleClassifier) {
		this.lifeCycleClassifier = lifeCycleClassifier;
	}

	@Override
	public float getNoiseThreshold() {
		return noiseThreshold;
	}

	public void setNoiseThreshold(float noiseTreshold) {
		this.noiseThreshold = noiseTreshold;
	}

	@Override
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}

	@Override
	public boolean isUseMultithreading() {
		return isUseMultithreading;
	}

	public void setUseMultithreading(boolean isUseMultiThreading) {
		this.isUseMultithreading = isUseMultiThreading;
	}

	@Override
	public ArrayList<BaseCaseFinder> getBaseCaseFinders() {
		return baseCaseFinders;
	}

	@Override
	public ArrayList<CutFinder> getCutFinders() {
		return cutFinders;
	}

	@Override
	public ArrayList<FallThrough> getFallThroughs() {
		return fallThroughs;
	}

	@Override
	public ArrayList<PostProcessor> getPostProcessors() {
		return postProcessors;
	}

	@Override
	public EfficientTreeReduceParameters getReduceParameters() {
		return reduceParameters;
	}

}
