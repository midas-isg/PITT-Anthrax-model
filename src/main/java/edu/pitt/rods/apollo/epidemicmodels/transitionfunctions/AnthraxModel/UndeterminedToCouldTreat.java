package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class UndeterminedToCouldTreat extends AbstractTransitionFunction {

	public UndeterminedToCouldTreat(String name, IStateNode fromNode,
			IStateNode toNode, AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.STILL_UNTREATED);
		addAttribute(NodeGroupConsts.REFRACTORY_TRANSITION, null);

	}

	public double getP(int t) {
		return network.getState().getHabituationRate();
	}
}
