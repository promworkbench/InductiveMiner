package org.processmining.plugins.inductiveminer2.framework.cutfinders;

import java.util.Iterator;

import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntDfg;
import org.processmining.plugins.inductiveminer2.helperclasses.normalised.NormalisedIntGraph;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;

public class Filter {
	public static IMLogInfo filterNoise(IMLogInfo logInfo, float threshold) {
		return new IMLogInfo(logInfo.getNormaliser().clone(), filterNoise(logInfo.getDfg(), threshold),
				logInfo.getActivityMultiSet().clone(), logInfo.getMinimumSelfDistancesBetween(),
				logInfo.getMinimumSelfDistances(), logInfo.getNumberOfEvents(), logInfo.getNumberOfActivityInstances(),
				logInfo.getNumberOfTraces());
	}

	public static NormalisedIntDfg filterNoise(NormalisedIntDfg dfg, float threshold) {
		NormalisedIntDfg newDfg = dfg.clone();

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
	private static void filterDirectlyFollowsGraph(NormalisedIntDfg dfg, float threshold) {
		NormalisedIntGraph graph = dfg.getDirectlyFollowsGraph();

		for (int activity = 0; activity < dfg.getNumberOfActivities(); activity++) {
			//find the maximum outgoing weight of this node
			long maxWeightOut = dfg.getEndActivityCardinality(activity);
			for (long edge : graph.getOutgoingEdgesOf(activity)) {
				maxWeightOut = Math.max(maxWeightOut, (int) graph.getEdgeWeight(edge));
			}

			//remove all edges that are not strong enough
			Iterator<Long> it = graph.getOutgoingEdgesOf(activity).iterator();
			while (it.hasNext()) {
				long edge = it.next();
				if (graph.getEdgeWeight(edge) < maxWeightOut * threshold) {
					it.remove();
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
	private static void filterConcurrencyGraph(NormalisedIntDfg dfg, float threshold) {
		NormalisedIntGraph graph = dfg.getConcurrencyGraph();

		for (int activity = 0; activity < dfg.getNumberOfActivities(); activity++) {
			//find the maximum outgoing weight of this node
			long maxWeightOut = dfg.getEndActivityCardinality(activity);
			for (long edge : graph.getOutgoingEdgesOf(activity)) {
				maxWeightOut = Math.max(maxWeightOut, (int) graph.getEdgeWeight(edge));
			}

			//remove all edges that are not strong enough
			Iterator<Long> it = graph.getOutgoingEdgesOf(activity).iterator();
			while (it.hasNext()) {
				long edge = it.next();
				if (graph.getEdgeWeight(edge) < maxWeightOut * threshold) {
					it.remove();
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
	private static void filterStartActivities(NormalisedIntDfg dfg, float threshold) {
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
	private static void filterEndActivities(NormalisedIntDfg dfg, float threshold) {
		long max = dfg.getMostOccurringEndActivityCardinality();
		for (int activity : dfg.getEndActivityIndices()) {
			if (dfg.getEndActivityCardinality(activity) < threshold * max) {
				dfg.removeEndActivity(activity);
			}
		}
	}
}
