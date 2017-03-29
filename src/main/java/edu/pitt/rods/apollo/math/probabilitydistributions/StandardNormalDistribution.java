package edu.pitt.rods.apollo.math.probabilitydistributions;

import java.util.Random;
import java.util.Vector;

/**
 * Contains implementation for Standard Normal distribution.
 * 
 * @author wrh
 * 
 * @param <T>
 */
public class StandardNormalDistribution<T> extends ContinuousDistribution {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6994232004786766730L;

	private StandardNormalDistribution() {
		super();
	}

	static StandardNormalDistribution<Double> _singleton;

	/*
	 * { _singleton = new StandardNormalDistribution(); }
	 */

	public static StandardNormalDistribution<Double> getInstance() {
		if (_singleton == null) {
			_singleton = new StandardNormalDistribution<Double>();
		}
		return _singleton;
	}

	public static void main(String[] args) {

		// StandardNormalDistribution<Integer> snd = StandardNormalDistribution
		// .getInstance();
		// double auc1 = snd.computeAUC(-99, 0);
		// double auc2 = snd.computeAUC(-2, 2);
		// double auc3 = snd.computeAUC(-99, 2);
		// double auc5 = snd.computeAUC(-99, -2);
		// double auc4 = snd.computeAUC(-3, 3);
		// double auc6 = snd.computeAUC(-99, 6.5);
		// double auc7 = snd.computeAUC(-99, 0.1);
		// double auc8 = snd.computeAUC(-99, 1.2);
		// double auc9 = snd.computeAUC(-99, 3.4);
		//
		// System.out.println("Area under std normal from -infinity to zero = "
		// + auc1);
		// System.out.println("Area under std normal from -2 to 2 =           "
		// + auc2);
		// System.out.println("Area under std normal from -infinity to 2 =    "
		// + auc3);
		// System.out.println("Area under std normal from -infinity to -2 =   "
		// + auc5);
		// System.out.println("Area under std normal from -3 to 3 =           "
		// + auc4);
		// System.out.println("Area under std normal from -infinity to 6.5 =  "
		// + auc6);
		// System.out.println("Area under std normal from -infinity to 0.1 =  "
		// + auc7);
		// System.out.println("Area under std normal from -infinity to 1.2 =  "
		// + auc8);
		// System.out.println("Area under std normal from -infinity to 3.4 =  "
		// + auc9);
		//
		// double sum_auc = auc5 + auc2;
		// System.out.println("Summed area from -99 to 2: " + sum_auc);
		// double one_minus_auc5 = 1 - auc5;
		// System.out.println("Area from -99 to 2 by 1-area from -99 to -2: "
		// + one_minus_auc5);
		//
		// double first = snd.cPhi(-10.557296264813118923722673325217);
		// double second = snd
		// .calcLeftMarsaglia(-4.7760246562870815267352600890309);
		// double diff = second - (1.0 - first);
		// System.out.println(second + "\t" + first + "\t" + diff);
		// System.out.println(snd
		// .calcLeftMarsaglia(-10.557296264813118923722673325217));
		//
		// /*
		// * long tmillis_start = System.currentTimeMillis(); for (int i=0;
		// * i<1000000; i++) { calcLeft(2); } long tmillis_end =
		// * System.currentTimeMillis();
		// *
		// * long diff1 = tmillis_end - tmillis_start;
		// *
		// * tmillis_start = System.currentTimeMillis(); for (int i=0;
		// i<1000000;
		// * i++) { calcLeft(-2); } tmillis_end = System.currentTimeMillis();
		// *
		// * long diff2 = tmillis_end - tmillis_start; System.out.println("time
		// * for +2: " + diff1); System.out.println("time for -2: " + diff2);
		// */
		//
		// /*
		// * try { FileWriter fw = new FileWriter("stdnormdev.txt"); double
		// * ave_above = 0.0, ave_below=0.0; int cabove = 0, cbelow = 0; for
		// (int
		// * i=0; i<10000; i++) { double dev = generateDeviate(); if (dev > 0) {
		// * ave_above += dev/10.0; cabove++; } else { ave_below += dev/10.0;
		// * cbelow++; } fw.write(generateDeviate() + "\r\n"); }
		// * ave_above/=(double)cabove; ave_below/=(double)cbelow;
		// * System.out.println("ave above 0: " + ave_above);
		// * System.out.println("ave below 0: " + ave_below);
		// *
		// * fw.close(); System.out.println("Wrote 10,000 standard normal
		// deviates
		// * to stdnormdev.txt");
		// *
		// * for (double d = -4; d<4; d+= 0.1) { System.out.println(d + "\t" +
		// * pdf(d)); } } catch(IOException ioe) { ioe.printStackTrace(); }
		// */
		// long tmillis_start = System.currentTimeMillis();
		// for (int i = 0; i < 1000; i++) {
		// snd.calcLeft(5.9);
		// }
		// long tmillis_end = System.currentTimeMillis();
		// long tdiff1 = tmillis_end - tmillis_start;
		// tmillis_start = System.currentTimeMillis();
		// for (int i = 0; i < 1000; i++) {
		// snd.calcLeft(5.9);
		// }
		// tmillis_end = System.currentTimeMillis();
		// long tdiff2 = tmillis_end - tmillis_start;
		// System.out.println(tdiff1 + "," + tdiff2);
		// System.out.println(snd.calcLeft(5.9) + "," + snd.calcLeftOdd(5.9));
		//
		// @SuppressWarnings("unused")
		// double prob, last_prob = Double.NEGATIVE_INFINITY;
		// for (int i = 0; i < 1000000; i++) {
		// double z = -6.5 - (double) i / 1000000D;
		// prob = snd.calcLeftMarsaglia(z);
		// // System.out.println(z + "\t" + prob);
		// // if (prob > last_prob) {
		// // System.out.println("z=" + z + ", prob > last_prob: " + prob + ">"
		// // + last_prob);
		// // }
		// if (prob > 1.0) {
		// System.out.println("At z=" + z + ", prob > 1:" + prob);
		// }
		// if (prob < 0.0) {
		// System.out.println("At z=" + z + ", prob < 0:" + prob);
		// }
		// last_prob = prob;
		// }
		// double smallest = 1E-16, largest = 1 - smallest;
		// System.out.println(smallest + "," + largest);
		// System.out.println(snd.calcRightMarsaglia(16.6));
	}

