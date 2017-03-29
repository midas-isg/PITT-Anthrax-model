package edu.pitt.rods.apollo.epidemicmodels.utils;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentEpiModelGlobalResult;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.epidemicmodels.utils.CostFunction.CostItem;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;

public class CompartmentInfluenzaSEIRModelOutput extends CompartmentModelOutput {

	public CompartmentInfluenzaSEIRModelOutput(
			AbstractStateTransitionNetwork stn, String outputPath
	/* int alertAt, double habituationRate, int hrsToCertainty */)
			throws IOException {
		super(stn, outputPath /* , alertAt, habituationRate, hrsToCertainty */);

	}

	public CompartmentInfluenzaSEIRModelOutput(
			AbstractStateTransitionNetwork stn, File outputFile
	/* int alertAt, double habituationRate, int hrsToCertainty */)
			throws IOException {
		super(stn, outputFile /* , alertAt, habituationRate, hrsToCertainty */);

	}

	protected double getVaccineCost(CompartmentEpiModelGlobalResult lastRun) {
		return getCost(lastRun, MovementType.PROPHYLAXED.name(),
				CostItem.PROPHYLAXIS);
	}

	@Override
	public void writeEnd(int modelFinishedAt) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void writeHeaders() throws IOException {

		if (debugMode) {
			generalOutput.write(",TotalNumInModel");
			for (IStateNode node : stn.getNodes()) {
				generalOutput.write("," /* + node.getName() + " SPop, " */
						+ node.getName() + " EPop");
			}
			generalOutput.write("\n");
		}

		if (debugMode) {
			diseaseProgressionOutput
					.write("Hour,Num Susceptible, Num Became Infected, Num Infectious, Num Immune\n");
		}

	}

	public void writeBasicOutput(double cost) throws IOException {
		basicOutput.write(Double.toString(cost));

	}

	@Override
	public void writeResultOfLastRun(CompartmentEpiModelGlobalResult lastRun)
			throws IOException {
		// basicOutput.write(")

		if (debugMode) {
			DecimalFormat df = new DecimalFormat("0.00");

			Double totalCostD = getVaccineCost(lastRun) + getAlertCost();

			totalCostD /= 1000000000;
			String totalCost = df.format(totalCostD);

			String prophCost = df.format(getVaccineCost(lastRun)
					+ getAlertCost());

			graph.write(alertAt + "," + hrsToCertainty + "," + totalCost + ","
					+ prophCost + "\n");

			graph.flush();
		}

	}

	@Override
	public void writeSummary(int t, String[] nodeNames, double[] origNodePops,
			double[] endingNodePops, double totNumInModel,
			CompartmentModelHourlyResult hourlyResult) throws IOException {
		if (debugMode) {
			generalOutput.write("," + totNumInModel);
			for (int i = 0; i < nodeNames.length; i++)
				generalOutput.write(/* "," + origNodePops[i] + */","
						+ endingNodePops[i]);

			generalOutput.write("\n");
			generalOutput.flush();
		}

	}

}
