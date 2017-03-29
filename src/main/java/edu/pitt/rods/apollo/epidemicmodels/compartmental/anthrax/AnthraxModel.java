package edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax;

import edu.pitt.rods.apollo.ParameterizedComponent;
import edu.pitt.rods.apollo.epidemicmodels.AbstractEpiModel;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentEpiModelGlobalResult;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedAsxToRecovered;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedAsxToSuccessfullyTreatedProd;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedAsxToUnsuccessfullyTreatedProd;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedFulToDead;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedFulToRecovered;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedProdToRecovered;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedProdToSuccessfullyTreatedFul;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.TreatedProdToUnsuccessfullyTreatedFul;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UndeterminedToCouldTreat;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UndeterminedToWaitToTreat;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UnprophylaxedToProphylaxed;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UnprophylaxedToSuccessfullyProphylaxed;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UnprophylaxedToUnSuccessfullyProphylaxed;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedAsymptomaticUntreatedProdromal;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedFulminantToDead;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedProdromalUntreatedFulminant;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedToSuccessfullyTreatedFul;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedToSuccessfullyTreatedProd;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedToUnsuccessfullyTreatedFul;
import edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel.UntreatedToUnsuccessfullyTreatedProd;
import edu.pitt.rods.apollo.epidemicmodels.utils.CompartmentAnthraxEpiModelOutput;
import edu.pitt.rods.apollo.epidemicmodels.utils.CompartmentModelOutput;
import edu.pitt.rods.apollo.epidemicmodels.utils.CostFunction;
import edu.pitt.rods.apollo.epidemicmodels.utils.CostFunction.CostItem;
import edu.pitt.rods.apollo.epidemicmodels.utils.EpiModelParamStringBuilder;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.BraithwaiteAnthraxStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.StateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.StateTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;
import edu.pitt.rods.apollo.utilities.RodsMoneyFormat;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import org.apache.commons.lang3.ArrayUtils;

/* what dis bugga?
 * 
 * This class is inspired by the Braithwaite Epi Model, which is described in
 * MDM Mar April 2006 pp 182-193.
 * 
 * While this class was inspired by the Braithwaite Model, a number of
 * additions have been to the model, including (but not limited to):
 * 1.  length-of-stay (in a compartment) based transitions
 * 2.  transitions based Poisson distributions
 * 3.  compartments to represent a habituation effect
 * 
 * 
 * 
 */
