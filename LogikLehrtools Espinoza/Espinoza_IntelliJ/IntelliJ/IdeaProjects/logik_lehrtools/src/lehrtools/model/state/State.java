package lehrtools.model.state;

import lehrtools.formula.Variable;

import java.util.LinkedList;
import java.util.Set;

/**
 * Abstract class that serves as base class  for ResolutionState, and State_DP.
 */
public abstract class State
{
    /**
     * List of steps containing all the calculation that have benn made.
     */
    public final LinkedList<Step> steps;
    /**
     * Describes the state the Resolution is in at the moment of the update.
     * In case a Step_Back Event is triggered, the State of the REsolution is stored elsewhere.
     */
    public final ModelState state;
    /**
     * Stores the Resolution state in case a Step_Back Event as triggered.
     */
    public final ModelState back_step_state;
    /**
     * Set of Variables conatined in the Formula, over which resolvents could be calculated.
     */

    /**
     * Constructor For ResolutionState
     */
    public State(LinkedList<Step> steps,
                           ModelState state,
                           ModelState back_step_state
                          )
    {
        this.steps = steps;
        this.state = state;
        this.back_step_state = back_step_state;

    }
}
