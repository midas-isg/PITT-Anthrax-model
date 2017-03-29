package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

/*
 * going to be a standard treatment capacity transition
 */
public class SusceptiblesToRecovered extends AbstractTransitionFunction {

    StateNode unVaccinatedNode;
    StateNode recoveredNode;

    public SusceptiblesToRecovered(String name, IStateNode fromNodeName, IStateNode toNode, AbstractStateTransitionNetwork network) {
        super(name, fromNodeName, toNode, network, MovementType.PROPHYLAXED);
        addAttribute(NodeGroupConsts.TREATMENT, "");
        addAttribute(NodeGroupConsts.SEIR_TO_IMMUNE, "");

        unVaccinatedNode = (StateNode) fromNode;
        recoveredNode = (StateNode) toNode;
    }

    public double getP(int t) {
        if (t > 0) {
            // if ((fromNode.getNumberThatHaveBeenInNodeForNHours(t) > 0.0)
            // && network.isInTreatmentMode()) {
            // ArrayList<String> groups = new ArrayList<String>();
            //
            // if (fromNode
            // .hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN
            // ))
            // groups
            // .add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);
            //
            // if (network.getState().isOutbreakCertain()) {
            // if (groups.size() == 0)
            // groups
            // .add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN);
            // groups
            // .add(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN);
            //
            // }
            // Double rate = network.getResponseCapacityForGroup(groups,
            // fromNode, t);

            Double vaccCap = ((InfluenzaStateTransitionNetwork) network).getVaccinationCapacity(t);
            Double vaccEfficacy = ((InfluenzaStateTransitionNetwork) network).getVaccinationEfficacy();
            Double numToTreat = fromNode.getNumberOfTreatablePeopleInTheEntireNode(t);

            Double rawRate = (vaccCap > numToTreat ? 1d : vaccCap / numToTreat);
            Double rate = vaccEfficacy * rawRate;

            // Of the available capacity, what percentage will go to the recovered bin?
            //   -- include the vaccine efficacy in this calculation
            return rate;
        } else {
            return 0.0;
        }
    }
}
