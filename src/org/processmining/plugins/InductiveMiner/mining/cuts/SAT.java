package org.processmining.plugins.InductiveMiner.mining.cuts;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;
import org.processmining.plugins.InductiveMiner.Pair;
import org.processmining.plugins.InductiveMiner.mining.DirectlyFollowsRelation;
import org.sat4j.pb.IPBSolver;
import org.sat4j.pb.SolverFactory;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.TimeoutException;

public abstract class SAT {

	public abstract class Var {
		private final int varInt;
		private boolean result;

		public Var(int varInt) {
			this.varInt = varInt;
		}

		public int getVarInt() {
			return varInt;
		}

		@Override
		public abstract String toString();

		public boolean isResult() {
			return result;
		}

		public void setResult(boolean result) {
			this.result = result;
		}
	}

	public class Node extends Var {
		private final XEventClass activity;

		public Node(int varInt, XEventClass activity) {
			super(varInt);
			this.activity = activity;
		}

		public XEventClass getActivity() {
			return activity;
		}

		public String toString() {
			return activity.toString();
		}
	}

	public class Edge extends Var {
		private final XEventClass from;
		private final XEventClass to;

		public Edge(int varInt, XEventClass from, XEventClass to) {
			super(varInt);
			this.from = from;
			this.to = to;
		}

		public String toString() {
			return from.toString() + "->" + to.toString();
		}

	}

	protected Map<Integer, Var> varInt2var;
	protected int varCounter;
	protected IPBSolver solver;
	protected Map<XEventClass, Node> node2var;
	protected final DirectlyFollowsRelation directlyFollowsRelation;
	protected final Double threshold;
	protected int countNodes;
	XEventClass[] nodes;

	protected SAT(DirectlyFollowsRelation directlyFollowsRelation, double threshold) {
		this.threshold = threshold;
		this.directlyFollowsRelation = directlyFollowsRelation;
		countNodes = directlyFollowsRelation.getDirectlyFollowsGraph().vertexSet().size();

		//make a list of nodes instead of the set
		nodes = new XEventClass[countNodes];
		{
			int i = 0;
			for (XEventClass a : directlyFollowsRelation.getDirectlyFollowsGraph().vertexSet()) {
				nodes[i] = a;
				i++;
			}
		}
	}

	public abstract Object[] solve();

	protected void newSolver() {
		varInt2var = new HashMap<Integer, ParallelCutSAT.Var>();
		varCounter = 1;
		solver = SolverFactory.newDefaultOptimizer();

		//initialise nodes
		node2var = new HashMap<XEventClass, Node>();
		for (XEventClass a : directlyFollowsRelation.getDirectlyFollowsGraph().vertexSet()) {
			Node var = new Node(varCounter, a);
			node2var.put(a, var);
			varInt2var.put(varCounter, var);

			varCounter++;
		}
	}

	protected Pair<Set<XEventClass>, Set<XEventClass>> compute() throws TimeoutException {
		IProblem problem = solver;
		if (problem.isSatisfiable()) {

			//process resulting model
			int[] model = problem.model();
			for (int i = 0; i < model.length; i++) {
				int x = model[i];
				varInt2var.get(Math.abs(x)).setResult(x > 0);
			}

			//debug nodes
			Set<XEventClass> sigma1 = new HashSet<XEventClass>();
			Set<XEventClass> sigma2 = new HashSet<XEventClass>();
			for (XEventClass a : node2var.keySet()) {
				if (node2var.get(a).isResult()) {
					sigma1.add(a);
				} else {
					sigma2.add(a);
				}
			}
			debug("  solution: " + sigma1 + ", " + sigma2);
			return new Pair<Set<XEventClass>, Set<XEventClass>>(sigma1, sigma2);
		} else {
			debug("  no solution");
		}
		return null;
	}

	protected static void debug(String x) {
		System.out.println(x);
	}
}