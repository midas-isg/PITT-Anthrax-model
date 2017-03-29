package edu.pitt.rods.apollo.statetransitionnetwork;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel;

public class BraithwaiteAnthraxStateTransitionNetwork extends
		AbstractStateTransitionNetwork {

	Double[] vaccinationSchedule = null;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5851988660428586490L;

	// private GompertzFunction rcp = new GompertzFunction(5000.0, -5.0,
	// -0.0185);

	public BraithwaiteAnthraxStateTransitionNetwork(String args
	// double percentCompliantWithTreatment, double gompertzA,
	// double gompertzB, double gompertzC) {
	) {
		super(args);
		vaccinationSchedule = get1DDoubleArrayParam(AnthraxModel.PROPH_SCHEDULE);

	}

	public double getVaccinationCapacity(int temporalUnit) {
		if (vaccinationSchedule.length == 0)
			return 0;
		return vaccinationSchedule[temporalUnit];
	}

}
