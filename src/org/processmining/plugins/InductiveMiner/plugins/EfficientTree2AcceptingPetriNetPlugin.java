package org.processmining.plugins.InductiveMiner.plugins;

import org.processmining.acceptingpetrinet.models.AcceptingPetriNet;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree2AcceptingPetriNet;

public class EfficientTree2AcceptingPetriNetPlugin {
	@Plugin(name = "Convert efficient tree to Accepting Petri Net and reduce", level = PluginLevel.PeerReviewed, returnLabels = {
			"Accepting Petri net" }, returnTypes = {
					AcceptingPetriNet.class }, parameterLabels = { "Efficient Tree" }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J.J. Leemans", email = "s.j.j.leemans@tue.nl")
	@PluginVariant(variantLabel = "Convert Process Tree to Petri Net, default", requiredParameterLabels = { 0 })
	public AcceptingPetriNet convertAndReduce(PluginContext context, EfficientTree tree) {
		return EfficientTree2AcceptingPetriNet.convert(tree);
	}
}
