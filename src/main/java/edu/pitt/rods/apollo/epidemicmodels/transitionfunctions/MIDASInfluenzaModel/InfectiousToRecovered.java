package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel;

import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class InfectiousToRecovered extends AbstractTransitionFunction {

    /*
     * num infections rec rate
     */
    StateNode infectedNode;
    MIDASInfluenzaModelNetworkParams params;

    public InfectiousToRecovered(String name, IStateNode fromNodeName,
            IStateNode toNodeName, AbstractStateTransitionNetwork network) {
        super(name, fromNodeName, toNodeName, network, MovementType.RECOVERED);
        addAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_SEIR_MODEL, "");
        addAttribute(NodeGroupConsts.SEIR_TO_IMMUNE, "");
        infectedNode = (StateNode) fromNode;
        params = (MIDASInfluenzaModelNetworkParams) ((InfluenzaStateTransitionNetwork) network).theParams;

    }

    public double getP(int t) {
        if (t > 0) {
            double numInfectious = infectedNode.getHistoricalNumberOfPeopleInTheNode(t - 1);
            double numThatRecover = numInfectious * params.getRecoveryRate();

            // we have to return percentage of infectious that recover
            return numThatRecover / numInfectious;
        }
        return 0.0;
    }
}
