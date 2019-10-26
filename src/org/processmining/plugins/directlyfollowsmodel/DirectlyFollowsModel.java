package org.processmining.plugins.directlyfollowsmodel;

import org.processmining.plugins.inductiveminer2.helperclasses.IntDfg;

/**
 * Moved to the DirectlyFollowsModelMiner package
 * @author sander
 *
 */
@Deprecated
public interface DirectlyFollowsModel extends IntDfg {
	public String getActivityOfIndex(int value);

	public String[] getAllActivities();

	public DirectlyFollowsModel clone();

	/**
	 * Adds an activity.
	 * 
	 * @param activity
	 * @return the index of the activity.
	 */
	public int addActivity(String activity);
	
	public int hashCode();
	
	public boolean equals(Object obj);
}
