package org.processmining.plugins.InductiveMiner.mining.cuts.IMin.solve;

import java.lang.reflect.InvocationTargetException;

import org.processmining.framework.packages.PackageManager.Canceller;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.AtomicResult;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.CutFinderIMinInfo;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.SATResult;
import org.processmining.plugins.InductiveMiner.mining.cuts.IMin.solve.single.SATSolveSingle;

public abstract class SATSolve {

	protected final AtomicResult bestTillNow;
	protected final CutFinderIMinInfo info;
	protected final Canceller canceller;

	public SATSolve(CutFinderIMinInfo info, AtomicResult bestTillNow, Canceller canceller) {
		this.info = info;
		this.bestTillNow = bestTillNow;
		this.canceller = canceller;
	}

	public abstract void solve();

	public void solveDefault(final Class<? extends SATSolveSingle> c, boolean commutative) {
		double maxCut;
		if (commutative) {
			maxCut = 0.5 + info.getActivities().size() / 2;
		} else {
			maxCut = info.getActivities().size();
		}
		for (int i = 1; i < maxCut; i++) {
			final int j = i;
			info.getJobList().addJob(new Runnable() {
				public void run() {
					if (!canceller.isCancelled()) {
						SATSolveSingle solver = null;
						try {
							solver = c.getConstructor(CutFinderIMinInfo.class).newInstance(info);
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (SecurityException e) {
							e.printStackTrace();
						}
						SATResult result = solver.solveSingle(j, bestTillNow.get().getProbability());
						if (result != null && result.getProbability() >= bestTillNow.get().getProbability()) {
							if (bestTillNow.maximumAndGet(result)) {
								debug("new maximum " + result);
							}
						}
					}
				}
			});
		}
	}

	protected void debug(String x) {
		if (info.isDebug()) {
			System.out.println(x);
		}
	}
}
