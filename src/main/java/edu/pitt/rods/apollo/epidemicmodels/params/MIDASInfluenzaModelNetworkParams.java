package edu.pitt.rods.apollo.epidemicmodels.params;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;

public class MIDASInfluenzaModelNetworkParams {

    public static final String R0_PARAM = "r0";
    public static final String AVE_LATENT_PERIOD_D_PARAM = "avelat";
    public static final String AVE_DURATION_OF_INFECTIOUSNESS_D_PARAM = "ave_duration_infectiousness";
    public static final String BETA_PARAM = "beta";
    public static final String OUTBREAK_PARAM = "outbreak";
//	public static final String GOMPERTZ_A = "gompertzA";
//	public static final String GOMPERTZ_B = "gompertzB";
//	public static final String GOMPERTZ_C = "gompertzC";
    
    public Double[] vaccinationSchedule = null;
    public Double vaccinationEfficacy = 1.0d;
    
    public boolean outbreak = false;
    public Double R0;
    public Double averageLatentPeriod = 0d;
    public Double averageDurationOfInfectiousness = 0d;
    public Double beta = 0d;
    public Double gompertzParameterA = 0d;
    public Double gompertzParameterB = 0d;
    public Double gompertzParameterC = 0d;

//	public Double getGompertzParameterA() {
//		return gompertzParameterA;
//	}
//
//	public Double getGompertzParameterB() {
//		return gompertzParameterB;
//	}
//
//	public Double getGompertzParameterC() {
//		return gompertzParameterC;
//	}
    public Double getBeta() {
        return beta;
    }

    public Double getAverageDurationOfInfectiousness() {
        return averageDurationOfInfectiousness;
    }

    public Double getR0() {
        return R0;
    }

    public Double getAverageLatentPeriod() {
        return averageLatentPeriod;
    }

    public Double getRecoveryRate() {
        return 1 / getAverageDurationOfInfectiousness();
    }

    public Double getDiseaseOnsetRate() {
        return 1 / getAverageLatentPeriod();
    }

    public Double[] getVacciationSchedule() {
        return vaccinationSchedule;
    }

    public Double getVaccinationEfficacy() {
        return vaccinationEfficacy;
    }
    
    public MIDASInfluenzaModelNetworkParams(AbstractStateTransitionNetwork network) {
        R0 = network.getDoubleParam(R0_PARAM);
        outbreak = network.getBooleanParam(OUTBREAK_PARAM);
        averageLatentPeriod = network.getDoubleParam(AVE_LATENT_PERIOD_D_PARAM);
        averageDurationOfInfectiousness = network.getDoubleParam(AVE_DURATION_OF_INFECTIOUSNESS_D_PARAM);
        beta = network.getDoubleParam(BETA_PARAM);
//		gompertzParameterA = network.getDoubleParam(GOMPERTZ_A);
//		gompertzParameterB = network.getDoubleParam(GOMPERTZ_B);
//		gompertzParameterC = network.getDoubleParam(GOMPERTZ_C);
        vaccinationSchedule = network.get1DDoubleArrayParam(MIDASInfluenzaModelParams.PARAM_VACCINATION_SCHEDULE);
        vaccinationEfficacy = network.parameterExists(MIDASInfluenzaModelParams.PARAM_VACCINATION_EFFICACY) ?
                                network.getDoubleParam(MIDASInfluenzaModelParams.PARAM_VACCINATION_EFFICACY) : 1.0d;
    }
}
