package edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax;

import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_ASYMPTOMATIC;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_DEAD;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_LATENT;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_NEWLY_DEAD;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_PROPHYLAXED;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_RECOVERED;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_FULMINANT;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_SYMPTOMATIC;
import static edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel.APOLLO_COMPARTMENT_UNEXPOSED;
import edu.pitt.rods.apollo.epidemicmodels.params.AnthraxModelConfigurationLoader;
import edu.pitt.rods.apollo.epidemicmodels.utils.EpiModelParamStringBuilder;
import edu.pitt.rods.apollo.exception.ConfigurationLoadException;
import edu.pitt.rods.apollo.statetransitionnetwork.BraithwaiteAnthraxStateTransitionNetwork;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import org.apache.commons.lang3.ArrayUtils;

/**
 *
 * Author: Nick Millett Email: nick.millett@gmail.com Date: Jun 19, 2014 Time: 4:46:09 PM Class: AnthraxModelRunner IDE: NetBeans 6.9.1
 */
public class AnthraxModelRunner {

    private Map<String, Double[]> compartmentTimeSeriesMap;

    public void runModel(InputStream configuration) throws IOException, ConfigurationLoadException {

        AnthraxModelConfigurationLoader loader = new AnthraxModelConfigurationLoader();
        EpiModelParamStringBuilder s = loader.loadAnthraxModelParams(configuration);

        BraithwaiteAnthraxStateTransitionNetwork stn = new BraithwaiteAnthraxStateTransitionNetwork(
                s.getParamString());
        AnthraxModel model = new AnthraxModel(s.getParamString());
        model.setAuthor("Wagner");
        model.setDescription("FOR RESEARCH ONLY: implements model published in Medical Decision Making, Mar-Apr 2006 pp 182-93.  Braithaite et all The cost effectiveness of strategies to reduce mortality from an intentional release of aerosolized anthrax spores");
        model.setStateTransitionNetwork(stn);
        model.buildGenericModel();

        compartmentTimeSeriesMap = model.getCompartmentTimeSeriesMap();
        model.run();

    }

    public Map<String, Double[]> getCompartmentTimeSeriesMap() {
        return compartmentTimeSeriesMap;
    }

    public AnthraxCompartmentResults getCompartmentResults() {
        AnthraxCompartmentResults results = new AnthraxCompartmentResults();
        results.setUnexposedTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_UNEXPOSED)));
        results.setExposedTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_LATENT)));
        results.setRecoveredTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_RECOVERED)));
		results.setNewlyDeceasedTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_NEWLY_DEAD)));
		results.setProphylaxedTimeSeres(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_PROPHYLAXED)));
		results.setAsymptomaticTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_ASYMPTOMATIC)));
		results.setProdromalTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_SYMPTOMATIC)));
		results.setFulminantTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_FULMINANT)));
		results.setDeadTimeSeries(ArrayUtils.toPrimitive(compartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_DEAD)));
        return results;
    }
    
    public static void main(String[] args) throws FileNotFoundException, IOException, ConfigurationLoadException {
        
        AnthraxModelRunner runner = new AnthraxModelRunner();
        runner.runModel(new FileInputStream("test_config.txt"));
        AnthraxCompartmentResults results = runner.getCompartmentResults();
        System.out.println(results.getUnexposedTimeSeries().toString());
    }
}
