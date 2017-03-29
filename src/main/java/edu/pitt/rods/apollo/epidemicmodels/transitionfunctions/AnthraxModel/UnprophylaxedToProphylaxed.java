package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import java.util.ArrayList;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

/*
 * if we have capacity
 *   while (time to certainty > t > time to detection)
 *     if release:
 *       rate = (TC per hour * (num in this bin/(num in this bin + num in other untreated less
 *               than prodromal bins))
 *     if no release
 *       rate = TC per hour
 * else (no capacity)
 *   rate = 0.0 
 */

/*
 * Assumptions:
 * 1.  Only possible to treat if an alert has been sounded
 * 2.  Outbreak (Yes/No) has no effect on the distribution of prophylaxis
 */
public class UnprophylaxedToProphylaxed extends AbstractTransitionFunction {

	public UnprophylaxedToProphylaxed(String name, IStateNode fromNode,
			IStateNode toNode, AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.PROPHYLAXED);
		addAttribute(NodeGroupConsts.TREATMENT, "");
		// Response capacity
	}

	// only if alerting!
	public double getP(int t) {
		if ((fromNode.getNumberThatHaveBeenInNodeForNHours(t) > 0.0)
				&& network.isInTreatmentMode()) {
			ArrayList<String> groups = new ArrayList<String>();

			if (fromNode
					.hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN))
				groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);

			if (network.getState().isOutbreakCertain())
			// && (fromNode
			// .hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN)
			// )) {
			{
				if (groups.size() == 0)
					groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);
				groups.add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN);

			}
			Double rate = network.getResponseCapacityForGroup(groups, fromNode,
					t);
			// of the available capacity, what percentage will take it?
			return rate;
		} else {
			return 0.0;
		}
	}
}
