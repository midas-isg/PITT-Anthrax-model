package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class TreatedAsxToSuccessfullyTreatedProd extends
		AbstractTransitionFunction {
	// SingleValueParameter muTreatedAsxToTreatedProd = new
	// SingleValueParameter(
	// "muTreatedAsxToTreatedProd");
	// SingleValueParameter sigmaTreatedAsxToTreatedProd = new
	// SingleValueParameter(
	// "sigmaTreatedAsxToTreatedProd");
	//
	// SingleValueParameter efficacy = new SingleValueParameter(
	// "prodromalTreatementEfficiacy");

	double muTreatedAsxToTreatedProd = Parameters.getAsxToProdMu();
	double sigmaTreatedAsxToTreatedProd = Parameters.getAsxToProdSigma();
	double efficacy = Parameters.getProdTreatmentEfficacy();

	LogNormalDist lnd = null;

	// all of the people in this node are going to transition into
	// one of the prodromal nodes
	public double getP(int t) {
		double dt = t * 1.0;
		if (fromNode.getNumberThatHaveBeenInNodeForNHours(t) == 0)
			return 0.0;
		if (lnd.fastCDF(dt - 1) == 1.0)
			return 1.0;

		double result = (lnd.fastCDF(dt) - (lnd.fastCDF(dt - 1)))
				/ (1 - lnd.fastCDF(dt - 1));

		return result * efficacy;
	}

	public TreatedAsxToSuccessfullyTreatedProd(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.IV);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_PROD_PROGRESSION, "");

		// muTreatedAsxToTreatedProd
		// .setRunTimeValueInternal(ParameterConstants.ASX_TO_PROD_MU);
		// sigmaTreatedAsxToTreatedProd
		// .setRunTimeValueInternal(ParameterConstants.ASX_TO_PROD_SIGMA);
		// lnd = new LogNormalDist(muTreatedAsxToTreatedProd.getRunTimeValue(),
		// sigmaTreatedAsxToTreatedProd.getRunTimeValue());
		lnd = new LogNormalDist(muTreatedAsxToTreatedProd,
				sigmaTreatedAsxToTreatedProd);

	}

}
