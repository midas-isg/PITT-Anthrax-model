package edu.pitt.rods.apollo.statetransitionnetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public interface IStateNode {

    public abstract String toString();

    /*
	 * This can only be done for the current hour, hence no temporal parameter.
	 */
    public abstract void transitionPeopleIntoNode(int hour, double n);

    /*
	 * People can transition from any hour, so we need a temporal parameter.
	 */
    public abstract void transitionPeopleOutOfNodeAtHour(int hour, double n,
            boolean diseaseProgression);

    public abstract double getNumberThatHaveBeenInNodeForNHours(int n);

    public abstract int getSizeOfNodeHistory();

    public abstract double getNumberOfPeopleInTheEntireNode(int timeIncrement);

    public abstract double getTotalNumberOfTransitionsOutOfNodeAtHour(
            int timeIncrement);

    public abstract double getNumberOfTreatablePeopleInTheEntireNode(int hour);

    public abstract double getNumberOfPeopleTreatedAtHour(int hour);

    public abstract StateTransitionFunction addTransitionFunction(
            Class<? extends StateTransitionFunction> c, IStateNode fromNode,
            IStateNode toNode);

    public abstract StateTransitionFunction addTransitionFunction(
            Class<? extends StateTransitionFunction> c, IStateNode fromNode,
            IStateNode toNode, List<InfluenzaStateTransitionNetwork> networks);

    public abstract ArrayList<StateTransitionFunction> getTransitionFunctions();

    public abstract AbstractStateTransitionNetwork getStateTransitionNetwork();

    public abstract String getName();

    public abstract void addAttribute(String key, String value);

    public abstract boolean hasAttribute(String key);

    public abstract String getAttribute(String key);

    // ok at the beginning of each hour...push the queue
    public abstract void update(Observable o, Object arg);
}