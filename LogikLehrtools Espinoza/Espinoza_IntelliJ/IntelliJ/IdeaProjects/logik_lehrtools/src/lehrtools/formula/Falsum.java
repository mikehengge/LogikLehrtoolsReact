package lehrtools.formula;

import java.util.Map;

import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;

/**
 * Class for the constant false.
 */
public class Falsum extends Formula {

	private static final Falsum instance = new Falsum();

	/**
	 * Private constructor for singleton pattern.
	 */
	private Falsum() {
	}

	/**
	 * Returns the single instance of the Falsum class.
	 * 
	 * @return Falsum object
	 */
	public static Falsum mk() {
		return instance;
	}

	@Override
	public boolean syntEqual(Formula f) {
		if (f == this)
			return true;
		else 
			return false;
		
	}

	@Override
	public String toString() {
		return "0";
	}

	@Override
	public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException {
		return false;
	}

	@Override
	public Formula substitute(Variable var, Formula form) {
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
	public Formula_NF dnf_set_base_representation() {
		
		return new Formula_NF(new Clause());
	}

	
}
