package edu.pitt.rods.apollo.statetransitionnetwork;

import java.util.ArrayList;
import java.util.HashMap;

import edu.pitt.rods.apollo.ParameterizedComponent;
import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;
import edu.pitt.rods.apollo.math.GompertzFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.StateOfTheWorld;

public abstract class AbstractStateTransitionNetwork extends ParameterizedComponent {

    /**
     *
     */
    private static final long serialVersionUID = 5859952387642838982L;
    public String segmentName = "";
    public HashMap<String, Double> visitorsToSegment = new HashMap<String, Double>();
    private double percentCompliantWithTreatment = 0.0;
    boolean treatBeforeCertainty = true;
    StateOfTheWorld state;
    // private GompertzFunction rcp = new GompertzFunction(0.0, -5.0, -0.0185);
    ArrayList<StateNode> nodes;

    public AbstractStateTransitionNetwork(String args) {
        super(args);
        nodes = new ArrayList<StateNode>();
        state = new StateOfTheWorld(percentCompliantWithTreatment);
    }

    public boolean isTreatBeforeCertainty() {
        return treatBeforeCertainty;
    }

    public boolean isInTreatmentMode() {
        return ((isTreatBeforeCertainty() && getState().isAlert()) || (getState().isOutbreakCertain()));
    }

    public StateOfTheWorld getState() {
        return state;
    }

    public void resetState() {
        state.reset();
    }

    public ArrayList<StateNode> getAllNodesInGroup(String group) {
        ArrayList<StateNode> result = new ArrayList<StateNode>();
        for (StateNode node : getNodes()) {
            if (node.hasAttribute(group)) {
                result.add(node);
            }
        }
        return result;
    }

    public ArrayList<StateTransitionFunction> getAllFunctionsInGroup(
            String group) {
        ArrayList<StateTransitionFunction> result = new ArrayList<StateTransitionFunction>();
        for (StateNode node : getNodes()) {
            for (StateTransitionFunction s : node.getTransitionFunctions()) {
                if (s.hasAttribute(group)) {
                    result.add(s);
                }
            }

        }
        return result;
    }

    public double[] getTreatment(IStateNode node) {
        double capacity = 1000.0;
        double[] result = new double[node.getSizeOfNodeHistory()];
        for (int i = 0; i < node.getSizeOfNodeHistory(); i++) {
            result[i] = 0.0;
        }

        // Collections.fill(treat, 0.0);

        for (int i = node.getSizeOfNodeHistory() - 1; i > 0; i--) {
            double sickest = node.getNumberThatHaveBeenInNodeForNHours(i);
            if (sickest < capacity) {
                result[i] = sickest;
                capacity -= sickest;
            } else {
                result[i] = capacity;
                capacity = 0.0;
            }
        }
        return result;
    }

    public StateNode addNode(String name, double numberOfPeople) {
        StateNode node = getNode(name);
        if (node == null) {
            node = new StateNode(name, numberOfPeople, this);
            this.nodes.add(node);
            state.addObserver(node);
        }
        return node;
    }

    public IStateNode addNode(String name) {
        StateNode node = getNode(name);
        if (node == null) {
            node = new StateNode(name, 0.0, this);
            this.nodes.add(node);
            state.addObserver(node);
        }
        return node;
    }

    public ArrayList<StateNode> getNodes() {
        return nodes;
    }

    public StateNode getNode(String name) {
        for (StateNode s : getNodes()) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }

        }
        return null;
    }

    public double getResponseCapacityForGroup(ArrayList<String> groups,
            IStateNode fromNode, int hour) {

        // double oldPopulationOfNode = fromNode
        // .getNumberOfPeopleInTheEntireNode(hour);

        double populationOfNode = fromNode.getNumberOfTreatablePeopleInTheEntireNode(hour);
        // if (oldPopulationOfNode != populationOfNode) {
        // System.out.println("Was using " + oldPopulationOfNode +
        // " now using: " + populationOfNode);
        // }
        if (populationOfNode == 0.0) {
            return 0.0;
        }
        // double responseCapacity = ((ResponseCapacityParameter)
        // getParameter(ParameterNames.ResponseCapacity))
        // .getCapacity(state.getHour());
        // TODO
        // double responseCapacity = 0d;
        double responseCapacity = getVaccinationCapacity(state.getHoursSinceAlert());

        ArrayList<StateNode> nodeGroup = new ArrayList<StateNode>();
        for (String ng : groups) {
            nodeGroup.addAll(getAllNodesInGroup(ng));
        }
        double populationOfGroup = 0.0;

        for (IStateNode groupNode : nodeGroup) {
            populationOfGroup += groupNode.getNumberOfTreatablePeopleInTheEntireNode(hour);
        }
        if (populationOfGroup == 0.0) {
            return 0.0;
        }

        if (populationOfGroup <= responseCapacity) {
            return 1.0;
        }
        double result = (responseCapacity / populationOfGroup);
        if (result > 1) {
            System.out.println("hi!");
        }
        return result;
    }

    abstract public double getVaccinationCapacity(int temporalUnit);
}