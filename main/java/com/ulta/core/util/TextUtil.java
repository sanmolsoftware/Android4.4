/**
 * 
 * Copyright(c) ULTA, Inc. All Rights reserved. 
 *
 *
 */
package com.ulta.core.util;

import com.ulta.core.conf.types.LogLevel;
import com.ulta.core.util.log.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * The Class TextUtil.
 *
 * @author viva
 */
public final class TextUtil {

	/**
	 * Instantiates a new text util.
	 */
	private TextUtil() {
	}

	/** The Constant FRACTIONS. */
	private static final Map<Double, String> FRACTIONS = new HashMap<Double, String>();
	
	/** The Constant IOException. */
	private static final String IOException = "IOException";

	static {
		FRACTIONS.put(0.0, "0");
		FRACTIONS.put(0.25, "1/4");
		FRACTIONS.put(0.33, "1/3");
		FRACTIONS.put(0.50, "1/2");
		FRACTIONS.put(0.75, "3/4");
		FRACTIONS.put(0.66, "2/3");
		FRACTIONS.put(1.0, "1");
	}

	/**
	 * Gets the string representation.
	 *
	 * @param value the value
	 * @return the string representation
	 */
	public static String getStringRepresentation(final double value) {

		final double decimalPart = value % 1;
		long numericPart = (long) (value / 1);
		final StringBuilder builder = new StringBuilder();

		String fractionalString = "";

		if (decimalPart > 0) {

			final String nearestUnit = roundToNearestUnit(decimalPart);

			if ("1".equals(nearestUnit)) {
				numericPart++;
			} else if (!"0".equals(nearestUnit)) {
				fractionalString = nearestUnit;
			}
		}

		if (numericPart > 0) {
			builder.append(numericPart).append(" ");
		}

		if (fractionalString.trim().length() > 0) {
			builder.append(fractionalString).append(" ");
		}
		return builder.toString();
	}

	/**
	 * Round to nearest unit.
	 *
	 * @param decimalPart the decimal part
	 * @return the string
	 */
	private static String roundToNearestUnit(final double decimalPart) {

		double minDifference = 1;
		double selectedFraction = 0;

		for (double fraction : FRACTIONS.keySet()) {
			final double difference = Math.abs(decimalPart - fraction);

			if (difference < minDifference) {
				minDifference = difference;
				selectedFraction = fraction;
			}
		}
		return FRACTIONS.get(selectedFraction);
	}

	/**
	 * Hcf.
	 *
	 * @param args the args
	 * @return the long
	 */
	public static long hcf(final long... args) {

		final List<Long> list = new ArrayList<Long>();

		long[] temp = new long[args.length];
		//long[] temp = Arrays.copyOf(args, args.length);

		long min = min(temp);

		long divisor = 2;

		while (divisor <= min) {

			boolean isDivisible = true;
			inner: for (long value : temp) {
				if (value % divisor != 0) {
					isDivisible = false;
					break inner;
				}
			}

			if (isDivisible) {
				for (int i = 0; i < temp.length; i++) {
					temp[i] /= divisor;
				}
				list.add(divisor);
			} else {
				divisor++;
			}
			min = min(temp);

		}

		long hcf = 1;

		for (long value : list) {
			hcf *= value;
		}
		return hcf;

	}

	/**
	 * Min.
	 *
	 * @param args the args
	 * @return the long
	 */
	private static long min(final long... args) {

		long minValue = Long.MAX_VALUE;

		for (long longValue : args) {
			minValue = Math.min(longValue, minValue);
		}

		return minValue;

	}

	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(final String args[]) {

		final BufferedReader reader = new BufferedReader(new InputStreamReader(
				System.in));

		while (true) {
			final String[] arr;
			try {
				arr = reader.readLine().split(" ");
				for (String a : arr) {
					Logger.Log("{0} === {1}" + "<" + a + ">"
							+ getStringRepresentation(Double.valueOf(a)));

				}
			} catch (IOException ioException) {
				Logger.Log(IOException, LogLevel.ERR);
				Logger.Log(ioException);
			}

		}
	}

}
