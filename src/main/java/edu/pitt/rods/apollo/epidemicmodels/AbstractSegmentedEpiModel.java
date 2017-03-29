package edu.pitt.rods.apollo.epidemicmodels;

import java.util.ArrayList;
import java.util.List;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza.MidasInfluenzaModel;

/**
 * 
 * Segmented compartment models are made up of multiple copies of other
 * compartment models.
 * 
 * There are two key features of segmented compartment models that are absent in
 * singular compartment models: the susceptiblity rate array, and the list of
 * segments that make up the model.
 * 
 * The susceptibility rate array is a 2d array where [x,y] defines the
 * susceptibility of population y on population x.
 * 
 */
public abstract class AbstractSegmentedEpiModel extends AbstractEpiModel {

	protected double[][] susceptibilityRate = new double[2][2];
	protected List<MidasInfluenzaModel> segments = new ArrayList<MidasInfluenzaModel>();

	public AbstractSegmentedEpiModel(String args) {
		super(args);

	}

	protected double getSusceptiblityRateOfSegmentXToY(int x, int y) {
		return susceptibilityRate[x][y];
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 7954847322448502068L;

	public boolean hasNext() {

		return false;
	}

}
