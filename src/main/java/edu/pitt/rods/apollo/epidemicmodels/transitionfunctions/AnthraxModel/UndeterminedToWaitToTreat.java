package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class UndeterminedToWaitToTreat extends AbstractTransitionFunction {

	public UndeterminedToWaitToTreat(String name, IStateNode fromNode,
			IStateNode toNode, AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.STILL_UNTREATED);
		addAttribute(NodeGroupConsts.REFRACTORY_TRANSITION, null);

	}

	public double getP(int t) {
		return 1.0 - network.getState().getHabituationRate();
	}
}
