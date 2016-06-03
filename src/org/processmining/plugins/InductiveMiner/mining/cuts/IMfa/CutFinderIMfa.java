package org.processmining.plugins.InductiveMiner.mining.cuts.IMfa;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.plugins.InductiveMiner.dfgOnly.Dfg;
import org.processmining.plugins.InductiveMiner.graphs.Graph;
import org.processmining.plugins.InductiveMiner.mining.IMLogInfo;
import org.processmining.plugins.InductiveMiner.mining.MinerState;
import org.processmining.plugins.InductiveMiner.mining.cuts.Cut;
import org.processmining.plugins.InductiveMiner.mining.cuts.CutFinder;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMa.CutFinderIMaInterleaved;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;

public class CutFinderIMfa implements CutFinder {

	public Cut findCut(IMLog log, IMLogInfo logInfo, MinerState minerState) {
		//filter logInfo
		IMLogInfo logInfoFiltered = filterNoise(logInfo, minerState.parameters.getNoiseThreshold());

		//call IMa cut detection, without log
		Cut cut = CutFinderIMaInterleaved.findCut(null, logInfoFiltered.getDfg(), false);

		return cut;
	}

	/*
	 * filter noise
	 */

	public static IMLogInfo filterNoise(IMLogInfo logInfo, float threshold) {
		return new IMLogInfo(filterNoise(logInfo.getDfg(), threshold), logInfo.getActivities().copy(),
				logInfo.getMinimumSelfDistancesBetween(), logInfo.getMinimumSelfDistances(),
				logInfo.getNumberOfEvents(), logInfo.getNumberOfActivityInstances());
	}

	public static Dfg filterNoise(Dfg dfg, float threshold) {
		Dfg newDfg = dfg.clone();

		filterStartActivities(newDfg, threshold);
		filterEndActivities(newDfg, threshold);
		filterDirectlyFollowsGraph(newDfg, threshold);
		filterConcurrencyGraph(newDfg, threshold);
		return newDfg;
	}

	/**
	 * Filter a graph. Only keep the edges that occur often enough, compared
	 * with other outgoing edges of the source. 0 <= threshold <= 1.
	 * 
	 * @param graph
	 * @param threshold
	 * @return
	 */
	private static void filterDirectlyFollowsGraph(Dfg dfg, float threshold) {
		Graph<XEventClass> graph = dfg.getDirectlyFollowsGraph();

		for (int activity : dfg.getActivityIndices()) {
			//find the maximum outgoing weight of this node
			long maxWeightOut = dfg.getEndActivityCardinality(activity);
			for (long edge : graph.getOutgoingEdgesOf(activity)) {
				maxWeightOut = Math.max(maxWeightOut, (int) graph.getEdgeWeight(edge));
			}

			//add all edges that are strong enough
			for (long edge : graph.getOutgoingEdgesOf(activity)) {
				if (graph.getEdgeWeight(edge) < maxWeightOut * threshold) {
					dfg.removeDirectlyFollowsEdge(edge);
				}
			}
		}
	}

	/**
	 * Filter a graph. Only keep the edges that occur often enough, compared
	 * with other outgoing edges of the source. 0 <= threshold <= 1.
	 * 
	 * @param graph
	 * @param threshold
	 * @return
	 */
	private static void filterConcurrencyGraph(Dfg dfg, float threshold) {
		Graph<XEventClass> graph = dfg.getConcurrencyGraph();

		for (int activity : dfg.getActivityIndices()) {
			//find the maximum outgoing weight of this node
			long maxWeightOut = dfg.getEndActivityCardinality(activity);
			for (long edge : graph.getOutgoingEdgesOf(activity)) {
				maxWeightOut = Math.max(maxWeightOut, (int) graph.getEdgeWeight(edge));
			}

			//add all edges that are strong enough
			for (long edge : graph.getOutgoingEdgesOf(activity)) {
				if (graph.getEdgeWeight(edge) < maxWeightOut * threshold) {
					dfg.removeConcurrencyEdge(edge);
				}
			}
		}
	}

	/**
	 * Filter start activities. Only keep those occurring more times than
	 * threshold * the most occurring activity. 0 <= threshold <= 1.
	 * 
	 * @param activities
	 * @param threshold
	 * @return
	 */
	private static void filterStartActivities(Dfg dfg, float threshold) {
		long max = dfg.getMostOccurringStartActivityCardinality();
		for (int activity : dfg.getStartActivityIndices()) {
			if (dfg.getStartActivityCardinality(activity) < threshold * max) {
				dfg.removeStartActivity(activity);
			}
		}
	}

	/**
	 * Filter start activities. Only keep those occurring more times than
	 * threshold * the most occurring activity. 0 <= threshold <= 1.
	 * 
	 * @param activities
	 * @param threshold
	 * @return
	 */
	private static void filterEndActivities(Dfg dfg, float threshold) {
		long max = dfg.getMostOccurringEndActivityCardinality();
		for (int activity : dfg.getEndActivityIndices()) {
			if (dfg.getEndActivityCardinality(activity) < threshold * max) {
				dfg.removeEndActivity(activity);
			}
		}
	}
}