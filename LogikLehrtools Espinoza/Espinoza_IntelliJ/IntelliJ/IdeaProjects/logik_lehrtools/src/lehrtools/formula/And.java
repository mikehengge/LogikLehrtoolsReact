package lehrtools.formula;

import java.util.ArrayList;
import java.util.Map;

import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;

/**
 * Class for an AND-formula.
 */
public class And extends BinaryFormula {

	/**
	 * Constructor for an AND-formula.
	 * 
	 * @param f1
	 *            left f in infix notation
	 * @param f2
	 *            right f in infix notation
	 */
	public And(Formula f1, Formula f2) {
		super(f1, f2);
	}

	@Override
	public boolean syntEqual(Formula f) {
		if (f == null)
			return false;
		if (f == this)
			return true;
		if (!(f instanceof And))
			return false;
		And formula = (And) f;
        return f1.syntEqual(formula.getF1()) && f2.syntEqual(formula.getF2());
	}

	@Override
	public String toString() {
		return "(" + f1 + " AND " + f2 + ")";
	}

	@Override
	public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException {
		return f1.evaluate(assignment) && f2.evaluate(assignment);
	}

	@Override
	public Formula substitute(Variable var, Formula form) {
		return new And(f1.substitute(var, form), f2.substitute(var, form));
	}

	@Override
	public boolean isDNF() {
		return isMinterm();
	}

	@Override
	public boolean isMinterm() {
		return f1.isMinterm() && f2.isMinterm();
	}

	@Override
	public boolean isCNF() {
		return f1.isCNF() && f2.isCNF();
	}

	@Override
	public boolean isClause() {
		return false;
	}

	@Override
	public boolean isNNF() {
		return f1.isNNF() && f2.isNNF();
	}

	@Override
	public Formula dnf() {
		Formula nnf_f1 = f1.nnf().simplify();
		Formula nnf_f2 = f2.nnf().simplify();
		
		if(nnf_f1 instanceof Or)
		{
			Formula distrib_1 = new And(((Or) nnf_f1).getF1(),f2).simplify();
			Formula distrib_2 = new And(((Or) nnf_f1).getF2(),f2).simplify();
			Formula distrib_1_dnf = distrib_1.dnf().simplify();
			Formula distrib_2_dnf = distrib_2.dnf().simplify();
			return new Or(distrib_1_dnf,distrib_2_dnf);
		}else if(nnf_f2 instanceof Or)
		{
			Formula distrib_1 = new And(f1,((Or) nnf_f2).getF1()).simplify();
			Formula distrib_2 = new And(f1,((Or) nnf_f2).getF2()).simplify();
			return new Or(distrib_1.dnf(),distrib_2.dnf());
		}else
		{
			Formula and_formula = new And(nnf_f1.dnf(),nnf_f2.dnf()).simplify();
			if (and_formula.isDNF())
				return and_formula;
			else
				return and_formula.dnf();
		}
			
		
	}

	@Override
	public Formula nnf() {
		Formula nnf_formula = new And(f1.nnf(),f2.nnf());
		return nnf_formula.simplify();
	}

	@Override
	public Formula cnf() {
		And nnf_formula = (And) this.nnf();
		return new And(nnf_formula.getF1().cnf(),
				       nnf_formula.getF2().cnf());
	}

	@Override
	public Formula simplify() {
		Formula reduced_f1 = f1.simplify();
		Formula reduced_f2 = f2.simplify();
		if(reduced_f2.syntEqual(Verum.mk())) return reduced_f1;
		if(reduced_f1.syntEqual(Verum.mk())) return reduced_f2;
		if(reduced_f1.syntEqual(Falsum.mk()) || reduced_f2.syntEqual(Falsum.mk())) return Falsum.mk();
		if(reduced_f1.syntEqual(reduced_f2)) return reduced_f1;
		if(reduced_f1.syntEqual(new Not(reduced_f2).simplify())) return Falsum.mk();
		return new And(reduced_f1,reduced_f2);
	}

	@Override
	public Formula_NF dnf_set_base_representation() {
		
		if(this.isDNF())
		{
			Formula_NF dnf_f1 = f1.dnf_set_base_representation();
			Formula_NF dnf_f2 = f2.dnf_set_base_representation();
			Object[] clauses_1 = dnf_f1.get_clauses().toArray();
			Object[] clauses_2 = dnf_f2.get_clauses().toArray();
			Clause clause_1 = (Clause) clauses_1[0];
			Clause clause_2 = (Clause) clauses_2[0];
			clause_1.unionWith(clause_2);
			clause_1.set_prefix("", new ArrayList<>());
			return new Formula_NF(clause_1);
		}else 
		{
			return this.dnf().dnf_set_base_representation();
		}
	}
	
	
}