package org.processmining.plugins.InductiveMiner.efficienttree;

import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.And2Or;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.IntShortLanguage;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.LoopLoop;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.LoopTau;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.OrXorTau;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.SameOperator;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.SingleChild;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.TauChildOfOr;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.TauChildOfSeqAndInt;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.XorDoubleSingleChild;
import org.processmining.plugins.InductiveMiner.efficienttree.reductionrules.XorTauTau;

public class EfficientTreeReduceParameters {

	private boolean collapsed;
	private boolean reduceToOr = true;

	/**
	 * 
	 * @param collapsed
	 *            Denotes what a leaf A means: true = seq(xor(tau, A_start), A_complete), false =
	 *            A.
	 */
	public EfficientTreeReduceParameters(boolean collapsed) {
		setCollapsed(collapsed);
	}

	public boolean isCollapsed() {
		return collapsed;
	}

	public void setCollapsed(boolean collapsed) {
		this.collapsed = collapsed;
	}
	
	public boolean isReduceToOr() {
		return reduceToOr;
	}
	
	public void setReduceToOr(boolean reduceToOr) {
		this.reduceToOr = reduceToOr;
	}

	public EfficientTreeReductionRule[] rulesXor = new EfficientTreeReductionRule[] { new SingleChild(),
			new XorTauTau(), new SameOperator(), new XorDoubleSingleChild() };
	public EfficientTreeReductionRule[] rulesSeq = new EfficientTreeReductionRule[] { new SingleChild(),
			new TauChildOfSeqAndInt(), new SameOperator() };
	public EfficientTreeReductionRule[] rulesAndWithReduceToOr = new EfficientTreeReductionRule[] { new SingleChild(),
			new TauChildOfSeqAndInt(), new SameOperator(), new And2Or() };
	public EfficientTreeReductionRule[] rulesAndWithoutReduceToOr = new EfficientTreeReductionRule[] { new SingleChild(),
			new TauChildOfSeqAndInt(), new SameOperator() }; //the basic variant does not use OR's
	public EfficientTreeReductionRule[] rulesLoop = new EfficientTreeReductionRule[] { new LoopLoop(), new LoopTau() };
	public EfficientTreeReductionRule[] rulesIntCollapsed = new EfficientTreeReductionRule[] { new SingleChild(),
			new TauChildOfSeqAndInt(), new SameOperator() };
	public EfficientTreeReductionRule[] rulesIntExpanded = new EfficientTreeReductionRule[] { new SingleChild(),
			new TauChildOfSeqAndInt(), new SameOperator(), new IntShortLanguage() };
	public EfficientTreeReductionRule[] rulesOr = new EfficientTreeReductionRule[] { new SingleChild(),
			new SameOperator(), new TauChildOfOr(), new OrXorTau() };

	public EfficientTreeReductionRule[] getRulesXor() {
		return rulesXor;
	}

	public EfficientTreeReductionRule[] getRulesSequence() {
		return rulesSeq;
	}

	public EfficientTreeReductionRule[] getRulesLoop() {
		return rulesLoop;
	}

	public EfficientTreeReductionRule[] getRulesConcurrent() {
		if (reduceToOr) {
			return rulesAndWithReduceToOr;
		} else {
			return rulesAndWithoutReduceToOr;
		}
	}

	public EfficientTreeReductionRule[] getRulesInterleaved() {
		if (collapsed) {
			return rulesIntCollapsed;
		} else {
			return rulesIntExpanded;
		}
	}
	
	public EfficientTreeReductionRule[] getRulesOr() {
		return rulesOr;
	}
}
