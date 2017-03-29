package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel;

import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class ExposedToInfectious extends AbstractTransitionFunction {

    /*
     * calculation is number of infected dis_rate
     */
    StateNode exposedNode;
    MIDASInfluenzaModelNetworkParams params;

    public ExposedToInfectious(String name, IStateNode fromNodeName,
            IStateNode toNodeName, AbstractStateTransitionNetwork network) {
        super(name, fromNodeName, toNodeName, network,
                MovementType.STILL_UNTREATED);
        addAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_SEIR_MODEL, "");
        addAttribute(NodeGroupConsts.SEIR_TO_INFECTIOUS, "");
        exposedNode = (StateNode) fromNode;
        params = (MIDASInfluenzaModelNetworkParams) ((InfluenzaStateTransitionNetwork) network).theParams;

    }

    public double getP(int t) {
        if (t > 0) {
            double numToMove = exposedNode.getHistoricalNumberOfPeopleInTheNode(t - 1);
            numToMove *= params.getDiseaseOnsetRate();
            if (numToMove == 0.0) {
                return 0.0;
            } else {
                return numToMove
                        / exposedNode.getHistoricalNumberOfPeopleInTheNode(t - 1);
            }
        }
        return 0.0;
    }
}
