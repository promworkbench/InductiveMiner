package org.processmining.plugins.directlyfollowsmodel;

import org.processmining.plugins.inductiveminer2.helperclasses.IntDfg;

public interface DirectlyFollowsModel extends IntDfg {
	public String getActivityOfIndex(int value);

	public String[] getAllActivities();

	public DirectlyFollowsModel clone();
}
