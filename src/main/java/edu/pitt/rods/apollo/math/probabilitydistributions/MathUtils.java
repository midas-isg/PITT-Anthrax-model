package edu.pitt.rods.apollo.math.probabilitydistributions;

import java.util.Random;

import org.apache.log4j.Logger;

public class MathUtils {
	static Logger log = Logger.getLogger(MathUtils.class);

	final public static double MAX_NEG_EXP = -745;

	final public static double SQRT2PI = Math.sqrt(2D * Math.PI);

	final public static double SQRT2 = Math.sqrt(2D);

	public static final double MAX_POS_EXP = 709;

	public static final double DEG_TO_RAD = Math.PI / 180D;

	public static final double RAD_TO_DEG = 180D / Math.PI;

	public static double lnXplusY(double lnX, double lnY) {
		double temp, lnyMINUSlnx, lnxPLUSlny;
		if (lnY > lnX) {
			temp = lnX;
			lnX = lnY;
			lnY = temp;
		}

		lnyMINUSlnx = lnY - lnX;
		// System.out.print("lnyMINUSlnx=" + lnyMINUSlnx + ",");

		if (lnyMINUSlnx < MAX_NEG_EXP) {
			lnxPLUSlny = lnX;
		} else {
			lnxPLUSlny = lnX + Math.log1p(Math.exp(lnyMINUSlnx));
		}

		// System.out.println(" lnxPLUSy=" + lnxPLUSy);

		return lnxPLUSlny;
	}

	public static double stirling(double y1) {
		double y2 = y1 * y1;
		return ((13860.0 - (462.0 - (132.0 - (99.0 - 140.0 / y2) / y2) / y2)
				/ y2)
				/ y1 / 166320.0);
	}

	/*
	 * Compute hypotenuse of right triangle using method that avoids
	 * overflow/underflow.
	 * 
	 * Note that: (x^2+y^2) = x^2(1+y^2/x^2) = y^2(1+x^2/y^2)
	 * 
	 * public static double hypot(double x, double y) { double z; x =
	 * Math.abs(x); y = Math.abs(y); if (x > y) { z = y/x; z = xMath.sqrt(1+zz);
	 * } else if (y > 0) { z = x/y; z = yMath.sqrt(1+zz); } else { z = 0.0; }
	 * return z; } //
	 */

	/*
	 * public double incompleteGamma(double a, double x) { return
	 * incompleteGamma(a, x, DEFAULT_MAX_ITER, DEFAULT_EPS); }
	 * 
	 * public double incompleteGamma(double a, double x, double maxIter, double
	 * eps) { double igamma; if (x < 0.0 || a <= 0.0) {
	 * System.err.println("MathUtils.incompleteGamma():");
	 * System.err.println("\tx should be less than zero, " + "a less than or
	 * equal to zero: x=" + x + ", a=" + a); System.err.println("\tReturing
	 * NaN."); igamma = Double.NaN; } else if (x < (a + 1D)) { gammaSeries(a, x)
	 * } else { }
	 * 
	 * return igamma; }
	 */

	public static double erfc_Chebyshev(double x) {
		double z = Math.abs(x);
		double t = 1D / (1D + 0.5D * z);
		double exp = -z
				* z
				- 1.26551223D
				+ t
				* (1.00002368D + t
						* (0.37409196D + t
								* (0.09678418D + t
										* (-0.18628806D + t
												* (0.27886807D + t
														* (-1.13520398D + t
																* (1.48851587D + t
																		* (-0.82215223D + t * 0.17087277))))))));
		double erfc = t * Math.exp(exp);
		return x < 0.0 ? 2D - erfc : erfc;
	}

	public static double erf_Chebyshev(double x) {
		return 1D - erfc_Chebyshev(x);
	}

	public static void main(String[] args) {
		Random rand = new Random();
		rand.nextDouble();
		rand.nextDouble();
	}

