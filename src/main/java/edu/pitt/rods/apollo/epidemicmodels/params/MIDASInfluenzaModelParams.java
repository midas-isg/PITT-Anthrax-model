package edu.pitt.rods.apollo.epidemicmodels.params;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza.MidasInfluenzaModel;

public class MIDASInfluenzaModelParams {

    public static final String ITERATIONS_TO_CERTAINTY = "iterationsToCertainty";
    public static final String ITERATION_OF_ALERT_PARAM = "timeOfAlert";
    public static final String TOTAL_NUM_PEOPLE_PARAM = "totalNumPeople";
    public static final String TOTAL_NUM_INFECTIOUS_PEOPLE_PARAM = "totalNumInfectiousPeople";
    public static final String FRACTION_IMMUNE_PARAM = "fractionImmune";
    public static final String PERCENT_COMPLIANT_PARAM = "percentCompliant";
    public static final String SEGMENT_NAME_PARAM = "segmentName";
    public static final String OUTBREAK_PARAM = "outbreak";
    public static final String SUSCEPTIBLE_PARAM = "susceptible";
    public static final String EXPOSED_PARAM = "exposed";
    public static final String INFECTIOUS_PARAM = "infectious";
    public static final String RECOVERED_PARAM = "recovered";
    
    // Moved from InfluenzaResponseParameterSet
    public static final String PARAM_VACCINATION_SCHEDULE = "Vaccination Schedule";
    public static final String PARAM_VACCINATION_EFFICACY = "vaccinationEfficacy";
    public static final String PARAM_PREEMPTIVELY_TREATED = "Number Treated Before Epidemic";
    public static final String PARAM_NUM_PREEMPTIVELY_TREATED = "# Preemptively Treated";
    
    Integer iterations = 0;
    Integer iterationOfAlert = 0;
    Double totalNumberOfPeople = 0d;
    Double initialNumberOfInfectionsPeople = 0d;
    double susceptible, exposed, infectious, recovered;

    public double getSusceptible() {
        return susceptible;
    }

    public void setSusceptible(double susceptible) {
        this.susceptible = susceptible;
    }

    public double getExposed() {
        return exposed;
    }

    public void setExposed(double exposed) {
        this.exposed = exposed;
    }

    public double getInfectious() {
        return infectious;
    }

    public void setInfectious(double infectious) {
        this.infectious = infectious;
    }

    public double getRecovered() {
        return recovered;
    }

    public void setRecovered(double recovered) {
        this.recovered = recovered;
    }
    Double fractionImmune = 0d;
    Integer iterationsToCertainty = 0;
    Double percentCompliant = 0.0;
    Double numPreemptivelyTreated = 0d;
    String segmentName = "";
    Boolean outbreak = true;

    public Boolean isOutbreak() {
        return outbreak;
    }

    public String getSegmentName() {
        return segmentName;
    }

    public Double getPercentCompliant() {
        return percentCompliant;
    }

    public Integer getIterations() {
        return iterations;
    }

    public Integer getIterationOfAlert() {
        return iterationOfAlert;
    }

    public Double getTotalNumberOfPeople() {
        return totalNumberOfPeople;
    }

    public Double getInitialNumberOfInfectionsPeople() {
        return initialNumberOfInfectionsPeople;
    }

    public Double getFractionImmune() {
        return fractionImmune;
    }

    public Integer getIterationsToCertainty() {
        return iterationsToCertainty;
    }

    public Double getNumPreemptivelyTreated() {
        return numPreemptivelyTreated;
    }

    public MIDASInfluenzaModelParams(MidasInfluenzaModel model) {

        iterations = model.getIntegerParam(RunParameterSet.PARAM_RUN_DURATION);
        iterationOfAlert = model.getIntegerParam(ITERATION_OF_ALERT_PARAM);
        totalNumberOfPeople = model.getDoubleParam(TOTAL_NUM_PEOPLE_PARAM);
        initialNumberOfInfectionsPeople = model.getDoubleParam(TOTAL_NUM_INFECTIOUS_PEOPLE_PARAM);
        fractionImmune = model.getDoubleParam(FRACTION_IMMUNE_PARAM);
        iterationsToCertainty = model.getIntegerParam(ITERATIONS_TO_CERTAINTY);
        percentCompliant = model.getDoubleParam(PERCENT_COMPLIANT_PARAM);
        numPreemptivelyTreated = model.getDoubleParam(PARAM_PREEMPTIVELY_TREATED);
        segmentName = model.getStringParam(SEGMENT_NAME_PARAM);
        outbreak = model.getBooleanParam(OUTBREAK_PARAM);

        susceptible = model.getDoubleParam(SUSCEPTIBLE_PARAM);
        exposed = model.getDoubleParam(EXPOSED_PARAM);
        infectious = model.getDoubleParam(INFECTIOUS_PARAM);
        recovered = model.getDoubleParam(RECOVERED_PARAM);
    }
}
