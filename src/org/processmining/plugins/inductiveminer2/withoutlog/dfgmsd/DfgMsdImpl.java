package org.processmining.plugins.inductiveminer2.withoutlog.dfgmsd;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.processmining.plugins.inductiveminer2.helperclasses.IntDfgImpl;
import org.processmining.plugins.inductiveminer2.helperclasses.graphs.IntGraph;
import org.processmining.plugins.inductiveminer2.helperclasses.graphs.IntGraphImplQuadratic;

public class DfgMsdImpl extends IntDfgImpl implements DfgMsd {
	private IntGraph minimumSelfDistanceGraph = new IntGraphImplQuadratic();
	private String[] activities;

	public DfgMsdImpl(String[] activities) {
		this.activities = activities;
	}

	public DfgMsdImpl() {
		this.activities = new String[0];
	}

	@Override
	public int addActivity(String activity) {
		int index = ArrayUtils.indexOf(activities, activity);
		if (index < 0) {
			activities = Arrays.copyOf(activities, activities.length + 1);
			index = activities.length - 1;
			activities[index] = activity;
		}
		addActivity(index);
		return index;
	}

	public IntGraph getMinimumSelfDistanceGraph() {
		return minimumSelfDistanceGraph;
	}

	public void setMinimumSelfDistanceGraph(IntGraph minimumSelfDistanceGraph) {
		this.minimumSelfDistanceGraph = minimumSelfDistanceGraph;
	}

	@Override
	public void touchActivity(int index) {
		super.touchActivity(index);
		minimumSelfDistanceGraph.addNode(index);
	}

	public String getActivityOfIndex(int value) {
		return activities[value];
	}

	public DfgMsdImpl clone() {
		DfgMsdImpl result = (DfgMsdImpl) super.clone();

		result.minimumSelfDistanceGraph = this.minimumSelfDistanceGraph.clone();
		result.activities = this.activities.clone();

		return result;
	}

	public String[] getAllActivities() {
		return activities.clone();
	}

}
