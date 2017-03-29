package edu.pitt.rods.apollo.statetransitionnetwork;

import java.util.ArrayList;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public abstract class AbstractTransitionFunction implements
        StateTransitionFunction {

    Logger logger = Logger.getLogger(this.getClass().getName());
    protected MovementType movementType;
    protected IStateNode fromNode;
    protected IStateNode toNode;
    protected String name;
    protected AbstractStateTransitionNetwork network;
    TreeMap<String, String> attributes = new TreeMap<String, String>();
    ArrayList<Double> parameters = new ArrayList<Double>();

    public ArrayList<Double> getParameters() {
        return parameters;
    }

    protected AbstractTransitionFunction(String name, IStateNode fromNodeName,
            IStateNode toNodeName, AbstractStateTransitionNetwork network,
            MovementType movementType) {
        super();
        logger.setLevel(Level.INFO);

        this.fromNode = fromNodeName;
        this.name = name;
        this.toNode = toNodeName;
        this.network = network;
        this.movementType = movementType;
    }

    public IStateNode getFromNode() {
        return fromNode;
    }

    public IStateNode getToNode() {
        return toNode;
    }

    public String getName() {
        return name;
    }

    public void addParameter(Double svp) {
        parameters.add(svp);
    }

    public void addAttribute(String key, String value) {
        attributes.put(key, value);
    }

    public boolean hasAttribute(String key) {
        return attributes.containsKey(key);
    }

    public String getAttribute(String key) {
        return attributes.get(key);
    }

    public MovementType getMovementType() {
        return movementType;
    }

    // should this be "final"
    public double process(int t, CompartmentModelHourlyResult result) {

        /*
         * if (network.state.isAlert()) { if
         * ((fromNode.getName().contains("Prodromal")) ||
         * (toNode.getName().contains("Prodromal"))) if (t > 110 && t < 120)
         * debug = true; }
         */
        /*
         * ok im going with the assumption that we need to work based on the
         * population of the node at the end of the last hour...
         *
         * so for example, if there are 1000 people in the node and we are
         * treating with a capacity of infinity and an efficacy of .45, then 450
         * people move
         *
         * if we aren't treating,
         */

        Double totalMoved = 0.0;
        // is this fishy!?!
        boolean diseaseProgression = hasAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL);
        //|| hasAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_SEIR_MODEL);
        for (int i = 0; i < fromNode.getSizeOfNodeHistory(); i++) {

            if ((i != 0)
                    || (hasAttribute(NodeGroupConsts.REFRACTORY_TRANSITION))) {
                // only 1 thing can happen at hour 0, that is moving refractory
                // people

                // disease progression nodes use initial population
                // treatment progressions use current population

                // basically...we are at hour 3, so there should be 0 people at
                // hour 0,1,2 they
                // in thie SIER model everyone is always at the end of the
                // array!!!
                double nodePop = 0;
                nodePop = fromNode.getNumberThatHaveBeenInNodeForNHours(i);
                //nodePop = ((StateNode)fromNode).getHistoricalNumberOfPeopleInTheNode(i-1);

                if (hasAttribute(NodeGroupConsts.TREATMENT)) {
                    nodePop = fromNode.getNumberThatHaveBeenInNodeForNHours(i)
                            - fromNode.getNumberOfPeopleTreatedAtHour(i);
                }
                if (nodePop > 0.0) {
                    double rate = getP(i);
                    // if we are treating we need to work with the value of the
                    // node at the last hour. Otherwise, we need to work with
                    // the current value of the node...
                    double numToMove = nodePop;
                    if (rate != 1.0) {
                        numToMove = numToMove *= rate;
                    }
                    if (rate > 1.0) {
                        System.out.println("na");
                    }

                    // add one because we bump the hour before we update...
                    fromNode.transitionPeopleOutOfNodeAtHour(i + 1, numToMove,
                            diseaseProgression);

                    if (diseaseProgression) // disease progressions move into hour 0
                    {
                        toNode.transitionPeopleIntoNode(0, numToMove);
                    } else // add one because we bump the hour before we update
                    {
                        toNode.transitionPeopleIntoNode(i + 1, numToMove);
                    }
                    totalMoved += numToMove;

                }

            }
        }
        // if (debug)
        // System.out.println(fromNode);

        return totalMoved;
    }
}
