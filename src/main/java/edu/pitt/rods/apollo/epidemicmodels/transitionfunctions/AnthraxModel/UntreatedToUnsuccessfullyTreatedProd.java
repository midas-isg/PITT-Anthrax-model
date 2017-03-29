package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import java.util.ArrayList;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

public class UntreatedToUnsuccessfullyTreatedProd extends
		AbstractTransitionFunction {

	double efficacy = Parameters.getOneMinusProdTreatmentEfficacy();

	public UntreatedToUnsuccessfullyTreatedProd(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.IV);
		addAttribute(NodeGroupConsts.TREATMENT, "");
		efficacy = Parameters.getOneMinusProdTreatmentEfficacy();
	}

	// only if alerting!
	/*
	 * so rougly 80-90% of the people who receive prophylaxis will recover
	 */

	public double getP(int t) {
		if ((fromNode.getNumberThatHaveBeenInNodeForNHours(t) > 0.0)
				&& network.isInTreatmentMode()) {
			ArrayList<String> groups = new ArrayList<String>();
			groups.add(NodeGroupConsts.PRODROMAL_TREATMENT);
			double[] treat = network.getTreatment(fromNode);
			// so treat[t] is the population we can move before efficacy
			Double rate = treat[t] / fromNode.getNumberThatHaveBeenInNodeForNHours(t);

			return rate * efficacy;
		} else {
			return 0.0;
		}
	}

}
