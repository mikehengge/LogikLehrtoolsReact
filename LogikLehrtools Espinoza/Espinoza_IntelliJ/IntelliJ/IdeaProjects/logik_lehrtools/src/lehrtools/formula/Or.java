package lehrtools.formula;

import java.util.Map;

import lehrtools.model.Formula_NF;

/**
 * Class for an OR-formula.
 */
public class Or extends BinaryFormula {

	/**
	 * Constructor for OR-formula.
	 * 
	 * @param f1
	 *            left f in infix notation
	 * @param f2
	 *            right f in infix notation
	 */
	public Or(Formula f1, Formula f2) {
		super(f1, f2);
	}

	@Override
	public boolean syntEqual(Formula f) {
		if (f == null)
			return false;
		if (f == this)
			return true;
		if (!(f instanceof Or))
			return false;
		Or formula = (Or) f;
		if(f1.syntEqual(formula.getF1()) && f2.syntEqual(formula.getF2())) 
			return true;
		else
			return false;
	}

	@Override
	public String toString() {
		return "(" + f1 + " OR " + f2 + ")";
	}

	@Override
	public boolean evaluate(Map<Variable, Boolean> assignment)
			throws IllegalArgumentException {
		return f1.evaluate(assignment) || f2.evaluate(assignment);
	}

	@Override
	public Formula substitute(Variable var, Formula form) {
		return new Or(f1.substitute(var, form), f2.substitute(var, form));
	}

	@Override
	public boolean isDNF() {
		return f1.isDNF() && f2.isDNF();
	}

	@Override
	public boolean isMinterm() {
		return false;
	}

	@Override
	public boolean isCNF() {
		return isClause();
	}

	@Override
	public boolean isClause() {
		return f1.isClause() && f2.isClause();
	}

	@Override
	public boolean isNNF() {
		return f1.isNNF() && f2.isNNF();
	}

	@Override
	public Formula dnf() {
		return new Or( f1.dnf().simplify(),f2.dnf().simplify());
	}

	@Override
	public Formula nnf() {
		Formula nnf_formula = new Or(f1.nnf(),f2.nnf());
		return nnf_formula.simplify(); 
	}

	@Override
	public Formula cnf() {
		BinaryFormula nnf_formula = (BinaryFormula) this.nnf();
		Formula cnf_f1 = nnf_formula.getF1().cnf();
		Formula cnf_f2 = nnf_formula.getF2().cnf();
		if((cnf_f1 instanceof And) && (cnf_f2 instanceof And))
		{
			//(A^B) v (C^D) = (AvC)^(AvD)^(BvC)^(BvD)
			Formula A = ((BinaryFormula) cnf_f1).getF1();
			Formula B = ((BinaryFormula) cnf_f1).getF2();
			Formula C = ((BinaryFormula) cnf_f2).getF1();
			Formula D = ((BinaryFormula) cnf_f2).getF2();
			return new And( new Or(A,C),                //(AvC)
			       new And( new Or(A,D),                // ^(AvD)
			       new And( new Or(B,C) , new Or(B,D) ) //^(BvC)^(BvD)
			       ));
		}
		if(cnf_f1 instanceof And)
		{
			//(A^B)vC = (AvC)^(BvC)
			Formula A = ((BinaryFormula) cnf_f1).getF1();
			Formula B = ((BinaryFormula) cnf_f1).getF2();
			return new And( new Or(A,cnf_f2), new Or(B,cnf_f2));
		}
		if(cnf_f2 instanceof And)
		{
			//(Av(B^C)) = (AvB)^(AvC)
			Formula B = ((BinaryFormula) cnf_f2).getF1();
			Formula C = ((BinaryFormula) cnf_f2).getF2();
			return new And( new Or(cnf_f1, B), new Or(cnf_f1,C));
		}
		return new Or(cnf_f1,cnf_f2);
	}

	@Override
	public Formula simplify() {
		Formula reduced_f1 = f1.simplify();
		Formula reduced_f2 = f2.simplify();
		if(reduced_f2.syntEqual(Falsum.mk())) return reduced_f1;
		if(reduced_f1.syntEqual(Falsum.mk())) return reduced_f2;
		if(reduced_f1.syntEqual(Verum.mk()) || reduced_f2.syntEqual(Verum.mk())) return Verum.mk();
		if(reduced_f1.syntEqual(reduced_f2)) return reduced_f1;
		if(reduced_f1.syntEqual(new Not(reduced_f2).simplify())) return Verum.mk();
		return new Or(reduced_f1,reduced_f2);
	}

	@Override
	public Formula_NF dnf_set_base_representation() {
		if(this.isDNF())
		{
			Formula_NF dnf_f1 = f1.dnf_set_base_representation();
			Formula_NF dnf_f2 = f2.dnf_set_base_representation();
			return dnf_f1.union_with(dnf_f2);
		}else 
		{
			return this.dnf().dnf_set_base_representation();
		}
	}
}
