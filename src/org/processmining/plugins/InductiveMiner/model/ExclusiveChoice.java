package org.processmining.plugins.InductiveMiner.model;

import java.util.List;



public class ExclusiveChoice extends Binoperator{

	public ExclusiveChoice(int countChildren) {
		super(countChildren);
	}
	
	public ExclusiveChoice(List<Node> children) {
		super(children);
	}

	public String getOperatorString() {
		return "x";
	}
	
	/*
	public boolean canProduceEpsilon() {
		boolean result = false;
		for (Node child : children) {
			result = result || child.canProduceEpsilon();
		}
		return result;
	}
	
	public Set<Pair<XEventClass, XEventClass>> getDirectlyFollowsEdges() {
		Set<Pair<XEventClass, XEventClass>> result = new HashSet<Pair<XEventClass, XEventClass>>();
		for (Node child : children) {
			result.addAll(child.getDirectlyFollowsEdges());
		}
		return result;
	}
	*/

}
