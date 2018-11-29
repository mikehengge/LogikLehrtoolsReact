package lehrtools.model.state;

import lehrtools.model.Formula_NF;

/**
 * Used in the Resoltion procedure to store the formula to be tested.
 */
public class Step_Start extends Step {
	/**
	 * Formula to be tested for satisfiability.
	 */
	public final Formula_NF formula;

	/**
	 * Constructor
	 * @param type Decribes the step that was executed. It should be set to ModelState.START.
	 * @param index always 0.
	 * @param formula  Formula to be tested for satisfiability.
	 */
	public Step_Start(ModelState type, int index, Formula_NF formula) {
		super(type, index);
		this.formula = formula;
	}


}
