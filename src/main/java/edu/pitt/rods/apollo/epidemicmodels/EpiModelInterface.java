package edu.pitt.rods.apollo.epidemicmodels;

/**
 * Interface to all EpiModels. At their core, and EpiModel can be described,
 * run, and queried.
 * 
 * @author John Levander
 */
public interface EpiModelInterface {

	public String getResults();

	public String getName();

	public String getAuthor();

	public String getOrganization();

	public String getDescription();

	public void run();
}
