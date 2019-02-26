package lehrtools.formula;

import java.util.Map;

import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;
import lehrtools.model.Literal;

/**
 * Class for a variable-formula.
 */
public class Variable extends Formula implements Comparable<Variable> {
	private String name;

	/**
	 * Returns the name of this variable.
	 * 
	 * @return name of the variable
	 */
	public String getName() {
		return name;
	}

	/**
	 * Constructor for a Variable.
	 * 
	 * @param name
	 *            name of the variable
	 */
	public Variable(String name) {
		this.name = name;
	}
	
	/**
	 * Copy Constructor for a Variable.
	 * 
	 * @param variable
	 *            		variable to be copied
	 */
	public Variable(Variable variable)
	{
		name = variable.getName();
	}

	@Override
	public boolean syntEqual(Formula f) {
		if (f == null)
			return false;
		if (f == this)
			return true;
		if (!(f instanceof Variable))
			return false;
		return name.equals( ( (Variable) f).getName());
	}

	@Override
	public String toString() {
		return name;
	}

	@Override
	public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException {
		if (!assignment.containsKey(this))
			throw new IllegalArgumentException("Variable " + this
					+ " was not assigned.");
		return assignment.get(this);
	}

	@Override
	public Formula substitute(Variable var, Formula form) {
		if(this.syntEqual(var)) 
			return form;
		else
			return this;
	}

	@Override
	public boolean isDNF() {
		return true;
	}

	@Override
	public boolean isMinterm() {
		return true;
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public boolean isAtomicFormula() {
		return true;
	}

	@Override
	public boolean isCNF() {
		return true;
	}

	@Override
	public boolean isClause() {
		return true;
	}

	@Override
	public boolean isNNF() {
		return true;
	}

	@Override
	public Formula dnf() {
		return this;
	}

	@Override
	public Formula nnf() {
		return this;
	}

	@Override
	public Formula cnf() {
		return this;
	}

	@Override
	public Formula simplify() {
		return this;
	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public int compareTo(Variable v) {
		return this.getName().compareTo(v.getName());
	}

	@Override
	public Formula_NF dnf_set_base_representation() {
		
		return  new Formula_NF(
				new Clause(
				new Literal(this,true)));
	}

	
}