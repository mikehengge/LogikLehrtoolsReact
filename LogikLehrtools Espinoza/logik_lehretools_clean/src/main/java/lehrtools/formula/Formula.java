package lehrtools.formula;

import java.util.Map;

import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;
import lehrtools.model.Literal;

/**
 * Abstract class for formulas of propositional logic.
 */
public abstract class Formula {
	public static final Formula VERUM = Verum.mk();
	public static final Formula FALSUM = Falsum.mk();

	/**
	 * Formula as a String.
	 * 
	 * @return a string representation of this formula
	 */
	abstract public String toString();

	/**
	 * Test if two formulas are syntactically equal. Note: This is not a test
	 * for semantically equality.
	 * 
	 * @param f
	 *            formula to compare with
	 * @return true if the given formula is syntactically equal to this formula,
	 *         false otherwise
	 */
	abstract public boolean syntEqual(Formula f);

	/**
	 * Evaluate a Formula with the given variable assignment.
	 * 
	 * @param assignment
	 *            mapping between variables and truth values
	 * @return true or false
	 * @throws IllegalArgumentException
	 *             if a variable was not assigned
	 */
	abstract public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException;

	/**
	 * Substitute a variable with a formula.
	 * 
	 * @param var
	 *            variable to substitute
	 * @param form
	 *            formula to place in
	 * @return substituted formula
	 */
	abstract public Formula substitute(Variable var, Formula form);

	/**
	 * Test if the formula is a literal. A literal is an atomic formula or the
	 * negation of an atomic formula.
	 * 
	 * @return true if formula is a literal, false otherwise
	 */
	abstract public boolean isLiteral();

	/**
	 * Test if the formula is an atomic formula. An atomic formula is a
	 * variable, verum or bottom.
	 * 
	 * @return true if formula is atomic, false otherwise
	 */
	abstract public boolean isAtomicFormula();

	/**
	 * Test if the formula is a clause.
	 * 
	 * @return true if formula is a clause, false otherwise
	 */
	abstract public boolean isClause();

	/**
	 * Test if the formula is a minterm.
	 * 
	 * @return true if formula is a minterm, false otherwise
	 */
	abstract public boolean isMinterm();

	/**
	 * Test if the formula is in NNF (Negation Normal Form).
	 * 
	 * @return true if formula is in NNF, false otherwise
	 */
	abstract public boolean isNNF();

	/**
	 * Test if the formula is in CNF (Conjunctive Normal Form).
	 * 
	 * @return true if formula is in CNF, false otherwise
	 */
	abstract public boolean isCNF();

	/**
	 * Test if the formula is in DNF (Disjunctive Normal Form).
	 * 
	 * @return true if formula is in DNF, false otherwise
	 */
	abstract public boolean isDNF();

	/**
	 * Compute an equivalent formula in NNF.
	 * 
	 * @return equivalent formula in NNF
	 */
	abstract public Formula nnf();

	/**
	 * Compute an equivalent formula in CNF.
	 * 
	 * @return equivalent formula in CNF
	 */
	abstract public Formula cnf();

	/**
	 * Compute an equivalent formula in DNF.
	 * 
	 * @return equivalent formula in DNF
	 */
	abstract public Formula dnf();

	/**
	 * Simplify the formula
	 * 
	 * @return the simplified formula
	 */
	abstract public Formula simplify();
	/**
	 * Converts this formula into a set based representation 
	 * more suitable for the resolution operations.
	 * 
	 * @return : instance of Formula_NF representing the Formula.
	 * 
	 */
	abstract public Formula_NF dnf_set_base_representation();
	
	/**
	 * Converts this formula into a set based representation 
	 * more suitable for the Resolution, Backward Dual Resolution and DP60 operations.
	 *
	 * @return : instance of Formula_NF representing the Formula.
	 * 
	 */
	 public Formula_NF cnf_set_base_representation(){
	 		Formula neg_form = new Not(this);
			Formula dnf_form = neg_form.dnf();
			Formula_NF dnf_set_base = dnf_form.dnf_set_base_representation();
			Formula_NF cnf_set_base = new Formula_NF();
			for(Clause clause : dnf_set_base.get_clauses())
			{
				if(clause.isDegenarate()) continue;
				Clause cnf_clause = new Clause();
				for(Literal literal : clause.getLiterals())
					cnf_clause.addLiteral(literal.negate());
				cnf_set_base.add_clause(cnf_clause);
			}
			return cnf_set_base;
			
		}

	/**
	 * Apply De Morgan laws on the formula. Note: This function will have an
	 * effect if and only if the formula has the form (NOT (s1 OR s2)) or (NOT
	 * (s1 AND s2)). For every other form, an IllegalArgumentException will be
	 * thrown.
	 * 
	 * @param not
	 *            The Formula on which to apply the De Morgan rule
	 * @return the formula with De Morgan applied
	 */
	public static Formula applyDeMorgan(Not not)
			throws IllegalArgumentException {
		Formula argument = not.getF();
		if(!(argument instanceof BinaryFormula))
			throw new IllegalArgumentException();
		BinaryFormula casted_argument = (BinaryFormula) argument;
		Not new_f1 = new Not(casted_argument.getF1());
		Not new_f2 = new Not(casted_argument.getF2());
		if(casted_argument instanceof Or)
			return new And(new_f1, new_f2);
		else
			return new Or(new_f1, new_f2);
			
	}

	@Override
	public boolean equals(Object o) {
		if (o == null)
			return false;
		if (o == this)
			return true;
		if (!(o instanceof Formula))
			return false;
		return ((Formula) o).syntEqual(this);
	}
}
