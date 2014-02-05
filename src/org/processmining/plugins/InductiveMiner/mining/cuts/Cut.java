package org.processmining.plugins.InductiveMiner.mining.cuts;

import java.util.Collection;
import java.util.Set;

import org.deckfour.xes.classification.XEventClass;

public class Cut {
	public enum Operator {
		xor, sequence, parallel, loop
	}

	private Collection<Set<XEventClass>> partition = null;
	private Operator operator = null;

	public boolean isValid() {
		if (getOperator() == null || getPartition().size() <= 1) {
			return false;
		}
		for (Set<XEventClass> part : getPartition()) {
			if (part.size() == 0) {
				return false;
			}
		}
		return true;
	}
	
	public Cut(Operator operator, Collection<Set<XEventClass>> partition) {
		this.partition = partition;
		this.operator = operator;
	}
	
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(operator);
		result.append(" ");
		result.append(partition);
		return result.toString();
	}

	public Collection<Set<XEventClass>> getPartition() {
		return partition;
	}

	public void setPartition(Collection<Set<XEventClass>> partition) {
		this.partition = partition;
	}

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}
}
