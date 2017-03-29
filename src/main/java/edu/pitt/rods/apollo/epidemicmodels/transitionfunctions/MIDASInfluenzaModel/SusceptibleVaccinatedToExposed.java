/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza.MidasInfluenzaModel;
import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;
import edu.pitt.rods.apollo.statetransitionnetwork.*;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

/**
 *
 * @author atg19
 */
public class SusceptibleVaccinatedToExposed extends AbstractTransitionFunction {
    StateNode infectiousNode;
    StateNode susceptibleNode;
    StateNode exposedNode;
    MIDASInfluenzaModelNetworkParams params;

    public SusceptibleVaccinatedToExposed(String name, IStateNode fromNodeName,
            IStateNode toNode, AbstractStateTransitionNetwork network) {
        super(name, fromNodeName, toNode, network, MovementType.STILL_UNTREATED);
        addAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_SEIR_MODEL, "");
        addAttribute(NodeGroupConsts.SEIR_TO_INFECTED, "");
        infectiousNode = network.getNode(MidasInfluenzaModel.INFECTIOUS);
        susceptibleNode = (StateNode) fromNode;
        exposedNode = (StateNode) toNode;
        params = (MIDASInfluenzaModelNetworkParams) ((InfluenzaStateTransitionNetwork) network).theParams;
    }

    public double getP(int t) {
        if ((t > 0) && (network.getState().isOutbreak())) {

            double beta = params.getBeta();
            // ok we are returning a percentage here so be careful...
            double numToMove = susceptibleNode.getHistoricalNumberOfPeopleInTheNode(t - 1);
            numToMove *= infectiousNode.getHistoricalNumberOfPeopleInTheNode(t - 1);
            numToMove *= beta;

            // again,careful here, we return what percentage to move
            return numToMove / susceptibleNode.getHistoricalNumberOfPeopleInTheNode(t - 1);
        }
        return 0.0;
    }
}
