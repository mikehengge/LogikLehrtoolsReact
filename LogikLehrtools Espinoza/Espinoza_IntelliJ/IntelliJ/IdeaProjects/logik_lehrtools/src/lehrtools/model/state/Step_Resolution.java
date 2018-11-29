package lehrtools.model.state;

import java.util.LinkedList;

import lehrtools.model.Formula_NF;
/**
 * Contains all the information needed to describe a resolution step for the Resolution or Backward Dual Resolution algorithm
 * It usually  is the result of the calculation after a user input.
 */
public class Step_Resolution extends Step {
	/**
	 * List of S_Res_Calculation instances describing the all derived resolvents.
	 */
	public final LinkedList<S_Calculation> steps;
	/**
	 * Formula_NF instance containing all resolvents.
	 */
	public final Formula_NF resolvents;
	/**
	 * Formula_NF instance containing the Formula the resolution step was applied too.
	 */
	public final Formula_NF parents;

	/**
	 *
	 * @param type Decribes the step that was executed.
	 * @param index The iteration the step was executed.
	 * @param steps List of S_Res_Calculation instances describing the all derived resolvents.
	 * @param resolvents Formula_NF instance containing all resolvents.
	 * @param parents Formula_NF instance containing the Formula the resolution step was applied too.
	 */
	public Step_Resolution(ModelState type, int index,LinkedList<S_Calculation> steps,Formula_NF resolvents, Formula_NF parents )
	{
		super(type,index);
		this.steps = steps;
		this.resolvents = resolvents;
		//TODO : parents not really needed
		this.parents = parents;
	}


}
