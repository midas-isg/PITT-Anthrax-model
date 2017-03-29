package edu.pitt.rods.apollo.epidemicmodels.remote;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.SEIR.influenza.MidasInfluenzaModel;
import edu.pitt.rods.apollo.utilities.RodsMoneyFormat;

/**
 * Enables a MidasInfluenzaModel to be run from a webservice.
 * 
 * @author John Levander
 * 
 */
public class RemoteSEIRModel extends AbstractRemoteEpiModel {

	private static final long serialVersionUID = -1743265118459591614L;

	public String getJobResult(long jobId) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(
				getOutputDirectory() + Long.toString(jobId) + ".csv"));
		String cost = br.readLine();
		return RodsMoneyFormat.format(Double.valueOf(cost));
	}

	public long submitJob(String args) {

		long jobId = System.currentTimeMillis();

		// args += MIDASInfluenzaModelParams.OUTPUT_FILENAME_PARAM + ":string="
		// + getOutputDirectory() + Long.toString(jobId) + ".csv";

		MidasInfluenzaModel model = new MidasInfluenzaModel(args);

		model.buildNonSegmentedModel();

		new Thread(model).start();

		return jobId;
	}

	public int getJobStatus(long jobId) {
		// if file exists, return true, else return false
		if (new File(getOutputDirectory() + Long.toString(jobId) + ".csv")
				.exists())
			return 1;
		else
			return 0;

	}
}