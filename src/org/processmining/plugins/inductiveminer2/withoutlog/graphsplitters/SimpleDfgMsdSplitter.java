package org.processmining.plugins.inductiveminer2.withoutlog.graphsplitters;

import java.util.Iterator;
import java.util.List;

import org.processmining.plugins.InductiveMiner.mining.cuts.Cut.Operator;
import org.processmining.plugins.inductiveminer2.framework.cutfinders.Cut;
import org.processmining.plugins.inductiveminer2.helperclasses.IntDfg;
import org.processmining.plugins.inductiveminer2.withoutlog.MinerStateWithoutLog;
import org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd.DfgMsd;

import gnu.trove.iterator.TIntIterator;
import gnu.trove.map.hash.TIntIntHashMap;
import gnu.trove.set.TIntSet;

public class SimpleDfgMsdSplitter implements DfgMsdSplitter {

	public DfgMsd[] split(DfgMsd graph, Cut cut, MinerStateWithoutLog minerState) {
		return split2(graph, cut.getPartition(), cut.getOperator(), minerState);
	}

	public static void filterDfg(IntDfg graph, IntDfg subDfg, TIntSet sigma, Operator operator, List<TIntSet> partition,
			int sigmaN) {
		TIntIntHashMap node2sigma = getNode2Sigma(partition);

		//remove activities
		{
			Iterator<Integer> it = subDfg.getActivities().iterator();
			while (it.hasNext()) {
				if (!sigma.contains(it.next())) {
					it.remove();
				}
			}
		}

		//remove start activities
		{
			Iterator<Integer> it = subDfg.getStartActivities().iterator();
			while (it.hasNext()) {
				if (!sigma.contains(it.next())) {
					it.remove();
				}
			}
		}

		//remove end activities
		{
			Iterator<Integer> it = subDfg.getEndActivities().iterator();
			while (it.hasNext()) {
				if (!sigma.contains(it.next())) {
					it.remove();
				}
			}
		}

		//walk through the edges (dfg)
		{
			Iterator<Long> it = subDfg.getDirectlyFollowsGraph().getEdges().iterator();
			while (it.hasNext()) {
				long edge = it.next();
				int cardinality = (int) subDfg.getDirectlyFollowsGraph().getEdgeWeight(edge);
				int source = subDfg.getDirectlyFollowsGraph().getEdgeSource(edge);
				int target = subDfg.getDirectlyFollowsGraph().getEdgeTarget(edge);

				if (!sigma.contains(source)) {
					if (!sigma.contains(target)) {
						//edge not in sigma
						it.remove();

						if (operator == Operator.sequence) {
							//add as empty trace
							if (node2sigma.get(source) < sigmaN && node2sigma.get(target) > sigmaN) {
								subDfg.addEmptyTraces(cardinality);
							}
						}
					} else {
						//edge going into sigma
						it.remove();
						if (operator == Operator.sequence || operator == Operator.loop) {
							//add as start activity
							subDfg.getStartActivities().add(target, cardinality);
						}
					}
				} else { //source in sigma
					if (!sigma.contains(target)) {
						//edge going out of sigma
						it.remove();
						if (operator == Operator.sequence || operator == Operator.loop) {
							//source is an end activity
							subDfg.getEndActivities().add(source, cardinality);
						}
					} else {
						//edge within sigma
					}
				}
			}
		}

		if (operator == Operator.sequence) {
			//add empty traces for start activities in sigmas after this one
			for (int sigmaJ = sigmaN + 1; sigmaJ < partition.size(); sigmaJ++) {
				for (TIntIterator it = partition.get(sigmaJ).iterator(); it.hasNext();) {
					int activity = it.next();
					subDfg.addEmptyTraces(graph.getStartActivities().getCardinalityOf(activity));
				}
			}

			//add empty traces for end activities in sigmas before this one
			for (int sigmaJ = 0; sigmaJ < sigmaN; sigmaJ++) {
				for (TIntIterator it = partition.get(sigmaJ).iterator(); it.hasNext();) {
					int activity = it.next();
					subDfg.addEmptyTraces(graph.getEndActivities().getCardinalityOf(activity));
				}
			}
		}
	}

	public static DfgMsd[] split2(DfgMsd graph, List<TIntSet> partition, Operator operator,
			MinerStateWithoutLog minerState) {
		DfgMsd[] subDfgs = new DfgMsd[partition.size()];

		int sigmaN = 0;
		for (TIntSet sigma : partition) {
			DfgMsd subDfg = graph.clone();
			subDfgs[sigmaN] = subDfg;

			filterDfg(graph, subDfg, sigma, operator, partition, sigmaN);

			//walk through the edges (msd)
			{
				Iterator<Long> it = subDfg.getMinimumSelfDistanceGraph().getEdges().iterator();
				while (it.hasNext()) {
					long edge = it.next();
					int source = subDfg.getMinimumSelfDistanceGraph().getEdgeSource(edge);
					int target = subDfg.getMinimumSelfDistanceGraph().getEdgeTarget(edge);

					if (!sigma.contains(source) || !sigma.contains(target)) {
						it.remove();
					}
				}
			}

			sigmaN++;
		}

		return subDfgs;
	}

	private static TIntIntHashMap getNode2Sigma(List<TIntSet> partition) {
		TIntIntHashMap node2sigma = new TIntIntHashMap(10, 0.5f, -1, -1);
		int sigmaN = 0;
		for (TIntSet sigma : partition) {
			TIntIterator it = sigma.iterator();
			while (it.hasNext()) {
				node2sigma.put(it.next(), sigmaN);
			}
			sigmaN++;
		}
		return node2sigma;
	}

}
