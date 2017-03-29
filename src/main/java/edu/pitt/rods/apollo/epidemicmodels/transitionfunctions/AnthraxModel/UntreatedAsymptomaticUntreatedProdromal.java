package edu.pitt.rods.apollo.epidemicmodels.transitionfunctions.AnthraxModel;

import edu.pitt.rods.apollo.math.probabilitydistributions.LogNormalDist;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractTransitionFunction;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.Parameters;

/*
 * Assumptions:
 * 1.  Transition can only happen if there is an outbreak
 * 2.  Transition will happen regardless of alert state (yes/no)
 * 3.  this guy needs to listen for a change in their params, if there is a change
 * they need to redefine the lognormal dist
 */
public class UntreatedAsymptomaticUntreatedProdromal extends
		AbstractTransitionFunction {

	// static SingleValueParameter muAsymptomaticToProdromal = new
	// SingleValueParameter(
	// ParameterConstants.ASX_TO_PROD_MU_NAME);
	// static SingleValueParameter sigmaAsymptomaticToProdromal = new
	// SingleValueParameter(
	// ParameterConstants.ASX_TO_PROD_SIGMA_NAME);
	double muAsymptomaticToProdromal = Parameters.getAsxToProdMu();
	double sigmaAsymptomaticToProdromal = Parameters.getAsxToProdSigma();
	LogNormalDist lnd = null;

	/*
	 * if we are treating, there is an edge case in that it is possible to treat
	 * enough people that the distribution will tell us to move more people than
	 * are left. For example, if there are 5 people in the node at hour 10, and
	 * we have the capacity to treat 20000 per hour, then all 5 people will be
	 * treated. But, since these transitions (Untreated->Treated, and
	 * (ASX->Prodromal) happen at the same time, it isn't possible to simply act
	 * according to the distribution function. We have to first determine how
	 * many are "remaning" in the node "after" they are treated.
	 * 
	 * A more stright forward way is to allow "treatement" nodes to fire before
	 * disease progression nodes. Maybe I'll do that...
	 * 
	 * update: ok the treatment nodes run first...so the distribution will
	 * function on the remaining people.
	 * 
	 * (non-Javadoc)
	 * 
	 * @seeedu.pitt.rods.bioecon.datamodel.statetransitionnetwork.
	 * StateTransitionFunction#getP(java.lang.Integer)
	 */
	public double getP(int t) {
		double dt = t * 1.0;

		if (fromNode.getNumberThatHaveBeenInNodeForNHours(t) == 0)
			return 0.0;
		if (lnd.fastCDF(dt - 1) == 1.0)
			return 1.0;

		double prob = (lnd.fastCDF(dt) - (lnd.fastCDF(dt - 1)))
				/ (1 - lnd.fastCDF(dt - 1));

		if (prob == 0.0)
			return 0.0;
		return prob;

	}

	public UntreatedAsymptomaticUntreatedProdromal(String name,
			IStateNode fromNode, IStateNode toNode,
			AbstractStateTransitionNetwork network) {
		super(name, fromNode, toNode, network, MovementType.STILL_UNTREATED);
		addAttribute(
				NodeGroupConsts.DISEASE_PROGRESSION_FOR_MULTI_BIN_PER_HOUR_MODEL,
				"");
		addAttribute(NodeGroupConsts.TO_PROD_PROGRESSION, "");
		// muAsymptomaticToProdromal
		// .setRunTimeValueInternal(ParameterConstants.ASX_TO_PROD_MU);
		// muAsymptomaticToProdromal.setUnitsInternal("");
		// sigmaAsymptomaticToProdromal
		// .setRunTimeValueInternal(ParameterConstants.ASX_TO_PROD_SIGMA);
		// lnd = new LogNormalDist(muAsymptomaticToProdromal.getRunTimeValue(),
		// sigmaAsymptomaticToProdromal.getRunTimeValue());
		lnd = new LogNormalDist(muAsymptomaticToProdromal,
				sigmaAsymptomaticToProdromal);
		// sigmaAsymptomaticToProdromal.setUnitsInternal("");
		// addParameter(muAsymptomaticToProdromal);
		// addParameter(sigmaAsymptomaticToProdromal);

	}

}
