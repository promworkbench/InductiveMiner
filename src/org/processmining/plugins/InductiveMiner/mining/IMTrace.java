package org.processmining.plugins.InductiveMiner.mining;

import java.util.BitSet;
import java.util.Iterator;

import org.deckfour.xes.model.XAttributeMap;
import org.deckfour.xes.model.XEvent;
import org.deckfour.xes.model.XTrace;

@Deprecated
public class IMTrace implements Iterable<XEvent> {
	
	public org.processmining.plugins.InductiveMiner.mining.logs.IMTrace wrap() {
		return (org.processmining.plugins.InductiveMiner.mining.logs.IMTrace) this;
	}

	private final int XTraceIndex;
	private final BitSet outEvents;
	private final IMLog log;

	public IMTrace(int XTraceIndex, BitSet outEvents, IMLog log) {
		assert (XTraceIndex >= 0);

		this.XTraceIndex = XTraceIndex;
		this.outEvents = outEvents;
		this.log = log;
	}

	private XTrace getXTrace() {
		return log.getTraceWithIndex(XTraceIndex);
	}

	/**
	 * 
	 * @return Whether the trace contains no events.
	 */
	public boolean isEmpty() {
		int next = outEvents.nextClearBit(0);
		return next == -1 || next >= getXTrace().size();
	}

	/**
	 * @return The number of events in the trace.
	 */
	public int size() {
		return getXTrace().size() - outEvents.cardinality();
	}

	public IMEventIterator iterator() {
		return new IMEventIterator(0, Integer.MAX_VALUE);
	}

	/**
	 * Returns a sublist. This is O(n) in the number of events in the trace.
	 * 
	 * @param from
	 *            index at which the sub list starts. Inclusive.
	 * @param to
	 *            index at which the sub list ends. Exclusive.
	 * @return
	 */
	public Iterable<XEvent> subList(final int from, final int to) {
		return new IMEventIterable(from, to);
	}

	@Deprecated
	public XEvent get(int index) {
		Iterator<XEvent> it = iterator();
		for (int i = 0; i < index; i++) {
			it.next();
		}
		return it.next();
	}

	public String toString() {
		StringBuilder result = new StringBuilder();
		for (XEvent e : this) {
			result.append(log.classify(e));
		}
		return result.toString();
	}

	public XAttributeMap getAttributes() {
		return getXTrace().getAttributes();
	}

	public class IMEventIterable implements Iterable<XEvent> {
		int from;
		int to;

		public IMEventIterable(int from, int to) {
			this.from = from;
			this.to = to;
		}

		public IMEventIterator iterator() {
			return new IMEventIterator(from, to);
		}
	}

	public class IMEventIterator implements Iterator<XEvent> {
		int to;
		int now;
		int next;
		int counter;

		public IMEventIterator(int from, int to) {
			now = -1;
			this.to = Math.min(to, getXTrace().size());
			next = outEvents.nextClearBit(0) < this.to ? outEvents.nextClearBit(0) : -1;
			counter = 0;

			//walk to from
			for (int i = 0; i < from; i++) {
				next();
			}
		}

		public boolean hasNext() {
			return next != -1 && next < getXTrace().size() && counter < to;
		}

		/**
		 * Remove the current XEvent (= last given by next).
		 */
		public void remove() {
			outEvents.set(now);
		}

		public XEvent next() {
			now = next;
			next = outEvents.nextClearBit(next + 1);
			counter++;
			return getXTrace().get(now);
		}

		/**
		 * Split the trace such that the part before the current XEvent moves to
		 * a new trace. This iterator will not be altered. The newly trace will
		 * not be encountered by any trace iterator that was created before
		 * calling split().
		 * 
		 * @return the newly created trace
		 */
		public IMTrace split() {
			//copy this trace completely
			IMTrace newTrace = log.copyTrace(XTraceIndex, outEvents);

			//in the new trace, remove all events from now
			newTrace.outEvents.set(now, getXTrace().size());

			//in this trace, remove all events before now
			outEvents.set(0, now);

			return newTrace;
		}

	}
}
