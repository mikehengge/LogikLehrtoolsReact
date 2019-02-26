package lehrtools.model.state;

import java.util.LinkedList;
import java.util.Set;

import lehrtools.formula.Variable;
import lehrtools.model.Formula_NF;
/**
 * Contains all the information needed to describe a resolution step over a variable for the Resolution algorithm
 * It usually  is the result of the calculation after a user input.
 */
public class Step_Resolution_Over extends Step_Resolution {
	/**
	 *  The variable over which resolvents were derived.
	 */
	public final Variable resolved_over;
	/**
	 * Set of Variables that are still availabe for a resolution over step.
	 */
	public final Set<Variable> variables_left;

	/**
	 * Constructor
	 * @param type Decribes the step that was executed.
	 * @param index The iteration the step was executed.
	 * @param steps List of S_Res_Calculation instances describing the all derived resolvents.
	 * @param resolvents Formula_NF instance containing all resolvents.
	 * @param parents Formula_NF instance containing the Formula the resolution step was applied too.
	 * @param resolved_over The variable over which resolvents were derived.
	 * @param variables_left Set of Variables that are still availabe for a resolution over step.
	 */
	public Step_Resolution_Over(ModelState type, 
							    int index,LinkedList<S_Calculation> steps,
							    Formula_NF resolvents, 
							    Formula_NF parents,
							    Variable resolved_over,
							    Set<Variable> variables_left)
	{
		super(type,index,steps,resolvents,parents );
		this.resolved_over = resolved_over;
		this.variables_left = variables_left;
		
	}

}
