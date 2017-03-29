package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

/*
 * Assumptions:
 * 1.  outbreak is assumed 
 * 2.  if alert, move all prodromal to treated, if not alert, move some to fulminant
 */
public class UntreatedProdromalUntreatedFulminant extends
		AbstractTransitionFunction {

	// SingleValueParameter muProdromalToFulminant = new SingleValueParameter(
	// ParameterConstants.PROD_TO_FUL_MU_NAME);
	// SingleValueParameter sigmaProdromalToFulminant = new
	// SingleValueParameter(
	// ParameterConstants.PROD_TO_FUL_SIGMA_NAME);

	double muProdromalToFulminant = Parameters.getProdToFulMu();
	double sigmaProdromalToFulminant = Parameters.getProdToFulSigma();

	LogNormalDist lnd = null;

	public double getP(int t) {
		double dt = t * 1.0;
		if (fromNode.getNumberThatHaveBeenInNodeForNHours(t) == 0)
			return 0.0;
		if (lnd.fastCDF(dt - 1) == 1.0)
			return 1.0;

		double result = (lnd.fastCDF(dt) - (lnd.fastCDF(dt - 1)))
				/ (1 - lnd.fastCDF(dt - 1));

		return result;
	}

	public UntreatedProdromalUntreatedFulminant(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.STILL_UNTREATED);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_FUL_PROGRESSION, "");
		// muProdromalToFulminant
		// .setRunTimeValueInternal(ParameterConstants.PROD_TO_FUL_MU);
		// sigmaProdromalToFulminant
		// .setRunTimeValueInternal(ParameterConstants.PROD_TO_FUL_SIGMA);
		// lnd = new LogNormalDist(muProdromalToFulminant.getRunTimeValue(),
		// sigmaProdromalToFulminant.getRunTimeValue());
		lnd = new LogNormalDist(muProdromalToFulminant,
				sigmaProdromalToFulminant);
		// addParameter(muProdromalToFulminant);
		// addParameter(sigmaProdromalToFulminant);

	}
}
