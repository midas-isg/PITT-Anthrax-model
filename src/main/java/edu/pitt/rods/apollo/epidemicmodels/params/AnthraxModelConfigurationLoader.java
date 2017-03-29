package edu.pitt.rods.apollo.epidemicmodels.params;

import edu.pitt.rods.apollo.ParameterizedComponent;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.ASX_TO_PROD_MU;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.ASX_TO_PROD_SIGMA;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.ASYM_COMP_SIZE;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.BEGIN_TREATMENT_INTERVAL;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.EXP_COMP_SIZE;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.FUL_TO_DEAD_MU;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.FUL_TO_DEAD_SIGMA;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.FUL_TREAT_EFFICACY;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.PROD_TO_FUL_MU;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.PROD_TO_FUL_SIGMA;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.PROD_TREAT_EFFICACY;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.PROPH_SCHEDULE;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.PROPH_TREAT_EFFICACY;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.RUN_DURATION;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.UNEXP_COMP_SIZE;
import edu.pitt.rods.apollo.epidemicmodels.utils.EpiModelParamStringBuilder;
import edu.pitt.rods.apollo.exception.ConfigurationLoadException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 *
 * Author: Nick Millett Email: nick.millett@gmail.com Date: Jun 19, 2014 Time: 10:45:25 AM Class: AnthraxModelConfigurationLoader IDE: NetBeans 6.9.1
 */
public class AnthraxModelConfigurationLoader {

	private static final String[] DOUBLE_PARAMS = {UNEXP_COMP_SIZE, ASYM_COMP_SIZE,
		PROPH_TREAT_EFFICACY, PROD_TREAT_EFFICACY, FUL_TREAT_EFFICACY, ASX_TO_PROD_MU, ASX_TO_PROD_SIGMA,
	PROD_TO_FUL_MU, PROD_TO_FUL_SIGMA, FUL_TO_DEAD_MU, FUL_TO_DEAD_SIGMA};
	private static final Map<String, String> PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS;
	private static final String[] INTEGER_PARAMS = {BEGIN_TREATMENT_INTERVAL, RUN_DURATION};
	private static final Map<String, String> PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS;
	private static final String[] DOUBLE_ARRAY_PARAMS = {PROPH_SCHEDULE};
	private int runLength;
	private final Set<String> setParams;

	static {
		PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS = new HashMap<String, String>();
		PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.put(PROPH_TREAT_EFFICACY, "0.9");
		PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.put(PROD_TREAT_EFFICACY, "0.55");
		PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.put(FUL_TREAT_EFFICACY, "0.55");

		PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS = new HashMap<String, String>();
		PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS.put(BEGIN_TREATMENT_INTERVAL, "36");
	}

	public AnthraxModelConfigurationLoader() {
		setParams = new HashSet<String>();
	}

	public EpiModelParamStringBuilder loadAnthraxModelParams(InputStream configuration) throws IOException, ConfigurationLoadException {

		Properties modelParamsAsProperties = new Properties();
		modelParamsAsProperties.load(configuration);

		EpiModelParamStringBuilder paramStrBuild = new EpiModelParamStringBuilder();
		for (String param : DOUBLE_PARAMS) {
			String value;
			if ((value = modelParamsAsProperties.getProperty(param)) == null) {
				if (!PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.containsKey(param)) {
					throw new ConfigurationLoadException("The required parameter \"" + param + "\" was not specified in the configuration");
				} else {
					continue;
				}
			}

			try {
				double doubleValue = Double.parseDouble(value);
			} catch (NumberFormatException ex) {
				throw new ConfigurationLoadException("Parameter \"" + param + "\" has value \"" + value + "\" which cannot be parsed as a double");
			}
			paramStrBuild.addParam(param, "double", value);
			setParams.add(param);
		}

		for (String param : INTEGER_PARAMS) {
			String value;
			if ((value = modelParamsAsProperties.getProperty(param)) == null) {
				if (!PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS.containsKey(param)) {
					throw new ConfigurationLoadException("The required parameter \"" + param + "\" was not specified in the configuration");
				} else {
					continue;
				}
			}

			try {
				int intValue = Integer.parseInt(value);
				if (param.equals(RUN_DURATION)) {
					runLength = intValue;
				}
			} catch (NumberFormatException ex) {
				throw new ConfigurationLoadException("Parameter \"" + param + "\" has value \"" + value + "\" which cannot be parsed as an integer");
			}
			paramStrBuild.addParam(param, "integer", value);
			setParams.add(param);
		}

		for (String param : DOUBLE_ARRAY_PARAMS) {
			String value;
			if ((value = modelParamsAsProperties.getProperty(param)) == null) {
				if (!param.equals(PROPH_SCHEDULE)) {
					throw new ConfigurationLoadException("The required parameter \"" + param + "\" was not specified in the configuration");
				} else {
					continue;
				}
			}

			String[] valuesArray = value.split(",");
			double[] doubleArray = new double[valuesArray.length];

			for (int i = 0; i < valuesArray.length; i++) {
				String val = valuesArray[i];
				try {
					double doubleValue = Double.parseDouble(val);
					doubleArray[i] = doubleValue;
				} catch (NumberFormatException ex) {
					throw new ConfigurationLoadException("Value \"" + val + "\" in the time series for parameter \"" + param + "\" cannot be parsed as a double");
				}
			}
			paramStrBuild.addParam(param, ParameterizedComponent.PARAM_1D_DOUBLE_ARRAY, Arrays.toString(doubleArray));
			setParams.add(param);
		}

		loadDefaultParams(paramStrBuild);

		return paramStrBuild;
	}

	private void loadDefaultParams(EpiModelParamStringBuilder paramStrBuild) throws ConfigurationLoadException {

		if (!setParams.contains(PROPH_SCHEDULE)) {

			double[] schedule = new double[runLength];
			paramStrBuild.addParam(PROPH_SCHEDULE,
					ParameterizedComponent.PARAM_1D_DOUBLE_ARRAY,
					Arrays.toString(schedule));

			for (String param : PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.keySet()) {
				if (!setParams.contains(param)) {
					paramStrBuild.addParam(param, "double", PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.get(param));
				}
			}

			for (String param : PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS.keySet()) {
				if (!setParams.contains(param)) {
					paramStrBuild.addParam(param, "integer", PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS.get(param));
				}
			}
		} else {
			for (String param : PROPH_SCHEDULE_DEPENDANT_DOUBLE_PARAMS.keySet()) {
				if (!setParams.contains(param)) {
					throw new ConfigurationLoadException("The required prophylaxis parameter \"" + param + "\" was not specified in the configuration");
				}
			}

			for (String param : PROPH_SCHEDULE_DEPENDANT_INTEGER_PARAMS.keySet()) {
				if (!setParams.contains(param)) {
					throw new ConfigurationLoadException("The required prophylaxis parameter \"" + param + "\" was not specified in the configuration");
				}
			}
		}

	}

}
