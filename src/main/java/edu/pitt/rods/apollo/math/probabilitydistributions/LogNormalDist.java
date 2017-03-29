/*
 * Created on Apr 18, 2005
 *
 */
package edu.pitt.rods.apollo.math.probabilitydistributions;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import java.util.Vector;

/**
 * @author William R. Hogan, MD, MS
 * 
 *         Contains implementation for Log Normal distribution. pdf() and cdf()
 *         call StandardNormalDistribution pdf() and cdf() functions and
 *         transform them appropriately. Because the transformation requires
 *         taking ln(x), the class also provides a cdflnx() function that takes
 *         the natural log of the x value at which you wish to compute the cdf.
 * 
 */
public class LogNormalDist extends ContinuousDistribution {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3295992601296718157L;

	static StandardNormalDistribution<Double> _snd = StandardNormalDistribution
			.getInstance();

	double _mu;

	double _sigma;

	double _const1;

	double _const2;

	double _median;

	double _expect;

	Double _variance;

	public LogNormalDist(double mu, double sigma) {
		_mu = mu;
		_median = Math.exp(_mu);
		_sigma = sigma;
		double inv_sigma = 1D / _sigma;
		_const1 = inv_sigma * (1D / MathUtils.SQRT2PI);
		_const2 = -0.5 * inv_sigma * inv_sigma;
		_snd = StandardNormalDistribution.getInstance();
		if (_snd == null) {
			System.err.println("Got null StandardNormalDistribution instance!");
		}
		_expect = Math.exp(_mu + _sigma * _sigma / 2.0);
		_variance = null;
	}

	public double pdf(Double theta) {
		double pdf_x = _const1 / theta.doubleValue();
		double temp = Math.log(theta.doubleValue()) - _mu;
		temp *= temp * _const2;
		pdf_x *= Math.exp(temp);
		return pdf_x;
	}

	public Double cdf(double a) {
		if (a == 0.0)
			a = Double.MIN_VALUE;
		double auc = 0.0;
		if (a < 0)
			throw new IllegalArgumentException("LogNormalDist, cdf()."
					+ " a must be greater than or equal to zero.");
		if (a > 0) {
			double transform = Math.log(a) - _mu;
			transform /= _sigma;
			auc = _snd.computeAUC(transform);
		}
		return auc;
	}

	public double fastCDF(double a) {
		if (a == 0.0)
			a = Double.MIN_VALUE;
		double auc = 0.0;
		if (a < 0)
			throw new IllegalArgumentException("LogNormalDist, cdf()."
					+ " a must be greater than or equal to zero.");
		if (a > 0) {
			double transform = Math.log(a) - _mu;
			transform /= _sigma;
			auc = _snd.computeAUC(transform);
		}
		return auc;
	}

	public double getExpectation() {
		return _expect;
	}

	public double getMedian() {
		return _median;
	}

	public double getVariance() {
		if (_variance == null) {
			double sigsq = _sigma * _sigma;
			_variance = Math.exp(2 * _mu + sigsq) * (Math.exp(sigsq) - 1);
		}
		return _variance;
	}

	public double cdf_range(double a, double b) {
		if (b <= a)
			throw new IllegalArgumentException("LogNormalDist, cdf_range()."
					+ " a must be less than b.");
		if (a < 0 || b < 0)
			throw new IllegalArgumentException("LogNormalDist, cdf()."
					+ " a and b must be greater than zero.");
		return cdf(b) - cdf(a);
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
		param.addElement(_mu);
		param.addElement(_sigma);
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
		return "lgnorm";
	}

	public static double cdf(double mu, double sigma, double x) {
		double auc = 0.0;
		if (x < 0)
			throw new IllegalArgumentException("LogNormalDist, cdf()."
					+ " x must be greater than or equal to zero.");
		double transform = Math.log(x) - mu;
		transform /= sigma;
		auc = _snd.computeAUC(transform);
		return auc;
	}

	public static double cdflnx(double mu, double sigma, double lnx) {
		double auc = 0.0;
		double transform = lnx - mu;
		transform /= sigma;
		auc = _snd.computeAUC(transform);
		return auc;
	}

