package edu.pitt.rods.apollo.statetransitionnetwork;

import java.util.ArrayList;

import edu.pitt.rods.apollo.epidemicmodels.compartmental.results.CompartmentModelHourlyResult;

/* wat dis bugga? 
 * 
 * represents transition  functions in an augmented state transition network
 * 
 * Ideally, the methods should be able to express at least the following functional forms
 * 
 *  f() = constant   e.g.,  f() = 0.5 would mean that half of the population in state zero goes to state 1
 *  f(0 = probability distribution like a normal distribution around 0.5
 *  f(t) = the value or distribution is a function of time
 *  f(t, X)  where X is a vector of non temporal parameters that also influence the transition.  For example, capacity
 * 
 * the from and toNodeIDs are integers that refer to the position of the node in an ArrayList.
 *
 *
 * Some hard coded functions:
 * 
 * Node 0->1  0.0
 * Node 1->2  If t=0 and no_vax then 0.5;  if t = 0 and yes_vax then 0.5 * (1-efficacy * compliance); else 0
 * Node 2->3  f(t)
 * Node 3->4 f'(t)
 * 
 * Node 5->6   0.0
 * Node 6->7   0.0
 * Node 7->8   f(t) * .2
 * Node 8->9   f'(t) *.45
 * Node 8->10  unclear from paper at what rate they recover.  paper only gives rate that they become fulminant
 * Node 9->10
 * Node 9->11
 * 
 * Transition from unprophylaxed to prophylaxed based on detection time and finite capacity for prophylaxis
 * based on assumption of ignorance about status of person (could be unexposed, exposed, infected/asx, infected/prodromal?
 * So the prophylaxis available for the hour is distributed based on random selection (e.g., N_in_exposed_bin / SUM exp_bin,unexp, infected/asx, infected/prodr.)
 * stated as a function, it is f''(t, Node0(t), Node1(t), Node2(t), capacity(t) )
 * Node 0->5   if t>detection_time and ESR then  1/24  100,000 * 10/11
 * Node 1->6   if t>detection_time and ESR then  1/24  100,000 * 1/11* 1/2
 * Node 2->7   if t<detection_time then 0; else 
 * 
 * what the functions in general need to be called with or be able to access:
 * time, 
 * efficacy parameter
 * 
 * After talking to Mike, more info:
 *  
 * Note: only horizontal moves from bin to bin are functions, vertical moves are rates
 * Three 
 * 1.  efficacy (incl. habituation)
 * 2.  Treatment capacity
 * 3.  
 * 
 * 
 * Rates:
 * Braithwaite
 * 
 * 
 * Untreated->Treated
 * 4,166.67 per hour w/ESR
 * 416.67 per hour wo/ESR
 * 
 * 
 * From 2001 Anthrax:
 * Infected, asymptomatic->Infected, prodromal (6.2 days)
 * Infected, prodromal->Infected, fluminant (3.0 days)
 * Infected, fulminant->Death (2.3 days)
 * 
 * In 2001 attacks, observed efficacy was 55% for prodromal individuals, as 5 of 11 treated died
 * In paper, efficacy of prophylaxis for "Infected, asymptomatic" estimated to be 80%, varied from 
 *      55% to 100% in sensitivity analysis.
 * 
 * Assumed 44% (40%-47% SA) of people were adherent with postexposure prophylaxis after a 100 person exposure
 * Assumed 90% (80%-100% SA) of people were adherent with postexposure prophylaxis after a 100,000 person exposure
 * 
 * 
 * 
 * 
 *  
 * 

 */

public interface StateTransitionFunction {

	double getP(int t);

	double process(int t, CompartmentModelHourlyResult result);

	IStateNode getToNode();

	IStateNode getFromNode();

	public MovementType getMovementType();

	boolean hasAttribute(String key);

	void addAttribute(String key, String value);

	String getAttribute(String key);

	public ArrayList<Double> getParameters();

}
