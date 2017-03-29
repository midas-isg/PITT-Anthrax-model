package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import java.util.ArrayList;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class UnprophylaxedToUnSuccessfullyProphylaxed extends
		AbstractTransitionFunction {

	double efficacy = Parameters.getOneMinusPoEfficacy();

	public UnprophylaxedToUnSuccessfullyProphylaxed(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.PROPHYLAXED);
		addAttribute(NodeGroupConsts.TREATMENT, "");
		efficacy =  Parameters.getOneMinusPoEfficacy();
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
			// && (fromNode
			// .hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN)))
			// {
			{
				if (groups.size() == 0)
					groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);

				groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN);

			}

			Double rate = network.getResponseCapacityForGroup(groups, fromNode,
					t);
			// of those that we have the capacity to treat, who will get
			// treatment
			// if (rate == 1.0)
			// System.out.println("huh.");
			// of those that we can treat, who's treatment will be unsuccessful
			rate *= efficacy;
			return rate;
		} else {
			return 0.0;
		}
	}
}
