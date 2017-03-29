package edu.pitt.rods.apollo.epidemicmodels.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax.AnthraxModel;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentEpiModelGlobalResult;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.epidemicmodels.utils.CostFunction.CostItem;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;
import edu.pitt.rods.apollo.statetransitionnetwork.IStateNode;
import edu.pitt.rods.apollo.statetransitionnetwork.MovementType;
import edu.pitt.rods.apollo.statetransitionnetwork.braithwaite.NodeGroupConsts;

public class CompartmentAnthraxEpiModelOutput extends CompartmentModelOutput {

	protected double getDeathCost(CompartmentEpiModelGlobalResult lastRun) {
		return getCost(lastRun, MovementType.DIED.name(), CostItem.FATALITY);
	}

	protected double getICUCost(CompartmentEpiModelGlobalResult lastRun) {
		return getCost(lastRun, MovementType.ICU.name(), CostItem.ICU);
	}

	protected double getIVCost(CompartmentEpiModelGlobalResult lastRun) {
		return getCost(lastRun, MovementType.IV.name(), CostItem.IV);
	}

	protected double getProphylaxisCost(CompartmentEpiModelGlobalResult lastRun) {
		return getCost(lastRun, MovementType.PROPHYLAXED.name(),
				CostItem.PROPHYLAXIS);
	}

	public void writeResultOfLastRun(CompartmentEpiModelGlobalResult lastRun)
			throws IOException {
		DecimalFormat df = new DecimalFormat("0.00");

		Double totalCostD = getDeathCost(lastRun) + getIVCost(lastRun)
				+ getICUCost(lastRun) + getProphylaxisCost(lastRun)
				+ getAlertCost();

		totalCostD /= 1000000000;
		String totalCost = df.format(totalCostD);

		String prophCost = df.format(getProphylaxisCost(lastRun)
				+ getAlertCost());

		String ICUCost = df.format(getICUCost(lastRun));
		String IVCost = df.format(getIVCost(lastRun));

		String deathCost = df.format(getDeathCost(lastRun));
		graph.write(alertAt + "," + hrsToCertainty + "," + totalCost + ","
				+ prophCost + "," + IVCost + "," + ICUCost + "," + deathCost
				+ "\n");

		graph.flush();
	}

	public CompartmentAnthraxEpiModelOutput(AbstractStateTransitionNetwork stn,
			String outputPath, int alertAt, double habituationRate,
			int hrsToCertainty) throws IOException {
		super(stn, outputPath); // alertAt, habituationRate, hrsToCertainty

		File outputFile = new File(outputPath + "graph-time.csv");

		graph = new FileWriter(outputFile, true);
		if (!outputFile.exists() || (outputFile.length() == 0))
			graph.write("Previous False Alarms, Alert At #, Time Until Certain, Total Cost, Proph Cost, IV Cost, ICU Cost, Death Cost\n");

	}

	public void writeHeaders() throws IOException {
		generalOutput.write(",TotalNumInModel");
		for (IStateNode node : stn.getNodes()) {
			generalOutput.write("," + node.getName() + " SPop, "
					+ node.getName() + " EPop");
		}
		generalOutput.write("\n");
		treatmentOutput.write(",NumProphylaxied,NumIV,NumICU\n");
		diseaseProgressionOutput
				.write(",Num Became Prodromal,Num Became Fulminant, Num Became Dead, Num Recovered From Asx, Num Recoverd From Prodromal, Num Recovered from Fulminant\n");
		transitionOutput
				.write(",TimeSpentInAxs,TimeSpentInProd,TimeSpentInFul\n");
	}

	public void writeSummary(int t, String[] nodeNames, double[] origNodePops,
			double[] endingNodePops, double totNumInModel,
			CompartmentModelHourlyResult hourlyResult) throws IOException {
		treatmentOutput.write(Integer.valueOf(t) + ","
				+ hourlyResult.getValue(MovementType.PROPHYLAXED.name()) + ","
				+ hourlyResult.getValue(MovementType.IV.name()) + ","
				+ hourlyResult.getValue(MovementType.ICU.name()) + "\n");
		treatmentOutput.flush();

		diseaseProgressionOutput
				.write(Integer.valueOf(t)
						+ ","
						+ hourlyResult
								.getValue(NodeGroupConsts.TO_PROD_PROGRESSION)
						+ ","
						+ hourlyResult
								.getValue(NodeGroupConsts.TO_FUL_PROGRESSION)
						+ ","
						+ hourlyResult
								.getValue(NodeGroupConsts.TO_DEAD_PROGRESSION)
						+ ","
						+ hourlyResult.getValue(NodeGroupConsts.TO_RECOVER_FROM_ASX_PROGRESSION
								+ ","
								+ hourlyResult
										.getValue(NodeGroupConsts.TO_RECOVER_FROM_PROD_PROGRESSION)
								+ ","
								+ hourlyResult
										.getValue(NodeGroupConsts.TO_RECOVER_FROM_FUL_PROGRESSION)
								+ "\n"));

		generalOutput.write("," + totNumInModel);
		for (int i = 0; i < nodeNames.length; i++)
			generalOutput
					.write("," + origNodePops[i] + "," + endingNodePops[i]);

		generalOutput.write("\n");
		generalOutput.flush();
	}

	public void writeEnd(int modelFinishedAt) throws IOException {
		IStateNode asxUnTreatedRefuse = stn
				.getNode(AnthraxModel.ASX_REFUSE_TREATMENT);
		IStateNode asxUnTreatedAccept = stn
				.getNode(AnthraxModel.ASX_TAKES_TREATMENT);
		IStateNode asxTreatedWillProgress = stn
				.getNode(AnthraxModel.ASX_PO_WONT_RECOVER);

		IStateNode prodUnTreated = stn.getNode(AnthraxModel.PROD);
		IStateNode prodWillProgress = stn
				.getNode(AnthraxModel.PROD_WONT_RECOVER);

		IStateNode fulUnTreated = stn.getNode(AnthraxModel.FULMINANT);
		IStateNode fulWillProgress = stn.getNode(AnthraxModel.FUL_WONT_RECOVER);

		for (int t = 0; t < modelFinishedAt; t++) {
			double timeSpentAsx = asxUnTreatedRefuse
					.getTotalNumberOfTransitionsOutOfNodeAtHour(t)
					+ asxUnTreatedAccept
							.getTotalNumberOfTransitionsOutOfNodeAtHour(t)
					+ asxTreatedWillProgress
							.getTotalNumberOfTransitionsOutOfNodeAtHour(t);

			double timeSpentProd = prodUnTreated
					.getTotalNumberOfTransitionsOutOfNodeAtHour(t)
					+ prodWillProgress
							.getTotalNumberOfTransitionsOutOfNodeAtHour(t);

			double timeSpentFul = fulUnTreated
					.getTotalNumberOfTransitionsOutOfNodeAtHour(t)
					+ fulWillProgress
							.getTotalNumberOfTransitionsOutOfNodeAtHour(t);

			transitionOutput.write(Integer.valueOf(t) + "," + timeSpentAsx
					+ "," + timeSpentProd + "," + timeSpentFul + "\n");
		}

	}

	@Override
	public void writeBasicOutput(double cost) throws IOException {
		// TODO Auto-generated method stub

	}

}
