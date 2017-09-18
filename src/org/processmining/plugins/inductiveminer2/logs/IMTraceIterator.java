package org.processmining.plugins.inductiveminer2.logs;

import java.util.Iterator;

import org.processmining.plugins.InductiveMiner.mining.logs.XLifeCycleClassifier.Transition;

public interface IMTraceIterator extends Iterator<IMTrace> {

	/**
	 * Get the current trace index.
	 * 
	 * @return
	 */
	public int getTraceIndex();

	/**
	 * Move the iterator to the next trace without constructing a trace object.
	 */
	public void nextFast();

	/**
	 * The within-trace iterator, which is automatically reset upon moving to
	 * the next trace.
	 * 
	 * @return whether there is another event.
	 */
	public boolean itEventHasNext();

	/**
	 * The within-trace iterator, which is automatically reset upon moving to
	 * the next trace.
	 */
	public void itEventNext();

	/**
	 * The within-trace iterator, which is automatically reset upon moving to
	 * the next trace.
	 * 
	 * @return the current activity index.
	 */
	public int itEventGetActivityIndex();

	/**
	 * The within-trace iterator, which is automatically reset upon moving to
	 * the next trace.
	 * 
	 * @return the current life cycle transition index.
	 */
	public Transition itEventGetLifeCycleTransition();

	/**
	 * 
	 * @return whether the current trace is empty.
	 */
	public boolean isEmpty();

	/**
	 * Remove the current trace. Call next() or nextFast() afterwards to
	 * continue.
	 */
	public void remove();

	/**
	 * Remove the current event. Call itEventNext() afterwards to continue.
	 */
	public void itEventRemove();

	/**
	 * @see IMLog.splitTrace()
	 * @return the index of the inserted trace.
	 */
	public int itEventSplit();

	/**
	 * Reset the event iterator.
	 */
	public void itEventReset();

}
