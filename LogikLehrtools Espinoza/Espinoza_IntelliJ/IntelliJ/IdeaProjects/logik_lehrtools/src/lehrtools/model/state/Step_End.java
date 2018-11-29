package lehrtools.model.state;

import lehrtools.model.Clause;

/**
 * This Step contains no information that are direct result of a Calculation.
 * It contains information about the result of the procedure and its created
 * when the procedure ends.
 */
public class Step_End extends Step {
	/**
	 * Describes if the given formula is satisfiable or not. The value 1 stands for staisfiable
	 * and the value 0 for unsatisfiable.
	 */
	public final int proof;
	/**
	 * Of signifances only for the Resolution, it contains the first empty clause
	 * found to prove that the formula is unsatisfiable.
	 */
	public final Clause empty_clause;

	/**
	 * Constructor
	 * @param type The value of this field Should always be ModelState.END.
	 * @param index The iteration the empty clause was found.
	 * @param proof Value that tells fi the formula was satisfiable or not.
	 * @param empty_clause The first empty clause found during the Resolution procedure.
	 */
	public Step_End(ModelState type, int index,int proof,Clause empty_clause) {
		super(type, index);
		this.proof = proof;
		this.empty_clause = empty_clause;

	}

}
