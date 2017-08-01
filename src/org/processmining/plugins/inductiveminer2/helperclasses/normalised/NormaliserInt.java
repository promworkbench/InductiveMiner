package org.processmining.plugins.inductiveminer2.helperclasses.normalised;

import java.util.List;

import org.processmining.plugins.inductiveminer2.framework.cutfinders.Cut;

import gnu.trove.set.TIntSet;

public interface NormaliserInt {

	public int toNormal(int node);

	public int getNumberOfActivities();

	public List<TIntSet> deNormalise(List<TIntSet> partition);

	public int deNormalise(int normalisedIndex);

	public Cut deNormalise(Cut newCut);

}
