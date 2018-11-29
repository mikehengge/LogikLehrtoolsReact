package lehrtools.model;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import lehrtools.formula.Variable;
import lehrtools.model.state.S_Calculation;
import lehrtools.model.state.S_Res_Calculation;
import lehrtools.model.state.S_Sub_Calculation;
/**
 * This class provides the basic functionality to do the
 * different calculations needed by the implemented procedures.
 *
 */
public class ResolutionUtility {

	/**
	 * Calculates all possible resolvents of a given Formula_NF instance over a variable .
	 * For the Resolution and Backward Dual Resolution procedure the formula
	 * the resolvents are formed over is of the form: Res(F)^i = Res(F)^i-1 U R^i-1.
	 * The resolvent are calculated in function of Res(F)^i-1 and R^i-1.
	 *
	 * The S_Res_Calculations instances, that represent the derivation
	 * of a resolvent,  are stored in a LinkedList.
	 * @param r0 : Formula_NF instance representing Res(F)^i.
	 * @param r1 :   Formula_NF instance representing R^i-1.
	 * @param a  : Variable voer whicht the resolvents are derived.
	 * @param steps : List used to Store every Resolution step.
	 * @return : A new Formula containing all the resolvents formed over the given Formula.
	 */
	public static Formula_NF resolution_over(Formula_NF r0 , Formula_NF r1, Variable a, LinkedList<S_Calculation> steps)
	{
		Formula_NF resolvents = resolution_over(r0,r1,new Literal(a,true), steps);
		if(r0.equals(r1))
				return resolvents;
		else
			return resolvents.union_with(resolution_over(r0,r1,new Literal(a,false), steps));

	}
	/**
	 * Calculates all possible resolvents of a given Formula_NF instance.
	 * For the Resolution and Backward Dual Resolution procedure the formula
	 * the resolvents are formed over is of the form: Res(F)^i = Res(F)^i-1 U R^i-1.
	 * The resolvent are calculated in function of Res(F)^i-1 and R^i-1.
	 * Done by iterating over all Variables and calling the method
	 * resolution_over(...).
	 * The S_Res_Calculations instances, that represent the derivation
	 * of a resolvent,  are stored in a LinkedList.
	 * @param r0 : Formula_NF instance representing Res(F)^i.
	 * @param r1 :   Formula_NF instance representing R^i-1.
	 * @param steps : List used to Store every Resolution step.
	 * @return : A new Formula containing all the resolvents formed over the given Formula.
	 */
	public static Formula_NF resolution(Formula_NF r0, Formula_NF r1, LinkedList<S_Calculation> steps)
	{
		Formula_NF resolvents = new Formula_NF();
		
		for(Variable var : r0.vars())
			resolvents.add_all(resolution_over(r0, r1, var,steps).get_clauses());
		
		return resolvents;
	}
	//Backward Sub, clauses in F are absorbed
	/**
	 * Removes all clauses from the formula F that are subsumed by 
	 * clauses of the formula R. The remaining clauses are returned as
	 * a new formula.
	 * Every subsumption will be stored in a LinkedList .
	 * @param F : formula that will have its clauses subsumed.
	 * @param R : formula which clauses will subsume.
	 * @param steps : List used to Store every subsumption step.
	 * @return : A new formula containing the clauses that haven't been subsumed.
	 */
	public static Formula_NF subsumption(Formula_NF F , Formula_NF R , LinkedList<S_Calculation> steps)
	{
		Formula_NF subsumption = new Formula_NF();
		for(Clause clause : F.get_clauses() )
			if(!contains(clause,R)) 
				subsumption.add_clause(new Clause(clause));
			else
				steps.add(new S_Sub_Calculation(new Clause(clause),contained(clause,R) ) );
		return subsumption;
	}

