package edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;

import edu.pitt.rods.apollo.ParameterizedComponent;
import edu.pitt.rods.apollo.dictionaries.UtilityFunctionAttributeNames;
import edu.pitt.rods.apollo.epidemicmodels.AbstractEpiModel;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentEpiModelGlobalResult;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelNetworkParams;
import edu.pitt.rods.apollo.epidemicmodels.params.MIDASInfluenzaModelParams;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.MIDASInfluenzaModel.*;
import edu.pitt.rods.apollo.epidemicmodels.utils.CompartmentInfluenzaSEIRModelOutput;
import edu.pitt.rods.apollo.epidemicmodels.utils.CompartmentModelOutput;
import edu.pitt.rods.apollo.epidemicmodels.utils.EpiModelParamStringBuilder;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.InfluenzaStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.StateTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

/*
 * An implementation of the Influenza SIER Compartment Model provided by Bruce
 * Lee in Excel format.
 *
 * 4/9/2010, adding the ability for multiple epi models to interact. This
 * requires the ability for epiModels to know the state of other epiModels, this
 * will be accomplished in the following way: 1. epimodels will implement
 * iterator 2. next will run one iteraton 3. epi models will expose a "Read only"
 * method that will return the population of compartments
 *
 */
public class MidasInfluenzaModel extends AbstractEpiModel implements Runnable {

    static Logger logger = Logger.getLogger(MidasInfluenzaModel.class);
    DecimalFormat debugDecimalFormat = new DecimalFormat("#,##0.00##");
    private static final long serialVersionUID = 1825693176266339371L;
    public String segmentName;
    private Integer t = 0;
    public CompartmentModelOutput emo;
    boolean stopWhenNoNewInfectionsOccur = false;
    boolean noNewInfectionsOccurred = false;
    private String resultStr = "";

    /*
     * modelFinishedAt - we need to know this when writing the output files, it
     * basically tells us the array length of the compartments
     */
    private Integer modelFinishedAt = 0;
    public CompartmentEpiModelGlobalResult resultOfLastRun = null;
    public CompartmentModelHourlyResult hourlyResult = null;
    public static boolean debug = false;
    public static String outputPath = "";
    // the directory which program output will be written
    // private File outputFile = null;
    // description information
    public static final String modelName = "Influenza SEIR Compartment Model";
    public static final String modelAuthor = "Bruce Lee";
    public static final String modelDescription = "Influenza SEIR Compartment Model";
    MIDASInfluenzaModelParams theParams;

    public MIDASInfluenzaModelParams getTheParams() {
        return theParams;
    }
    
    // node names
    public static final String SUSCEPTIBLES = "susceptibles";
    public static final String EXPOSED = "exposed";
    public static final String INFECTIOUS = "infectious";
    public static final String RECOVERED = "recovered";
    public static final String SUSCEPTIBLE_VACCINATED = "susceptible_vaccinated";
    
    public boolean outbreak = true;
    // define the inputnodes
    public static final String[] inputNodes = {SUSCEPTIBLES, INFECTIOUS, RECOVERED};
    // define the non-input nodes
    public static final String[] nonInputNodes = {EXPOSED, SUSCEPTIBLE_VACCINATED};
    
    // remnanat from the Anthrax model. I don't have refractory or treatment
    // transitions built into the model yet. (I will need these soon though)
    public static final String nodeOrdering[] = {
        NodeGroupConsts.REFRACTORY_TRANSITION,
        NodeGroupConsts.DISEASE_PROGRESSION_FOR_SEIR_MODEL,
        NodeGroupConsts.TREATMENT};
    
    InfluenzaStateTransitionNetwork stn;