	public double computeAUC(double lower, double upper) {
		if (lower >= upper) {
			System.err
					.println("Second argument (upper limit) must be greater than first argument (lower limit)");
			System.err.println("Lower limit: " + lower);
			System.err.println("Upper limit: " + upper);
			return 0.0;
		}
		double sum = calcLeftMarsaglia(upper) - calcLeftMarsaglia(lower);
		return sum;
	}

	public double computeAUC(double upper) {
		return calcLeftMarsaglia(upper);
	}

	static double _limit = Math.exp(-23);

	protected double calcLeft(double z) {
		if (z <= -6.5)
			return 0.0;
		if (z >= 6.5)
			return 1.0;

		double factK = 1, k = 0, _2k_plus_1 = 1, _2_pow_k = 1;
		double sum = 0.0;
		double term = 1;
		double limit = Math.exp(-23);
		boolean k_even = true;

		while (Math.abs(term) > limit) {
			term = 0.3989422804 * Math.pow(z, _2k_plus_1);
			term = (k_even) ? term : -term;
			term /= _2k_plus_1;
			term /= _2_pow_k;
			// term *= Math.pow(z,k+1);
			term /= factK;
			// term =
			// 0.3989422804*Math.pow(-1,k)*Math.pow(z,k)/(2*k+1)/Math.pow(2,k)*
			// Math.pow(z,k+1)/factK;

			sum += term;
			k++;
			k_even = !k_even;
			_2k_plus_1 += 2;
			_2_pow_k *= 2;
			factK *= k;
			// System.out.println( k + "," + factK + "," + term + "," + sum);
			// if (k>100) break;
		}

		// System.out.println(k);

		sum += 0.5;
		// if (sum<0.0000000001) sum = 0.0;

		return sum;
	}

