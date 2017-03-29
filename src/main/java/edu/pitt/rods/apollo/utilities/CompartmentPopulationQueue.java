package edu.pitt.rods.apollo.utilities;

import org.apache.commons.math.util.ResizableDoubleArray;

/*
 * We are going to add to the behavior of the linked list, but
 * we don't want to allow the user the freedom to use all of the
 * functions available to a linked list...so we hide the linked 
 * list from them..
 */
public class CompartmentPopulationQueue {

	private static final long serialVersionUID = 3230899784723304067L;

	private ResizableDoubleArray internalPopulationArray = new ResizableDoubleArray();

	public int getSize() {
		// return queue.size();
		return internalPopulationArray.getNumElements();
	}

	public double getElementAt(int i) {
		// return queue.get(i);
		return internalPopulationArray.getElement(i);
	}

	public CompartmentPopulationQueue copy() {
		CompartmentPopulationQueue c = new CompartmentPopulationQueue();
		// c.queue = (ArrayList<Double>) queue.clone();
		ResizableDoubleArray rda = internalPopulationArray.copy();
		c.internalPopulationArray = rda;
		return c;
	}

	public void addElement(double n) {
		// queue.addFirst(n);
		// queue.add(0, n);
		internalPopulationArray.getInternalValues();
		ResizableDoubleArray rda = new ResizableDoubleArray(
				internalPopulationArray.getNumElements() + 1);
		rda.setElement(0, n);
		for (int i = 0; i < internalPopulationArray.getNumElements(); i++) {
			rda.setElement(i + 1, internalPopulationArray.getElement(i));
		}
		internalPopulationArray = rda;
	}

	public void setFirstElement(double n) {
		// queue.set(0, n);
		internalPopulationArray.setElement(0, n);
	}

	public void addToFirstElement(double n) {
		// queue.set(0, queue.get(0) + n);
		internalPopulationArray.setElement(0,
				internalPopulationArray.getElement(0) + n);
	}

	public void addToElementAt(int i, double numToAdd) {
		double element = internalPopulationArray.getElement(i);
		if (element == 0.0)
			internalPopulationArray.setElement(i, numToAdd);
		else
			internalPopulationArray.setElement(i, element + numToAdd);

		if (internalPopulationArray.getElement(i) < -1.0)
			System.out
					.println("ywah. " + internalPopulationArray.getElement(i));
//		if (internalPopulationArray.getElement(i) < 0.000000001)
//			internalPopulationArray.setElement(i, 0.0);
	}

	public void subtractFromElementAt(int i, double numToSubtract) {
		// double value = queue.get(i) - numToSubtract;
		internalPopulationArray.setElement(i,
				internalPopulationArray.getElement(i) - numToSubtract);

		if (internalPopulationArray.getElement(i) < -1.0)
			System.out
					.println("ywah. " + internalPopulationArray.getElement(i));
		if (internalPopulationArray.getElement(i) < 0.0000000000001)
			internalPopulationArray.setElement(i, 0.0);
		// queue.set(i, value);
	}

	public CompartmentPopulationQueue(double population) {
		super();

		// experimenting here...there needs to be an element AFTER
		// the current hour to specify what happens in the future
		addElement(0.0);
		addElement(population);
	}

	public CompartmentPopulationQueue() {
		super();

	}
}