	/**
	 * Helper Class for the resolution_over(...) method.
	 * Calculates the resolvents derived from r0 and r1.
	 * a resolvent is derived from a pair fo clashing clauses C0 and C1 over the given
	 * Literal literal, where C0 is a clause from r0 and C1 is a clause from r1.
	 * @param r0 Formula_NF instance used to derive new resolvents.
	 * @param r1 Formula_NF instance used to derive new resolvents.
	 * @param literal Literal used to determine teh clashing clauses.
	 * @param steps List used to Store every Resolution step.
	 * @return
	 */
	private static Formula_NF resolution_over (Formula_NF r0 , Formula_NF r1, Literal literal, LinkedList<S_Calculation> steps)
	{
		Set<Clause> r0_clauses = new HashSet<Clause>();
		Set<Clause> r1_clauses = new HashSet<Clause>();
		Set<Clause> resolvents = new HashSet<Clause>();

		for(Clause clause : r0.get_clauses())
			if(clause.contains(literal))
				r0_clauses.add(clause);
		for(Clause clause : r1.get_clauses())
			if(clause.contains(literal.negate()))
				r1_clauses.add(clause);

		//noinspection Duplicates
		for(Clause r0_clause: r0_clauses)
			for(Clause r1_clause : r1_clauses)
			{
				Clause resolvent = resolution(  new Clause(r0_clause),
						new Clause(r1_clause),
						literal);
				resolvent.parents(r0_clause, r1_clause);
				if(!verum(resolvent))
				{
					resolvents.add(resolvent);

				}

				steps.add(new S_Res_Calculation(resolvent, r0_clause, r1_clause));
			}

		return new Formula_NF(resolvents);
	}
	/**
	 * Applies the resolution rule to the given clauses. A requirement is that
	 * both clauses are clashing clauses over complementary literals. The complementary 
	 * literal has to be given too.
	 * @param clause1 : first argument for the resolution rule.
	 * @param clause2 : second argument for the resolution rule.
	 * @param literal : literal which clause1 and clause2 clash over
	 * @return :a clause representing the clause_1 of clause1 and clause2.
	 */
	private static Clause resolution(Clause clause1 , Clause clause2, Literal literal)
	{
		clause1.removeLiteral(literal);
		clause2.removeLiteral(literal.negate());
		clause1.unionWith(clause2);
		return clause1;
		
	}
	/**
	 * Test if a clause is a subset of another clause.
	 * @param is_contained : Clause to be tested as a superset of another clause.
	 * @param container : Clause to be tested as a subset of another clause.
	 * @return : true if the test was successful, false otherwise.
	 */
	private static boolean is_contained(Clause is_contained , Clause container)
	{
		for(Literal literal : is_contained.getLiterals())
			if(!container.contains(literal)) return false;
		return true;
	}
	/**
	 * Tests if a formula has a clause that contains a given clause.
	 * @param clause : Clause for which subsets will be searched for.
	 * @param formula : Formula containing clauses to be tested as subsets of the given clause.
	 * @return : true if a clause is found, otherwise false.
	 */
	private static boolean contains(Clause clause, Formula_NF formula)
	{
		for(Clause subset: formula.get_clauses())
			if(is_contained(subset, clause)) return true;
		
		return false;
	}
	/**
	 * Finds a clause in a given formula that is a subset of a the given clause.
	 * 
	 * @param clause : Clause for which subsets will be searched for.
	 * @param formula : Formula containing clauses to be tested as subsets of the given clause.
	 * @return : a clause contained in the given clause.
	 */
	private static Clause contained(Clause clause, Formula_NF formula)
	{
		for(Clause subset: formula.get_clauses())
			if(is_contained(subset, clause)) return new Clause(subset);
		
		return null;
	}
	/**
	 * Test if a clause contains complementary literals.
	 * @param  clause : clause to be tested for complementary literals.
	 * @return  true if complementary literals are found, false otherwise.
	 */
	private static boolean verum(Clause clause)
	{
		for(Literal lit : clause.getLiterals())
			if(clause.contains(lit.negate())) return true;
		return false;
	}
}
