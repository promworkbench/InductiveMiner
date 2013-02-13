package bPrime.mining;

import org.deckfour.xes.classification.XEventAndClassifier;
import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.classification.XEventLifeTransClassifier;
import org.deckfour.xes.classification.XEventNameClassifier;

public class MiningParameters {
	private XEventClassifier classifier;
	private boolean filterNoise;
	private int noiseThreshold;
	
	private String outputDFGfileName;
	
	public MiningParameters() {
		classifier = new XEventAndClassifier(new XEventNameClassifier(), new XEventLifeTransClassifier());
		filterNoise = true;
		noiseThreshold = 10;
		outputDFGfileName = null;
	}
	
	public void setClassifier(XEventClassifier classifier) {
		if (classifier != null) {
			this.classifier = classifier;
		}
	}
	
	public XEventClassifier getClassifier() {
		return this.classifier;
	}
	
	public boolean getFilterNoise() {
		return this.filterNoise;
	}
	
	public int getNoiseThreshold() {
		return noiseThreshold;
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
				return true;
			}
		}
		return false;
	}
	
	public int hashCode() {
		return classifier.hashCode();
	}
}