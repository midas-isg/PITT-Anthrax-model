package edu.pitt.rods.apollo.epidemicmodels.compartmental.results;

import java.io.Serializable;
import java.util.ArrayList;

public class CompartmentEpiModelGlobalResult implements Serializable {

    private static final long serialVersionUID = 218270864644180904L;
    public ArrayList<CompartmentModelHourlyResult> hourlyResults = new ArrayList<CompartmentModelHourlyResult>();

    public void addResult(CompartmentModelHourlyResult result) {
        hourlyResults.add(result);
        if (result == null) {
            System.out.println("WHoops");
        }

    }

    public double getDiseaseProgressionCount(String progression) {
        double result = 0.0;
        for (CompartmentModelHourlyResult hr : hourlyResults) {
            result += hr.getValue(progression);
        }
        return result;
    }

    public double[] getDiseaseProgressionCurve(String progression) {
        double[] result = new double[hourlyResults.size()];

        for (int i = 0; i < hourlyResults.size(); i++) {
            Double value = ((CompartmentModelHourlyResult) (hourlyResults.get(i))).getValue(progression);
            if (value == null) {
                result[i] = 0;
            } else {
                result[i] = value;
            }

        }
        return result;
    }

    public double getTreatmentCount(String treatment) {
        double result = 0.0;
        for (CompartmentModelHourlyResult hr : hourlyResults) {
            if (hr.getValue(treatment) != null) {
                result += hr.getValue(treatment);
            }
        }
        return result;
    }
}
