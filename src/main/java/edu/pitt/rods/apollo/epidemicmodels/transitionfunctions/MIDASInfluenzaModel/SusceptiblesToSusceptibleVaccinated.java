/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel;

import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

/**
 *
 * @author atg19
 */
public class SusceptiblesToSusceptibleVaccinated extends AbstractTransitionFunction {
    StateNode unVaccinatedNode;
    StateNode recoveredNode;

    public SusceptiblesToSusceptibleVaccinated(String name, IStateNode fromNodeName, IStateNode toNode, AbstractStateTransitionNetwork network) {
        super(name, fromNodeName, toNode, network, MovementType.PROPHYLAXED);
        addAttribute(NodeGroupConsts.TREATMENT, "");
        addAttribute(NodeGroupConsts.SEIR_TO_SUSCEPTIBLE_VACCINATED, "");

        unVaccinatedNode = (StateNode) fromNode;
        recoveredNode = (StateNode) toNode;
    }

    public double getP(int t) {
        if (t > 0) {
            Double vaccCap = ((InfluenzaStateTransitionNetwork) network).getVaccinationCapacity(t);
            Double vaccEfficacy = ((InfluenzaStateTransitionNetwork) network).getVaccinationEfficacy();
            Double numToTreat = fromNode.getNumberOfTreatablePeopleInTheEntireNode(t);

            Double rawRate = (vaccCap > numToTreat ? 1d : vaccCap / numToTreat);
            Double rate = (1 - vaccEfficacy) * rawRate;

            // Of the available capacity, what percentage will go to the recovered bin?
            //   -- include the vaccine efficacy in this calculation
            return rate;
        } else {
            return 0.0;
        }
    }
}
