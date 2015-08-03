package org.processmining.plugins.InductiveMiner.plugins.dialogs;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.classification.XEventClassifier;
import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.Classifiers;
import org.processmining.plugins.InductiveMiner.Classifiers.ClassifierWrapper;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersEKS;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIM;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMi;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMi2;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMin;
import org.processmining.plugins.InductiveMiner.mining.MiningParametersIMlc;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class IMMiningDialog extends JPanel {

	private static final long serialVersionUID = 7693870370139578439L;
	private final ParametersWrapper p = new ParametersWrapper();
	private final JComboBox<Variant> variantCombobox;
	private final JLabel noiseLabel;
	private final JSlider noiseSlider;
	private final JLabel noiseValue;

	public class ParametersWrapper {
		public MiningParameters parameters;
	}

	public abstract class Variant {
		@Override
		public abstract String toString();

		public abstract boolean hasNoise();

		public abstract boolean noNoiseImpliesFitness();

		public abstract MiningParameters getMiningParameters();
	}

	public class VariantIM extends Variant {
		public String toString() {
			return "Inductive Miner";
		}

		public boolean hasNoise() {
			return false;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersIM();
		}

		public boolean noNoiseImpliesFitness() {
			return false;
		}
	}

	public class VariantIMi extends Variant {
		public String toString() {
			return "Inductive Miner - infrequent";
		}

		public boolean hasNoise() {
			return true;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersIMi();
		}

		public boolean noNoiseImpliesFitness() {
			return true;
		}
	}

	public class VariantIMin extends Variant {
		public String toString() {
			return "Inductive Miner - incompleteness";
		}

		public boolean hasNoise() {
			return false;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersIMin();
		}

		public boolean noNoiseImpliesFitness() {
			return false;
		}
	}

	public class VariantIMEKS extends Variant {
		public String toString() {
			return "Inductive Miner - exhaustive K-successor";
		}

		public boolean hasNoise() {
			return false;
		}

		public boolean noNoiseImpliesFitness() {
			return false;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersEKS();
		}
	}

	public class VariantIMlc extends Variant {

		public String toString() {
			return "Inductive Miner - life cycle";
		}

		public boolean hasNoise() {
			return false;
		}

		public boolean noNoiseImpliesFitness() {
			return false;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersIMlc();
		}
	}

	public class VariantIMilc extends Variant {

		public String toString() {
			return "Inductive Miner - infrequent & life cycle";
		}

		public boolean hasNoise() {
			return true;
		}

		public boolean noNoiseImpliesFitness() {
			return false;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersIMlc();
		}
	}

	public class VariantIMi2 extends Variant {

		public String toString() {
			return "Inductive Miner - infrequent 2";
		}

		public boolean hasNoise() {
			return true;
		}

		public boolean noNoiseImpliesFitness() {
			return false;
		}

		public MiningParameters getMiningParameters() {
			return new MiningParametersIMi2();
		}
	}

	@SuppressWarnings("unchecked")
	public IMMiningDialog(XLog log) {
		p.parameters = new MiningParametersIMi();
		SlickerFactory factory = SlickerFactory.instance();

		int gridy = 1;

		setLayout(new GridBagLayout());

		//algorithm
		final JLabel variantLabel = factory.createLabel("Variant");
		{
			GridBagConstraints cVariantLabel = new GridBagConstraints();
			cVariantLabel.gridx = 0;
			cVariantLabel.gridy = gridy;
			cVariantLabel.weightx = 0.4;
			cVariantLabel.anchor = GridBagConstraints.NORTHWEST;
			add(variantLabel, cVariantLabel);
		}

		variantCombobox = factory.createComboBox(new Variant[] { new VariantIM(), new VariantIMi(), new VariantIMin(),
				new VariantIMEKS(), new VariantIMlc(), new VariantIMilc(), new VariantIMi2() });
		{
			GridBagConstraints cVariantCombobox = new GridBagConstraints();
			cVariantCombobox.gridx = 1;
			cVariantCombobox.gridy = gridy;
			cVariantCombobox.anchor = GridBagConstraints.NORTHWEST;
			cVariantCombobox.fill = GridBagConstraints.HORIZONTAL;
			cVariantCombobox.weightx = 0.6;
			add(variantCombobox, cVariantCombobox);
			variantCombobox.setSelectedIndex(1);
		}

		gridy++;

		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}

		gridy++;

		//noise threshold
		noiseLabel = factory.createLabel("Noise threshold");
		{
			GridBagConstraints cNoiseLabel = new GridBagConstraints();
			cNoiseLabel.gridx = 0;
			cNoiseLabel.gridy = gridy;
			cNoiseLabel.anchor = GridBagConstraints.WEST;
			add(noiseLabel, cNoiseLabel);
		}

		noiseSlider = factory.createSlider(SwingConstants.HORIZONTAL);
		{
			noiseSlider.setMinimum(0);
			noiseSlider.setMaximum(1000);
			noiseSlider.setValue((int) (p.parameters.getNoiseThreshold() * 1000));
			GridBagConstraints cNoiseSlider = new GridBagConstraints();
			cNoiseSlider.gridx = 1;
			cNoiseSlider.gridy = gridy;
			cNoiseSlider.fill = GridBagConstraints.HORIZONTAL;
			add(noiseSlider, cNoiseSlider);
		}

		noiseValue = factory.createLabel(String.format("%.2f", p.parameters.getNoiseThreshold()));
		{
			GridBagConstraints cNoiseValue = new GridBagConstraints();
			cNoiseValue.gridx = 2;
			cNoiseValue.gridy = gridy;
			add(noiseValue, cNoiseValue);
		}

		gridy++;

		final JLabel noiseExplanation = factory.createLabel("If set to 0.00, perfect log fitness is guaranteed.");
		{
			GridBagConstraints cNoiseExplanation = new GridBagConstraints();
			cNoiseExplanation.gridx = 1;
			cNoiseExplanation.gridy = gridy;
			cNoiseExplanation.gridwidth = 3;
			cNoiseExplanation.anchor = GridBagConstraints.WEST;
			add(noiseExplanation, cNoiseExplanation);
		}

		gridy++;

		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}

		gridy++;

		//spacer
		{
			JLabel spacer = factory.createLabel(" ");
			GridBagConstraints cSpacer = new GridBagConstraints();
			cSpacer.gridx = 0;
			cSpacer.gridy = gridy;
			cSpacer.anchor = GridBagConstraints.WEST;
			add(spacer, cSpacer);
		}

		gridy++;

		//classifiers
		{
			final JLabel classifierLabel = factory.createLabel("Event classifier");
			GridBagConstraints cClassifierLabel = new GridBagConstraints();
			cClassifierLabel.gridx = 0;
			cClassifierLabel.gridy = gridy;
			cClassifierLabel.weightx = 0.4;
			cClassifierLabel.anchor = GridBagConstraints.NORTHWEST;
			add(classifierLabel, cClassifierLabel);
		}

		final JComboBox<ClassifierWrapper> classifiers = factory.createComboBox(Classifiers.getClassifiers(log));
		{
			GridBagConstraints cClassifiers = new GridBagConstraints();
			cClassifiers.gridx = 1;
			cClassifiers.gridy = gridy;
			cClassifiers.anchor = GridBagConstraints.NORTHWEST;
			cClassifiers.fill = GridBagConstraints.HORIZONTAL;
			cClassifiers.weightx = 0.6;
			add(classifiers, cClassifiers);
		}

		gridy++;

		{
			GridBagConstraints gbcFiller = new GridBagConstraints();
			gbcFiller.weighty = 1.0;
			gbcFiller.gridy = gridy;
			gbcFiller.fill = GridBagConstraints.BOTH;
			add(Box.createGlue(), gbcFiller);
		}

		variantCombobox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Variant variant = (Variant) variantCombobox.getSelectedItem();
				float noise = p.parameters.getNoiseThreshold();
				XEventClassifier classifier = p.parameters.getClassifier();
				p.parameters = variant.getMiningParameters();
				p.parameters.setNoiseThreshold(noise);
				p.parameters.setClassifier(classifier);
				if (variant.hasNoise()) {
					noiseValue.setText(String.format("%.2f", p.parameters.getNoiseThreshold()));
				} else {
					int width = noiseValue.getWidth();
					int height = noiseValue.getHeight();
					noiseValue.setText("  ");
					noiseValue.setPreferredSize(new Dimension(width, height));
				}

				noiseLabel.setVisible(variant.hasNoise());
				noiseSlider.setVisible(variant.hasNoise());
				noiseExplanation.setVisible(variant.noNoiseImpliesFitness());
			}
		});

		noiseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				p.parameters.setNoiseThreshold((float) (noiseSlider.getValue() / 1000.0));
				noiseValue.setText(String.format("%.2f", p.parameters.getNoiseThreshold()));
			}
		});

		classifiers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				p.parameters.setClassifier(((ClassifierWrapper) classifiers.getSelectedItem()).classifier);
			}
		});
	}

	public MiningParameters getMiningParameters() {
		return p.parameters;
	}

}