    public MidasInfluenzaModel(String args) {
        // calls unpack, so parameters is now populated
        super(args);
        //	logger.setLevel(Level.DEBUG);
        theParams = new MIDASInfluenzaModelParams(this);
        this.segmentName = theParams.getSegmentName();
        stn = new InfluenzaStateTransitionNetwork(args);
        stn.segmentName = this.segmentName;

        resultOfLastRun = new CompartmentEpiModelGlobalResult();
        hourlyResult = new CompartmentModelHourlyResult();

        if (outputToFile) {
            try {
                emo = new CompartmentInfluenzaSEIRModelOutput(stn, outputPath);

                emo.openOutputFiles();
                emo.writeHeaders();

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

    }

    public InfluenzaStateTransitionNetwork getSTN() {
        return stn;
    }

    // builds the model "manually." I would love for this to come from XML
    // or something in the future.
    public void buildNonSegmentedModel() {

        // hopefully self explanatory
        IStateNode fromNode, toNode;

        logger.info("EpiModel Parameters: ");
        logger.info("\tModel Population: "
                + debugDecimalFormat.format(theParams.getTotalNumberOfPeople()));
        logger.info("\tInitial Infectious: "
                + debugDecimalFormat.format(theParams.getInitialNumberOfInfectionsPeople()));
        logger.info("\tFraction Immune: "
                + debugDecimalFormat.format(theParams.getFractionImmune()));
        logger.info("\tNum PreemptivelyTreated: "
                + debugDecimalFormat.format(theParams.getNumPreemptivelyTreated()));

        // double numImmune = (theParams.getTotalNumberOfPeople() + theParams
        // .getInitialNumberOfInfectionsPeople())
        // * theParams.getFractionImmune()
        // + theParams.getNumPreemptivelyTreated();
        //
        // double numSusceptible = theParams.getTotalNumberOfPeople() -
        // numImmune
        // - theParams.getInitialNumberOfInfectionsPeople();

        // just adding the nodes to the network, without the edges
        // ordering is important here. The order here determines the order of
        // the columns in the output file
        // stn.addNode(SUSCEPTIBLES, numSusceptible);
        stn.addNode(SUSCEPTIBLES, theParams.getSusceptible());
        if (outbreak) {
            // stn.addNode(INFECTIOUS,
            // theParams.getInitialNumberOfInfectionsPeople());
            stn.addNode(INFECTIOUS, theParams.getInfectious());
            // stn.addNode(EXPOSED,
            // theParams.getInitialNumberOfInfectionsPeople() / 2);
            stn.addNode(EXPOSED, theParams.getExposed());

        } else {
            stn.addNode(INFECTIOUS, 0);
            stn.addNode(EXPOSED);
        }
        stn.addNode(SUSCEPTIBLE_VACCINATED, 0);
        stn.addNode(RECOVERED, theParams.getRecovered());

        // now add the edges
        // S->E
        fromNode = stn.getNode(SUSCEPTIBLES);
        toNode = stn.getNode(EXPOSED);
        fromNode.addTransitionFunction(SusceptiblesToExposed.class, fromNode,
                toNode);

        // E->I
        fromNode = stn.getNode(EXPOSED);
        toNode = stn.getNode(INFECTIOUS);
        fromNode.addTransitionFunction(ExposedToInfectious.class, fromNode,
                toNode);

        // I->R
        fromNode = stn.getNode(INFECTIOUS);
        toNode = stn.getNode(RECOVERED);
        fromNode.addTransitionFunction(InfectiousToRecovered.class, fromNode,
                toNode);

        // Add the vaccination transitions
        // S->R
        fromNode = stn.getNode(SUSCEPTIBLES);
        fromNode.addAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN, null);
        toNode = stn.getNode(RECOVERED);
        fromNode.addTransitionFunction(SusceptiblesToRecovered.class, fromNode, toNode);
        
        // S->SV
        fromNode = stn.getNode(SUSCEPTIBLES);
        toNode = stn.getNode(SUSCEPTIBLE_VACCINATED);
        fromNode.addTransitionFunction(SusceptiblesToSusceptibleVaccinated.class, fromNode, toNode);
        
        // SV->E
        fromNode = stn.getNode(SUSCEPTIBLE_VACCINATED);
        toNode = stn.getNode(EXPOSED);
        fromNode.addTransitionFunction(SusceptibleVaccinatedToExposed.class, fromNode, toNode);

        // that is it, all we need to do now is execute!
    }

    public String execute() {

        /*
         * resultOfLastRun - used for record keeping. Provides quick access to
         * the most recent hourly result.
         */

        try {
            // just initializing the output

            while (hasNext()) {
                // the hourly result object is basically a history of what
                // happened at every hour
                hourlyResult = new CompartmentModelHourlyResult();
                next();

                // gather data for graphical output

                // gathering output information...no meat here
                String[] nodeNames = null;
                double[] origNodePops = null;
                double[] endingNodePops = null;
                double totNumInModel = 0.0;
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
                    hourlyResult.addCountToDescriptor("count_"
                            + nodes.get(i).getName(), totInNode);
                }

                // add the hourly result to the run results

                resultOfLastRun.addResult(hourlyResult);

                if (outputToFile) {

                    emo.writeSummary(t, nodeNames, origNodePops,
                            endingNodePops, totNumInModel, hourlyResult);
                }

                // }
            }

            if (outputToFile) {
                emo.writeEnd(modelFinishedAt);
                // emo.writeBasicOutput(calculateUtility());
            }

            if (outputToFile) {
                emo.closeOutputFiles();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return buildResultString();

    }

    public String getResults() {
        return buildResultString();
    }

    public void next() {

        /*
         * hoursSinceAlert - needed to know when the "hrsToCertainty" constraint
         * has been satisfied, as hours to certainty is relative to the time of
         * alert.
         */
        // not implemented yet
        // int hoursSinceAlert = 0;

        /*
         * emo - just a helper class to extract some of the output functionality
         * from this method.
         */

        // run the model from hour 0 until TIME
        // System.out.println(t);
        // alert code, really doesn't do much yet, but will in the
        // future
        // if (t > theParams.getIterationOfAlert())
        // stn.getState().signalAlert();
        // if (stn.getState().isAlert()) {
        // if (hoursSinceAlert >= theParams.getIterationsToCertainty())
        // stn.getState().signalOutbreakCertain();
        // hoursSinceAlert++;
        // }
        if (outputToFile) {
            try {
                emo.getGeneralOutput().write(Integer.toString(t));
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        // Here is the meat of the engine. It process transitions in the
        // specified
        // order, that is why we loop over "stfOrder." For example,
        // refractory transitions
        // happen before disease progressions, and disease progressions
        // happen
        // before tratment progressions...
        ArrayList<IStateNode> alreadyRan = new ArrayList<IStateNode>();

        for (String stfOrder : nodeOrdering) {
            for (IStateNode currentNode : stn.getNodes()) {
                for (StateTransitionFunction stf : currentNode.getTransitionFunctions()) {
                    if (stf.hasAttribute(stfOrder)) /*
                     * && (!alreadyRan.contains(currentNode)))
                     */ {
                        double numTransitioned = stf.process(t, hourlyResult);

                        logger.debug(segmentName
                                + " TemporalPoint "
                                + t
                                + ": "
                                + stf.getFromNode().getName()
                                + "("
                                + debugDecimalFormat.format(stf.getFromNode().getNumberOfPeopleInTheEntireNode(t))
                                + ")" + " --"
                                + debugDecimalFormat.format(numTransitioned)
                                + "--> " + stf.getToNode().getName());

                        recordDiseaseProgression(stf, hourlyResult,
                                numTransitioned);
                        recordMovementProgression(stf, hourlyResult,
                                numTransitioned);
                        if ((t > 10)
                                && (stf.hasAttribute(NodeGroupConsts.SEIR_TO_INFECTED))) {
                            noNewInfectionsOccurred = (numTransitioned > 0d) ? false
                                    : true;
                        }

                        alreadyRan.add(currentNode);
                    }
                }
            }
        }

        modelFinishedAt = t;

        t++;
    }

    private String buildResultString() {
        // double numInfectious = resultOfLastRun
        // .getDiseaseProgressionCount(NodeGroupConsts.SEIR_TO_INFECTIOUS)
        // + theParams.getInitialNumberOfInfectionsPeople();

        double numTreated = resultOfLastRun.getTreatmentCount(MovementType.PROPHYLAXED.name());
        double numInfected;
        if (outbreak) {
            numInfected = resultOfLastRun.getDiseaseProgressionCount(NodeGroupConsts.SEIR_TO_INFECTED);
        } // + numInfectious;
        else {
            numInfected = 0d;
        }

        String numVaccinatedStr = UtilityFunctionAttributeNames.NUM_VACCINATED
                + ":" + UtilityFunctionAttributeNames.NUM_VACCINATED_TYPE + "="
                + String.valueOf(numTreated) + ";";

        String numInfectedStr = UtilityFunctionAttributeNames.NUM_INFECTED
                + ":" + UtilityFunctionAttributeNames.NUM_INFECTED_TYPE + "="
                + String.valueOf(numInfected) + ";";

        // String modelResultsStr =
        // MIDASInfluenzaModelResultParams.RESULT_GLOBALRESULT
        // + ":os="
        // + ObjectStringSerializer
        // .SerializeObjectToString(resultOfLastRun) + ";";

        double[] sCurve = new double[resultOfLastRun.hourlyResults.size()];
        double[] eCurve = new double[resultOfLastRun.hourlyResults.size()];
        double[] iCurve = new double[resultOfLastRun.hourlyResults.size()];
        double[] rCurve = new double[resultOfLastRun.hourlyResults.size()];

        for (int i = 0; i < resultOfLastRun.hourlyResults.size(); i++) {
            CompartmentModelHourlyResult res = resultOfLastRun.hourlyResults.get(i);
            // ec.getSusceptible()
            // .getValue()
            // .add(res.getValue("count_"
            // + MidasInfluenzaModel.SUSCEPTIBLES));
            sCurve[i] = res.getValue("count_" + MidasInfluenzaModel.SUSCEPTIBLES) +
                        res.getValue("count_" + MidasInfluenzaModel.SUSCEPTIBLE_VACCINATED);
            // ec.getExposed().getValue()
            // .add(res.getValue("count_" + MidasInfluenzaModel.EXPOSED));
            eCurve[i] = res.getValue("count_" + MidasInfluenzaModel.EXPOSED);
            // ec.getInfectious()
            // .getValue()
            // .add(res.getValue("count_" + MidasInfluenzaModel.INFECTIOUS));
            iCurve[i] = res.getValue("count_" + MidasInfluenzaModel.INFECTIOUS);
            // ec.getRecovered()
            // .getValue()
            // .add(res.getValue("count_" + MidasInfluenzaModel.RECOVERED));
            rCurve[i] = res.getValue("count_" + MidasInfluenzaModel.RECOVERED);
        }

        double[] incidenceCurve = resultOfLastRun.getDiseaseProgressionCurve(NodeGroupConsts.SEIR_TO_INFECTIOUS);

        double[] vaccinationCurve = resultOfLastRun.getDiseaseProgressionCurve(MovementType.PROPHYLAXED.name());

        String vaccinationCurveStr = InfluenzaModelResultConstants.VACCINATION_CURVE
                + ":"
                + InfluenzaModelResultConstants.VACCINATION_CURVE_TYPE
                + "=" + Arrays.toString(vaccinationCurve) + ";";

        String sCurveStr = UtilityFunctionAttributeNames.S_CURVE + ":"
                + UtilityFunctionAttributeNames.INCIDENCE_CURVE_TYPE + "="
                + Arrays.toString(sCurve) + ";";
        String eCurveStr = UtilityFunctionAttributeNames.E_CURVE + ":"
                + UtilityFunctionAttributeNames.INCIDENCE_CURVE_TYPE + "="
                + Arrays.toString(eCurve) + ";";
        String iCurveStr = UtilityFunctionAttributeNames.I_CURVE + ":"
                + UtilityFunctionAttributeNames.INCIDENCE_CURVE_TYPE + "="
                + Arrays.toString(iCurve) + ";";
        String rCurveStr = UtilityFunctionAttributeNames.R_CURVE + ":"
                + UtilityFunctionAttributeNames.INCIDENCE_CURVE_TYPE + "="
                + Arrays.toString(rCurve) + ";";

        String modelResultsStr = sCurveStr + eCurveStr + iCurveStr + rCurveStr;

        String incidenceCurveStr = UtilityFunctionAttributeNames.INCIDENCE_CURVE
                + ":"
                + UtilityFunctionAttributeNames.INCIDENCE_CURVE_TYPE
                + "=" + Arrays.toString(incidenceCurve) + ";";

        logger.info("Results for " + this.segmentName + " EpiModel Run:");

        logger.info("\tNum Vaccinated:" + debugDecimalFormat.format(numTreated));
        logger.info("\tNum Infected:" + debugDecimalFormat.format(numInfected));

        return numVaccinatedStr + numInfectedStr + modelResultsStr
                + incidenceCurveStr + vaccinationCurveStr;

    }

    private void recordDiseaseProgression(StateTransitionFunction stf,
            CompartmentModelHourlyResult result, double numToMove) {

        if (stf.hasAttribute(NodeGroupConsts.SEIR_TO_INFECTED)) {
            result.addCountToDescriptor(NodeGroupConsts.SEIR_TO_INFECTED,
                    numToMove);
        }

        if (stf.hasAttribute(NodeGroupConsts.SEIR_TO_INFECTIOUS)) {
            result.addCountToDescriptor(NodeGroupConsts.SEIR_TO_INFECTIOUS,
                    numToMove);
        }

        if (stf.hasAttribute(NodeGroupConsts.SEIR_TO_IMMUNE)) {
            result.addCountToDescriptor(NodeGroupConsts.SEIR_TO_IMMUNE,
                    numToMove);
        }

    }

    private void recordMovementProgression(StateTransitionFunction stf,
            CompartmentModelHourlyResult result, double numToMove) {
        result.addCountToDescriptor(stf.getMovementType().name(), numToMove);

    }

    public static String getDefaultParams(double population, double r0,
            String segmentName) {
        double totalPopulation = population;
        double numInfectious = 1;
        // double r0 = 1d;
        double aveDurInfect = 2.0;
        double aveInfous = 6.0;

        // String path = null;
        //
        // Properties properties = new Properties();
        // URL url = ClassLoader.getSystemResource("bioecon.properties");
        // try {
        // properties.load(url.openStream());
        // path = properties.getProperty("debug_output_directory");
        //
        // } catch (IOException e1) {
        // System.out.println("Unable to open properties file.");
        // System.exit(-1);
        // }

        EpiModelParamStringBuilder paramStringBuilder = new EpiModelParamStringBuilder();
        paramStringBuilder.addParam(MIDASInfluenzaModelNetworkParams.R0_PARAM,
                "double", String.valueOf(r0));
        paramStringBuilder.addParam(
                MIDASInfluenzaModelNetworkParams.AVE_DURATION_OF_INFECTIOUSNESS_D_PARAM,
                "double", String.valueOf(aveInfous));
        paramStringBuilder.addParam(
                MIDASInfluenzaModelNetworkParams.AVE_LATENT_PERIOD_D_PARAM,
                "double", String.valueOf(aveDurInfect));

        double numContactPerT = (r0 / aveInfous) / totalPopulation;
        paramStringBuilder.addParam(
                MIDASInfluenzaModelNetworkParams.BETA_PARAM, "double",
                Double.toString(numContactPerT));

        // StateTransitionNetwork stn = new StateTransitionNetwork(
        // paramStringBuilder.getParamString());
        String stnParams = paramStringBuilder.getParamString();

        paramStringBuilder = new EpiModelParamStringBuilder();
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.SEGMENT_NAME_PARAM, "string",
                segmentName);
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.ITERATIONS_TO_CERTAINTY, "integer",
                "0");
        paramStringBuilder.addParam(
                MIDASInfluenzaModelNetworkParams.OUTBREAK_PARAM, "boolean",
                "true");

        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.PARAM_PREEMPTIVELY_TREATED, "double",
                "0");

        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.PARAM_VACCINATION_SCHEDULE,
                ParameterizedComponent.PARAM_1D_DOUBLE_ARRAY,
                Arrays.toString(new double[0]));
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.PARAM_VACCINATION_EFFICACY,
                "double", "1");

