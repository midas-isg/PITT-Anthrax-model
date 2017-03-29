package edu.pitt.rods.apollo.statetransitionnetwork;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.TreeMap;

import org.apache.commons.math.util.ResizableDoubleArray;

import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.StateOfTheWorld;
import edu.pitt.rods.apollo.utilities.CompartmentPopulationQueue;

/*
 * Needs to implement SVP for the GUI.
 */
public class StateNode implements Observer, IStateNode {

    private double nodePopulation = 0d;

    public double getNodePopulation() {
        return nodePopulation;
    }

    public void setNodePopulation(double nodePopulation) {
        this.nodePopulation = nodePopulation;
    }
    /**
     *
     */
    private static final long serialVersionUID = 2687914198374749952L;

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#toString
	 * ()
	 */
    @Override
    public String toString() {
        String result = "";
        for (int i = 0; i < compartmentPopulationQueue.getSize(); i++) {
            if (compartmentPopulationQueue.getElementAt(i) != 0.0) {
                result += "[" + i + "="
                        + compartmentPopulationQueue.getElementAt(i) + "]";
            }
        }
        return result;
    }

    /*
     * The diseaseProgressionArray shows the number of people that progressed in
     * disease during this time-step.
     *
     * diseaseProgressionArray[8] shows the number of people who have been in
     * the node for eight hours that progressed. We need this as these people
     * can't be treated.
     */
    ResizableDoubleArray diseaseProgressionArray = new ResizableDoubleArray(1);

    /*
     * The compartmentPopulationQueue keeps track of the current population of
     * the node/compartment. The queue is N+1 hours long, where N is the amount
     * of hours that the simulation has been running for.
     */
    private CompartmentPopulationQueue compartmentPopulationQueue;

    /*
     * The compartmentTransitionQueue keeps track of the population that needs
     * to transition into the node at a given hour. At the end of the hour (on
     * update) the people in nodeEnteranceQueue are moved into
     * compartmentPopulationQueue. I know I could do this in a more memory
     * efficient manner, but my goal is speed so using a HashMap or similar
     * object that requires Objects (Integer, Double) would be much slower than
     * this solution that is using natives FAST!
     */
    private ResizableDoubleArray compartmentTransitionQueue = new ResizableDoubleArray(
            1);

    /*
     * The compartmentTransitionHistory keeps a historical record of when people
     * transition out of a compartment. This is only necessary for debugging
     * purposes and there should be function that turns off this history when we
     * are running in non-debug mode.
     */
    private ResizableDoubleArray compartmentTransitionHistory = new ResizableDoubleArray(
            1);

    /*
     * nodePopulationHistory keeps a historical record of the total population
     * of the node at every hour.
     */
    private ResizableDoubleArray nodePopulationHistory = new ResizableDoubleArray(
            1);

    /*
	 * We really aren't adding people to the compartmentPopulationQueue yet, we
	 * are just putting the population "on deck" to enter the node when update
	 * occurs.
	 * 
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * transitionPeopleIntoNode(int, double)
	 */
    public void transitionPeopleIntoNode(int hour, double n) {
        // compartmentPopulationQueue.addToElementAt(hour, n);
        compartmentTransitionQueue.setElement(hour,
                compartmentTransitionQueue.getElement(hour) + n);
        // addPopulation(n, hour);
    }

    /*
	 * Here we really are transitioning people out of the node...because
	 * transitions are based on the starting population queue
	 * 
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * transitionPeopleOutOfNodeAtHour(java.lang.Integer, java.lang.Double)
	 */
    public void transitionPeopleOutOfNodeAtHour(int hour, double n,
            boolean diseaseProgression) {

        compartmentTransitionQueue.setElement(hour,
                compartmentTransitionQueue.getElement(hour) - n);

        if (diseaseProgression) {
            compartmentTransitionHistory.setElement(hour - 1,
                    compartmentTransitionHistory.getElement(hour - 1) + n);
        }

        // People whose disease has progressed this hour can not be treated.
        // So we need to keep track of them so we can accurately determine
        // the percentage of people that can be treated

        if ((diseaseProgression) && (n != 0.0)) {
            // System.out.println(name + " " + hour + " " + n);
            diseaseProgressionArray.setElement(hour - 1,
                    diseaseProgressionArray.getElement(hour - 1) + n);
        }

    }

