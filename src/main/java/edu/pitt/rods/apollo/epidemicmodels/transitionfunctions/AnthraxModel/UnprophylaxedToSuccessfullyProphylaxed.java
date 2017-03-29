package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import java.util.ArrayList;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class UnprophylaxedToSuccessfullyProphylaxed extends
		AbstractTransitionFunction {

	double efficacy = Parameters.getPoEfficacy();

	public UnprophylaxedToSuccessfullyProphylaxed(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.PROPHYLAXED);
		addAttribute(NodeGroupConsts.TREATMENT, "");
		efficacy = Parameters.getPoEfficacy();
	}

	// only if alerting!
	/*
	 * so rougly 80-90% of the people who receive prophylaxis will recover
	 */

	public double getP(int t) {
		if ((fromNode.getNumberThatHaveBeenInNodeForNHours(t) > 0.0)
				&& network.isInTreatmentMode()) {
			ArrayList<String> groups = new ArrayList<String>();
			if (fromNode
					.hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN))
				groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);

			if (network.getState().isOutbreakCertain())

			{
				if (groups.size() == 0)
					groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);
				groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN);

			}

			Double rate = network.getResponseCapacityForGroup(groups, fromNode,
					t);
			// of the amount to be prophylaxed, who will survive?
			rate *= efficacy;
			return rate;
		} else {
			return 0.0;
		}
	}
}