	protected double calcLeftOdd(double z) {
		if (z <= -6.0)
			return 0.0;
		if (z >= 6.0)
			return 1.0;

		double factK = 1, k = 0, _2k_plus_1 = 1, _2_pow_k = 1;
		double pos_sum = 0.0, neg_sum = 0.0;
		double term = 1;
		double limit = Math.exp(-23);
		boolean k_even = true;
		KahanSummation ks_pos = new KahanSummation();
		KahanSummation ks_neg = new KahanSummation();
		while (Math.abs(term) > limit) {
			term = 0.3989422804 * Math.pow(z, _2k_plus_1);
			term = (k_even) ? term : -term;
			term /= _2k_plus_1;
			term /= _2_pow_k;
			// term *= Math.pow(z,k+1);
			term /= factK;
			// term =
			// 0.3989422804*Math.pow(-1,k)*Math.pow(z,k)/(2*k+1)/Math.pow(2,k)*
			// Math.pow(z,k+1)/factK;

			if (k_even) {
				ks_pos.add(term);
			} else {
				ks_neg.add(term);
			}

			pos_sum = (k_even) ? pos_sum + term : pos_sum;
			neg_sum = (k_even) ? neg_sum : neg_sum + term;
			k++;
			k_even = !k_even;
			_2k_plus_1 += 2;
			_2_pow_k *= 2;
			factK *= k;
			// System.out.println( k + "," + factK + "," + term + "," + sum);
			// if (k>100) break;
		}

		// System.out.println(k);

		// double sum = 0.5 + pos_sum + neg_sum;
		double sum = 0.5 + ks_pos.value() + ks_neg.value();
		// if (sum<0.0000000001) sum = 0.0;

		return sum;
	}

	public double calcLeftMarsaglia(double x) {
		if (x < -7.5)
			return 0.0;
		if (x > 7.5)
			return 1.0;
		double s = x, t = 0, b = x, q = x * x, i = 1;
		while (s != t) {
			s = (t = s) + (b *= q / (i += 2));
		}
		return 0.5 + s * Math.exp(-0.5 * q - .91893853320467274178D);
	}

	public double cPhi(double x) {
		int i, j = (int) (.5 * (Math.abs(x) + 1));
		double[] R = { 1.25331413731550025D, .421369229288054473D,
				.236652382913560671D, .162377660896867462D,
				.123131963257932296D, .0990285964717319214D,
				.0827662865013691773D, .0710695805388521071D,
				.0622586659950261958D };
		double pwr = 1, a = R[j], z = 2 * j, b = a * z - 1, h = Math.abs(x) - z, s = a
				+ h * b, t = a, q = h * h;
		for (i = 2; s != t; i += 2) {
			a = (a + z * b) / i;
			b = (b + z * a) / (i + 1);
			pwr *= q;
			s = (t = s) + pwr * (a + h * b);
		}
		s = s * Math.exp(-.5 * x * x - .91893853320467274178D);
		if (x >= 0)
			return (double) s;
		return (double) (1. - s);
	}

	public double calcRightMarsaglia(double x) {
		int i, j = (int) (0.5 * (Math.abs(x) + 1));
		double[] R = { 1.25331413731550025, .421369229288054473,
				.236652382913560671, .162377660896867462, .123131963257932296,
				.0990285964717319214, .0827662865013691773,
				.0710695805388521071, .0622586659950261958 };
		double pwr = 1.0, a = R[j], z = 2 * j, b = a * z - 1, h = Math.abs(x)
				- z, s = a + h * b, t = a, q = h * h;
		for (i = 2; s != t; i += 2) {
			a = (a + z * b) / (double) i;
			b = (b + z * a) / (double) (i + 1);
			pwr *= q;
			s = (t = s) + pwr * (a + h * b);
		}
		s = s * Math.exp(-0.5 * x * x - .91893853320467274178D);
		if (x >= 0)
			return s;
		else
			return 1D - s;
	}

