package edu.pitt.rods.apollo.test;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModelRunner;
import edu.pitt.rods.apollo.exception.ConfigurationLoadException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import junit.framework.TestCase;

/**
 *
 * Author: Nick Millett Email: nick.millett@gmail.com Date: Jun 19, 2014 Time: 3:42:10 PM Class: AnthraxModelTest IDE: NetBeans 6.9.1
 */
public class AnthraxModelTest extends TestCase {

	private static final String CONFIGURATION_FILE_LOCATION = "/anthrax_model_configuration.txt";
	private static final String OUTPUT_FILE_LOCATION = "/anthrax_model_test_output.txt";

	public AnthraxModelTest() {

	}

	private InputStream getConfigurationStream() {
		return this.getClass().getResourceAsStream(CONFIGURATION_FILE_LOCATION);
	}

	private void compareOutputToFile(Map<String, Double[]> output) {
		InputStream fileStream = this.getClass().getResourceAsStream(OUTPUT_FILE_LOCATION);
		Scanner scanner = new Scanner(fileStream);

		String line;

		for (String compartment : AnthraxModel.APOLLO_COMPARTMENTS) {
//			System.out.println(compartment + ";" + Arrays.toString(output.get(compartment)));
			line = scanner.nextLine();
			if (!line.equals(compartment + ";" + Arrays.toString(output.get(compartment)))) {
				assert (false);
				return;
			}
		}
	}

	public void testLoadingConfiguration() {
		assert (getConfigurationStream() != null);
	}

	public void testRunningAnthraxModel() {
		try {
			InputStream configurationStream = getConfigurationStream();
			AnthraxModelRunner runner = new AnthraxModelRunner();
			runner.runModel(configurationStream);

			Map<String, Double[]> compartmentTimeSeries = runner.getCompartmentTimeSeriesMap();
			compareOutputToFile(compartmentTimeSeries);

		} catch (IOException ex) {
			Logger.getLogger(AnthraxModelTest.class.getName()).log(Level.SEVERE, null, ex);
		} catch (ConfigurationLoadException ex) {
			Logger.getLogger(AnthraxModelTest.class.getName()).log(Level.SEVERE, null, ex);
		}
	}
}
