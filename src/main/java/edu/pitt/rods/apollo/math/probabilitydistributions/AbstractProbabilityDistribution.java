/**
 * 
 */
package edu.pitt.rods.apollo.math.probabilitydistributions;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

/**
 * Abstract parent for all distributions.
 * 
 * @author mrr
 * 
 */
public abstract class AbstractProbabilityDistribution<T> implements
		ProbabilityDistribution<T>, java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2568099327067289356L;

	public Collection<T> generateVariates(int n, Random rand) {
		ArrayList<T> list = new ArrayList<T>();
		for (int i = 0; i < n; i++) {
			list.add(generateVariate(rand));
		}
		return list;
	}
}
