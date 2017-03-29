package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.segmented.MIDASInfluenzaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza.MidasInfluenzaModel;
import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractSegmentedTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class SusceptiblesToExposedWithOutsideInfluence extends
		AbstractSegmentedTransitionFunction {

	StateNode infectiousNode;
	StateNode susceptibleNode;
	StateNode exposedNode;
	Double visitorInfluence = 0d;

	List<String> influenceSegmentNames;
	List<Double> influenceSegmentValues;;
	Map<StateNode, Double> influenceNodes;

	MIDASInfluenzaModelNetworkParams params;

	/*
	 * The idea is that a transition can have outside influence. To define these
	 * outside influences we pass two things in addition to the standard
	 * parameters. First we pass the remote networks that can influence this
	 * network. Second, we pass the nodes that can influence this transition.
	 */
	public SusceptiblesToExposedWithOutsideInfluence(String name,
			IStateNode fromNode, IStateNode toNode,
			InfluenzaStateTransitionNetwork network,
			List<InfluenzaStateTransitionNetwork> remoteNetworks,
			String[] influenceNodeNames) {
		super(name, fromNode, toNode, network, remoteNetworks,
				MovementType.STILL_UNTREATED);

		addAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_SEIR_MODEL, "");
		addAttribute(NodeGroupConsts.SEIR_TO_INFECTED, "");

		// build a list of the names of the segments that influence this one
		influenceSegmentNames = new ArrayList<String>();
		for (String influenceSegmentName : network.visitorsToSegment.keySet()) {
			influenceSegmentNames.add(influenceSegmentName);
		}

		// the influence nodes are the E bin from the influence segments -=-
		// need to solve this with attributes
		influenceNodes = new HashMap<StateNode, Double>();
		for (String influenceSegmentName : influenceSegmentNames)
			for (AbstractStateTransitionNetwork visitorStn : remoteNetworks)
				if (influenceSegmentName
						.equalsIgnoreCase(visitorStn.segmentName))
					for (String influenceNodeName : influenceNodeNames)
						influenceNodes.put(visitorStn
								.getNode(influenceNodeName),
								network.visitorsToSegment
										.get(influenceSegmentName));

		// we are still going from Susceptible
		infectiousNode = network.getNode(MidasInfluenzaModel.INFECTIOUS);
		susceptibleNode = (StateNode) fromNode;
		exposedNode = (StateNode) toNode;
		params = (MIDASInfluenzaModelNetworkParams) network.theParams;

	}

	public double getP(int t) {
		if ((t > 0) && (network.getState().isOutbreak())) {

			double beta = params.getBeta();
			// ok we are returning a percentage here so be careful...
			double numToMove = 0d;
			for (Iterator<StateNode> it = influenceNodes.keySet().iterator(); it
					.hasNext();) {
				StateNode influenceNode = it.next();
				Double visitorInfluence = influenceNodes.get(influenceNode);
				numToMove += (influenceNode
						.getHistoricalNumberOfPeopleInTheNode(t - 1) * visitorInfluence);
			}
			numToMove *= beta
					* susceptibleNode
							.getHistoricalNumberOfPeopleInTheNode(t - 1);

			// again,careful here, we return what percentage to move
			return numToMove
					/ susceptibleNode
							.getHistoricalNumberOfPeopleInTheNode(t - 1);
		}
		return 0.0;
	}
}
