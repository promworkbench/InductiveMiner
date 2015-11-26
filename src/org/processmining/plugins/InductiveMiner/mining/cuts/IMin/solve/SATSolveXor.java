package org.processmining.plugins.InductiveMiner.mining.cuts.IMin.solve;

import org.processmining.plugins.InductiveMiner.mining.MinerStateBase;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.AtomicResult;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.CutFinderIMinInfo;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.solve.single.SATSolveSingleXor;

public class SATSolveXor extends SATSolve {

	public SATSolveXor(CutFinderIMinInfo info, AtomicResult result, MinerStateBase minerState) {
		super(info, result, minerState);
	}
	
	public void solve() {
		//debug("start SAT search for exclusive choice cut likelier than " + bestTillNow.get().getProbability());
		solveDefault(SATSolveSingleXor.class, true);
	}

}
