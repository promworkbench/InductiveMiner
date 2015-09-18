package org.processmining.plugins.InductiveMiner.efficienttree;

import org.processmining.plugins.InductiveMiner.mining.interleaved.Interleaved;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;
import org.processmining.processtree.impl.AbstractBlock.And;
import org.processmining.processtree.impl.AbstractBlock.Seq;
import org.processmining.processtree.impl.AbstractBlock.Xor;
import org.processmining.processtree.impl.AbstractBlock.XorLoop;
import org.processmining.processtree.impl.AbstractTask.Automatic;
import org.processmining.processtree.impl.AbstractTask.Manual;
import org.processmining.processtree.impl.ProcessTreeImpl;

public class EfficientTree2processTree {
	public static ProcessTree convert(EfficientTree tree) {
		ProcessTree newTree = new ProcessTreeImpl();
		newTree.setRoot(convert(tree, 0, newTree));
		return newTree;
	}

	public static Node convert(EfficientTree tree, int node, ProcessTree newTree) {
		if (tree.isTau(node)) {
			Node newNode = new Automatic("tau");
			newTree.addNode(newNode);
			return newNode;
		} else if (tree.isActivity(node)) {
			Node newNode = new Manual(tree.getActivityName(node));
			newTree.addNode(newNode);
			return newNode;
		} else if (tree.isOperator(node)) {
			Block newNode;
			if (tree.isXor(node)) {
				newNode = new Xor("");
			} else if (tree.isSequence(node)) {
				newNode = new Seq("");
			} else if (tree.isConcurrent(node)) {
				newNode = new And("");
			} else if (tree.isInterleaved(node)) {
				newNode = new Interleaved("");
			} else if (tree.isLoop(node)) {
				newNode = new XorLoop("");
			} else {
				throw new RuntimeException("not implemented");
			}
			newTree.addNode(newNode);
			for (int child : tree.getChildren(node)) {
				newNode.addChild(convert(tree, child, newTree));
			}
			return newNode;
		}
		throw new RuntimeException("not implemented");
	}
}
