package org.processmining.plugins.inductiveminer2.plugins;

import javax.swing.JComponent;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.contexts.uitopia.annotations.Visualizer;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.InductiveMiner.efficienttree.UnknownTreeNodeException;
import org.processmining.plugins.InductiveMiner.plugins.dialogs.IMMiningDialog;
import org.processmining.plugins.directlyfollowsmodel.DirectlyFollowsModel;
import org.processmining.plugins.graphviz.dot.Dot;
import org.processmining.plugins.graphviz.visualisation.DotPanel;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd2Dot;

public class DfgMsdVisualisationPlugin {
	@Plugin(name = "dfg+msd visualisation", returnLabels = { "Dot visualization" }, returnTypes = {
			JComponent.class }, parameterLabels = { "dfg+msd graph" }, userAccessible = true)
	@Visualizer
	@UITopiaVariant(affiliation = IMMiningDialog.affiliation, author = IMMiningDialog.author, email = IMMiningDialog.email)
	@PluginVariant(variantLabel = "Visualise process tree", requiredParameterLabels = { 0 })
	public DotPanel fancy(PluginContext context, DirectlyFollowsModel dfgMsd) throws UnknownTreeNodeException {
		Dot dot;
		if (dfgMsd.getNumberOfActivities() > 50) {
			dot = new Dot();
			dot.addNode("Graphs with more than 50 nodes are not visualised to prevent hanging...");
		} else {
			dot = DfgMsd2Dot.visualise(dfgMsd);
		}
		return new DotPanel(dot);
	}
}
