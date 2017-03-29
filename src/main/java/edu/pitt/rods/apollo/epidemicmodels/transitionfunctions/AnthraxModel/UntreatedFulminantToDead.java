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
 * 1.  only applicable if there is no alert, otherwise we hospitalize all fulminant
 * 2.  outbreak is assumed if node pop > 0
 *  
 */
public class UntreatedFulminantToDead extends AbstractTransitionFunction {

	// SingleValueParameter muFulminantToDead = new SingleValueParameter(
	// ParameterConstants.FUL_TO_DEAD_MU_NAME);
	// SingleValueParameter sigmaFulminantToDead = new SingleValueParameter(
	// ParameterConstants.FUL_TO_DEAD_SIGMA_NAME);

	double muFulminantToDead = Parameters.getFulToDeadMu();
	double sigmaFulminantToDead = Parameters.getFulToDeadSigma();
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

	public UntreatedFulminantToDead(String name, IStateNode fromNode,
			IStateNode toNode, AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.DIED);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_DEAD_PROGRESSION, "");
		// muFulminantToDead
		// .setRunTimeValueInternal(ParameterConstants.FUL_TO_DEAD_MU);
		// sigmaFulminantToDead
		// .setRunTimeValueInternal(ParameterConstants.FUL_TO_DEAD_SIGMA);
		// lnd = new LogNormalDist(muFulminantToDead.getRunTimeValue(),
		// sigmaFulminantToDead.getRunTimeValue());

		lnd = new LogNormalDist(muFulminantToDead, sigmaFulminantToDead);
		// addParameter(muFulminantToDead);
		// addParameter(sigmaFulminantToDead);

	}
}