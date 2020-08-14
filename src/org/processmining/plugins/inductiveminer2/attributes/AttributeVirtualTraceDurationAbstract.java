package org.processmining.plugins.inductiveminer2.attributes;

import java.util.Collection;

import org.deckfour.xes.model.XAttributable;

public abstract class AttributeVirtualTraceDurationAbstract extends AttributeVirtual {

	protected long min = Long.MAX_VALUE;
	protected long max = Long.MIN_VALUE;

	@Override
	public final void add(XAttributable x) {
		long value = getDuration(x);
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
		return null;
	}

	@Override
	public final double getNumericMin() {
		return -Double.MAX_VALUE;
	}

	@Override
	public final double getNumericMax() {
		return -Double.MAX_VALUE;
	}

	@Override
	public final long getDurationMin() {
		return min;
	}

	@Override
	public final long getDurationMax() {
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
	public final String getLiteral(XAttributable x) {
		return null;
	}

	@Override
	public final double getNumeric(XAttributable x) {
		return -Double.MAX_VALUE;
	}

	@Override
	public final long getTime(XAttributable x) {
		return Long.MIN_VALUE;
	}
}