	public static void main(String[] args) {
		// double mean = 2.395;
		// double sigma = 0.55677644;
		double mean = 4.564348;
		double sigma = 0.14;
		LogNormalDist lnd = new LogNormalDist(mean, sigma);
		System.out.println("Expectation: " + lnd.getExpectation());
		System.out.println("Variance: " + lnd.getVariance());
		System.out.println("Stdev: " + Math.sqrt(lnd.getVariance()));
		System.out.println("Median: " + lnd.getMedian());

		System.out.println("mu=4.564348, sigma=0.14 => ldn.cdf(39) = "
				+ lnd.cdf(39.0));

		try {
			FileWriter fw = new FileWriter("C:/lnd.csv");
			fw.write("From Hour,To Hour,P\n");
			for (double i = 1; i < 1000; i++) {
				fw.write((i - 1) + "," + i + ","
						+ (lnd.cdf(i) - lnd.cdf(i - 1)) + "\n");
			}
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*
		 * double sum = 0; for (double x = 1; x < 50; x++) { sum += lnd.pdf(x);
		 * System.out.println(x + "," + lnd.pdf(x) + "," + sum); }
		 * 
		 * 
		 * 
		 * Random rand = new Random(0); System.out.println("sum = " + sum);
		 * 
		 * System.out.println("AUC from 0 to 5: " + lnd.cdf(5.0));
		 * System.out.println("AUC from 0 to 6: " + lnd.cdf(6.0));
		 * System.out.println("AUC from 0 to 7: " + lnd.cdf(7.0));
		 * System.out.println("AUC from 0 to 8: " + lnd.cdf(8.0));
		 * System.out.println("AUC from 0 to 10: " + lnd.cdf(10.0));
		 * System.out.println("AUC from 0 to 11: " + lnd.cdf(11.0));
		 * System.out.println("AUC from 0 to 14: " + lnd.cdf(14.0));
		 * System.out.println("AUC from 0 to 15: " + lnd.cdf(15.0));
		 * 
		 * System.out.println("AUC from 1 to 10: " + lnd.cdf_range(1, 10));
		 */
		/*
		 * double auc_t1 = StandardNormalDistribution.computeAUC(-99,
		 * (Math.log(1)-mean)/sigma); System.out.println("auc up to 1: " +
		 * auc_t1); double auc_t2 = StandardNormalDistribution.computeAUC(-99,
		 * (Math.log(2)-mean)/sigma); System.out.println("auc up to 2: " +
		 * auc_t2); auc_t2 -= auc_t1; double auc_t3 =
		 * StandardNormalDistribution.computeAUC(-99, (Math.log(3)-mean)/sigma);
		 * System.out.println("auc up to 3: " + auc_t3); auc_t3 -=
		 * (auc_t1+auc_t2); double auc_t4 =
		 * StandardNormalDistribution.computeAUC(-99, (Math.log(4)-mean)/sigma);
		 * System.out.println("auc up to 4: " + auc_t4); auc_t4 -=
		 * (auc_t1+auc_t2+auc_t3); double auc_t5 =
		 * StandardNormalDistribution.computeAUC(-99, (Math.log(5)-mean)/sigma);
		 * auc_t5 -= (auc_t1+auc_t2+auc_t3+auc_t4); System.out.println("auc up
		 * to 5: " + auc_t5);
		 * 
		 * System.out.println("AUC between 1 and 2: " + auc_t2);
		 * System.out.println("AUC between 2 and 3: " + auc_t3);
		 * System.out.println("AUC between 3 and 4: " + auc_t4);
		 * System.out.println("AUC between 4 and 5: " + auc_t5);
		 * //System.out.println("AUC between 2 and 3: " + auc_t3);
		 */
		/*
		 * LogNormalDist lnd1 = new LogNormalDist(0, 0.22);// 0.55677644); int
		 * countLessOne = 0, countLessOneHalf = 0, countLessTwo = 0; for (int i
		 * = 0; i < 100000; i++) { double temp = (Double)
		 * (lnd1.generateVariate(rand)); if (temp < 2.0) { countLessTwo++; if
		 * (temp < 1.0) { countLessOne++; if (temp < 0.5) { countLessOneHalf++;
		 * } } } } System.out.println("Samples under 2.0: " + countLessTwo);
		 * System.out.println("Samples under 1.0: " + countLessOne);
		 * System.out.println("Samples under 0.5: " + countLessOneHalf);
		 * 
		 * sigma = 0.55677644; double mu1 = 1.4; double mu2 = 2.4; double mu3 =
		 * 2.7; LogNormalDist d1 = new LogNormalDist(mu1, sigma); LogNormalDist
		 * d2 = new LogNormalDist(mu2, sigma); LogNormalDist d3 = new
		 * LogNormalDist(mu3, sigma); for (int i = 0; i < 500; i++) { double x =
		 * (double) i / 24D; double y = d1.pdf(x) + 2 d2.pdf(x) + 4 d3.pdf(x);
		 * System.out.println(x + "\t" + y); }
		 * 
		 * System.out.println(d1.getExpectation() + "\t" + d1.getMedian() + "\t"
		 * + d1.getVariance()); System.out.println(d2.getExpectation() + "\t" +
		 * d2.getMedian() + "\t" + d2.getVariance());
		 * System.out.println(d3.getExpectation() + "\t" + d3.getMedian() + "\t"
		 * + d3.getVariance());
		 * 
		 * LogNormalDist lnd_1 = new LogNormalDist(2.7, sigma); for (int i = 0;
		 * i < 144; i++) { int tom = i + 24; double auc = lnd_1.cdf((double) tom
		 * / 24D) - lnd_1.cdf((double) i / 24D); System.out.println(i + "\t" +
		 * tom + "\t" + auc); }
		 * 
		 * LogNormalDist lnd_2 = new LogNormalDist(1.4, sigma);
		 * System.out.println(lnd_2.getExpectation() + "\t" +
		 * lnd_2.getMedian()); System.out.println(lnd_2.cdf(2.52));
		 * 
		 * LogNormalDist lnd_3 = new LogNormalDist(2.7, 0.556);
		 * System.out.println(lnd_3.cdf(5.0));
		 * System.out.println(LogNormalDist.cdf(2.7, 0.556, 5.0));
		 * System.out.println(LogNormalDist.cdflnx(2.7, 0.556, Math.log(5.0)));
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.pitt.rods.detection.utilities.distributions.DistributionStructure
	 * #generateVariate()
	 */
	public Double generateVariate(Random rand) {
		double x = _snd.generateVariate(rand);
		return Math.exp(x * _sigma + _mu);
	}

	public double cdf(Double x) {
		System.out.println("Dont call this, it's not implemented!");
		System.exit(-1);

		return 0;
	}
}
