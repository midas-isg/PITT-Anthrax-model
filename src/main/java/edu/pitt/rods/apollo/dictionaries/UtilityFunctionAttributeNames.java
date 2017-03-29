package edu.pitt.rods.apollo.dictionaries;

import edu.pitt.rods.apollo.ParameterizedComponent;

public class UtilityFunctionAttributeNames {
	public static final String NUM_VACCINATED = "numberVaccinated";
	public static final String NUM_VACCINATED_TYPE = "double";
	public static final String NUM_INFECTED = "numberInfected";
	public static final String NUM_INFECTED_TYPE = "double";
	
	public static final String INCIDENCE_CURVE = "incidence_curve";
	public static final String S_CURVE = "s_curve";
	public static final String E_CURVE = "e_curve";
	public static final String I_CURVE = "i_curve";
	public static final String R_CURVE = "r_curve";
	
	public static final String INCIDENCE_CURVE_TYPE = ParameterizedComponent.PARAM_1D_PRIMITIVE_DOUBLE_ARRAY;
	public static final String GLOBAL_SCHOOL_CLOSURE = "global_school_closure";
	public static final String GLOBAL_SCHOOL_CLOSURE_TYPE = "boolean";
	public static final String INDIVIDUAL_SCHOOL_CLOSURE = "individual_school_closure";
	public static final String INDIVIDUAL_SCHOOL_CLOSURE_TYPE = "boolean";

}
