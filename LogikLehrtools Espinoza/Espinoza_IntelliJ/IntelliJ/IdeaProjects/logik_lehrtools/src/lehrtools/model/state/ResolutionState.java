package lehrtools.model.state;

import java.util.LinkedList;
import java.util.Set;

import lehrtools.formula.Variable;

/**
 *Construct that stores the necessary information the ResolutionViewModel needs
 *to synchronize the state the Resolution instance is in. Is retrieved by the ViewModel
 *during the update event.
 *
 */
public class ResolutionState extends  State {

	/**
	 * Set of Variables contained in the Formula, over which resolvents could be calculated.
	 */
	public final Set<Variable> vars;
	/**
	 * Describes if the Resolution lead to a proof or disproof of the propositional formula.
	 * Only set a the end of the algorithm , 1 for proof, 0 for disproof. Until a decision is found
	 * the value will be -1. 
	 */
	public final int proof;
	/**
	 * Flag signaling that the Forward Subsumption was already calculated.
	 * Used to show and hide the respective Button on the View.
	 */
	public final boolean forward_subsumption;
	/**
	 * Flag signaling that the Backward Subsumption was already calculated.
	 * Used to show and hide the respective Button on the View.
	 */
	public final boolean backward_subsumption;
	
	/**
	 * Constructor For ResolutionState
	 */
	public ResolutionState(LinkedList<Step> steps,
				 ModelState state,
				 ModelState back_step_state,
				 Set<Variable> vars,
				 int proof,
				 boolean forward_subsumption,
				 boolean backward_subsumption
				 )
	{
		super(steps,state,back_step_state);
		this.vars = vars;
		this.proof = proof;
		this.forward_subsumption = forward_subsumption;
		this.backward_subsumption = backward_subsumption;
	}
	

}
