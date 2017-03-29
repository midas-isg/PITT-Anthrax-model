package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class TreatedProdToUnsuccessfullyTreatedFul extends
		AbstractTransitionFunction {
	// SingleValueParameter muTreatedProdToTreatedFul = new
	// SingleValueParameter(
	// "muTreatedProdToTreatedFul");
	// SingleValueParameter sigmaTreatedProdToTreatedFul = new
	// SingleValueParameter(
	// "sigmaTreatedProdToTreatedFul");
	//
	// SingleValueParameter efficacy = new SingleValueParameter(
	// "fulminantTreatementEfficiacy");

	double muTreatedProdToTreatedFul = Parameters.getProdToFulMu();
	double sigmaTreatedProdToTreatedFul = Parameters.getProdToFulSigma();
	double efficacy = Parameters.getOneMinusFulTreatmentEfficacy();
	LogNormalDist lnd = null;

	// all of the people in this node are going to transition into
	// one of the prodromal nodes
	public double getP(int t) {
		double dt = t * 1.0;
		if (fromNode.getNumberThatHaveBeenInNodeForNHours(t) == 0)
			return 0.0;
		if (lnd.fastCDF(dt - 1) == 1.0)
			return 1.0;

		double result = 1.0;
		if (!network.getState().isFastTransitionMode()) {
			result = (lnd.fastCDF(dt) - (lnd.fastCDF(dt - 1)))
					/ (1 - lnd.fastCDF(dt - 1));
		}

		return result * efficacy;
	}

	public TreatedProdToUnsuccessfullyTreatedFul(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.ICU);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_FUL_PROGRESSION, "");
		// muTreatedProdToTreatedFul
		// .setRunTimeValueInternal(ParameterConstants.PROD_TO_FUL_MU);
		// sigmaTreatedProdToTreatedFul
		// .setRunTimeValueInternal(ParameterConstants.PROD_TO_FUL_SIGMA);
		// lnd = new LogNormalDist(muTreatedProdToTreatedFul.getRunTimeValue(),
		// sigmaTreatedProdToTreatedFul.getRunTimeValue());
		// efficacy.setRunTimeValueInternal(ParameterConstants.ONE_MINUS_FUL_TREATMENT_EFFICACY);

		lnd = new LogNormalDist(muTreatedProdToTreatedFul,
				sigmaTreatedProdToTreatedFul);
	}

}
