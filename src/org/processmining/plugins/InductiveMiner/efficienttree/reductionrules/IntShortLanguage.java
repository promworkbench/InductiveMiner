package org.processmining.plugins.InductiveMiner.efficienttree.reductionrules;

import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeMetrics;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReductionRule;

public class IntShortLanguage implements EfficientTreeReductionRule {

	public boolean apply(EfficientTree tree, int node) {
		if (tree.isInterleaved(node)) {
			//no child should produce a trace of length two or longer
			for (int child : tree.getChildren(node)) {
				if (!EfficientTreeMetrics.traceLengthAtMostOne(tree, child)) {
					return false;
				}
			}

			//transform the interleaved operator into a parallel operator
			tree.getTree()[node] += EfficientTree.concurrent - EfficientTree.interleaved;
			return true;
		}
		return false;
	}

	
}
