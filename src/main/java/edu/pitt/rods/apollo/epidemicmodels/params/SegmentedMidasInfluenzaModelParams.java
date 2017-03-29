package edu.pitt.rods.apollo.epidemicmodels.params;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.segmented.influenza.SegmentedMidasInfluenzaModel;

public class SegmentedMidasInfluenzaModelParams {
	public static final String ITERATIONS_PARAM = "iterations";

	Integer iterations = 0;

	public Integer getIterations() {
		return iterations;
	}

	public SegmentedMidasInfluenzaModelParams(SegmentedMidasInfluenzaModel model) {
		iterations = model.getIntegerParam(ITERATIONS_PARAM);

	}
}
