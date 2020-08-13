package org.processmining.plugins.inductiveminer2.attributes;

import java.text.DecimalFormatSymbols;

import org.apache.commons.lang3.math.NumberUtils;
import org.deckfour.xes.model.XAttributable;
import org.deckfour.xes.model.XAttribute;
import org.deckfour.xes.model.XAttributeContinuous;
import org.deckfour.xes.model.XAttributeDiscrete;
import org.deckfour.xes.model.XAttributeTimestamp;

public class AttributeUtils {

	public static double valueDouble(Attribute attribute, XAttributable x) {
		if (attribute.isDuration()) {
			return attribute.getDuration(x);
		} else if (attribute.isNumeric()) {
			return attribute.getNumeric(x);
		} else if (attribute.isTime()) {
			return attribute.getTime(x);
		}
		return -Double.MAX_VALUE;
	}

	public static long parseTimeFast(XAttribute attribute) {
		if (attribute instanceof XAttributeTimestamp) {
			return ((XAttributeTimestamp) attribute).getValueMillis();
		}
		return Long.MIN_VALUE;
	}

	/**
	 * See if the given attribute has a numeric value. Returns -Double.MAX_VALUE
	 * if not.
	 * 
	 * @param attribute
	 * @return
	 */
	public static double parseDoubleFast(XAttribute attribute) {
		if (attribute instanceof XAttributeDiscrete || attribute instanceof XAttributeContinuous) {
			//the attribute was declared to be a number
			if (attribute instanceof XAttributeDiscrete) {
				return ((XAttributeDiscrete) attribute).getValue();
			} else {
				return ((XAttributeContinuous) attribute).getValue();
			}
		} else if (isStringNumeric(attribute.toString())) {
			//the attribute was declared to be a string, check if it is not a number anyway
			return NumberUtils.toDouble(attribute.toString(), -Double.MAX_VALUE);
		}
		return -Double.MAX_VALUE;
	}

	/**
	 * See if the given attribute has a numeric value. Returns Long.MIN_VALUE if
	 * not.
	 * 
	 * @param attribute
	 * @return
	 */
	public static long parseLongFast(XAttribute attribute) {
		if (attribute instanceof XAttributeDiscrete || attribute instanceof XAttributeContinuous) {
			//the attribute was declared to be a number
			if (attribute instanceof XAttributeDiscrete) {
				return ((XAttributeDiscrete) attribute).getValue();
			} else {
				return (long) ((XAttributeContinuous) attribute).getValue();
			}
		} else if (isStringNumeric(attribute.toString())) {
			//the attribute was declared to be a string, check if it is not a number anyway
			return (long) NumberUtils.toDouble(attribute.toString(), Long.MIN_VALUE);
		}
		return Long.MIN_VALUE;
	}

	public static boolean isStringNumeric(String str) {
		DecimalFormatSymbols currentLocaleSymbols = DecimalFormatSymbols.getInstance();
		char localeMinusSign = currentLocaleSymbols.getMinusSign();

		if (str.isEmpty() || !Character.isDigit(str.charAt(0)) && str.charAt(0) != localeMinusSign) {
			return false;
		}

		boolean isDecimalSeparatorFound = false;
		char localeDecimalSeparator = currentLocaleSymbols.getDecimalSeparator();

		for (char c : str.substring(1).toCharArray()) {
			if (!Character.isDigit(c)) {
				if (c == localeDecimalSeparator && !isDecimalSeparatorFound) {
					isDecimalSeparatorFound = true;
					continue;
				}
				return false;
			}
		}
		return true;
	}
}
