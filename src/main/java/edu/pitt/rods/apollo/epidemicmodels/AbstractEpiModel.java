package edu.pitt.rods.apollo.epidemicmodels;

import java.util.Iterator;

import edu.pitt.rods.apollo.ParameterizedComponent;

/**
 * Base class for all epidemic models.
 * <p>
 * At an abstract level, an epi model is a thing that:
 * <ul>
 * <li>accepts a set of input parameters via delimited string</li>
 * <li>runs a model</li>
 * <li>stores the results in a delimited string</li>
 * <li>has a description (author, name, organization)</li>
 * </ul>
 * <p>
 * This class extends BioEconParameterizedComponent so that it can accept
 * parameters as input. Input is passed through a delimited string via the
 * constructor, which is "unpacked" by the parent, BioEconParamterizedComponent.
 * These parameters are accessed using the getParam() method.
 * <p>
 * BioEconParameterizedComponenent extends AbstractBioEconObject so an EpiModel
 * is describable.
 * <p>
 * This class also adds the functionality of specifying biological agents that
 * apply to the model.
 * 
 * @author John Levander
 */
public abstract class AbstractEpiModel extends ParameterizedComponent implements
		EpiModelInterface, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5547952788859211508L;

	public abstract void next();

	public abstract boolean hasNext();

	protected boolean outputToFile = false;

	public AbstractEpiModel(String args) {
		super(args);
	}

}
