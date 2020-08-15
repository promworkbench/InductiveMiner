package org.processmining.plugins.inductiveminer2.attributes;

import java.util.Collection;

import org.deckfour.xes.model.XAttributable;

public interface Attribute extends Comparable<Attribute> {

	public String getName();

	public boolean isVirtual();

	public boolean isLiteral();

	public boolean isNumeric();

	public boolean isTime();

	public boolean isDuration();

	public Collection<String> getStringValues();

	/**
	 * The literal value, or null if it does not exist.
	 * 
	 * @param x
	 * @return
	 */

	public String getLiteral(XAttributable x);

	public double getNumericMin();

	public double getNumericMax();

	/**
	 * The numeric value, or -Double.MAX_VALUE if it does not exist.
	 * 
	 * @param x
	 * @return
	 */
	public double getNumeric(XAttributable x);

	public long getTimeMin();

	public long getTimeMax();

	/**
	 * The time value, or Long.MIN_VALUE if it does not exist.
	 * 
	 * @param x
	 * @return
	 */
	public long getTime(XAttributable x);

	public double getDurationMin();

	public double getDurationMax();

	/**
	 * The duration value, or -Double.MAX_VALUE if it does not exist.
	 * 
	 * @param x
	 * @return
	 */
	public double getDuration(XAttributable x);

	public int compareTo(Attribute arg0);

	public int hashCode();

	public boolean equals(Object obj);
}