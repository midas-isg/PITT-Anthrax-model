package edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax;

import edu.pitt.rods.apollo.ParameterizedComponent;

public class AnthraxModelResultConstants {
	public static final String PROPHYLAXIS_CURVE = "prophylaxis_curve";
	public static final String PROPHYLAXIS_CURVE_TYPE = ParameterizedComponent.PARAM_1D_PRIMITIVE_DOUBLE_ARRAY;

	public static final String FATALITY_CURVE = "fatality_curve";
	public static final String FATALITY_CURVE_TYPE = ParameterizedComponent.PARAM_1D_PRIMITIVE_DOUBLE_ARRAY;

	public static final String IV_CURVE = "iv_curve";
	public static final String IV_CURVE_TYPE = ParameterizedComponent.PARAM_1D_PRIMITIVE_DOUBLE_ARRAY;

	public static final String ICU_CURVE = "iv_curve";
	public static final String ICU_CURVE_TYPE = ParameterizedComponent.PARAM_1D_PRIMITIVE_DOUBLE_ARRAY;

	public static final String RECOVERED_CURVE = "recovered_curve";
	public static final String RECOVERED_CURVE_TYPE = ParameterizedComponent.PARAM_1D_PRIMITIVE_DOUBLE_ARRAY;

	public static final String NUMBER_SICK = "number_sick";

}
