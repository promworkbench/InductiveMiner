package org.processmining.plugins.InductiveMiner.mining;

import java.io.File;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;

public class MiningParameters {
	private XEventClassifier classifier;
	private float noiseThreshold;
	private String outputDFGfileName;
	private File outputFlowerLogFileName;
	
	public MiningParameters() {
		classifier = new XEventAndClassifier(new XEventNameClassifier(), new XEventLifeTransClassifier());
		noiseThreshold = (float) 0.2;
		outputDFGfileName = null;
		outputFlowerLogFileName = null;
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
	
	public String getOutputDFGfileName() {
		return outputDFGfileName; 
	}

	public void setOutputDFGfileName(String outputFileName) {
		this.outputDFGfileName = outputFileName;
	}
	
	public boolean equals(Object object) {
		if (object instanceof MiningParameters) {
			MiningParameters parameters = (MiningParameters) object;
			if (classifier.equals(parameters.classifier)) {
				if (noiseThreshold == parameters.getNoiseThreshold()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public int hashCode() {
		return classifier.hashCode();
	}

	public File getOutputFlowerLogFileName() {
		return outputFlowerLogFileName;
	}

	public void setOutputFlowerLogFileName(File outputFlowerLogFileName) {
		this.outputFlowerLogFileName = outputFlowerLogFileName;
	}
}