        paramStringBuilder.addParam("Run Duration (In Units)", "integer",
                "10000");
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.TOTAL_NUM_PEOPLE_PARAM, "double",
                String.valueOf(totalPopulation));
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.TOTAL_NUM_INFECTIOUS_PEOPLE_PARAM,
                "double", String.valueOf(numInfectious));
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.ITERATION_OF_ALERT_PARAM, "integer",
                "24");
        paramStringBuilder.addParam(
                MIDASInfluenzaModelParams.FRACTION_IMMUNE_PARAM, "double",
                "0.0");

        return stnParams + paramStringBuilder.getParamString();

    }

    // public static MidasInfluenzaModel getMeAllegheny() {
    // MidasInfluenzaModel model = new MidasInfluenzaModel(getDefaultParams(
    // 1215103, "allegheny"));
    //
    // return model;
    // }
    //
    // public static MidasInfluenzaModel getMeWestmoreland() {
    // MidasInfluenzaModel model = new MidasInfluenzaModel(getDefaultParams(
    // 361589, "westmoreland"));
    //
    // return model;
    // }
    // public static final String OUTPUT_FILENAME_PARAM = "outputFileName";
    public static void main(String[] args) throws IOException {
        // MidasInfluenzaModel model = new MidasInfluenzaModel(getDefaultParams(
        // 304059724, 1.3, "allegheny"));
        // allegh 1215103

        //MidasInfluenzaModel model2 = new MidasInfluenzaModel("");
        
//        String packedParameterString =
//        "totalNumPeople:double=1215103.0;totalNumInfectiousPeople:double=435.0;fractionImmune:double=0.0;percentCompliant:double=100.0;outbreak:boolean=true;r0:double=1.2;ave_duration_infectiousness:double=6.0;avelat:double=2.0;beta:double=1.6459510016846307E-7;Number Treated Before Epidemic:double=0.0;Vaccination Schedule:1ddblarray=[];gompertzA:double=1.0E7;gompertzB:double=-0.0;gompertzC:double=-0.11;Run Duration (In Units):integer=500;";
        double[] vaccinationSchedule = new double[500];
        for (int i = 0; i < vaccinationSchedule.length; i++) {
            if (i < 56) {
                vaccinationSchedule[i] = 3500;
            } else if (i < 87) {
                vaccinationSchedule[i] = 10000;
            } else if (i < 128) {
                vaccinationSchedule[i] = 3500;
            } else {
                vaccinationSchedule[i] = 0;
            }
        }
        String vaccinationScheduleStr = Arrays.toString(vaccinationSchedule);
        String packedParameterString = "totalNumPeople:double=1215103.0;" +
                                       "totalNumInfectiousPeople:double=435.0;" + 
                                       "fractionImmune:double=0.0;" +
                                       "susceptible:double=1215003.0;exposed:double=0.0;infectious:double=100.0;recovered:double=0.0;" + 
                                       "percentCompliant:double=100.0;" + 
                                       "outbreak:boolean=true;" +
                                       "r0:double=1.2;ave_duration_infectiousness:double=6.0;avelat:double=2.0;" + 
                                       "beta:double=1.6459510016846307E-7;" + 
                                       "Number Treated Before Epidemic:double=0.0;" +
                                       "Vaccination Schedule:1ddblarray=" + vaccinationScheduleStr + ";" +
                                       "vaccinationEfficacy:double=0.6;" +
                                       "gompertzA:double=1.0E7;gompertzB:double=-0.0;gompertzC:double=-0.11;" +
                                       "Run Duration (In Units):integer=500;";
                                       
        MidasInfluenzaModel model = null;
        model = new MidasInfluenzaModel(packedParameterString);
        model.setAuthor("Bruce Lee");
        model.setDescription("Influenza SEIR Compartment Model");
        model.buildNonSegmentedModel();

        String results = model.execute();
        ParameterizedComponent pc = new ParameterizedComponent(results);
        DecimalFormat df = new DecimalFormat("0.00");
        double[] incidence = pc.get1DdoubleArrayParam("incidence_curve");
        double tot = 0;
        for (double d : incidence) {
            tot += d;
        }
        System.out.println(tot + " people in the population infected, took " + incidence.length + " iterations.");
        System.out.println(model.buildResultString());

        // model.buildNonSegmentedModel();
//        double usaPop = 304059724;
//        double runPop = 0;
//        DecimalFormat r0df = new DecimalFormat("0.0");
//        for (double r0 = 2.5; r0 > 1.5; r0 = r0 - 0.1) {
//            File f = new File("c:/vacc/r0=" + r0df.format(r0).replace('.', '_')
//                    + ".csv");
//            PrintWriter pw = new PrintWriter(f);
//            pw.println("% of population that is susceptible,tot num infected");
//            for (int i = 1; i < 2; i++) {
//                runPop = usaPop * ((i * 1.0) / 100d);
//
//                MidasInfluenzaModel model = new MidasInfluenzaModel(
//                        getDefaultParams(runPop, r0, "allegheny"));
//                model.buildNonSegmentedModel();
//
//                String results = model.execute();
//                ParameterizedComponent pc = new ParameterizedComponent(results);
//                DecimalFormat df = new DecimalFormat("0.00");
//                double[] incidence = pc.get1DdoubleArrayParam("incidence_curve");
//                double tot = 0;
//                for (double d : incidence) {
//                    tot += d;
//                }
//                pw.println(i + "," + df.format(tot));
//                pw.flush();
//                System.out.println(r0 + " at " + df.format((i * 1.0) / 100d)
//                        + " of the population, took " + incidence.length
//                        + " iterations.");
//            }
//            pw.close();
//        }

    }

    public void run() {
        execute();

    }

    public boolean hasNext() {
        return (t < theParams.getIterations() && !(stopWhenNoNewInfectionsOccur && noNewInfectionsOccurred));
    }

    static {
        // Properties properties = new Properties();
        // URL url = ClassLoader.getSystemResource("bioecon.properties");
        // try {
        // properties.load(url.openStream());
        // if (properties.getProperty("debug").equalsIgnoreCase("true")) {
        // debug = true;
        // outputPath = properties.getProperty("debug_output_directory");
        // }
        // } catch (IOException e1) {
        // System.out.println("Unable to open properties file.");
        // System.exit(-1);
        // }
    }
}
