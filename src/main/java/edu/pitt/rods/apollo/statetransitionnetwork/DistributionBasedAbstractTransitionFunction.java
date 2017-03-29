package edu.pitt.rods.apollo.statetransitionnetwork;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;

public abstract class DistributionBasedAbstractTransitionFunction extends
		AbstractTransitionFunction {

	LogNormalDist distribution;

	protected DistributionBasedAbstractTransitionFunction(String name,
			IStateNode fromNodeName, IStateNode toNodeName,
			InfluenzaStateTransitionNetwork network, MovementType movementType,
			Double mean, Double sigma) {
		super(name, fromNodeName, toNodeName, network, movementType);
		distribution = new LogNormalDist(mean, sigma);
	}

}