public class AnthraxModel extends AbstractEpiModel implements Runnable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1625278496388217962L;
	CompartmentEpiModelGlobalResult resultOfLastRun;

	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	// /*
	// * inherits the following 'version control' member variables as well as
	// * get/set methods for each String name; String source; Date lastUpdate;
	// * String notes; String type;
	// *
	// * also inherits dese buggas, where you must store all the parameters, but
	// * they are also stored in the stn. ParameterSet epiModelParameters;
	// * ParameterSet epiModelInputs ParameterSet epiModelOutputs
	// */
	//
	public static final String APOLLO_COMPARTMENT_UNEXPOSED = "Apollo Compartment Unexposed";
	public static final String APOLLO_COMPARTMENT_LATENT = "Apollo Compartment Latent";
	public static final String APOLLO_COMPARTMENT_RECOVERED = "Apollo Compartment Recovered";
	public static final String APOLLO_COMPARTMENT_NEWLY_DEAD = "Apollo Compartment Newly Dead";
	public static final String APOLLO_COMPARTMENT_PROPHYLAXED = "Apollo Compartment Prophylaxed";
	public static final String APOLLO_COMPARTMENT_ASYMPTOMATIC = "Apollo Compartment Asymptomatic";
	public static final String APOLLO_COMPARTMENT_SYMPTOMATIC = "Apollo Compartment Symptomatic";
	public static final String APOLLO_COMPARTMENT_FULMINANT = "Apollo Compartment Fulminant";
	public static final String APOLLO_COMPARTMENT_DEAD = "Apollo Compartment Dead";
	public static final String[] APOLLO_COMPARTMENTS = {APOLLO_COMPARTMENT_UNEXPOSED, APOLLO_COMPARTMENT_LATENT,
		APOLLO_COMPARTMENT_RECOVERED, APOLLO_COMPARTMENT_NEWLY_DEAD, APOLLO_COMPARTMENT_PROPHYLAXED, APOLLO_COMPARTMENT_ASYMPTOMATIC,
		APOLLO_COMPARTMENT_SYMPTOMATIC, APOLLO_COMPARTMENT_FULMINANT, APOLLO_COMPARTMENT_DEAD};
	private Map<String, Double[]> apolloCompartmentTimeSeriesMap;

	public static final String UNEXPOSED = "Unexposed No Treatment";
	public static final String UNEXPOSED_TAKES_TREATMENT = "Unexposed Will Accept Treatment";
	public static final String UNEXPOSED_REFUSE_TREATMENT = "Unexposed Wont Accept Treatment";

	public static final String EXPOSED = "Exposed No Treatment";
	public static final String EXPOSED_TAKES_TREATMENT = "Exposed Will Accept Treatment";
	public static final String EXPOSED_REFUSE_TREATMENT = "Exposed Wont Accept Treatment";

	public static final String ASX = "Asymptomatic No Treatment";
	public static final String ASX_TAKES_TREATMENT = "Asymptomatic Will Accept Treatment";
	public static final String ASX_REFUSE_TREATMENT = "Asymptomatic Wont Accept Treatment";

	public static final String PROD = "Prodromal No Treatment";
	public static final String FULMINANT = "Fulminant No Treatment";

	public static final String UNEXPOS_PO = "Unexposed Treatment";
	public static final String EXPOS_PO = "Exposed Treatment";
	public static final String ASX_PO_WILL_RECOVER = "Asymptomatic Successful Treatment";
	public static final String ASX_PO_WONT_RECOVER = "Asymptomatic Unsuccessful Treatment";
	public static final String PROD_WILL_RECOVER = "Prodromal Successful Treatment";
	public static final String PROD_WONT_RECOVER = "Prodromal Unsuccessful Treatment";
	public static final String FUL_WILL_RECOVER = "Fulminant Successful Treatment";
	public static final String FUL_WONT_RECOVER = "Fulminant Unsuccessful Treatment";

	public static final String DEAD = "Dead";
	public static final String RECOVERED = "Recovered";

	public static final String nonInputNodes[] = {UNEXPOSED_REFUSE_TREATMENT,
		UNEXPOSED_TAKES_TREATMENT, EXPOSED_REFUSE_TREATMENT,
		EXPOSED_TAKES_TREATMENT, ASX_REFUSE_TREATMENT, ASX_TAKES_TREATMENT,
		PROD, FULMINANT, UNEXPOS_PO, EXPOS_PO, ASX_PO_WILL_RECOVER,
		ASX_PO_WONT_RECOVER, PROD_WILL_RECOVER, PROD_WONT_RECOVER,
		FUL_WILL_RECOVER, FUL_WONT_RECOVER, DEAD, RECOVERED};

	public static final String nodeOrdering[] = {
		NodeGroupConsts.REFRACTORY_TRANSITION,
		NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
		NodeGroupConsts.TREATMENT};

	public static final String UNEXP_COMP_SIZE = "unexposed_compartment_size";
	public static final String EXP_COMP_SIZE = "exposed_compartment_size";
	public static final String ASYM_COMP_SIZE = "asymptomatic_compartment_size";
	public static final String PROPH_SCHEDULE = "prophylaxis_schedule";
	public static final String PROPH_TREAT_EFFICACY = "prophylaxis_treatment_efficacy";
	public static final String PROD_TREAT_EFFICACY = "prodromal_treatment_efficacy";
	public static final String FUL_TREAT_EFFICACY = "fulminant_treatment_efficacy";
	public static final String ASX_TO_PROD_MU = "asymptomatic_to_prodromal_mu";
	public static final String ASX_TO_PROD_SIGMA = "asymptomatic_to_prodromal_sigma";
	public static final String PROD_TO_FUL_MU = "prodromal_to_fulminant_mu";
	public static final String PROD_TO_FUL_SIGMA = "prodromal_to_fulminant_sigma";
	public static final String FUL_TO_DEAD_MU = "fulminant_to_dead_mu";
	public static final String FUL_TO_DEAD_SIGMA = "fulminant_to_dead_sigma";
	public static final String BEGIN_TREATMENT_INTERVAL = "begin_treatment_interval";
	public static final String RUN_DURATION = "run_duration";

	double numsick = 0;

	public String getResults() {
		// TODO Auto-generated method stub
		return null;
	}

	boolean created = false;
	public static final String graphGroups[] = {};

	public static final String[] ggHospitalized = {PROD_WILL_RECOVER,
		PROD_WONT_RECOVER, FUL_WILL_RECOVER, FUL_WONT_RECOVER};
	public static final String[] ggProph = {UNEXPOS_PO, EXPOS_PO,
		ASX_PO_WILL_RECOVER, ASX_PO_WONT_RECOVER};
	public static final String inputNodes[] = {UNEXPOSED, EXPOSED, ASX};
	Double[] inputNodesPop = {1000000.0, 0.0, 25000.0};

	static String outputPath = "c:/bioecon/paper/test12/";

	public TreeMap<Integer, ArrayList<StateNode>> hourlyResults = new TreeMap<Integer, ArrayList<StateNode>>();

	AbstractStateTransitionNetwork stn;

	public int currentTime = -1;
	boolean changed = false;
	Integer unexposedCompartmentSize;
	Integer exposedCompartmentSize;
	Integer asymptomaticCompartmentSize;
	Integer runDuration;
	Double[] prophylaxisSchedule = null;
	int beginTreatmentInterval;

	int timeToRunModel = 0;

	public AnthraxModel(String name) {
		super(name);

		inputNodesPop[0] = getDoubleParam(UNEXP_COMP_SIZE);
//		inputNodesPop[1] = getDoubleParam(EXP_COMP_SIZE);
		inputNodesPop[2] = getDoubleParam(ASYM_COMP_SIZE);
		numsick = inputNodesPop[2];
		Parameters.setPoEfficacy(getDoubleParam(PROPH_TREAT_EFFICACY));
		Parameters
				.setOneMinusPoEfficacy(1 - getDoubleParam(PROPH_TREAT_EFFICACY));

		Parameters
				.setProdTreatmentEfficacy(getDoubleParam(PROD_TREAT_EFFICACY));
		Parameters
				.setOneMinusProdTreatmentEfficacy(1 - getDoubleParam(PROD_TREAT_EFFICACY));

		Parameters.setFulTreatmentEfficacy(getDoubleParam(FUL_TREAT_EFFICACY));
		Parameters
				.setOneMinusFulTreatmentEfficacy(1 - getDoubleParam(FUL_TREAT_EFFICACY));

		// log normal parameters for the various periods
		Parameters.setAsxToProdMu(getDoubleParam(ASX_TO_PROD_MU));
		Parameters.setAsxToProdSigma(getDoubleParam(ASX_TO_PROD_SIGMA));
		Parameters.setProdToFulMu(getDoubleParam(PROD_TO_FUL_MU));
		Parameters.setProdToFulSigma(getDoubleParam(PROD_TO_FUL_SIGMA));
		Parameters.setFulToDeadMu(getDoubleParam(FUL_TO_DEAD_MU));
		Parameters.setFulToDeadSigma(getDoubleParam(FUL_TO_DEAD_SIGMA));

		prophylaxisSchedule = get1DDoubleArrayParam(PROPH_SCHEDULE);
		beginTreatmentInterval = getIntegerParam(BEGIN_TREATMENT_INTERVAL);
		runDuration = getIntegerParam(RUN_DURATION);
		apolloCompartmentTimeSeriesMap = new HashMap<String, Double[]>();
		for (String compartment : APOLLO_COMPARTMENTS) {
			Double[] series = new Double[runDuration];
			for (int i = 0; i < runDuration; i++) {
				series[i] = 0.0;
			}
			apolloCompartmentTimeSeriesMap.put(compartment, series);
		}
	}

	public AnthraxModel(String name, boolean outputToFile) {
		super(name);
		this.outputToFile = outputToFile;

		if (outputToFile) {
			File of = new File(outputPath);
			of.mkdirs();

		}

	}

	public void resetModel() {

		AbstractStateTransitionNetwork stn = this.getStateTransitionNetwork();
		for (int i = 0; i < stn.getNodes().size(); i++) {
			StateNode node = stn.getNodes().get(i);
			node.reset(0d);
		}

		for (int i = 0; i < AnthraxModel.inputNodes.length; i++) {
			StateNode node = stn.getNode(AnthraxModel.inputNodes[i]);
			node.reset(inputNodesPop[i]);
		}

		stn.resetState();

	}

	public void buildGenericModel() {
		created = true;
		AbstractStateTransitionNetwork stn = this.getStateTransitionNetwork();

		IStateNode fromNode, horizToNode, vertToNode;

		// lets add ALL of the nodes:
		for (int i = 0; i < AnthraxModel.inputNodes.length; i++) {
			stn.addNode(AnthraxModel.inputNodes[i], inputNodesPop[i]);
		}

		for (int i = 0; i < AnthraxModel.nonInputNodes.length; i++) {
			stn.addNode(AnthraxModel.nonInputNodes[i]);
		}

		// from input node, to prophylaxis nodes
		fromNode = stn.getNode(UNEXPOSED);
		horizToNode = stn.getNode(UNEXPOSED_TAKES_TREATMENT);
		fromNode.addTransitionFunction(UndeterminedToCouldTreat.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(UNEXPOSED);
		horizToNode = stn.getNode(UNEXPOSED_REFUSE_TREATMENT);
		fromNode.addTransitionFunction(UndeterminedToWaitToTreat.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(EXPOSED);
		horizToNode = stn.getNode(EXPOSED_TAKES_TREATMENT);
		fromNode.addTransitionFunction(UndeterminedToCouldTreat.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(EXPOSED);
		horizToNode = stn.getNode(EXPOSED_REFUSE_TREATMENT);
		fromNode.addTransitionFunction(UndeterminedToWaitToTreat.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(ASX);
		horizToNode = stn.getNode(ASX_TAKES_TREATMENT);
		fromNode.addTransitionFunction(UndeterminedToCouldTreat.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(ASX);
		horizToNode = stn.getNode(ASX_REFUSE_TREATMENT);
		fromNode.addTransitionFunction(UndeterminedToWaitToTreat.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(UNEXPOSED_TAKES_TREATMENT);
		fromNode.addAttribute(
				NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN, null);
		horizToNode = stn.getNode(AnthraxModel.UNEXPOS_PO);
		fromNode.addTransitionFunction(UnprophylaxedToProphylaxed.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(UNEXPOSED_REFUSE_TREATMENT);
		fromNode.addAttribute(
				NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN, null);
		horizToNode = stn.getNode(AnthraxModel.UNEXPOS_PO);
		fromNode.addTransitionFunction(UnprophylaxedToProphylaxed.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(AnthraxModel.EXPOSED_TAKES_TREATMENT);
		fromNode.addAttribute(
				NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN, null);
		horizToNode = stn.addNode(AnthraxModel.EXPOS_PO);
		fromNode.addTransitionFunction(UnprophylaxedToProphylaxed.class,
				fromNode, horizToNode);

		fromNode = stn.getNode(AnthraxModel.EXPOSED_REFUSE_TREATMENT);
		fromNode.addAttribute(
				NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN, null);
		horizToNode = stn.addNode(AnthraxModel.EXPOS_PO);
		fromNode.addTransitionFunction(UnprophylaxedToProphylaxed.class,
				fromNode, horizToNode);

		// ASX
		// not treating ASX
		fromNode = stn.getNode(AnthraxModel.ASX_TAKES_TREATMENT);
		fromNode.addAttribute(
				NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN, null);
		vertToNode = stn.getNode(AnthraxModel.PROD);
		fromNode.addTransitionFunction(
				UntreatedAsymptomaticUntreatedProdromal.class, fromNode,
				vertToNode);

		fromNode = stn.getNode(AnthraxModel.ASX_REFUSE_TREATMENT);
		fromNode.addAttribute(
				NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN, null);
		vertToNode = stn.getNode(AnthraxModel.PROD);
		fromNode.addTransitionFunction(
				UntreatedAsymptomaticUntreatedProdromal.class, fromNode,
				vertToNode);

		// treating ASX, can go to two nodes
		fromNode = stn.getNode(AnthraxModel.ASX_TAKES_TREATMENT);
		horizToNode = stn.getNode(AnthraxModel.ASX_PO_WILL_RECOVER);
		fromNode.addTransitionFunction(
				UnprophylaxedToSuccessfullyProphylaxed.class, fromNode,
				horizToNode);
		horizToNode = stn.getNode(AnthraxModel.ASX_PO_WONT_RECOVER);
		fromNode.addTransitionFunction(
				UnprophylaxedToUnSuccessfullyProphylaxed.class, fromNode,
				horizToNode);

		fromNode = stn.getNode(AnthraxModel.ASX_REFUSE_TREATMENT);
		horizToNode = stn.getNode(AnthraxModel.ASX_PO_WILL_RECOVER);
		fromNode.addTransitionFunction(
				UnprophylaxedToSuccessfullyProphylaxed.class, fromNode,
				horizToNode);
		horizToNode = stn.getNode(AnthraxModel.ASX_PO_WONT_RECOVER);
		fromNode.addTransitionFunction(
				UnprophylaxedToUnSuccessfullyProphylaxed.class, fromNode,
				horizToNode);

		// ASX(Recover)-> Recoverd
		fromNode = stn.getNode(AnthraxModel.ASX_PO_WILL_RECOVER);
		horizToNode = stn.getNode(AnthraxModel.RECOVERED);
		fromNode.addTransitionFunction(TreatedAsxToRecovered.class, fromNode,
				horizToNode);
		// ASX(Won't Recover)-> PROD
		fromNode = stn.getNode(AnthraxModel.ASX_PO_WONT_RECOVER);
		vertToNode = stn.getNode(AnthraxModel.PROD_WILL_RECOVER);
		fromNode.addTransitionFunction(
				TreatedAsxToSuccessfullyTreatedProd.class, fromNode, vertToNode);
		vertToNode = stn.getNode(AnthraxModel.PROD_WONT_RECOVER);
		fromNode.addTransitionFunction(
				TreatedAsxToUnsuccessfullyTreatedProd.class, fromNode,
				vertToNode);
		// ASX path is complete

		// PROD path
		// not treating PROD
		fromNode = stn.getNode(AnthraxModel.PROD);
		vertToNode = stn.getNode(AnthraxModel.FULMINANT);
		fromNode.addTransitionFunction(
				UntreatedProdromalUntreatedFulminant.class, fromNode,
				vertToNode);

		// treating prod, can go to two nodes
		fromNode = stn.getNode(AnthraxModel.PROD);
		vertToNode = stn.getNode(AnthraxModel.PROD_WILL_RECOVER);
		StateTransitionFunction stf = fromNode.addTransitionFunction(
				UntreatedToSuccessfullyTreatedProd.class, fromNode, vertToNode);
		stf.addAttribute(NodeGroupConsts.PRODROMAL_TREATMENT, null);
		vertToNode = stn.getNode(AnthraxModel.PROD_WONT_RECOVER);
		stf = fromNode.addTransitionFunction(
				UntreatedToUnsuccessfullyTreatedProd.class, fromNode,
				vertToNode);
		stf.addAttribute(NodeGroupConsts.PRODROMAL_TREATMENT, null);

		// // PROD(Recover) --> Recovered
		fromNode = stn.getNode(AnthraxModel.PROD_WILL_RECOVER);
		vertToNode = stn.getNode(AnthraxModel.RECOVERED);
		fromNode.addTransitionFunction(TreatedProdToRecovered.class, fromNode,
				vertToNode);

		// PROD(Won't Recover -> Fulminant Will/Wont Recover)
		fromNode = stn.getNode(AnthraxModel.PROD_WONT_RECOVER);
		vertToNode = stn.getNode(AnthraxModel.FUL_WILL_RECOVER);
		fromNode.addTransitionFunction(
				TreatedProdToSuccessfullyTreatedFul.class, fromNode, vertToNode);
		vertToNode = stn.getNode(AnthraxModel.FUL_WONT_RECOVER);
		fromNode.addTransitionFunction(
				TreatedProdToUnsuccessfullyTreatedFul.class, fromNode,
				vertToNode);

		// PROD Path is complete
		// Fulminant
		// not treating fulmianant
		fromNode = stn.getNode(AnthraxModel.FULMINANT);
		vertToNode = stn.getNode(AnthraxModel.DEAD);
		fromNode.addTransitionFunction(UntreatedFulminantToDead.class,
				fromNode, vertToNode);

		fromNode = stn.getNode(AnthraxModel.FULMINANT);
		vertToNode = stn.getNode(AnthraxModel.FUL_WILL_RECOVER);
		fromNode.addTransitionFunction(UntreatedToSuccessfullyTreatedFul.class,
				fromNode, vertToNode);
		vertToNode = stn.getNode(AnthraxModel.FUL_WONT_RECOVER);
		fromNode.addTransitionFunction(
				UntreatedToUnsuccessfullyTreatedFul.class, fromNode, vertToNode);

		fromNode = stn.getNode(AnthraxModel.FUL_WILL_RECOVER);
		vertToNode = stn.getNode(AnthraxModel.RECOVERED);
		fromNode.addTransitionFunction(TreatedFulToRecovered.class, fromNode,
				vertToNode);

		fromNode = stn.getNode(AnthraxModel.FUL_WONT_RECOVER);
		vertToNode = stn.getNode(AnthraxModel.DEAD);
		fromNode.addTransitionFunction(TreatedFulToDead.class, fromNode,
				vertToNode);

		// addEpiModelInputs();
	}

	public void setStateTransitionNetwork(AbstractStateTransitionNetwork stn) {
		this.stn = stn;
	}

	public AbstractStateTransitionNetwork getStateTransitionNetwork() {
		return stn;
	}

	public void epiCalculation(Object initializationConditions,
			Object resultType) {
		// TODO: maybe eliminate but it is declared in the Abstract Class
	}

	double[][] compartmentRunResults;

	private void storeCompartmentAtTime(IStateNode node, int t) {

		String nodeName = node.getName();

		if (nodeName.equals(UNEXPOSED) || nodeName.equals(UNEXPOSED_REFUSE_TREATMENT)
				|| nodeName.equals(UNEXPOSED_TAKES_TREATMENT) || nodeName.equals(UNEXPOS_PO)) {

			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_UNEXPOSED)[t] += node.getNumberOfPeopleInTheEntireNode(t);

		} else if (nodeName.equals(RECOVERED)) {
			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_RECOVERED)[t] += node.getNumberOfPeopleInTheEntireNode(t);
		} else if (nodeName.equals(DEAD)) {
			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_DEAD)[t] += node.getNumberOfPeopleInTheEntireNode(t);
		} else if (nodeName.equals(ASX) || nodeName.equals(ASX_PO_WILL_RECOVER) || nodeName.equals(ASX_PO_WONT_RECOVER)
				|| nodeName.equals(ASX_REFUSE_TREATMENT) || nodeName.equals(ASX_TAKES_TREATMENT)) {

			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_LATENT)[t] += node.getNumberOfPeopleInTheEntireNode(t);
			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_ASYMPTOMATIC)[t] += node.getNumberOfPeopleInTheEntireNode(t);

		} else if (nodeName.equals(PROD) || nodeName.equals(PROD_WILL_RECOVER) || nodeName.equals(PROD_WONT_RECOVER)) {

			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_LATENT)[t] += node.getNumberOfPeopleInTheEntireNode(t);
			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_SYMPTOMATIC)[t] += node.getNumberOfPeopleInTheEntireNode(t);

		} else if (nodeName.equals(FULMINANT) || nodeName.equals(FUL_WILL_RECOVER) || nodeName.equals(FUL_WONT_RECOVER)) {

			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_LATENT)[t] += node.getNumberOfPeopleInTheEntireNode(t);
			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_FULMINANT)[t] += node.getNumberOfPeopleInTheEntireNode(t);

		} else {
			// every other compartment is also latent
			apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_LATENT)[t] += node.getNumberOfPeopleInTheEntireNode(t);
		}
	}

	public Map<String, Double[]> getCompartmentTimeSeriesMap() {
		return apolloCompartmentTimeSeriesMap;
	}

	public Double runIt(int alertAt, int hrsToCertainty) {

		/*
		 * hoursSinceAlert - needed to know when the "hrsToCertainty" constraint
		 * has been satisfied, as hours to certainty is relative to the time of
		 * alert.
		 */
		int hoursSinceAlert = 0;

		/*
		 * resultOfLastRun - used for record keeping. Provides quick access to
		 * the most recent hourly result.
		 */
		resultOfLastRun = new CompartmentEpiModelGlobalResult();

		/*
		 * modelFinishedAt - we need to know this when writing the output files,
		 * it basically tells us the array length of the compartments
		 */
		int modelFinishedAt = 0;

		/*
		 * emo - just a helper class to extract some of the output functionality
		 * from this method.
		 */
		CompartmentModelOutput emo = null;
		try {
			if (outputToFile) {
				emo = new CompartmentAnthraxEpiModelOutput(stn, outputPath,
						alertAt, stn.getState().getHabituationRate(),
						hrsToCertainty);
				emo.writeHeaders();
			}

			for (int t = -1; t < runDuration; t++) {

				if (t > alertAt) {
					stn.getState().signalAlert();
				}
				if (stn.getState().isAlert()) {
					if (hoursSinceAlert >= hrsToCertainty) {
						stn.getState().signalOutbreakCertain();
					}
					hoursSinceAlert++;
				}

				if (outputToFile) {
					emo.getGeneralOutput().write(Integer.toString(t));
				}

				CompartmentModelHourlyResult hourlyResult = new CompartmentModelHourlyResult();
				/*
				 * we process the transition functions in the following order:
				 * 1:refractory transition 2:disease progression 3:treatment
				 * progression This is required #2 and #3 were competing for the
				 * same people
				 */
				for (String stfOrder : nodeOrdering) {
					for (IStateNode currentNode : stn.getNodes()) {

						for (StateTransitionFunction stf : currentNode
								.getTransitionFunctions()) {
							if (stf.hasAttribute(stfOrder)) {
								// if (stn.getState().getHour() == 114)
								// if (stn.getState().getHour() == 63)
								// if (stf.getFromNode().getName()
								// .equalsIgnoreCase(
								// ASX_TAKES_TREATMENT))
								// if (stf.getFromNode().getName()
								// .equalsIgnoreCase(
								// "asymptomatic wont accept treatment"))
								// System.out.println(stf.getFromNode()
								// .getName()
								// + "--->"
								// + stf.getToNode().getName());

								double numTransitioned = stf.process(t,
										hourlyResult);

								recordDiseaseProgression(stf, hourlyResult,
										numTransitioned);

								recordMovementProgression(stf, hourlyResult,
										numTransitioned);

							}

						}
					}

				}
				// increment the hour to trigger queued transitions, everybody
				// transitions
				// at once...on the very last nanosecond of the hour

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
						origNodePops[i] = nodes.get(i)
								.getNumberOfPeopleInTheEntireNode(
										stn.getState().getHour());
					}
				}

				stn.getState().incHour();
				if (outputToFile) {
					for (int i = 0; i < nodes.size(); i++) {
						endingNodePops[i] = nodes.get(i)
								.getNumberOfPeopleInTheEntireNode(
										stn.getState().getHour());
						totNumInModel += endingNodePops[i];
					}
				}

				resultOfLastRun.addResult(hourlyResult);

				if (outputToFile) {
					emo.writeSummary(t, nodeNames, origNodePops,
							endingNodePops, totNumInModel, hourlyResult);
				}

				// compute output for Apollo DB here
				for (IStateNode currentNode : stn.getNodes()) {
					if (t >= 0) {
						storeCompartmentAtTime(currentNode, t);
					}

				}

				// after every time increment, tell the observers we ran.
				// in the GUI for example, we can take this time to update
				// the graphs
				currentTime = t;

				modelFinishedAt = t;
				if (isModelFinished(t)) {
					break;
				}
			}

			if (outputToFile) {
				emo.writeEnd(modelFinishedAt);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		setAdditionalApolloCompartments();

//		Double[] dead = apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_DEAD);
//		Double[] newlyDead = apolloCompartmentTimeSeriesMap.get(APOLLO_COMPARTMENT_NEWLY_DEAD);
//		for (int t = 0; t < runDuration; t++) {
//
//			double numDead = dead[t];
//			double numNewlyDead = 0.0;
//			for (int i = 0; i <= t; i++) {
//				numNewlyDead += newlyDead[i];
//			}
//			System.out.println(t + "     " + numDead + "    " + numNewlyDead);
//		}

		double totalCost = 0.0;
		if (stn.getState().isAlert()) {
			totalCost += CostFunction.getCost(CostItem.ALERT);
		}
		totalCost += CostFunction.getCost(CostItem.PROPHYLAXIS)
				* resultOfLastRun.getTreatmentCount(MovementType.PROPHYLAXED
						.name());
		totalCost += CostFunction.getCost(CostItem.IV)
				* resultOfLastRun.getTreatmentCount(MovementType.IV.name());

		totalCost += CostFunction.getCost(CostItem.ICU)
				* resultOfLastRun.getTreatmentCount(MovementType.ICU.name());

		totalCost += CostFunction.getCost(CostItem.FATALITY)
				* resultOfLastRun.getTreatmentCount(MovementType.DIED.name());
		return totalCost;

	}

	private void recordMovementProgression(StateTransitionFunction stf,
			CompartmentModelHourlyResult result, double numToMove) {
		result.addCountToDescriptor(stf.getMovementType().name(), numToMove);

	}

	private void recordDiseaseProgression(StateTransitionFunction stf,
			CompartmentModelHourlyResult result, double numToMove) {

		if (stf.hasAttribute(NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL)) {
			if (stf.hasAttribute(NodeGroupConsts.TO_PROD_PROGRESSION)) {
				result.addCountToDescriptor(
						NodeGroupConsts.TO_PROD_PROGRESSION, numToMove);
			}
			if (stf.hasAttribute(NodeGroupConsts.TO_FUL_PROGRESSION)) {
				result.addCountToDescriptor(NodeGroupConsts.TO_FUL_PROGRESSION,
						numToMove);
			}
			if (stf.hasAttribute(NodeGroupConsts.TO_DEAD_PROGRESSION)) {
				result.addCountToDescriptor(
						NodeGroupConsts.TO_DEAD_PROGRESSION, numToMove);
				;
			}
			if (stf.hasAttribute(NodeGroupConsts.TO_RECOVER_FROM_ASX_PROGRESSION)) {
				result.addCountToDescriptor(
						NodeGroupConsts.TO_RECOVER_FROM_ASX_PROGRESSION,
						numToMove);
			}
			if (stf.hasAttribute(NodeGroupConsts.TO_RECOVER_FROM_PROD_PROGRESSION)) {
				result.addCountToDescriptor(
						NodeGroupConsts.TO_RECOVER_FROM_PROD_PROGRESSION,
						numToMove);
			}
			if (stf.hasAttribute(NodeGroupConsts.TO_RECOVER_FROM_FUL_PROGRESSION)) {
				result.addCountToDescriptor(
						NodeGroupConsts.TO_RECOVER_FROM_FUL_PROGRESSION,
						numToMove);
			}
		}
	}

	public void runModel(int time, int numberOfPreviousFalseAlarms,
			int hourToBeginTreatment) {
	}

	public void initializeModel() {

	}

	public boolean isModelFinished(int t) {
		if (t <= 0) {
			return false;
		}
		if (!stn.getState().isFastTransitionMode()) {
			return false;
		}
		for (IStateNode currentNode : stn.getNodes()) {
			if (currentNode
					.hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_AFTER_CERTAIN)
					|| (currentNode
					.hasAttribute(NodeGroupConsts.WILL_TAKE_PROPHYLAXIS_BEFORE_CERTAIN))) {
				if (currentNode.getNumberOfPeopleInTheEntireNode(t) > 0.0) {
					return false;
				}
			}
		}

		if ((stn.getNode(AnthraxModel.ASX_PO_WONT_RECOVER)
				.getNumberOfPeopleInTheEntireNode(t) < 0.33)
				&& (stn.getNode(AnthraxModel.PROD_WONT_RECOVER)
				.getNumberOfPeopleInTheEntireNode(t) < 0.33)
				&& (stn.getNode(AnthraxModel.FUL_WONT_RECOVER)
				.getNumberOfPeopleInTheEntireNode(t) < 0.33)) {
			return true;
		} else {
			return false;
		}

	}

	// private void addEpiModelInputs() {
	// ParameterSet ps = new ParameterSet("EpiModel inputs");
	//
	// for (StateNode sn : stn.getNodes()) {
	// for (StateTransitionFunction stf : sn.getTransitionFunctions()) {
	// for (SingleValueParameter svp : stf.getParameters()) {
	// ps.addParameter(svp);
	//
	// System.out.println(stf.getClass().toString() + " "
	// + svp.getName());
	// System.out.println(sn.getName());
	// }
	// }
	// }
	//
	// this.epiModelInputs = ps;
	//
	// }
	private static void displayArray(int[] arr, FileWriter fw)
			throws IOException {

		for (Integer hr : arr) {
			fw.write(" " + hr);
		}
		fw.write("\n");
	}

	public static void dumpSummary(int[] alertAt, int[] hoursToCertainty,
			int[] additionalHours, double habituation) {
		File of = new File(outputPath);
		of.mkdirs();
		of = new File(outputPath + "summary.txt");
		try {
			FileWriter fw = new FileWriter(of);
			fw.write("Alerting at (hours):");
			displayArray(alertAt, fw);
			fw.write("Certain at (hours):");
			displayArray(hoursToCertainty, fw);
			// fw.write("Prev Alarms:");
			// displayArray(numPrevious, fw);
			fw.write("Additional Hours:");
			displayArray(additionalHours, fw);
			fw.write("Habituation: " + habituation + "\n");
			fw.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static void main(String[] args) throws IOException {
		// if false, alert at hours
		// boolean alertAtCases = true;

		String parameters = "";
		int hoursToDetectMin = 36;
		int hoursToDetectMax = 36;

		int hoursToVerificationMin = 0;
		int hoursToVerificationMax = 0;

		double[] percentComplaintWithTreatment = new double[1];
		for (int i = 0; i < 1; i++) {
			percentComplaintWithTreatment[i] = Double.valueOf(100.0);
		}

		int[] alertAtArr = new int[hoursToDetectMax - hoursToDetectMin + 1];
		for (int i = 0; i <= hoursToDetectMax - hoursToDetectMin; i++) {
			alertAtArr[i] = hoursToDetectMin + i;
		}

		int[] hoursToVerification = new int[hoursToVerificationMax
				- hoursToVerificationMin + 1];
		for (int i = 0; i <= hoursToVerificationMax - hoursToVerificationMin; i++) {
			hoursToVerification[i] = hoursToVerificationMin + i;
		}

		// int[] additionalHours = { 0 };
		// int m = 0;
		ArrayList<String> result = new ArrayList<String>();

		double[][] resultArray = new double[hoursToVerificationMax + 1][hoursToDetectMax + 1];

		for (double comply : percentComplaintWithTreatment) {
			for (int alertAt : alertAtArr) {
				for (int hrsToCertainty : hoursToVerification) {

					EpiModelParamStringBuilder s = new EpiModelParamStringBuilder();

					s.addParam(UNEXP_COMP_SIZE, "double", "100000");
					s.addParam(EXP_COMP_SIZE, "double", "25000");
					s.addParam(ASYM_COMP_SIZE, "double", "25000");
					s.addParam(PROPH_TREAT_EFFICACY, "double", "0.9");
					s.addParam(PROD_TREAT_EFFICACY, "double", "0.55");
					s.addParam(FUL_TREAT_EFFICACY, "double", "0.55");
					Double[] schedule = new Double[500];
					for (int i = 0; i < 500; i++) {
						schedule[i] = 100d;
					}
					s.addParam(PROPH_SCHEDULE,
							ParameterizedComponent.PARAM_1D_DOUBLE_ARRAY,
							Arrays.toString(schedule));
					s.addParam(BEGIN_TREATMENT_INTERVAL, "integer", "36");
					s.addParam(RUN_DURATION, "integer", "500");
					BraithwaiteAnthraxStateTransitionNetwork stn = new BraithwaiteAnthraxStateTransitionNetwork(
							s.getParamString());
					AnthraxModel model = new AnthraxModel(s.getParamString());
					model.setAuthor("Wagner");
					model.setDescription("FOR RESEARCH ONLY:  implements model published in Medical Decision Making, Mar-Apr 2006 pp 182-93.  Braithaite et all The cost effectiveness of strategies to reduce mortality from an intentional release of aerosolized anthrax spores");
					model.setStateTransitionNetwork(stn);
					model.buildGenericModel();

					double totalCostOfRun = model
							.runIt(alertAt, hrsToCertainty);
					System.out.println(model.buildResultString());
					result.add("=" + RodsMoneyFormat.format(totalCostOfRun));
					resultArray[hrsToCertainty][alertAt] = totalCostOfRun;

				}

			}

			// PrintWriter pw = new PrintWriter(new FileWriter(
			// "c:/testing---heatMapV2-" + comply + ".csv"));
			// pw.println(",Hours To Detection,");
			// for (int i = 0; i < resultArray[0].length; i++)
			// pw.print("," + i);
			// pw.println();
			// for (int i = 0; i < resultArray.length; i++) {
			// for (int j = 0; j < resultArray[i].length; j++) {
			// if (j == 0)
			// pw.print(i + ",");
			// pw.print(resultArray[i][j] + ",");
			//
			// }
			// pw.println();
			// }
			// pw.close();
		}

	}

	// public void runModel(int time) {
	// if (!created) {
	// this.setStateTransitionNetwork(new StateTransitionNetwork(1.0));
	//
	// buildGenericModel();
	// }
	// resetModel();
	// run();
	//
	// }
	public void run() {
		runIt(beginTreatmentInterval, 0);

	}

	public String execute() {
		runIt(beginTreatmentInterval, 0);
		return buildResultString();

	}

	private String getProgressionCurveResultStr(String progression,
			String curveName, String curveType) {
		double[] curve = resultOfLastRun
				.getDiseaseProgressionCurve(progression);
		return curveName + ":" + curveType + "=" + Arrays.toString(curve) + ";";

	}

	private Double[] getProgressionCurveResultStartingAtTimeZero(String progression,
			String curveName, String curveType) {
		double[] curve = resultOfLastRun
				.getDiseaseProgressionCurve(progression);
		Double[] newCurve = new Double[curve.length - 1];

		// The curves have an extra time step since the main program loop starts at t=-1,
		// so we ignore the first element here and start at 1
		for (int i = 1; i < curve.length; i++) {
			newCurve[i - 1] = curve[i];
		}
		return newCurve;
	}

	private void setAdditionalApolloCompartments() {
		apolloCompartmentTimeSeriesMap.put(APOLLO_COMPARTMENT_NEWLY_DEAD, getProgressionCurveResultStartingAtTimeZero(
				MovementType.DIED.toString(),
				AnthraxModelResultConstants.FATALITY_CURVE,
				AnthraxModelResultConstants.FATALITY_CURVE_TYPE));

		apolloCompartmentTimeSeriesMap.put(APOLLO_COMPARTMENT_PROPHYLAXED, getProgressionCurveResultStartingAtTimeZero(
				MovementType.PROPHYLAXED.toString(),
				AnthraxModelResultConstants.PROPHYLAXIS_CURVE,
				AnthraxModelResultConstants.PROPHYLAXIS_CURVE_TYPE));
	}

	public String buildResultString() {

		String prophylaxedCurveStr = getProgressionCurveResultStr(
				MovementType.PROPHYLAXED.toString(),
				AnthraxModelResultConstants.PROPHYLAXIS_CURVE,
				AnthraxModelResultConstants.PROPHYLAXIS_CURVE_TYPE);
		String fatalityCurveStr = getProgressionCurveResultStr(
				MovementType.DIED.toString(),
				AnthraxModelResultConstants.FATALITY_CURVE,
				AnthraxModelResultConstants.FATALITY_CURVE_TYPE);
		String ivCurveStr = getProgressionCurveResultStr(
				MovementType.IV.toString(),
				AnthraxModelResultConstants.IV_CURVE,
				AnthraxModelResultConstants.IV_CURVE_TYPE);
		String icuCurveStr = getProgressionCurveResultStr(
				MovementType.ICU.toString(),
				AnthraxModelResultConstants.ICU_CURVE,
				AnthraxModelResultConstants.ICU_CURVE_TYPE);
		String recoveredCurveStr = getProgressionCurveResultStr(
				MovementType.RECOVERED.toString(),
				AnthraxModelResultConstants.RECOVERED_CURVE,
				AnthraxModelResultConstants.RECOVERED_CURVE_TYPE);
		EpiModelParamStringBuilder s = new EpiModelParamStringBuilder();
		s.addParam(AnthraxModelResultConstants.NUMBER_SICK, "double",
				String.valueOf(numsick));

		return prophylaxedCurveStr + fatalityCurveStr + ivCurveStr
				+ icuCurveStr + recoveredCurveStr + s.getParamString();
	}

	@Override
	public void next() {
		// TODO Auto-generated method stub

	}
}
