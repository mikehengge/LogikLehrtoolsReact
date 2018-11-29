package lehrtools.model;
import lehrtools.formula.Variable;
import lehrtools.model.state.S_Calculation;
import lehrtools.model.state.S_DP_Calculation;
import lehrtools.model.state.S_Res_Calculation;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Class that provides the basic functionality for the DP60 algorithm.
 * All methods are static so this class doesn't need to be instantiated.
 * For  unit propagation and varaible elimination
 */
public class DP60_Utility {


    /**
     * Applies  the first rule(unit propagation) to a propositional formula in set based representation
     * for a given unit clause.
     * @param formula propositional formula in set based representation
     * @param unit_clause unit clause contained in the propositional formula
     * @return : A new formula without the given literal.
     */
    public static Formula_NF rule_01(Formula_NF formula, Clause unit_clause, LinkedList<S_DP_Calculation> calculations) {
        Literal literal = unit_clause.getUnitLit();
        Formula_NF reduced_formula = new Formula_NF();
        for (Clause clause : formula.get_clauses())
        {
            if (!clause.contains(literal))
            {
                if (clause.contains(literal.negate()))
                {
                    Clause reduced_clause = new Clause(clause);
                    reduced_clause.removeLiteral(literal.negate());
                    reduced_formula.add_clause(reduced_clause);
                    calculations.add(new S_DP_Calculation(clause, unit_clause, reduced_clause,"Rule 01 Variable", literal));
                }
                else
                    reduced_formula.add_clause(new Clause(clause));
            }
            else
                calculations.add(new S_DP_Calculation(clause, unit_clause, new Clause(),"Rule 01 Clause", literal));

        }
        return reduced_formula;
    }

    /**
     * Method that applies the second rule (affirmative-negative rule/ pure literal rule) to a
     * propositional formula in set based representation for a given pure literal.
     * @param formula propositional formula in set based representation.
     * @param pure_literal a pure literal contained in formula.
     * @return a formula where all clauses containing the pure literal have been removed.
     */
    public static Formula_NF rule_02(Formula_NF formula, Literal pure_literal, LinkedList<S_DP_Calculation> calculations)
    {
        Formula_NF reduced_formula = new Formula_NF();
        for(Clause clause: formula.get_clauses())
            if(!clause.contains(pure_literal))
                reduced_formula.add_clause(new Clause(clause));
            else
                calculations.add( new S_DP_Calculation(clause,new Clause(),new Clause(),"Rule 02", pure_literal));

        return reduced_formula;
    }

    /**
     * Method tha applies the third rule (variable elimination) to a propositional
     * formula in set based representation a given literal.
     * If the literal corresponds to a unit clause then this method will be the equivalent to
     * applying the rule 01 (unit propagation)
     * This is done by applying resolution over the variable that is to be eliminated,  then
     * adding the resolvents to the formula and removing the parents.
     * @param formula : propositional formula in set based representation.
     * @param variable : variable to be eliminated.
     * @return formula without the given variable
     */
    //TODO : what if there are repeated resolvents
    //TODO : review whole algorithm again
    public static Formula_NF rule_03(Formula_NF formula, Variable variable, LinkedList<S_Calculation> calculations )
    {
        Formula_NF resolvents =  ResolutionUtility.resolution_over(formula, formula, variable, calculations);
        Set<Clause> parents = get_parents(calculations);
        Formula_NF reduced_formula = new Formula_NF();

        for(Clause clause : formula.get_clauses())
            if(!parents.contains(clause))
                reduced_formula.add_clause(clause);

        return union(reduced_formula , resolvents);

    }


    /**
     * Method that retrieves all the literals
     * in a propositional formula in set based representation.
     * @param formula : propositional formula in set based representation.
     * @return : The Set of all Literals ina formula.
     */
    public static Set<Literal> get_literals(Formula_NF formula)
    {
        HashSet<Literal> literals = new HashSet<>();
        for(Clause clause :formula.get_clauses())
            literals.addAll(clause.getLiterals());

        return literals;
    }

    /**
     * Method that finds all pure literals in a propositional formula in set based representation.
     *
     * @param formula propositional formula in set based representation.
     * @param pure_literals Set of literals where all pure literals will be stored.
     *                      An empty set should be pased.
     * @return : returns true if a pure literal was found and false if no pure literal as found.
     */
    public static boolean get_pure_literal(Formula_NF formula, Set<Literal> pure_literals)
    {
        Set<Literal> literals = get_literals(formula);
        Set<Variable> variables = formula.vars();
        for(Variable variable : variables)
        {
            Literal positive_literal = new Literal(variable,true);
            Literal negative_literal = new Literal(variable,false);
            if(  literals.contains(positive_literal) && !literals.contains(negative_literal)  )
                pure_literals.add(positive_literal);
            else if(  !literals.contains(positive_literal) && literals.contains(negative_literal)  )
                pure_literals.add(negative_literal);

        }
        return !pure_literals.isEmpty();
    }

    /**
     * Tests if a propositional formula in set based representation contains a clause using the equals method
     * for Sets of Literals.
     * @param formula propositional formula in set based representation
     * @param clause clause to be tested
     * @return true if clause is contained or else false.
     */
    public static boolean is_contained(Formula_NF formula, Clause clause)
    {
        for(Clause f_clause : formula.get_clauses())
            if(clause.getLiterals().equals(f_clause.getLiterals()))
                return true;
        return false;
    }


    /**
     * Helper method, that gets all the parents from a list of resolution
     * calculations.
     * @param calculations : a list with all the calculations made by the resolution algorithm.
     * @return list of all clauses the resolution was applied to.
     */
    private static Set<Clause> get_parents(LinkedList<S_Calculation> calculations)
    {
        HashSet<Clause> parents = new HashSet<>();
        for(S_Calculation calculation : calculations)
        {
            parents.add( ((S_Res_Calculation)calculation).parent_1);
            parents.add( ((S_Res_Calculation)calculation).parent_2);
        }

        return parents;
    }

    /**
     * Creates union of two propositional formulas in set based representation, considering
     * two clauses as equals if they contain the same literals ,ignoring the parents.
     * For two clauses that are equal the one from reduced_formula will be aded to the result.
     * @param reduced_formula  propositional formula in set based representation.
     * @param resolvents propositional formula in set based representation.
     * @return formula containing the clauses of reduced_formula and resolvents.
     */
    private static Formula_NF union(Formula_NF reduced_formula, Formula_NF resolvents)
    {
        for(Clause clause : resolvents.get_clauses())
            if(!is_contained(reduced_formula,clause))
                reduced_formula.add_clause(clause);
        return reduced_formula;
    }

}
