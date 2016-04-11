package org.processmining.plugins.InductiveMiner.conversion;

import org.processmining.contexts.uitopia.annotations.UITopiaVariant;
import org.processmining.framework.plugin.PluginContext;
import org.processmining.framework.plugin.annotations.Plugin;
import org.processmining.framework.plugin.annotations.PluginVariant;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTree2processTree;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduce;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduce.ReductionFailedException;
import org.processmining.plugins.InductiveMiner.efficienttree.EfficientTreeReduceParameters;
import org.processmining.processtree.Block;
import org.processmining.processtree.Node;
import org.processmining.processtree.ProcessTree;

public class ReduceTree {

	@Plugin(name = "Reduce process tree language-equivalently", returnLabels = { "Process Tree" }, returnTypes = { ProcessTree.class }, parameterLabels = { "Process Tree" }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J.J. Leemans", email = "s.j.j.leemans@tue.nl")
	@PluginVariant(variantLabel = "Reduce Process Tree Language-equivalently, default", requiredParameterLabels = { 0 })
	public ProcessTree reduceTree(PluginContext context, ProcessTree tree) {
		return reduceTree(tree, new EfficientTreeReduceParameters(false));
	}
	
	@Plugin(name = "Reduce collapsed process tree language-equivalently", returnLabels = { "Process Tree" }, returnTypes = { ProcessTree.class }, parameterLabels = { "Process Tree" }, userAccessible = true)
	@UITopiaVariant(affiliation = UITopiaVariant.EHV, author = "S.J.J. Leemans", email = "s.j.j.leemans@tue.nl")
	@PluginVariant(variantLabel = "Reduce Process Tree Language-equivalently, default", requiredParameterLabels = { 0 })
	public ProcessTree reduceCollapsedTree(PluginContext context, ProcessTree tree) {
		return reduceTree(tree, new EfficientTreeReduceParameters(true));
	}
	
	public static void reduceChildrenOf(Block node, EfficientTreeReduceParameters reduceParameters) {
		for (Node child : node.getChildren()) {
			//convert child to an efficient tree
			EfficientTree partialTree = new EfficientTree(child);
			try {
				EfficientTreeReduce.reduce(partialTree, reduceParameters);
			} catch (ReductionFailedException e) {
				return;
			}
			EfficientTree2processTree.replaceNode(child, partialTree);
		}
	}

	public static ProcessTree reduceTree(ProcessTree tree, EfficientTreeReduceParameters reduceParameters) {
		EfficientTree efficientTree = new EfficientTree(tree);
		try {
			EfficientTreeReduce.reduce(efficientTree, reduceParameters);
			return EfficientTree2processTree.convert(efficientTree);
		} catch (Exception e) {
			return tree;
		}
	}
}
