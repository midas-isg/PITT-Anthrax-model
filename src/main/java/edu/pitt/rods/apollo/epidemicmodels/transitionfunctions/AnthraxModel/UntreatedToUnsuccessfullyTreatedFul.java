package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class UntreatedToUnsuccessfullyTreatedFul extends
		AbstractTransitionFunction {

	double efficacy = Parameters.getOneMinusFulTreatmentEfficacy();

	public UntreatedToUnsuccessfullyTreatedFul(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.ICU);
		addAttribute(NodeGroupConsts.TREATMENT, "");
		efficacy = Parameters.getOneMinusFulTreatmentEfficacy();
	}

	// only if alerting!
	/*
	 * so rougly 80-90% of the people who receive prophylaxis will recover
	 */

	public double getP(int t) {
		if ((fromNode.getNumberThatHaveBeenInNodeForNHours(t) > 0.0)
				&& network.isInTreatmentMode()) {
			Double capacity = 1.0; // we are not implementing a capacity
			// on treatment at this time!
			return capacity * efficacy;
		} else {
			return 0.0;
		}
	}

}
