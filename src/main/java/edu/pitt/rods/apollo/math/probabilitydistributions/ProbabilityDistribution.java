package edu.pitt.rods.apollo.math.probabilitydistributions;

import java.util.Collection;
import java.util.Random;
import java.util.Vector;

/**
 * All the probability distributions should implement this interface. Do not
 * implement this interface directly. Instead, extend the abstract
 * ProbabilityDistributionObject class which implements this interface.
 * <p>
 * The main goal is to have all the distribution classes use the same program
 * structure (abstract parent). As Java 5.0 is being used, it is easy to take
 * advantage of generics and have the type parameter 'T' play an important role
 * in providing design flexibility.
 * 
 * @author mrr
 * @param T
 *            any subclass of Object class
 * 
 */
public interface ProbabilityDistribution<T> {

	/**
	 * The method returns a distribution sample with one or more elements.
	 * <p>
	 * Note: V and T are different
	 * <p>
	 * 
	 * @param <V>
	 *            subclass of Number type
	 * @param n
	 *            can be an argument of any type. Only one distribution supplies
	 *            an Integer type as this parameter. For Other distributions,
	 *            this parameters remain 'null'. In other words, most
	 *            distributions don't use this parameter at all.
	 * 
	 * @return an object of Sample class containing elements of type T.
	 */
	public Collection<T> generateVariates(int n, Random rand);

	/**
	 * Generate just one variate
	 */
	public T generateVariate(Random rand);

	/**
	 * 
	 * @param <T>
	 *            Most likely will be Number or one of its subclasses.
	 * @param x
	 *            The value for which you want the pdf.
	 * @return probability density function value for x.
	 *         <p>
	 *         e.g. Any class that implements this interface should use this
	 *         argument to get a 'y' value for a particular 'x' value.
	 */
	public double pdf(T x);

	/**
	 * 
	 * @param <T>
	 *            Most likely will be Number or one of its subclasses.
	 * @param x
	 *            The value for which you want the pdf.
	 * @return cumulative density function value for x.
	 *         <p>
	 *         e.g. Any class that implements this interface should use this
	 *         argument to get a 'y' value for a particular 'x' value.
	 */
	public double cdf(T x);

	/**
	 * 
	 * @param
	 * @return Expectation of the distribution. Even discrete distributions over
	 *         positive integers (e.g., binomial distribution) have a real
	 *         number for their expectation. Therefore, the return type of this
	 *         method must always be double.
	 * 
	 *         TODO Need to figure out what to do in case expectation is
	 *         undefined.
	 */
	public double getExpectation();

	public Vector<Object> getParamList();

	public String getAbbreviation();
}
