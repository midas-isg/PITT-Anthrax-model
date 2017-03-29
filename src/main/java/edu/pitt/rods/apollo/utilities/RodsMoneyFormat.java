package edu.pitt.rods.apollo.utilities;

import java.text.DecimalFormat;

/**
 * This utility class is designed to take a Double and return the financial
 * representation of the number.
 * 
 * @author JDL50
 * 
 */
public class RodsMoneyFormat {
	/**
	 * 
	 * 78,000 => 78K 373,000 => 373K 1,200,000 = 1.02M 37,233,200 = 37.23M
	 * 999,000,000 => 999M 1,000,000,000 => 1B
	 * 
	 * @param amount
	 * @return
	 */
	public static String format(Double amount) {
		DecimalFormat df = new DecimalFormat("##0.0#");
		boolean negative = 0 > amount;
		String str = "$";
		if (negative) {
			str = "-$";
		}
		amount = Math.abs(amount);
		if (Math.abs(amount) < 1000) {
			return str + df.format(amount);
		} else if (Math.abs((amount / 1000)) < 1000) {
			return (str + df.format(amount / 1000) + "K");
		} else if (Math.abs((amount / 1000000)) < 1000) {
			return (str + df.format(amount / 1000000) + "M");
		} else if (Math.abs((amount / 1000000000)) < 1000) {
			return (str + df.format(amount / 1000000000) + "B");
		} else {
			return (str + df.format(amount / 1000000000000.0) + "T");
		}
	}

	public static void main(String[] args) {
		System.out.println(RodsMoneyFormat.format(78900.0));
		System.out.println(RodsMoneyFormat.format(7890000.0));
		System.out.println(RodsMoneyFormat.format(786544900.0));
		System.out.println(RodsMoneyFormat.format(1000000000.0));
		System.out.println(RodsMoneyFormat.format(31000000000000.0));
	}
}
