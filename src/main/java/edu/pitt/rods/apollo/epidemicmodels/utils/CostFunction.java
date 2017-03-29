package edu.pitt.rods.apollo.epidemicmodels.utils;

import java.util.HashMap;

public class CostFunction {
	public static final double COST_OF_FATALITY = 833321.0;
	public static final double COST_OF_IV = 6033.34;
	public static final double COST_OF_PROPHYLAXIS = 836.40;
	public static final double COST_OF_ICU = 12385.0;
	public static final double COST_OF_ALERT = 1000000.0;

	/*
	 * by JDL, average private sector cost of all adult and pediatric influenza
	 * vaccines.
	 * 
	 * http://www.cdc.gov/vaccines/programs/vfc/cdc-vac-price-list.htm#adflu
	 */
	public static final double COST_OF_INFLUENZA_VACCINE = 10.99;

	/* by JDL again, just a rough estimate...waiting for bruces input */
	public static final double COST_OF_HAVING_INFLUENZA = 5977.53;
	/* bruce's number */
	public static final double COST_OF_SYMPTOMATIC_INFLUENZA = 5977.53;
	public static final double COST_OF_INFLUENZA_CLINIC = 100.00;
	public static final double COST_OF_INFLUENZA_HOSPITALIZATION = 100.00;
	public static final double COST_OF_INFLUENZA_DEATH = 300.00;

	public enum CostItem {
		ALERT, FATALITY, ICU, IV, PROPHYLAXIS, INFLUENZA_VACCINATION, INFLUENXA_ILLNESS, SYMPTOMATIC_INFLUENZA, INFLUENZA_HOSPITALIZTION, INFLUENZA_CLINIC
	}

	private static HashMap<CostItem, Double> costs;

	static {
		costs = new HashMap<CostItem, Double>();
		costs.put(CostItem.ALERT, COST_OF_ALERT);
		costs.put(CostItem.FATALITY, COST_OF_FATALITY);
		costs.put(CostItem.ICU, COST_OF_ICU);
		costs.put(CostItem.IV, COST_OF_IV);
		costs.put(CostItem.PROPHYLAXIS, COST_OF_PROPHYLAXIS);
		costs.put(CostItem.INFLUENZA_VACCINATION, COST_OF_INFLUENZA_VACCINE);
		costs.put(CostItem.INFLUENXA_ILLNESS, COST_OF_HAVING_INFLUENZA);
		costs.put(CostItem.SYMPTOMATIC_INFLUENZA, COST_OF_SYMPTOMATIC_INFLUENZA);
		costs.put(CostItem.INFLUENZA_CLINIC, COST_OF_INFLUENZA_CLINIC);
		costs.put(CostItem.INFLUENZA_HOSPITALIZTION,
				COST_OF_INFLUENZA_HOSPITALIZATION);

	}

	public static double getCost(CostItem costItem) {
		return costs.get(costItem);

	}

}
