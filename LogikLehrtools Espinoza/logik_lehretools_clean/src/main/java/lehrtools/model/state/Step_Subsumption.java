package lehrtools.model.state;

import java.util.LinkedList;

import lehrtools.model.Formula_NF;
/**
 * Contains all the information needed to describe a subsumption step for the Resolution algorithm
 * or an absorption step for the Backward Dual Resolution.
 * It usually  is the result of the calculation after a user input.
 */
public class Step_Subsumption extends Step {
	/**
	 * List of S_Sub_Calculation instances describing the all subsumed clauses.
	 */
	public final LinkedList<S_Calculation> steps;
	/**
	 * The Formula_NF instance containing the clauses that will to subsumption or absorption.
	 */
	public final Formula_NF from;
	/**
	 * The Formula_NF instance containing the clauses that will to subsumed or absorbed.
	 */
	public final Formula_NF to;
	/**
	 * The resulting formula after all possible Clauses were subsumed or absorbed.
	 */
	public final Formula_NF subsumed_formula;

	/**
	 * Constructor
	 * @param type Decribes the step that was executed.
	 * @param index The iteration the step was executed.
	 * @param steps List of S_Sub_Calculation instances describing the all subsumed clauses.
	 * @param from The Formula_NF instance containing the clauses that will to subsumption or absorption.
	 * @param to The Formula_NF instance containing the clauses that will to subsumed or absorbed.
	 * @param subsumed_formula The resulting formula after all possible Clauses were subsumed or absorbed.
	 */
	public Step_Subsumption(ModelState type, 
						    int index,
						    LinkedList<S_Calculation> steps,
						    Formula_NF from,
						    Formula_NF to,
						    Formula_NF subsumed_formula
						    )
	{
		super(type, index);
		this.steps = steps;
		this.from = from;
		this.to = to;
		this.subsumed_formula = subsumed_formula;
	}
}
