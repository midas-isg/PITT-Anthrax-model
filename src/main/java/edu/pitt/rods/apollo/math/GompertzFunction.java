package edu.pitt.rods.apollo.math;

public class GompertzFunction {

	private double a, b, c = 0.0;

	public GompertzFunction(double a, double b, double c) {
		super();
		this.a = a;
		this.b = b;
		this.c = c;
	}

	public double evaluate(double t) {
		double ct = c * t;
		double eToTheCT = Math.pow(Math.E, ct);
		double beToTheCT = b * eToTheCT;
		double eTotheBEToTheCT = Math.pow(Math.E, beToTheCT);
		return a * eTotheBEToTheCT;
	}
}
