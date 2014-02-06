package org.processmining.plugins.InductiveMiner.mining;

import org.deckfour.uitopia.api.event.TaskListener.InteractionResult;
import org.deckfour.xes.info.XLogInfo;
import org.deckfour.xes.info.XLogInfoFactory;
import org.deckfour.xes.model.XLog;
import org.processmining.connections.logmodel.LogPetrinetConnectionImpl;
import org.processmining.contexts.uitopia.UIPluginContext;
import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.models.connections.petrinets.EvClassLogPetrinetConnection;
import org.processmining.plugins.InductiveMiner.model.conversion.ProcessTreeModel2PetriNet;
import org.processmining.plugins.InductiveMiner.model.conversion.ProcessTreeModel2PetriNet.WorkflowNet;
import org.processmining.plugins.connectionfactories.logpetrinet.TransEvClassMapping;

public class MiningPluginPetrinet {
	
	public Object[] mineDefaultPetrinet(XLog log) {
		return this.mineParametersPetrinet(log, new MiningParametersIM());
	}
	
	//@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J.J. Leemans", email = "s.j.j.leemans@tue.nl")
	//@PluginVariant(variantLabel = "Mine a Process Tree Petri net, default", requiredParameterLabels = { 0 })
	public Object[] mineDefaultPetrinet(PluginContext context, XLog log) {
		return this.mineParametersPetrinet(context, log, new MiningParametersIM());
	}
	
	public Object[] mineParametersPetrinet(XLog log, MiningParameters parameters) {
		return mineParametersPetrinet(null, log, parameters);
	}
	
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J.J. Leemans", email = "s.j.j.leemans@tue.nl")
	@PluginVariant(variantLabel = "Mine a Petri net, parameterized", requiredParameterLabels = { 0, 1 })
	public Object[] mineParametersPetrinet(PluginContext context, XLog log, MiningParameters parameters) {
		
		//call the connectionless function
		Miner miner = new Miner();
		Object[] arr = miner.mineParametersPetrinetWithoutConnections(log, parameters);
		ProcessTreeModel2PetriNet.WorkflowNet workflowNet = (WorkflowNet) arr[1];
		TransEvClassMapping mapping = (TransEvClassMapping) arr[2];
		
		if (context != null) {
			ProcessTreeModel2PetriNet.addMarkingsToProm(context, workflowNet);
		
			//create connections
			XLogInfo info = XLogInfoFactory.createLogInfo(log, parameters.getClassifier());
			context.addConnection(new LogPetrinetConnectionImpl(log, info.getEventClasses(), workflowNet.petrinet, workflowNet.transition2eventClass));
			
			context.addConnection(new EvClassLogPetrinetConnection("classifier-log-petrinet connection", workflowNet.petrinet, log, parameters.getClassifier(), mapping));
		}
		
		return new Object[] { workflowNet.petrinet, workflowNet.initialMarking, workflowNet.finalMarking };
	}
	
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J.J. Leemans", email = "s.j.j.leemans@tue.nl")
	@PluginVariant(variantLabel = "Mine a Petri net, dialog", requiredParameterLabels = { 0 })
	public Object[] mineGuiPetrinet(UIPluginContext context, XLog log) {
		MiningParameters parameters = new MiningParametersIM();
		MiningDialog dialog = new MiningDialog(log, parameters);
		InteractionResult result = context.showWizard("Mine a Petri net using Inductive Miner", true, true, dialog);
		if (result != InteractionResult.FINISHED) {
			return null;
		}
		return mineParametersPetrinet(context, log, parameters);
	}
}
