package org.processmining.plugins.inductiveminer2.attributes;

import java.util.Collection;

import org.deckfour.xes.model.XAttributable;

public abstract class AttributeVirtualTraceNumericAbstract extends AttributeVirtual {

	protected double min = Double.MAX_VALUE;
	protected double max = -Double.MAX_VALUE;

	@Override
	public final void add(XAttributable x) {
		double value = getNumeric(x);
		if (value != -Double.MAX_VALUE) {
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
		return true;
	}

	@Override
	public final boolean isTime() {
		return false;
	}

	@Override
	public final boolean isDuration() {
		return false;
	}

	@Override
	public final Collection<String> getStringValues() {
		return null;
	}

	@Override
	public final double getNumericMin() {
		return min;
	}

	@Override
	public final double getNumericMax() {
		return max;
	}

	@Override
	public final long getTimeMin() {
		return Long.MIN_VALUE;
	}

	@Override
	public final long getTimeMax() {
		return Long.MIN_VALUE;
	}

	@Override
	public final long getDurationMin() {
		return Long.MIN_VALUE;
	}

	@Override
	public final long getDurationMax() {
		return Long.MIN_VALUE;
	}

	@Override
	public final String getLiteral(XAttributable x) {
		return null;
	}

	@Override
	public final long getTime(XAttributable x) {
		return Long.MIN_VALUE;
	}

	@Override
	public final long getDuration(XAttributable x) {
		return Long.MIN_VALUE;
	}

}
