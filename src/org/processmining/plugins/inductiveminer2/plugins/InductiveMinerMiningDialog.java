package org.processmining.plugins.inductiveminer2.plugins;

import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.Box;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.deckfour.xes.model.XLog;
import org.processmining.plugins.InductiveMiner.ClassifierChooser;
import org.processmining.plugins.inductiveminer2.mining.MiningParameters;
import org.processmining.plugins.inductiveminer2.variants.InductiveMinerVariant;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIM;
import org.processmining.plugins.inductiveminer2.variants.MiningParametersIMPartialTraces;

import com.fluxicon.slickerbox.factory.SlickerFactory;

public class InductiveMinerMiningDialog extends JPanel {

	private static final long serialVersionUID = 7693870370139578439L;
	private final JComboBox<InductiveMinerVariant> variantCombobox;
	private final JLabel noiseLabel;
	private final JSlider noiseSlider;
	private final JLabel noiseValue;
	private final JLabel doiLabel;
	private final JLabel doiValue;

	public static final String email = "s.leemans@qut.edu.au";
	public static final String affiliation = "Queensland University of Technology";
	public static final String author = "S.J.J. Leemans";

	private final InductiveMinerVariant[] variants = new InductiveMinerVariant[] { new MiningParametersIM(),
			new MiningParametersIMPartialTraces() };

	@SuppressWarnings("unchecked")
	public InductiveMinerMiningDialog(XLog log) {
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

		//		variantCombobox = factory.createComboBox(new Variant[] { new VariantIM(), new VariantIMf(), new VariantIMa(),
		//				new VariantIMfa(), new VariantIMc(), new VariantIMEKS(), new VariantIMlc(), new VariantIMflc(),
		//				new VariantIMpt(), new VariantIMfpt(), new VariantIMcpt() });
		variantCombobox = factory.createComboBox(variants);
		{
			GridBagConstraints cVariantCombobox = new GridBagConstraints();
			cVariantCombobox.gridx = 1;
			cVariantCombobox.gridy = gridy;
			cVariantCombobox.anchor = GridBagConstraints.NORTHWEST;
			cVariantCombobox.fill = GridBagConstraints.HORIZONTAL;
			cVariantCombobox.weightx = 0.6;
			add(variantCombobox, cVariantCombobox);
			//variantCombobox.setSelectedIndex(1);
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
			GridBagConstraints cNoiseSlider = new GridBagConstraints();
			cNoiseSlider.gridx = 1;
			cNoiseSlider.gridy = gridy;
			cNoiseSlider.fill = GridBagConstraints.HORIZONTAL;
			add(noiseSlider, cNoiseSlider);
		}

		noiseValue = factory.createLabel(String.format("%.2f", getMiningParameters().getNoiseThreshold()));
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

		final ClassifierChooser classifiers = new ClassifierChooser(log);
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

		//doi
		{
			doiLabel = factory.createLabel("More information");
			GridBagConstraints cDoiLabel = new GridBagConstraints();
			cDoiLabel.gridx = 0;
			cDoiLabel.gridy = gridy;
			cDoiLabel.weightx = 0.4;
			cDoiLabel.anchor = GridBagConstraints.NORTHWEST;
			add(doiLabel, cDoiLabel);
		}

		{
			doiValue = factory.createLabel("doi doi");
			GridBagConstraints cDoiValue = new GridBagConstraints();
			cDoiValue.gridx = 1;
			cDoiValue.gridy = gridy;
			cDoiValue.anchor = GridBagConstraints.NORTHWEST;
			cDoiValue.weightx = 0.6;
			add(doiValue, cDoiValue);
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
				InductiveMinerVariant variant = (InductiveMinerVariant) variantCombobox.getSelectedItem();
				if (variant.hasNoise()) {
					noiseValue.setText(String.format("%.2f", getMiningParameters().getNoiseThreshold()));
				} else {
					int width = noiseValue.getWidth();
					int height = noiseValue.getHeight();
					noiseValue.setText("  ");
					noiseValue.setPreferredSize(new Dimension(width, height));
				}

				if (variant.getDoi() != null) {
					doiLabel.setVisible(true);
					doiValue.setVisible(true);
					doiValue.setText(variant.getDoi());
				} else {
					doiLabel.setVisible(false);
					doiValue.setVisible(false);
				}

				noiseLabel.setVisible(variant.hasNoise());
				noiseSlider.setVisible(variant.hasNoise());
				noiseExplanation.setVisible(variant.noNoiseImpliesFitness());
			}
		});

		noiseSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				for (InductiveMinerVariant variant : variants) {
					variant.getMiningParameters().setNoiseThreshold((float) (noiseSlider.getValue() / 1000.0));
				}
				noiseValue.setText(String.format("%.2f", getMiningParameters().getNoiseThreshold()));
			}
		});
		noiseSlider.setValue((int) (getMiningParameters().getNoiseThreshold() * 1000));

		classifiers.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				for (InductiveMinerVariant variant : variants) {
					variant.getMiningParameters().setClassifier(classifiers.getSelectedClassifier());
				}
			}
		});
		for (InductiveMinerVariant variant : variants) {
			variant.getMiningParameters().setClassifier(classifiers.getSelectedClassifier());
		}

		doiValue.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				String doi = ((InductiveMinerVariant) variantCombobox.getSelectedItem()).getDoi();
				if (doi != null) {
					openWebPage(doi);
				}
			}
		});
		doiValue.setText(((InductiveMinerVariant) variantCombobox.getSelectedItem()).getDoi());
	}

	public MiningParameters getMiningParameters() {
		return ((InductiveMinerVariant) variantCombobox.getSelectedItem()).getMiningParameters();
	}

	public static void openWebPage(String url) {
		if (Desktop.isDesktopSupported()) {
			Desktop desktop = Desktop.getDesktop();
			try {
				desktop.browse(new URI(url));
			} catch (IOException | URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			Runtime runtime = Runtime.getRuntime();
			try {
				runtime.exec("xdg-open " + url);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public InductiveMinerVariant getVariant() {
		return (InductiveMinerVariant) variantCombobox.getSelectedItem();
	}

}
