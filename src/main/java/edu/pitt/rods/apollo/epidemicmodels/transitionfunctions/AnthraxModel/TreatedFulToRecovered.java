package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class TreatedFulToRecovered extends AbstractTransitionFunction {
	// SingleValueParameter muTreatedFulToRecovered = new SingleValueParameter(
	// "muTreatedProdToRecovered");
	// SingleValueParameter sigmaTreatedFulToRecovered = new
	// SingleValueParameter(
	// "sigmaTreatedProdToRecovered");

	double muTreatedFulToRecovered = Parameters.getFulToRecoveredMu();
	double sigmaTreatedFulToRecovered = Parameters.getFulToRecoveredSigma();

	LogNormalDist lnd = null;

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

		return result;
	}

	public TreatedFulToRecovered(String name, IStateNode fromNode,
			IStateNode toNode, AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.RECOVERED);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_RECOVER_FROM_FUL_PROGRESSION, "");
		// muTreatedFulToRecovered
		// .setRunTimeValueInternal(ParameterConstants.FUL_TO_RECOVERED_MU);
		// sigmaTreatedFulToRecovered
		// .setRunTimeValueInternal(ParameterConstants.FUL_TO_RECOVERED_SIGMA);
		// lnd = new LogNormalDist(muTreatedFulToRecovered.getRunTimeValue(),
		// sigmaTreatedFulToRecovered.getRunTimeValue());
		lnd = new LogNormalDist(muTreatedFulToRecovered,
				sigmaTreatedFulToRecovered);

	}

}