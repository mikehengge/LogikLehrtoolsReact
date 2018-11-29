package lehrtools.formula;

import java.util.Map;

import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;
import lehrtools.model.Literal;

/**
 * Class for a NOT-formula.
 */
public class Not extends Formula {
	private Formula f;

	/**
	 * Returns the operand of this Not formula.
	 * 
	 * @return operand of this Not formula
	 */
	public Formula getF() {
		return f;
	}

	/**
	 * Constructor for NOT-formula.
	 * 
	 * @param f
	 *            f of the not formula
	 */
	public Not(Formula f) {
		this.f = f;
	}

	@Override
	public boolean syntEqual(Formula form) {
		if (form == null)
			return false;
		if (form == this)
			return true;
		if (!(form instanceof Not))
			return false;
		return this.f.syntEqual( ((Not) form).getF() );
	}

	@Override
	public String toString() {
		return "(" + "NOT " + f + ")";
	}

	@Override
	public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException {
		return !f.evaluate(assignment);
	}

	@Override
	public Formula substitute(Variable var, Formula form) {
		
		return new Not(f.substitute(var, form));
	}

	@Override
	public boolean isDNF() {
		return f.isAtomicFormula();
	}

	@Override
	public boolean isMinterm() {
		return f.isAtomicFormula();
	}

	@Override
	public boolean isLiteral() {
		return f.isAtomicFormula();
	}

	@Override
	public boolean isAtomicFormula() {
		return false;
	}

	@Override
	public boolean isCNF() {
		return f.isLiteral();
	}

	@Override
	public boolean isClause() {
		return f.isLiteral();
	}

	@Override
	public boolean isNNF() {
		return f.isAtomicFormula();
	}

	@Override
	public Formula dnf() {
		if(f.isAtomicFormula()) return this;
		Formula nnf_formula = this.nnf();
		Formula dnf_formula = nnf_formula.dnf();
		return  dnf_formula;
				
	}

	@Override
	public Formula nnf() {
		if(f instanceof BinaryFormula)
		{
			BinaryFormula binary_f = (BinaryFormula) Formula.applyDeMorgan(this);
			return binary_f.nnf().simplify();
		}
		else
		{
			if(f instanceof Not)
				return ((Not) f).getF().nnf().simplify();
			else
				return this.simplify();
		}
	}

	@Override
	public Formula cnf() {
		if(f.isLiteral()) return this;
		Formula cnf_formula = f.dnf();
		cnf_formula = new Not(cnf_formula);
		return cnf_formula.nnf();
	}

	@Override
	public Formula simplify() {
		Formula reduced_formula = f.simplify();
		if(reduced_formula instanceof Not ) return ((Not) reduced_formula).getF();
		if(reduced_formula instanceof Verum) return Falsum.mk();
		if(reduced_formula instanceof Falsum) return  Verum.mk();
		return new Not( reduced_formula);
	}

	@Override
	public Formula_NF dnf_set_base_representation() {
		if(this.isDNF())
		{
			Literal lit = new Literal((Variable) f, false);
			return new Formula_NF(new Clause(lit));
		}
		else 
		{
			return this.dnf().dnf_set_base_representation();
		}
	}

	
}