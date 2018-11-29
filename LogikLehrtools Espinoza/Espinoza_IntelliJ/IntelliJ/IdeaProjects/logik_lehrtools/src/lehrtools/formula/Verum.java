package lehrtools.formula;

import java.util.Map;

import lehrtools.model.Formula_NF;

/**
 * Class for the constant true.
 */
public class Verum extends Formula {

	private static final Verum instance = new Verum();

	/**
	 * Private constructor for singleton pattern.
	 */
	private Verum() {
	}

	/**
	 * Returns the single instance of the Verum class.
	 * 
	 * @return Verum object
	 */
	public static Verum mk() {
		return instance;
	}

	@Override
	public boolean syntEqual(Formula f) {
        return f == this;
	}

	@Override
	public String toString() {
		return "1";
	}

	@Override
	public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException {
		return true;
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
		return new Formula_NF();
	}

	
}