package edu.pitt.rods.apollo.epidemicmodels.remote;

import edu.pitt.rods.apollo.epidemicmodels.ModelMetaData;

//import edu.pitt.rods.bioecon.AbstractBioEconObject;

/**
 * Base class for classes that run via a remote request.
 * <p>
 * This class allows the user to define an output directory, which is the
 * location where the results are persisted.
 * 
 * @author John Levander
 */
public abstract class AbstractRemoteEpiModel extends ModelMetaData implements
		java.io.Serializable, RemoteEpiModelInterface {

	protected String outputDirectory = "";

	public String getOutputDirectory() {
		return outputDirectory;
	}

	public void setOutputDirectory(String outputDirectory) {
		this.outputDirectory = outputDirectory;
	}

	private static final long serialVersionUID = -2746844225935061864L;

}