	/**
	 * @param lnx
	 *            Must be greater than lny!
	 * @param lny
	 * @return ln(x-y)
	 */
	public static double lnXminusY(double lnX, double lnY) {
		double lnyMINUSlnx, lnxMINUSy;
		if (lnY > lnX) {
			lnxMINUSy = Double.NaN;
			System.err
					.println("MathUtils.lnXminusY(): lnx must be greater than lny");
		}

		lnyMINUSlnx = lnY - lnX;
		// System.out.print("lnyMINUSlnx=" + lnyMINUSlnx + ",");

		if (lnyMINUSlnx < MAX_NEG_EXP) {
			lnxMINUSy = lnX;
		} else {
			lnxMINUSy = lnX + Math.log(1D - Math.exp(lnyMINUSlnx));
		}

		// System.out.println(" lnxPLUSy=" + lnxPLUSy);

		return lnxMINUSy;
	}

	public static double[] polyDeg2Roots(double a, double b, double c) {
		double[] roots = new double[2];
		double b2 = b * b;
		double four_ac = 4 * a * c;
		double two_a = 2 * a;
		if (b > 0) {
			roots[1] = -b + Math.sqrt(b2 - four_ac);
			roots[1] /= two_a;
			roots[0] = a / (roots[1] * c);
		} else {
			roots[0] = -b - Math.sqrt(b2 - four_ac);
			roots[0] /= two_a;
			roots[1] = a / (roots[0] * c);
		}

		return roots;
	}

	// Returns the incomplete gamma function P(a, x).
	public static double incompleteGamma(double a, double x) {
		// void gcf(float *gammcf, float a, float x, float *gln);
		// void gser(float *gamser, float a, float x, float *gln);

		double gam = 0.0;
		if (x < 0.0 || a <= 0.0) {
			throw new IllegalArgumentException(
					"For incompleteGamma, x must be < 0" + " and a must be <=0");
		}
		// Use the series representation.
		if (x < (a + 1.0)) {
			gam = gammaSeries(a, x);
		}
		// Use the continued fraction representation...
		else {
			gam = 1.0 - gammaContinuedFraction(a, x); // ...and take its
			// complement.
		}
		return gam;
	}

	public static double incompleteGammaComplement(double a, double x) {
		// Returns the incomplete gamma function Q(a, x) ? 1 ? P(a, x).
		double gam;
		if (x < 0.0 || a <= 0.0) {
			throw new IllegalArgumentException(
					"For incompleteGammaComplement, x "
							+ "must be < 0 and a must be <=0");
		}
		if (x < (a + 1.0)) { // Use the series representation...
			gam = 1.0 - gammaSeries(a, x); // ...and take its complement.

		} else { // Use the continued fraction representation.
			gam = gammaContinuedFraction(a, x);
		}
		return gam;
	}

	/**
	 * @param a
	 * @param x
	 * @return
	 */
	private static double gammaContinuedFraction(double a, double x) {
		int i;
		double an, b, c, d, del, h;
		double gln = GammaDist.logGamma(a);
		b = x + 1.0 - a; // Set up for evaluating continued fraction
		// by modified Lentz’s method (§5.2) with b0 = 0.
		double dmin = 1E-320;
		int itmax = 100;
		c = 1.0 / dmin;
		d = 1.0 / b;
		h = d;
		for (i = 1; i <= itmax; i++) { // Iterate to convergence.
			an = -i * (i - a);
			b += 2.0;
			d = an * d + b;
			if (Math.abs(d) < dmin)
				d = dmin;
			c = b + an / c;
			if (Math.abs(c) < dmin)
				c = dmin;
			d = 1.0 / d;
			del = d * c;
			h *= del;
			if (Math.abs(del - 1.0) < 3E-07)
				break;
		}
		if (i > itmax)
			log.error("a too large, itmax too small in "
					+ "MathUtils.gammaContinuedFraction()");
		double gammcf = Math.exp(-x + a * Math.log(x) - (gln)) * h; // Put
		// factors
		// in front.
		return gammcf;
	}

	/**
	 * @param a
	 * @param x
	 * @return
	 */
	private static double gammaSeries(double a, double x) {
		int n;
		double sum, del, ap, gamser;
		double gln = GammaDist.logGamma(a);

		ap = a;
		del = sum = 1.0 / a;
		for (n = 1; n <= 100; n++) {
			++ap;
			del *= x / ap;
			sum += del;
			if (Math.abs(del) < Math.abs(sum) * 3E-07D) {
				gamser = sum * Math.exp(-x + a * Math.log(x) - (gln));
				return gamser;
			}
		}
		log.error("a too large, ITMAX too small in routine MathUtils.gammaSeries()");
		return 0.0;

	}
}
