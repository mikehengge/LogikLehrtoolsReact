package lehrtools.model;

import java.util.Objects;

import lehrtools.formula.Variable;
/**
 * Describes a Literal for a set based representation for a propositional formula
 *
 */
public class Literal 
{

	/**
	 * the variable of the Literal
	 */
	private Variable _variable;
	/**
	 * Represents if the variable is negated or not
	 */
	private boolean _phase;
	/**
	 * Contructor for the Variable
	 * @param variable : variable of the literal.
	 * @param phase : phase of the variable.
	 */
	public Literal(Variable variable, boolean phase)
	{
		_variable = variable;
		_phase = phase;
	}
	/**
	 * Copy constructor used to create a deep copy of a Literal
	 * @param literal : literal to be cloned.
	 */
	public Literal(Literal literal)
	{
		_variable = new Variable(literal.variable());
		_phase = literal.phase();
	}
	/**
	 * Getter of the varible
	 * @return : variableof the literal
	 */
	public Variable variable() {return _variable;}
	/**
	 * Getter for the phase
	 * @return : phase of the literal. 
	 */
	public boolean phase() {return _phase;}
	/**
	 * Negates the literal
	 * @return new Literal with an inverse phase.
	 */
	public Literal negate() {return new Literal(_variable, !_phase);}
	
	@Override
	public int hashCode()
	{
		
		return 2*_variable.hashCode() + (_phase ? 1: 0);
	}
	
	@Override
	public boolean equals(Object literal)
	{
		 // self check
	    if (this == literal) return true;
	    // null check
	    if (literal == null) return false;
	    // type check and cast
	    if (getClass() != literal.getClass()) return false;
	    Literal lit = (Literal) literal;
	    // field comparison
	    return Objects.equals(_variable, lit.variable())
	            && Objects.equals(_phase, lit.phase());
	}
	
	@Override
	public String toString() {return (_phase ? ""  : "¬" ) +_variable.toString();}
	
	
}
