package edu.pitt.rods.apollo.math.probabilitydistributions;

import java.util.Collection;
import java.util.Random;
import java.util.Vector;

/**
 * Contains implementation for Gamma distribution.
 * 
 * @author wrh
 * 
 * @param <T>
 */
public class GammaDist extends ContinuousDistribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7250835048691704278L;

	/*
	 * The largest argument for which logGamma(x) is representable in the
	 * machine.
	 */
	private final static double logGamma_xBig = 2.55e305;

	// Function cache for logGamma
	private static double logGammaCache_res = 0.0;

	private static double logGammaCache_x = 0.0;

	/**
	 * Relative machine precision.
	 */
	private final static double EPS = 2.22e-16;

	/**
	 * Square root of 2 pi's>.
	 */
	private final static double SQRT2PI = 2.5066282746310005024157652848110452530069867406099;

	private final static double LOGSQRT2PI = Math.log(SQRT2PI);

	/**
	 * The natural logarithm of the gamma function. Based on public domain
	 * NETLIB (Fortran) code by W. J. Cody and L. Stoltz<BR>
	 * Applied Mathematics Division<BR>
	 * Argonne National Laboratory<BR>
	 * Argonne, IL 60439<BR>
	 * <P>
	 * References:
	 * <OL>
	 * <LI>W. J. Cody and K. E. Hillstrom, 'Chebyshev Approximations for the
	 * Natural Logarithm of the Gamma Function,' Math. Comp. 21, 1967, pp.
	 * 198-203.
	 * <LI>K. E. Hillstrom, ANL/AMD Program ANLC366S, DGAMMA/DLGAMA, May, 1969.
	 * <LI>Hart, Et. Al., Computer Approximations, Wiley and sons, New York,
	 * 1968.
	 * </OL>
	 * </P>
	 * <P>
	 * From the original documentation:
	 * </P>
	 * <P>
	 * This routine calculates the LOG(GAMMA) function for a positive real
	 * argument X. Computation is based on an algorithm outlined in references 1
	 * and 2. The program uses rational functions that theoretically approximate
	 * LOG(GAMMA) to at least 18 significant decimal digits. The approximation
	 * for X > 12 is from reference 3, while approximations for X < 12.0 are
	 * similar to those in reference 1, but are unpublished. The accuracy
	 * achieved depends on the arithmetic system, the compiler, the intrinsic
	 * functions, and proper selection of the machine-dependent constants.
	 * </P>
	 * <P>
	 * Error returns:<BR>
	 * The program returns the value XINF for X .LE. 0.0 or when overflow would
	 * occur. The computation is believed to be free of underflow and overflow.
	 * </P>
	 * 
	 * @return Double.MAX_VALUE for x < 0.0 or when overflow would occur, i.e. x
	 *         > 2.55E305
	 * @author Jaco van Kooten
	 */
	public static double logGamma(double x) {
		// Log Gamma related constants
		final double lg_d1 = -.5772156649015328605195174, lg_d2 = .4227843350984671393993777, lg_d4 = 1.791759469228055000094023;
		final double lg_p1[] = { 4.945235359296727046734888,
				201.8112620856775083915565, 2290.838373831346393026739,
				11319.67205903380828685045, 28557.24635671635335736389,
				38484.96228443793359990269, 26377.48787624195437963534,
				7225.813979700288197698961 };
		final double lg_q1[] = { 67.48212550303777196073036,
				1113.332393857199323513008, 7738.757056935398733233834,
				27639.87074403340708898585, 54993.10206226157329794414,
				61611.22180066002127833352, 36351.27591501940507276287,
				8785.536302431013170870835 };
		final double lg_p2[] = { 4.974607845568932035012064,
				542.4138599891070494101986, 15506.93864978364947665077,
				184793.2904445632425417223, 1088204.76946882876749847,
				3338152.967987029735917223, 5106661.678927352456275255,
				3074109.054850539556250927 };
		final double lg_q2[] = { 183.0328399370592604055942,
				7765.049321445005871323047, 133190.3827966074194402448,
				1136705.821321969608938755, 5267964.117437946917577538,
				13467014.54311101692290052, 17827365.30353274213975932,
				9533095.591844353613395747 };
		final double lg_p4[] = { 14745.02166059939948905062,
				2426813.369486704502836312, 121475557.4045093227939592,
				2663432449.630976949898078, 29403789566.34553899906876,
				170266573776.5398868392998, 492612579337.743088758812,
				560625185622.3951465078242 };
		final double lg_q4[] = { 2690.530175870899333379843,
				639388.5654300092398984238, 41355999.30241388052042842,
				1120872109.61614794137657, 14886137286.78813811542398,
				101680358627.2438228077304, 341747634550.7377132798597,
				446315818741.9713286462081 };
		final double lg_c[] = { -0.001910444077728, 8.4171387781295e-4,
				-5.952379913043012e-4, 7.93650793500350248e-4,
				-0.002777777777777681622553, 0.08333333333333333331554247,
				0.0057083835261 };
		// Rough estimate of the fourth root of logGamma_xBig
		final double lg_frtbig = 2.25e76;

		final double pnt68 = 0.6796875;

		double xden, corr, xnum;
		int i;
		double y, xm1, xm2, xm4, res, ysq;

		if (x == logGammaCache_x)
			return logGammaCache_res;
		y = x;
		if (y > 0.0 && y <= logGamma_xBig) {
			if (y <= EPS) {
				res = -Math.log(y);
			} else if (y <= 1.5) {
				// ----------------------------------------------------------------------
				// EPS .LT. X .LE. 1.5
				// ----------------------------------------------------------------------
				if (y < pnt68) {
					corr = -Math.log(y);
					xm1 = y;
				} else {
					corr = 0.0;
					xm1 = y - 1.0;
				}
				if (y <= 0.5 || y >= pnt68) {
					xden = 1.0;
					xnum = 0.0;
					for (i = 0; i < 8; i++) {
						xnum = xnum * xm1 + lg_p1[i];
						xden = xden * xm1 + lg_q1[i];
					}
					res = corr + xm1 * (lg_d1 + xm1 * (xnum / xden));
				} else {
					xm2 = y - 1.0;
					xden = 1.0;
					xnum = 0.0;
					for (i = 0; i < 8; i++) {
						xnum = xnum * xm2 + lg_p2[i];
						xden = xden * xm2 + lg_q2[i];
					}
					res = corr + xm2 * (lg_d2 + xm2 * (xnum / xden));
				}
			} else if (y <= 4.0) {
				// ----------------------------------------------------------------------
				// 1.5 .LT. X .LE. 4.0
				// ----------------------------------------------------------------------
				xm2 = y - 2.0;
				xden = 1.0;
				xnum = 0.0;
				for (i = 0; i < 8; i++) {
					xnum = xnum * xm2 + lg_p2[i];
					xden = xden * xm2 + lg_q2[i];
				}
				res = xm2 * (lg_d2 + xm2 * (xnum / xden));
			} else if (y <= 12.0) {
				// ----------------------------------------------------------------------
				// 4.0 .LT. X .LE. 12.0
				// ----------------------------------------------------------------------
				xm4 = y - 4.0;
				xden = -1.0;
				xnum = 0.0;
				for (i = 0; i < 8; i++) {
					xnum = xnum * xm4 + lg_p4[i];
					xden = xden * xm4 + lg_q4[i];
				}
				res = lg_d4 + xm4 * (xnum / xden);
			} else {
				// ----------------------------------------------------------------------
				// Evaluate for argument .GE. 12.0
				// ----------------------------------------------------------------------
				res = 0.0;
				if (y <= lg_frtbig) {
					res = lg_c[6];
					ysq = y * y;
					for (i = 0; i < 6; i++)
						res = res / ysq + lg_c[i];
				}
				res /= y;
				corr = Math.log(y);
				res = res + LOGSQRT2PI - 0.5 * corr;
				res += y * (corr - 1.0);
			}
		} else {
			// ----------------------------------------------------------------------
			// Return for bad arguments
			// ----------------------------------------------------------------------
			res = Double.MAX_VALUE;
		}
		// ----------------------------------------------------------------------
		// Final adjustments and return
		// ----------------------------------------------------------------------
		logGammaCache_x = x;
		logGammaCache_res = res;
		return res;
	}

	double _alpha, _beta;

	double _c, _t, _b;

	double _expect;

	public GammaDist(double alpha, double beta) {
		super();

		_alpha = alpha;
		_beta = beta;
		if (_alpha <= 1.0) {
			_c = 1.0D / alpha;
			_t = 0.07D * 0.75D * Math.sqrt(1.0D - alpha);
			_b = 1.0D + Math.exp(-_t) * _alpha / _t;
		} else {
			_b = alpha - 1.0D;
			_c = 3.0D * alpha - 0.75D;
		}
		_expect = _alpha / _beta;
	}

	public double pdf(Double x) {

		return 0.0;
	}

	public double cdf(Double x) {

		return 0.0;
	}

	public double getExpectation() {
		return _expect;
	}

	public static void main(String[] args) {
		GammaDist gd = new GammaDist(2D, 3D);
		Random rand = new Random(0);
		Collection<Double> rnd_gamma = gd.generateVariates(5000, rand);
		for (double i : rnd_gamma) {
			System.out.println(i);
		}

		GammaDist gamma = new GammaDist(2.0, 3.0);
		rnd_gamma = gamma.generateVariates(5000, rand);
		for (Double d : rnd_gamma) {
			System.out.println(d);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.detection.utilities.distributions.DistributionStructure
	 * #getParam()
	 */
	public Vector<Object> getParamList() {
		Vector<Object> param = new Vector<Object>();
		param.addElement(_alpha);
		param.addElement(_beta);
		return param;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.detection.utilities.distributions.DistributionStructure
	 * #getAbbreviation()
	 */
	public String getAbbreviation() {
		return "gamma";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.detection.utilities.distributions.DistributionStructure
	 * #generateVariate()
	 */
	public Double generateVariate(Random rand) {
		double variate;
		if (_alpha <= 1.0) {
			boolean accept = false;
			double x = Double.NEGATIVE_INFINITY;
			double u, w, v, y;

			while (!accept) {
				u = rand.nextDouble();
				w = rand.nextDouble();
				v = _b * u;
				if (v <= 1.0) {
					x = _t * Math.pow(v, _c);
					accept = ((w <= ((2.0D - x) / (2.0D + x))) || (w <= Math
							.exp(-x)));
				} else {
					x = -Math.log(_c * _t * (_b - v));
					y = x / _t;
					accept = (((w * (_alpha + y - _alpha * y)) <= 1.0) || (w <= (Math
							.pow(y, _alpha - 1.0D))));
				}
			}
			variate = x / _beta;
		} else {
			boolean accept = false;
			double x = Double.NEGATIVE_INFINITY;
			double u, v, w, y;
			while (!accept) {
				u = rand.nextDouble();
				v = rand.nextDouble();
				w = u * (1.0D - u);
				y = Math.sqrt(_c / w) * (u - 0.5D);
				x = _b + y;
				if (x >= 0) {
					double z = 64.0D * w * w * w * v * v;
					accept = ((z <= (1.0D - 2.0D * y * y / x)) || (Math.log(z) <= (2.0D * (_b
							* Math.log(x / _b) - y))));
				}
			}
			variate = x / _beta;
		}
		return variate;
	}

}
