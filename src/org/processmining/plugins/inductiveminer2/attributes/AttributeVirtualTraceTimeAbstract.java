package org.processmining.plugins.inductiveminer2.attributes;

import java.util.Collection;

import org.deckfour.xes.model.XAttributable;

public abstract class AttributeVirtualTraceTimeAbstract extends AttributeVirtual {

	protected long min = Long.MAX_VALUE;
	protected long max = Long.MIN_VALUE;

	@Override
	public final void add(XAttributable x) {
		long value = getTime(x);
		if (value != Long.MIN_VALUE) {
			min = Math.min(value, min);
			max = Math.max(value, max);
		}
	}

	@Override
	public final boolean isLiteral() {
		return false;
	}

	@Override
	public final boolean isNumeric() {
		return false;
	}

	@Override
	public final boolean isTime() {
		return true;
	}

	@Override
	public final boolean isDuration() {
		return false;
	}

	@Override
	public final Collection<String> getStringValues() {
		assert false;
		return null;
	}

	@Override
	public final double getNumericMin() {
		assert false;
		return -Double.MAX_VALUE;
	}

	@Override
	public final double getNumericMax() {
		assert false;
		return -Double.MAX_VALUE;
	}

	@Override
	public final long getTimeMin() {
		return min;
	}

	@Override
	public final long getTimeMax() {
		return max;
	}

	@Override
	public final double getDurationMin() {
		assert false;
		return -Double.MAX_VALUE;
	}

	@Override
	public final double getDurationMax() {
		assert false;
		return -Double.MAX_VALUE;
	}

	@Override
	public final String getLiteral(XAttributable trace) {
		assert false;
		return null;
	}

	@Override
	public final double getNumeric(XAttributable trace) {
		assert false;
		return -Double.MAX_VALUE;
	}

	@Override
	public final double getDuration(XAttributable trace) {
		assert false;
		return -Double.MAX_VALUE;
	}
}