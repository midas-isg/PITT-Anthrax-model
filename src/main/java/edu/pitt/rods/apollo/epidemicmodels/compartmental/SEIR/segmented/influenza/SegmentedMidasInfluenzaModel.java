package edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.segmented.influenza;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import edu.pitt.rods.apollo.epidemicmodels.AbstractSegmentedEpiModel;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza.MidasInfluenzaModel;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.epidemicmodels.params.SegmentedMidasInfluenzaModelParams;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel.ExposedToInfectious;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel.InfectiousToRecovered;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel.SusceptiblesToExposed;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel.SusceptiblesToRecovered;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.segmented.MIDASInfluenzaModel.SusceptiblesToExposedWithOutsideInfluence;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class SegmentedMidasInfluenzaModel extends AbstractSegmentedEpiModel
        implements Runnable {

    /**
     *
     */
    private static final long serialVersionUID = 2749253243446077321L;
    static Logger logger = Logger.getLogger(MidasInfluenzaModel.class);
    static DecimalFormat debugDecimalFormat = new DecimalFormat("#,##0.00##");
    // 2d double array, segment [x,y] is the susceptiblity rate of x to y
    // segment [x,x] is the susceptibility rate of people within their own
    // population
    SegmentedMidasInfluenzaModelParams theParams;
    Integer t = 0;
    protected List<InfluenzaStateTransitionNetwork> networks = new ArrayList<InfluenzaStateTransitionNetwork>();

    public SegmentedMidasInfluenzaModel(String args,
            List<MidasInfluenzaModel> segments,
            List<InfluenzaStateTransitionNetwork> networks) {
        super(args);
        theParams = new SegmentedMidasInfluenzaModelParams(this);

        this.segments = segments;
        this.networks = networks;
    }

    public SegmentedMidasInfluenzaModel(String args) {
        super(args);
//		theParams = new SegmentedMidasInfluenzaModelParams(this);
//
//		MidasInfluenzaModel alleghModel = MidasInfluenzaModel.getMeAllegheny();
//		// alleghModel.segmentName = "allegheny";
//		// alleghModel.getSTN().segmentName = "allegheny";
//		alleghModel.getSTN().visitorsToSegment.put("westmoreland", 0.15);
//
//		MidasInfluenzaModel westModel = MidasInfluenzaModel.getMeWestmoreland();
//		westModel.segmentName = "westmoreland";
//		westModel.getSTN().segmentName = "westmoreland";
//		westModel.getSTN().visitorsToSegment.put("allegheny", 0.15);
//
//		networks.add(alleghModel.getSTN());
//		networks.add(westModel.getSTN());
//
//		segments.add(alleghModel);
//		segments.add(westModel);
//
//		buildSegmentedModel(alleghModel);
//		buildSegmentedModel(westModel);
//
//		addWestmorelandInfluenceToAlleghenyModel(alleghModel, westModel,
//				networks);

    }

    // everything stays the same except we add the sick folks from westmoreland
    public static void buildSegmentedModel(MidasInfluenzaModel model) {
        AbstractStateTransitionNetwork stn = model.getSTN();
        // hopefully self explanatory
        IStateNode fromNode, toNode;

        logger.info("EpiModel Parameters: ");
        logger.info("\tModel Population: "
                + debugDecimalFormat.format(model.getTheParams().getTotalNumberOfPeople()));
        logger.info("\tInitial Infectious: "
                + debugDecimalFormat.format(model.getTheParams().getInitialNumberOfInfectionsPeople()));
        logger.info("\tFraction Immune: "
                + debugDecimalFormat.format(model.getTheParams().getFractionImmune()));
        logger.info("\tNum PreemptivelyTreated: "
                + debugDecimalFormat.format(model.getTheParams().getNumPreemptivelyTreated()));

        double numImmune = (model.getTheParams().getTotalNumberOfPeople() + model.getTheParams().getInitialNumberOfInfectionsPeople())
                * model.getTheParams().getFractionImmune()
                + model.getTheParams().getNumPreemptivelyTreated();

        double numSusceptible = model.getTheParams().getTotalNumberOfPeople()
                - numImmune
                - model.getTheParams().getInitialNumberOfInfectionsPeople();

        // just adding the nodes to the network, without the edges
        // ordering is important here. The order here determines the order of
        // the columns in the output file
        stn.addNode(MidasInfluenzaModel.SUSCEPTIBLES, numSusceptible);
        stn.addNode(MidasInfluenzaModel.EXPOSED);

        if (model.outbreak) {
            stn.addNode(MidasInfluenzaModel.INFECTIOUS, model.getTheParams().getInitialNumberOfInfectionsPeople());
        } else {
            stn.addNode(MidasInfluenzaModel.INFECTIOUS, 0);
        }

        stn.addNode(MidasInfluenzaModel.RECOVERED, numImmune);

        // now add the edges
        // S->E
        fromNode = stn.getNode(MidasInfluenzaModel.SUSCEPTIBLES);
        toNode = stn.getNode(MidasInfluenzaModel.EXPOSED);
        fromNode.addTransitionFunction(SusceptiblesToExposed.class, fromNode,
                toNode);

        // E->I
        fromNode = stn.getNode(MidasInfluenzaModel.EXPOSED);
        toNode = stn.getNode(MidasInfluenzaModel.INFECTIOUS);
        fromNode.addTransitionFunction(ExposedToInfectious.class, fromNode,
                toNode);

        // I->R
        fromNode = stn.getNode(MidasInfluenzaModel.INFECTIOUS);
        toNode = stn.getNode(MidasInfluenzaModel.RECOVERED);
        fromNode.addTransitionFunction(InfectiousToRecovered.class, fromNode,
                toNode);

        // part of Update 1: add the vaccination transitions
        fromNode = stn.getNode(MidasInfluenzaModel.SUSCEPTIBLES);
        fromNode.addAttribute(
                NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN, null);
        toNode = stn.getNode(MidasInfluenzaModel.RECOVERED);
        fromNode.addTransitionFunction(SusceptiblesToRecovered.class, fromNode, toNode);

        // that is it, all we need to do now is execute!

    }

    public static void addWestmorelandInfluenceToAlleghenyModel(
            MidasInfluenzaModel alleghModel, MidasInfluenzaModel westmModel,
            List<InfluenzaStateTransitionNetwork> networks) {
        // now add the edges
        // S->E
        AbstractStateTransitionNetwork alleghStn = alleghModel.getSTN();
        //AbstractStateTransitionNetwork westmStn = westmModel.getSTN();

        StateNode fromNode = alleghStn.getNode(MidasInfluenzaModel.SUSCEPTIBLES);
        StateNode toNode = alleghStn.getNode(MidasInfluenzaModel.EXPOSED);
        fromNode.addTransitionFunction(
                SusceptiblesToExposedWithOutsideInfluence.class, fromNode,
                toNode, networks,
                new String[]{MidasInfluenzaModel.INFECTIOUS});
    }

    public boolean hasNext() {
        return t < theParams.getIterations();
    }

    public void next() {
        for (MidasInfluenzaModel model : segments) {
            model.hourlyResult = new CompartmentModelHourlyResult();
            model.next();
            String[] nodeNames = null;
            double[] origNodePops = null;
            double[] endingNodePops = null;
            double totNumInModel = 0.0;
            AbstractStateTransitionNetwork stn = model.getSTN();
            ArrayList<StateNode> nodes = stn.getNodes();
            if (outputToFile) {
                nodeNames = new String[nodes.size()];
                origNodePops = new double[nodes.size()];
                endingNodePops = new double[nodes.size()];

                for (int i = 0; i < nodes.size(); i++) {
                    nodeNames[i] = nodes.get(i).getName();
                    origNodePops[i] = nodes.get(i).getNumberOfPeopleInTheEntireNode(
                            stn.getState().getHour());
                }

                // increment the hour to trigger queued transitions,
                // everybody
                // transitions at once...on the very last instant of the
                // hour

                // this causes "observers" of the state to process their
                // update
                // queues. A lot happens as a result of this call, it is not
                // simply hour++
                stn.getState().incHour();

                // more data for output...including the graph this time
                for (int i = 0; i < nodes.size(); i++) {
                    double totInNode = nodes.get(i).getNumberOfPeopleInTheEntireNode(
                            stn.getState().getHour());
                    if (outputToFile) {
                        endingNodePops[i] = totInNode;
                        totNumInModel += endingNodePops[i];
                    }
                    model.hourlyResult.addCountToDescriptor("count_"
                            + nodes.get(i).getName(), totInNode);
                }

                // add the hourly result to the run results
                model.resultOfLastRun.addResult(model.hourlyResult);

                if (outputToFile) {

                    try {
                        model.emo.writeSummary(t, nodeNames, origNodePops,
                                endingNodePops, totNumInModel,
                                model.hourlyResult);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
        t++;
        // need some way to see the # infected in each segment, then ADD
        // additional infected based on the susceptibility rate


    }

    public Map<String, String> execute() {
        while (hasNext()) {
            next();
        }

        HashMap<String, String> map = new HashMap<String, String>();

        for (MidasInfluenzaModel model : segments) {
            map.put(model.segmentName, model.getResults());
        }

        return map;
    }

    public static void main(String args[]) {
//		EpiModelParamStringBuilder paramStringBuilder = new EpiModelParamStringBuilder();
//
//		paramStringBuilder.addParam(RunParameterSet.PARAM_RUN_DURATION,
//				"integer", "500");
//
//		String paramString = paramStringBuilder.getParamString();
//
//		SegmentedMidasInfluenzaModel model = new SegmentedMidasInfluenzaModel(
//				paramString);
//
//		Map<String, String> map = model.execute();
//		for (Iterator<String> it = map.keySet().iterator(); it.hasNext();) {
//			ParameterizedComponent parameters = new ParameterizedComponent(
//					it.next());
//			//
//		}
    }

    public void run() {
        // TODO Auto-generated method stub
    }

    public String getResults() {
        // TODO Auto-generated method stub
        return null;
    }
}
