package edu.pitt.rods.apollo.epidemicmodels.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentEpiModelGlobalResult;
import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;
import edu.pitt.rods.apollo.epidemicmodels.utils.CostFunction.CostItem;
import edu.pitt.rods.apollo.statetransitionnetwork.AbstractStateTransitionNetwork;

public abstract class CompartmentModelOutput {
	FileWriter basicOutput = null;
	FileWriter generalOutput = null;
	FileWriter treatmentOutput = null;
	FileWriter diseaseProgressionOutput = null;
	FileWriter transitionOutput = null;
	FileWriter graph = null;
	String outputPath = "";
	String habituationRate = "";
	File basicOutputFile = null;
	int alertAt;
	int hrsToCertainty;
	String segmentName = "";
	// if debugmode, more files are output
	boolean debugMode = true;

	protected double getCost(CompartmentEpiModelGlobalResult lastRun,
			String transitionDescription, CostItem costItem) {
		return lastRun.getTreatmentCount(transitionDescription)
				* CostFunction.getCost(costItem);
	}

	protected double getAlertCost() {
		if (stn.getState().isAlert())
			return CostFunction.getCost(CostItem.ALERT);
		else
			return 0;
	}

	protected AbstractStateTransitionNetwork stn = null;

	public CompartmentModelOutput(AbstractStateTransitionNetwork stn,
			String outputPath /*
							 * int alertAt , double habituationRate , int
							 * hrsToCertainty
							 */) throws IOException {
		super();
		debugMode = true;
		this.stn = stn;
//		this.alertAt = alertAt;
//		this.hrsToCertainty = hrsToCertainty;
		this.outputPath = outputPath;
		this.segmentName = stn.segmentName;
		basicOutputFile = new File(outputPath + "basic" + alertAt + "-"
				+ habituationRate + "-" + hrsToCertainty + ".csv");

	}

	public CompartmentModelOutput(AbstractStateTransitionNetwork stn,
			File outputFile /*
							 * int alertAt , double habituationRate , int
							 * hrsToCertainty
							 */) throws IOException {
		super();
		debugMode = true;
		this.stn = stn;
//		this.alertAt = alertAt;
//		this.hrsToCertainty = hrsToCertainty;
		this.outputPath = "";
		basicOutputFile = outputFile;

	}

	public abstract void writeResultOfLastRun(
			CompartmentEpiModelGlobalResult resultOfLastRun) throws IOException;

	public abstract void writeHeaders() throws IOException;

	public FileWriter getGeneralOutput() {
		return generalOutput;
	}

	public FileWriter getTreatmentOutput() {
		return treatmentOutput;
	}

	public FileWriter getDiseaseProgressionOutput() {
		return diseaseProgressionOutput;
	}

	public FileWriter getTransitionOutput() {
		return transitionOutput;
	}

	public abstract void writeSummary(int t, String[] nodeNames,
			double[] origNodePops, double[] endingNodePops,
			double totNumInModel, CompartmentModelHourlyResult hourlyResult)
			throws IOException;

	public abstract void writeEnd(int modelFinishedAt) throws IOException;

	public abstract void writeBasicOutput(double cost) throws IOException;

	public void openOutputFiles() throws IOException {
		basicOutput = new FileWriter(basicOutputFile);
		if (debugMode) {
			transitionOutput = new FileWriter(outputPath + segmentName + "_"
					+ "transition-output-" + alertAt + "-" + habituationRate
					+ "-" + hrsToCertainty + ".csv");
			diseaseProgressionOutput = new FileWriter(outputPath + segmentName
					+ "_" + "dxProgression-output-" + alertAt + "-"
					+ habituationRate + "-" + hrsToCertainty + ".csv");
			treatmentOutput = new FileWriter(outputPath + segmentName + "_"
					+ "treatment-output-" + alertAt + "-" + habituationRate
					+ "-" + hrsToCertainty + ".csv");

			generalOutput = new FileWriter(outputPath + segmentName + "_"
					+ "np-output-" + alertAt + "-" + habituationRate + "-"
					+ hrsToCertainty + ".csv");
		}
	}

	public void closeOutputFiles() throws IOException {
		basicOutput.close();
		if (debugMode) {
			generalOutput.close();
			treatmentOutput.close();
			diseaseProgressionOutput.close();
			transitionOutput.close();
		}
	}
}