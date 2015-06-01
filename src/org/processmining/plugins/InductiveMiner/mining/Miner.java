package org.processmining.plugins.InductiveMiner.mining;

import java.util.Iterator;

import org.processmining.plugins.InductiveMiner.conversion.ReduceTree;
import org.processmining.plugins.InductiveMiner.mining.baseCases.BaseCaseFinder;
import org.processmining.plugins.InductiveMiner.mining.cuts.Cut;
import org.processmining.plugins.InductiveMiner.mining.cuts.Cut.Operator;
import org.processmining.plugins.InductiveMiner.mining.cuts.CutFinder;
import org.processmining.plugins.InductiveMiner.mining.fallthrough.FallThrough;
import org.processmining.plugins.InductiveMiner.mining.logSplitter.LogSplitter.LogSplitResult;
import org.processmining.plugins.InductiveMiner.mining.logs.IMLog;
import org.processmining.plugins.InductiveMiner.mining.operators.Interleaved;
import org.processmining.plugins.InductiveMiner.mining.operators.InterleavedXor;
import org.processmining.processtree.Block;
import org.processmining.processtree.Block.Seq;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock;
import org.processmining.processtree.impl.AbstractBlock.Xor;
import org.processmining.processtree.impl.AbstractTask;
import org.processmining.processtree.impl.ProcessTreeImpl;

public class Miner {
	/*
	 * Do not directly call this class, use one of the plugins from the
	 * InductiveMiner.plugins folder
	 */

	public static ProcessTree mine(IMLog log, MiningParameters parameters) {
		//create process tree
		ProcessTree tree = new ProcessTreeImpl();
		MinerState minerState = new MinerState(parameters);
		Node root = mineNode(log, tree, minerState);

		root.setProcessTree(tree);
		tree.setRoot(root);

		debug("discovered tree " + tree.getRoot(), minerState);

		//reduce if necessary
		if (parameters.isReduce()) {
			ReduceTree.reduceTree(tree);
			debug("after reduction " + tree.getRoot(), minerState);
		}

		return tree;
	}

	public static Node mineNode(IMLog log, ProcessTree tree, MinerState minerState) {

		//construct basic information about log
		IMLogInfo logInfo = minerState.parameters.getLog2LogInfo().createLogInfo(log);

		//output information about the log
		debug("\nMine epsilon=" + logInfo.getNumberOfEpsilonTraces() + ", " + logInfo.getActivities(), minerState);
		//debug(log, minerState);

		//find base cases
		Node baseCase = findBaseCases(log, logInfo, tree, minerState);
		if (baseCase != null) {
			return baseCase;
		}

		//find cut
		Cut cut = findCut(log, logInfo, minerState);
		if (cut != null && cut.isValid()) {
			//cut is valid

			debug(" chosen cut: " + cut, minerState);

			//split logs
			LogSplitResult splitResult = splitLog(log, logInfo, cut, minerState);

			//make node
			Block newNode = newNode(cut.getOperator());
			addNode(tree, newNode);

			//recurse
			if (cut.getOperator() != Operator.loop) {
				for (IMLog sublog : splitResult.sublogs) {
					Node child = mineNode(sublog, tree, minerState);
					newNode.addChild(child);
				}
			} else {
				//loop needs special treatment:
				//ProcessTree requires a ternary loop
				Iterator<IMLog> it = splitResult.sublogs.iterator();

				//mine body
				IMLog firstSublog = it.next();
				{
					Node firstChild = mineNode(firstSublog, tree, minerState);
					newNode.addChild(firstChild);
				}

				//mine redo parts by, if necessary, putting them under an xor
				Block redoXor;
				if (splitResult.sublogs.size() > 2) {
					redoXor = new Xor("");
					addNode(tree, redoXor);
					newNode.addChild(redoXor);
				} else {
					redoXor = newNode;
				}
				while (it.hasNext()) {
					IMLog sublog = it.next();
					Node child = mineNode(sublog, tree, minerState);
					redoXor.addChild(child);
				}

				//add tau as third child
				{
					Node tau = new AbstractTask.Automatic("tau");
					addNode(tree, tau);
					newNode.addChild(tau);
				}
			}

			//get rid of the interleaving
			if (cut.getOperator() == Operator.interleaved) {
				newNode = removeInterleaved(newNode);
			}
			
			return newNode;

		} else {
			//cut is not valid; fall through
			return findFallThrough(log, logInfo, tree, minerState);
		}
	}

	private static Block removeInterleaved(Block node) {
		//check whether all children are sequences
		for (Node child : node.getChildren()) {
			if (!(child instanceof Seq)) {
				return node;
			}
		}
		
		//for now, just pick the first children
		Block newNode = new Interleaved("");
		addNode(node.getProcessTree(), newNode);
		
		for (Node grandChild : ((Block) node.getChildren().get(0)).getChildren()) {
			newNode.addChild(grandChild);
		}
		
		return newNode;
	}

	private static Block newNode(Operator operator) {
		switch (operator) {
			case loop :
				return new AbstractBlock.XorLoop("");
			case parallel :
				return new AbstractBlock.And("");
			case sequence :
				return new AbstractBlock.Seq("");
			case xor :
				return new AbstractBlock.Xor("");
			case interleaved :
				return new InterleavedXor("");
			default:
				return null;
		}
	}

	public static void addNode(ProcessTree tree, Node node) {
		node.setProcessTree(tree);
		tree.addNode(node);
	}

	public static Node findBaseCases(IMLog log, IMLogInfo logInfo, ProcessTree tree, MinerState minerState) {
		Node n = null;
		Iterator<BaseCaseFinder> it = minerState.parameters.getBaseCaseFinders().iterator();
		while (n == null && it.hasNext()) {
			n = it.next().findBaseCases(log, logInfo, tree, minerState);
		}
		return n;
	}

	public static Cut findCut(IMLog log, IMLogInfo logInfo, MinerState minerState) {
		Cut c = null;
		Iterator<CutFinder> it = minerState.parameters.getCutFinders().iterator();
		while (it.hasNext() && (c == null || !c.isValid())) {
			c = it.next().findCut(log, logInfo, minerState);
		}
		return c;
	}

	public static Node findFallThrough(IMLog log, IMLogInfo logInfo, ProcessTree tree, MinerState minerState) {
		Node n = null;
		Iterator<FallThrough> it = minerState.parameters.getFallThroughs().iterator();
		while (n == null && it.hasNext()) {
			n = it.next().fallThrough(log, logInfo, tree, minerState);
		}
		return n;
	}

	public static LogSplitResult splitLog(IMLog log, IMLogInfo logInfo, Cut cut, MinerState minerState) {
		LogSplitResult result = minerState.parameters.getLogSplitter().split(log, logInfo, cut, minerState);

		//merge the discarded events of this log splitting into the global discarded events list
		minerState.discardedEvents.addAll(result.discardedEvents);

		return result;
	}

	public static void debug(Object x, MinerState minerState) {
		if (minerState.parameters.isDebug()) {
			System.out.println(x.toString());
		}
	}
}
