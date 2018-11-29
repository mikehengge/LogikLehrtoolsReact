package lehrtools.formula;

/**
 * Abstract class for binary formulas.
 */
public abstract class BinaryFormula extends Formula {
	protected Formula f1;
	protected Formula f2;

	public Formula getF1() {
		return f1;
	}

	public Formula getF2() {
		return f2;
	}

	/**
	 * Constructor for BinaryFormula.
	 * 
	 * @param f1
	 *            left f in infix notation
	 * @param f2
	 *            right f in infix notation
	 */
	public BinaryFormula(Formula f1, Formula f2) {
		this.f1 = f1;
		this.f2 = f2;
	}

	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public boolean isAtomicFormula() {
		return false;
	}
}
