package org.processmining.plugins.inductiveminer2.attributes;

import java.util.Collection;

import org.deckfour.xes.model.XAttributable;

public abstract class AttributeVirtualTraceDurationAbstract extends AttributeVirtual {

	protected double min = Double.MAX_VALUE;
	protected double max = -Double.MAX_VALUE;

	@Override
	public final void add(XAttributable x) {
		double value = getDuration(x);
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
		return false;
	}

	@Override
	public final boolean isDuration() {
		return true;
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
	public final double getDurationMin() {
		return min;
	}

	@Override
	public final double getDurationMax() {
		return max;
	}

	@Override
	public final long getTimeMin() {
		assert false;
		return Long.MIN_VALUE;
	}

	@Override
	public final long getTimeMax() {
		assert false;
		return Long.MIN_VALUE;
	}

	@Override
	public final String getLiteral(XAttributable x) {
		assert false;
		return null;
	}

	@Override
	public final double getNumeric(XAttributable x) {
		assert false;
		return -Double.MAX_VALUE;
	}

	@Override
	public final long getTime(XAttributable x) {
		assert false;
		return Long.MIN_VALUE;
	}
}