package org.processmining.plugins.InductiveMiner.mining.SAT;

import org.processmining.plugins.InductiveMiner.ThreadPool;
import org.processmining.plugins.InductiveMiner.mining.DirectlyFollowsRelation;
import org.processmining.plugins.InductiveMiner.mining.MiningParameters;

public class SATSolveParallel extends SATSolve {

	public SATSolveParallel(DirectlyFollowsRelation directlyFollowsRelation, MiningParameters parameters, ThreadPool pool, AtomicResult result) {
		super(directlyFollowsRelation, parameters, pool, result);
	}
	
	public void solve() {
		//debug("start SAT search for parallel cut likelier than " + bestTillNow.get().getProbability());
		for (int i = 1; i < 0.5 + directlyFollowsRelation.getDirectlyFollowsGraph().vertexSet().size() / 2; i++) {
			final int j = i;
			pool.addJob(new Runnable() {
				public void run() {
					SATSolveSingle solver = new SATSolveSingleParallel(directlyFollowsRelation, parameters);
					SATResult result = solver.solveSingle(j, bestTillNow.get().getProbability());
					if (result != null && result.getProbability() >= bestTillNow.get().getProbability()) {
						if (bestTillNow.maximumAndGet(result)) {
							debug("new maximum " + result);
						}
					}
				}
			});
		}
	}
}
