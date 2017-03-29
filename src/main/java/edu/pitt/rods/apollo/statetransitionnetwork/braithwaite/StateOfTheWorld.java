package edu.pitt.rods.apollo.statetransitionnetwork.braithwaite;

import java.util.Observable;

public class StateOfTheWorld extends Observable {

	private boolean alert = false;

	private int hour = 0;
	private double habituationRate = 0.0;
	private boolean habituation = true;
	private boolean outbreak = false;

	public boolean isOutbreak() {
		return outbreak;
	}

	public void setOutbreak(boolean outbreak) {
		this.outbreak = outbreak;
	}

	private boolean outbreakCertain = false;
	private boolean fastTrans = false;
	private int hoursSinceAlert = 0;
	private int hoursSinceCertainty = 0;

	public void signalAlert() {
		alert = true;
	}

	public boolean isFastTransitionMode() {
		return fastTrans;
	}

	public void signalOutbreakCertain() {
		outbreakCertain = true;
	}

	public void incHour() {
		hour++;
		if (alert)
			hoursSinceAlert++;
		if (outbreakCertain)
			hoursSinceCertainty++;
		setChanged();
		notifyObservers(hour);

	}

	public boolean isAlert() {
		return alert;
	}

	public boolean isOutbreakCertain() {
		return outbreakCertain;
	}

	public Integer getHour() {
		return hour;
	}

	public void reset() {
		alert = false;

		hour = 0;
		habituation = true;
		outbreakCertain = false;
		fastTrans = false;
		hoursSinceAlert = 0;
		hoursSinceCertainty = 0;
		outbreak = false;
	}

	public StateOfTheWorld(
	/* Integer previousFalseAlarms, */Double percentCompliantWithTreatment) {
		super();

		Double[] hr = new Double[3];
		hr[0] = 0.2;
		hr[1] = 0.5;
		hr[2] = 3.0;

		this.habituationRate = percentCompliantWithTreatment;
	}

	public Double getHabituationRate() {
		if (habituation)
			return habituationRate;
		else
			return 1.0;
	}

	public int getHoursSinceCertainty() {
		return hoursSinceCertainty;
	}

	public int getHoursSinceAlert() {
		return hoursSinceAlert;
	}

}