	/*
	 * static double[] _2k_plus_1; static double[] _2_to_k; static double[]
	 * _k_fact; static int _cache_size = 50; static double _limit =
	 * Math.exp(-23);
	 * 
	 * protected static double calcLeftNew(double z) { if (z<-6.5) return 0.0;
	 * if (z>6.5) return 1.0;
	 * 
	 * if (_2k_plus_1 == null) { _2k_plus_1 = new double[_cache_size]; _2_to_k =
	 * new double[_cache_size]; _k_fact = new double[_cache_size];
	 * 
	 * _k_fact[0] = 1; _2k_plus_1[0] = 1; _2_to_k[0] = 1; for (int i=1;
	 * i<_cache_size; i++) { _k_fact[i] = _k_fact[i-1]i; _2k_plus_1[i] =
	 * _2k_plus_1[i-1]+2; _2_to_k[i] = _2_to_k[i-1]2; } }
	 * 
	 * double term = 1, sum = 0; int k = 0; boolean k_even = true;
	 * 
	 * while(Math.abs(term)>_limit) { term =
	 * 0.3989422804Math.pow(z,_2k_plus_1[k]); term = (k_even) ? term : -term;
	 * term /= _2k_plus_1[k]; term /= _2_to_k[k]; //term *= Math.pow(z,k+1);
	 * term /= _k_fact[k]; //term =
	 * 0.3989422804Math.pow(-1,k)Math.pow(z,k)/(2k+1
	 * )/Math.pow(2,k)Math.pow(z,k+1)/factK;
	 * 
	 * sum+=term; k++; k_even = !k_even;
	 * 
	 * //System.out.println( k + "," + factK + "," + term + "," + sum); //if
	 * (k>100) break; }
	 * 
	 * //System.out.println(k);
	 * 
	 * sum+=0.5; if (sum<0.0000000001) sum = 0.0;
	 * 
	 * return sum; }
	 */

	protected double reserve_deviate = 0.0;

	protected boolean has_reserve_deviate = false;

	public Double generateVariate(Random rand) {
		double deviate;
		if (!has_reserve_deviate) {
			double v1, v2, rsq;
			do {
				v1 = 2.0 * rand.nextDouble() - 1.0;
				v2 = 2.0 * rand.nextDouble() - 1.0;
				rsq = v1 * v1 + v2 * v2;
			} while (rsq >= 1.0 || rsq == 0.0);
			double fac = Math.sqrt(-2.0 * Math.log(rsq) / rsq);
			reserve_deviate = v1 * fac;
			has_reserve_deviate = true;
			deviate = v2 * fac;
		} else {
			has_reserve_deviate = false;
			deviate = reserve_deviate;
		}
		return deviate;
	}

	public double pdf(Double x) {
		double pdf_x = Math.exp(x.doubleValue() * x.doubleValue() / -2.0)
				/ MathUtils.SQRT2PI;
		return pdf_x;
	}

	public double cdf(Double x) {
		return calcLeftMarsaglia(x);
	}

	public double inverse_cdf(double x) {
		if (x > 0.5)
			return -inverse(x);
		else {
			return inverse(x);
		}
	}

	protected double inverse(double x) {
		double xp = 0D;
		double lim = 1.e-20D;
		double p0 = -0.322232431088;
		double p1 = -1.0;
		double p2 = -0.342242088547;
		double p3 = -0.0204231210245;
		double p4 = -0.453642210148e-4;
		double q0 = 0.0993484626060;
		double q1 = 0.588581570495;
		double q2 = 0.531103462366;
		double q3 = 0.103537752850;
		double q4 = 0.38560700634e-2;
		if (x < lim || x == 1.0)
			return -1. / lim;
		if (x == 0.5)
			return 0;
		if (x > 0.5)
			x = 1.0 - x;
		double y = Math.sqrt(Math.log(1.0 / (x * x)));
		xp = y + ((((y * p4 + p3) * y + p2) * y + p1) * y + p0)
				/ ((((y * q4 + q3) * y + q2) * y + q1) * y + q0);
		if (x < 0.5)
			xp = -xp;
		return xp;
	}

	/* Expectation of standard normal distribution is always zero */
	public double getExpectation() {
		return 0D;
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
		return "snrm";
	}
}
