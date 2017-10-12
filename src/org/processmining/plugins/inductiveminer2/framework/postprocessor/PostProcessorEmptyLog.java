package org.processmining.plugins.inductiveminer2.framework.postprocessor;

import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.inductiveminer2.loginfo.IMLogInfo;
import org.processmining.plugins.inductiveminer2.logs.IMLog;
import org.processmining.plugins.inductiveminer2.mining.MinerState;

/**
 * This post processor aims to avoid superfluous skips and consists of two
 * parts: one part replaces a tau of an empty log with a special node, the other
 * part removes this special node again.
 * 
 * @author sander
 *
 */

public class PostProcessorEmptyLog implements PostProcessor {

	public void postProcess(EfficientTree tree, IMLog log, IMLogInfo logInfo, MinerState minerState) {

		//part one: the log is empty
		if (log.size() == 0) {
			if (tree.isTau(tree.getRoot())) {
				tree.getTree()[0] = EfficientTree.skip;
				System.out.println("post process: introduce skip node " + tree.toString());
			}
		} else {
			//part two: the tree is an xor and one child is a skip
			int root = tree.getRoot();
			if (tree.isXor(root)) {
				int childNr = 0;
				while (childNr < tree.getNumberOfChildren(root)) {
					int childIndex = tree.getChild(root, childNr);
					if (tree.isSkip(childIndex)) {
						tree.removeChild(root, childIndex);
						System.out.println("post process: remove skip node " + tree.toString());
					} else {
						childNr++;
					}
				}
			}
		}
	}

}
