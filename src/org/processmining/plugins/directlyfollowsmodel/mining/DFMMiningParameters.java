package org.processmining.plugins.directlyfollowsmodel.mining;

import org.deckfour.xes.classification.XEventClassifier;
import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier;

public interface DFMMiningParameters {
	public double getNoiseThreshold();

	public XEventClassifier getClassifier();

	public XLifeCycleClassifier getLifeCycleClassifier();
}
