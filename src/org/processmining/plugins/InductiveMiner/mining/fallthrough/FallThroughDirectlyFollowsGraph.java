package org.processmining.plugins.InductiveMiner.mining.fallthrough;

import java.util.ArrayList;
import java.util.List;

import org.deckfour.xes.classification.XEventClass;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.processmining.plugins.InductiveMiner.MaybeString;
import org.processmining.plugins.InductiveMiner.Triple;
import org.processmining.plugins.InductiveMiner.mining.IMLog;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.Miner;
import org.processmining.plugins.InductiveMiner.mining.MinerState;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMi.CutFinderIMi;
import org.processmining.plugins.InductiveMiner.mining.metrics.MinerMetrics;
import org.processmining.plugins.InductiveMiner.mining.metrics.PropertyDirectlyFollowsGraph;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;

public class FallThroughDirectlyFollowsGraph implements FallThrough {

	/**
	 * Creates a flower model with a directly-follows graph attached to it
	 */

	private static FallThroughFlower flower = new FallThroughFlower();

	public Node fallThrough(IMLog log, IMLogInfo logInfo, ProcessTree tree, MinerState minerState) {

		Miner.debug(" fall through: directly-follows graph", minerState);

		//mine a flower model
		Node flowerNode = flower.fallThrough(log, logInfo, tree, minerState);

		IMLogInfo filteredLogInfo = CutFinderIMi.filterNoise(logInfo, minerState.parameters.getNoiseThreshold());

		//make a list of weighted edges
		List<Triple<MaybeString, MaybeString, Long>> edges = new ArrayList<Triple<MaybeString, MaybeString, Long>>();
		DefaultDirectedWeightedGraph<XEventClass, DefaultWeightedEdge> graph = filteredLogInfo
				.getDirectlyFollowsGraph();
		for (DefaultWeightedEdge edge : graph.edgeSet()) {
			edges.add(new Triple<MaybeString, MaybeString, Long>(new MaybeString(graph.getEdgeSource(edge).toString()),
					new MaybeString(graph.getEdgeTarget(edge).toString()), (long) graph.getEdgeWeight(edge)));
		}

		//add start activities
		for (XEventClass a : logInfo.getStartActivities()) {
			edges.add(new Triple<MaybeString, MaybeString, Long>(new MaybeString(null), new MaybeString(a.toString()), logInfo.getStartActivities()
					.getCardinalityOf(a)));
		}

		//add end activities
		for (XEventClass a : logInfo.getEndActivities()) {
			edges.add(new Triple<MaybeString, MaybeString, Long>(new MaybeString(a.toString()), new MaybeString(null), logInfo.getEndActivities()
					.getCardinalityOf(a)));
		}

		PropertyDirectlyFollowsGraph property = new PropertyDirectlyFollowsGraph();
		try {
			flowerNode.setIndependentProperty(property, edges);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
			return null;
		} catch (InstantiationException e) {
			e.printStackTrace();
			return null;
		}

		MinerMetrics.attachProducer(flowerNode,
				"fall through: directly-follows graph, " + MinerMetrics.getProducer(flowerNode));

		return flowerNode;
	}

}
