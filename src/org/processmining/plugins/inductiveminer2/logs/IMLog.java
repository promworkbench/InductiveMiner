package org.processmining.plugins.inductiveminer2.logs;

public interface IMLog extends Iterable<IMTrace> {

	/**
	 * 
	 * @return The number of traces in the log.
	 */
	public int size();

	/**
	 * Do not mix iterators when removing events.
	 */
	@Override
	public IMTraceIterator iterator();

	public int getNumberOfActivities();

	public String getActivity(int index);

	/**
	 * 
	 * @return a completely independent copy. This is the only method that
	 *         should be used in log splitting, such that extra information can
	 *         be preserved by the log implementation.
	 */
	public IMLog clone();

}
