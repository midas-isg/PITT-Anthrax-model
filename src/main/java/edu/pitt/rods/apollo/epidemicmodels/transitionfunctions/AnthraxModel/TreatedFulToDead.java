package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class TreatedFulToDead extends AbstractTransitionFunction {
	// SingleValueParameter muTreatedFulToDead = new SingleValueParameter(
	// "muTreatedFulToDead");
	// SingleValueParameter sigmaTreatedFulToDead = new SingleValueParameter(
	// "sigmaTreatedFulToDead");
	//
	double muTreatedFulToDead = Parameters.getFulToDeadMu();
	double sigmaTreatedFulToDead = Parameters.getFulToDeadSigma();
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

	public TreatedFulToDead(String name, IStateNode fromNode,
			IStateNode toNode, AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.DIED);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_DEAD_PROGRESSION, "");
		// muTreatedFulToDead.setRunTimeValueInternal(ParameterConstants.FUL_TO_DEAD_MU);
		// sigmaTreatedFulToDead
		// .setRunTimeValueInternal(ParameterConstants.FUL_TO_DEAD_SIGMA);
		// lnd = new LogNormalDist(muTreatedFulToDead.getRunTimeValue(),
		// sigmaTreatedFulToDead.getRunTimeValue());
		lnd = new LogNormalDist(muTreatedFulToDead, sigmaTreatedFulToDead);

	}

}
