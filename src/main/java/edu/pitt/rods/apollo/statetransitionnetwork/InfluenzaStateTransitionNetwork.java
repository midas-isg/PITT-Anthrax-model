package edu.pitt.rods.apollo.statetransitionnetwork;

import java.util.Random;

import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;

public class InfluenzaStateTransitionNetwork extends AbstractStateTransitionNetwork {

    public Object theParams;
    private static final long serialVersionUID = 8919399888931999559L;
    int[] vaccinationSchedule;
    //private GompertzFunction rcp = new GompertzFunction(5000.0, -5.0, -0.0185);
    // super high capacity
    // GompertzBasedResponseCapacityParameter rcp = new
    // GompertzBasedResponseCapacityParameter(
    // ParameterNames.GompertzBasedResponseCapacity, 5000.0, -5.0, -0.0185,
    // 50000.0, -5.0, -0.0185 );
    public static Random randomNumberGenerator = new Random();

    public InfluenzaStateTransitionNetwork(String args) {
        super(args);
        theParams = new MIDASInfluenzaModelNetworkParams(this);
        state.setOutbreak(((MIDASInfluenzaModelNetworkParams) theParams).outbreak);
    }

    /**
     * How many people can be vaccinated, given the vaccine production given in the schedule
     * 
     * @param hour
     * @return 
     */
    public double getVaccinationCapacity(int hour) {
        if (((MIDASInfluenzaModelNetworkParams) theParams).getVacciationSchedule().length == 0) {
            return 0;
        }
        return ((MIDASInfluenzaModelNetworkParams) theParams).getVacciationSchedule()[hour];
    }
    
    /**
     * Return the efficacy of the vaccine
     * @return 
     */
    public double getVaccinationEfficacy() {
        return ((MIDASInfluenzaModelNetworkParams) theParams).getVaccinationEfficacy();
    }
}
