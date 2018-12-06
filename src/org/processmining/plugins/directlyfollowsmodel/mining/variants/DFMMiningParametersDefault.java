package org.processmining.plugins.directlyfollowsmodel.mining.variants;

import org.processmining.plugins.InductiveMiner.mining.logs.LifeCycleClassifier;
import org.processmining.plugins.directlyfollowsmodel.mining.DFMMiningParametersAbstract;

public class DFMMiningParametersDefault extends DFMMiningParametersAbstract {
	public DFMMiningParametersDefault() {
		setNoiseThreshold(0.8);
		setLifeCycleClassifier(new LifeCycleClassifier());
	}
}
