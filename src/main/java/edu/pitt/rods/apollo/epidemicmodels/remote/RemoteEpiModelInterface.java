package edu.pitt.rods.apollo.epidemicmodels.remote;

import java.io.IOException;

/**
 * Interface that allows an EpiModel to be run from a remote request.
 * 
 * @author John Levander
 * 
 */
public interface RemoteEpiModelInterface {

	/**
	 * Requests that a job be run.
	 * 
	 * @param args
	 *            A delimited string that defines parameters for the job.
	 * @return a ID that is used to identify the job
	 */
	public long submitJob(String args);

	/**
	 * Checks the status of a job.
	 * 
	 * @param jobId
	 *            the identifier of the job
	 * @return
	 */
	public int getJobStatus(long jobId);

	/**
	 * Retreives the result of a job
	 * 
	 * @param jobId
	 *            the identifier of the job
	 * @return A delimted string that defines the results of the job
	 * @throws IOException
	 */
	public String getJobResult(long jobId) throws IOException;
}
