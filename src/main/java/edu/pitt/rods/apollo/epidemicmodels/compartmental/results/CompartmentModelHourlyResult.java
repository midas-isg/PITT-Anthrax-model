package edu.pitt.rods.apollo.epidemicmodels.compartmental.results;

import java.io.Serializable;
import java.util.HashMap;

public class CompartmentModelHourlyResult implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -2282093601577502161L;
    HashMap<String, Double> descriptorCountMap = new HashMap<String, Double>();

    public boolean descriptorExists(String descriptor) {
        return descriptorCountMap.containsKey(descriptor);
    }

    public void addCountToDescriptor(String descriptor, Double value) {
        if (descriptorExists(descriptor)) {
            descriptorCountMap.put(descriptor,
                    descriptorCountMap.get(descriptor) + value);
        } else {
            createDescriptor(descriptor, value);
        }
    }

    public void createDescriptor(String descriptor) {
        descriptorCountMap.put(descriptor, new Double(0.0));
    }

    public void createDescriptor(String descriptor, Double count) {
        descriptorCountMap.put(descriptor, count);
    }

    public Double getValue(String descriptor) {
        return descriptorCountMap.get(descriptor);
    }
}
