package edu.pitt.rods.apollo.epidemicmodels.compartmental.anthrax;

/**
 *
 * Author: Nick Millett
 * Email: nick.millett@gmail.com
 * Date: Jun 23, 2014
 * Time: 1:55:35 PM
 * Class: AnthraxCompartmentResults
 * IDE: NetBeans 6.9.1
 */
public class AnthraxCompartmentResults {

	private double[] unexposedTimeSeries;
	private double[] exposedTimeSeries;
	private double[] recoveredTimeSeries;
	private double[] newlyDeceasedTimeSeries;
	private double[] prophylaxedTimeSeres;
	private double[] asymptomaticTimeSeries;
	private double[] prodromalTimeSeries;
	private double[] fulminantTimeSeries;
	private double[] deadTimeSeries;

	/**
	 * @return the unexposedTimeSeries
	 */
	public double[] getUnexposedTimeSeries() {
		return unexposedTimeSeries;
	}

	/**
	 * @return the exposedTimeSeries
	 */
	public double[] getExposedTimeSeries() {
		return exposedTimeSeries;
	}

	/**
	 * @return the recoveredTimeSeries
	 */
	public double[] getRecoveredTimeSeries() {
		return recoveredTimeSeries;
	}

	/**
	 * @return the newlyDeceasedTimeSeries
	 */
	public double[] getNewlyDeceasedTimeSeries() {
		return newlyDeceasedTimeSeries;
	}
	
	/**
	 * @return the prophylaxedTimeSeres
	 */
	public double[] getProphylaxedTimeSeres() {
		return prophylaxedTimeSeres;
	}

	/**
	 * @param unexposedTimeSeries the unexposedTimeSeries to set
	 */
	public void setUnexposedTimeSeries(double[] unexposedTimeSeries) {
		this.unexposedTimeSeries = unexposedTimeSeries;
	}

	/**
	 * @param exposedTimeSeries the exposedTimeSeries to set
	 */
	public void setExposedTimeSeries(double[] exposedTimeSeries) {
		this.exposedTimeSeries = exposedTimeSeries;
	}

	/**
	 * @param recoveredTimeSeries the recoveredTimeSeries to set
	 */
	public void setRecoveredTimeSeries(double[] recoveredTimeSeries) {
		this.recoveredTimeSeries = recoveredTimeSeries;
	}

	/**
	 * @param newlyDeceasedTimeSeries the newlyDeceasedTimeSeries to set
	 */
	public void setNewlyDeceasedTimeSeries(double[] newlyDeceasedTimeSeries) {
		this.newlyDeceasedTimeSeries = newlyDeceasedTimeSeries;
	}

	/**
	 * @param prophylaxedTimeSeres the prophylaxedTimeSeres to set
	 */
	public void setProphylaxedTimeSeres(double[] prophylaxedTimeSeres) {
		this.prophylaxedTimeSeres = prophylaxedTimeSeres;
	}

	/**
	 * @return the asymptomaticTimeSeries
	 */
	public double[] getAsymptomaticTimeSeries() {
		return asymptomaticTimeSeries;
	}

	/**
	 * @param asymptomaticTimeSeries the asymptomaticTimeSeries to set
	 */
	public void setAsymptomaticTimeSeries(double[] asymptomaticTimeSeries) {
		this.asymptomaticTimeSeries = asymptomaticTimeSeries;
	}

	/**
	 * @return the prodromalTimeSeries
	 */
	public double[] getProdromalTimeSeries() {
		return prodromalTimeSeries;
	}

	/**
	 * @param prodromalTimeSeries the prodromalTimeSeries to set
	 */
	public void setProdromalTimeSeries(double[] prodromalTimeSeries) {
		this.prodromalTimeSeries = prodromalTimeSeries;
	}

	/**
	 * @return the fulminantTimeSeries
	 */
	public double[] getFulminantTimeSeries() {
		return fulminantTimeSeries;
	}

	/**
	 * @param fulminantTimeSeries the fulminantTimeSeries to set
	 */
	public void setFulminantTimeSeries(double[] fulminantTimeSeries) {
		this.fulminantTimeSeries = fulminantTimeSeries;
	}

	/**
	 * @return the deadTimeSeries
	 */
	public double[] getDeadTimeSeries() {
		return deadTimeSeries;
	}

	/**
	 * @param deadTimeSeries the deadTimeSeries to set
	 */
	public void setDeadTimeSeries(double[] deadTimeSeries) {
		this.deadTimeSeries = deadTimeSeries;
	}

}
