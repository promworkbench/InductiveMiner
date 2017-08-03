package org.processmining.plugins.inductiveminer2.plugins;

import javax.swing.JOptionPane;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.model.XLog;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.packages.PackageManager.Canceller;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginLevel;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.mining.InductiveMiner;
import org.processmining.plugins.inductiveminer2.mining.MiningParameters;

public class InductiveMinerPlugin {
	@Plugin(name = "Mine efficient tree with Inductive Miner", level = PluginLevel.Regular, returnLabels = {
			"Process Tree" }, returnTypes = { EfficientTree.class }, parameterLabels = { "Log" }, userAccessible = true)
	@UITopiaVariant(affiliation = InductiveMinerMiningDialog.affiliation, author = InductiveMinerMiningDialog.author, email = InductiveMinerMiningDialog.email)
	@PluginVariant(variantLabel = "Mine a Process Tree, dialog", requiredParameterLabels = { 0 })
	public EfficientTree mineGuiProcessTree(final UIPluginContext context, XLog xLog) {
		InductiveMinerMiningDialog dialog = new InductiveMinerMiningDialog(xLog);
		InteractionResult result = context.showWizard("Mine using Inductive Miner", true, true, dialog);

		if (result != InteractionResult.FINISHED) {
			context.getFutureResult(0).cancel(false);
			return null;
		}

		MiningParameters parameters = dialog.getMiningParameters();
		IMLog log = parameters.getIMLog(xLog);

		//check that the log is not too big and mining might take a long time
		if (!confirmLargeLogs(context, log, dialog)) {
			context.getFutureResult(0).cancel(false);
			return null;
		}

		context.log("Mining...");

		return InductiveMiner.mineEfficientTree(log, parameters, new Canceller() {
			public boolean isCancelled() {
				return context.getProgress().isCancelled();
			}
		});
	}

	public static boolean confirmLargeLogs(final UIPluginContext context, IMLog log, InductiveMinerMiningDialog dialog) {
		if (dialog.getVariant().getWarningThreshold() > 0) {
			int numberOfActivities = log.getNumberOfActivities();
			if (numberOfActivities > dialog.getVariant().getWarningThreshold()) {
				int cResult = JOptionPane.showConfirmDialog(null,
						dialog.getVariant().toString() + " might take a long time, as the event log contains "
								+ numberOfActivities
								+ " activities.\nThe chosen variant of Inductive Miner is exponential in the number of activities.\nAre you sure you want to continue?",
						"Inductive Miner might take a while", JOptionPane.YES_NO_OPTION);

				return cResult == JOptionPane.YES_OPTION;
			}
		}
		return true;
	}
}