    public double getNumberThatHaveBeenInNodeForNHours(int n) {
        return compartmentPopulationQueue.getElementAt(n);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * getSizeOfNodeHistory()
	 */
    public int getSizeOfNodeHistory() {
        // experimental
        return compartmentPopulationQueue.getSize() - 1;
    }

    /*
	 * Specific metadata functions that will be used for graphs. That is, these
	 * function will keep an historical record of the node...
	 */

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * getNumberOfPeopleInTheEntireNode(java.lang.Integer)
	 */
    public double getNumberOfPeopleInTheEntireNode(int timeIncrement) {
        // return numberOfPeople.get(timeIncrement);
        // double result1 = nodeHistoryArray.getElement(timeIncrement);
        double result2 = 0.0;
        for (int i = 0; i < compartmentPopulationQueue.getSize(); i++) {
            result2 += compartmentPopulationQueue.getElementAt(i);
        }
        // if (result1 != result2)
        // System.out.println("R1 " + result1 + " != R2 " + result2);
        // return nodeHistoryArray.getElement(timeIncrement);
        return result2;
    }

    public double getHistoricalNumberOfPeopleInTheNode(int time) {
        return nodePopulationHistory.getElement(time);
    }
    String name;
    AbstractStateTransitionNetwork stateTransitionNetwork;
    TreeMap<String, String> attributes = new TreeMap<String, String>();
    ArrayList<StateTransitionFunction> transitionFunctions = new ArrayList<StateTransitionFunction>();

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * addTransitionFunction(java.lang.Class,
	 * edu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode,
	 * edu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode)
	 */
    public StateTransitionFunction addTransitionFunction(
            Class<? extends StateTransitionFunction> c, IStateNode fromNode,
            IStateNode toNode) {
        StateNode f = (StateNode) fromNode;
        StateNode t = (StateNode) toNode;

        StateTransitionFunction stf = TransitionFunctionFactory.createFunction(
                c, f, t, stateTransitionNetwork);
        transitionFunctions.add(stf);
        return stf;
    }

    public StateTransitionFunction addTransitionFunction(
            Class<? extends StateTransitionFunction> c,
            IStateNode fromNode,
            IStateNode toNode,
            List<InfluenzaStateTransitionNetwork> remoteStateTransitionNetworks,
            String[] array) {
        StateNode f = (StateNode) fromNode;
        StateNode t = (StateNode) toNode;

        StateTransitionFunction stf = TransitionFunctionFactory.createFunction(
                c, f, t, stateTransitionNetwork, remoteStateTransitionNetworks,
                array);
        transitionFunctions.add(stf);
        return stf;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * getTransitionFunctions()
	 */
    public ArrayList<StateTransitionFunction> getTransitionFunctions() {
        return transitionFunctions;
    }

    protected StateNode(String name, double numberOfPeople,
            AbstractStateTransitionNetwork stateTransitionNetwork) {
        this.name = name;
        // this.numberOfPeople.add(numberOfPeople);
        // this.nodeHistoryArray.addElement(numberOfPeople);
        this.stateTransitionNetwork = stateTransitionNetwork;
        setNodePopulation(numberOfPeople);
        compartmentPopulationQueue = new CompartmentPopulationQueue(
                numberOfPeople);
        compartmentTransitionHistory.addElement(0.0);
        // startingCompartmentPopulationQueue =
        // compartmentPopulationQueue.copy();
        resetInternalCounterArrays();
    }

    public void reset(Double numberOfPeople) {
        setNodePopulation(numberOfPeople);
        compartmentPopulationQueue = new CompartmentPopulationQueue(
                numberOfPeople);
        compartmentTransitionHistory.addElement(0.0);
        resetInternalCounterArrays();
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * getStateTransitionNetwork()
	 */
    public AbstractStateTransitionNetwork getStateTransitionNetwork() {
        return stateTransitionNetwork;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#getName
	 * ()
	 */
    public String getName() {
        return name;
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * addAttribute(java.lang.String, java.lang.String)
	 */
    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * hasAttribute(java.lang.String)
	 */
    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    /*
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#
	 * getAttribute(java.lang.String)
	 */
    public String getAttribute(String key) {
        return attributes.get(key);
    }

    /*
	 * Here is how things are working. First, for hour N all transition
	 * calculations are based off of the starting population of each compartment
	 * for hour N. All transitions are queued in the compartmentTransitionQueue.
	 * When update() is called, this queue is processed and the transitions
	 * between compartments occur.
	 * 
	 * I will explain this another way that might be clearer... During hour N,
	 * nothing internally changes until all calculations have been made for the
	 * hour. The calculations are stored in the compartmentTransitionQueue.
	 * Then, at the very last instant of the hour, every transition happens at
	 * once and the changes are reflected in hour N+1.
	 * 
	 * This means, when you are looking at hour N. You are really looking at the
	 * result of the transitions that occurred at hour N.
	 * 
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.bioecon.datamodel.statetransitionnetwork.IStateNode#update
	 * (java.util.Observable, java.lang.Object)
	 */
    public void update(Observable o, Object arg) {
        if (o instanceof StateOfTheWorld) {

            if (arg instanceof Integer) {
                // if the hour changes

                compartmentPopulationQueue.addElement(0.0);
                compartmentTransitionHistory.addElement(0.0);

                performQueuedTransitions();

                nodePopulationHistory.addElement(getNumberOfPeopleInTheEntireNode(0));

                resetInternalCounterArrays();

            }
        }

    }

    public double getNumberOfTreatablePeopleInTheEntireNode(int hour) {
        return getNumberOfPeopleInTheEntireNode(hour)
                - diseaseProgressionArray.getElement(hour);

    }

    public double getNumberOfPeopleTreatedAtHour(int hour) {
        return diseaseProgressionArray.getElement(hour);

    }

    private void performQueuedTransitions() {
        // minus one because this queue was from 1 hour ago
        // to move someone into hour 2 from hour 1, you add them
        // at hour 2, then time ticks, and they are placed at hour 2
		
        for (int i = 0; i < compartmentPopulationQueue.getSize() - 1; i++) {
            double diff = compartmentTransitionQueue.getElement(i);
            // if (name.equalsIgnoreCase("Prodromal No Treatment") &&
            // (diff>0.0))
            // System.out.println("ok");
            compartmentPopulationQueue.addToElementAt(i, diff);
            //
        }
    }

    private void resetInternalCounterArrays() {
        int size = compartmentPopulationQueue.getSize();
        diseaseProgressionArray = new ResizableDoubleArray(size);
        diseaseProgressionArray.setNumElements(size);
        compartmentTransitionQueue = new ResizableDoubleArray(size);
        compartmentTransitionQueue.setNumElements(size);
    }

    public double getTotalNumberOfTransitionsOutOfNodeAtHour(int timeIncrement) {
        return compartmentTransitionHistory.getElement(timeIncrement);
    }

    public StateTransitionFunction addTransitionFunction(
            Class<? extends StateTransitionFunction> c, IStateNode fromNode,
            IStateNode toNode, List<InfluenzaStateTransitionNetwork> networks) {
        // TODO Auto-generated method stub
        return null;
    }
}
