package lehrtools.model.state;

import lehrtools.formula.Variable;
import lehrtools.model.Clause;
import lehrtools.model.Literal;

import java.util.HashSet;
import java.util.LinkedList;
/**
 *Construct that stores the necessary information the DP60ViewModel needs
 *to synchronize the state the DP60 instance is in. Is retrieved by the ViewModel
 *during the update event.
 *
 */
public class State_DP extends State
{
    /**
     * Set of Clauses available for the Unit Clause Step
     */
    public final HashSet<Clause> rule_01_clauses;
    /**
     * Set of Literals availabel for the Pur eLiteral Step
     */
    public final HashSet<Literal> rule_02_pure_literals;
    /**
     * Set of Variables available for the Variable Elimination Step.
     */
    public final HashSet<Variable> rule_03_variables;
    /**
     * Describes if the DP60 algorithm lead to a proof or disproof of the propositional formula.
     * Only set a the end of the algorithm , 1 for proof, 0 for disproof. Until a decision is found
     * the value will be -1.
     */
    public final int proof;


    public State_DP(LinkedList<Step> steps,
                    ModelState state,
                    ModelState back_step_state,
                    HashSet<Clause> rule_01_clauses,
                    HashSet<Literal> rule_02_pure_literals,
                    HashSet<Variable> rule_03_variables,
                    int proof
                    )
    {
        super(steps, state, back_step_state);
        this.rule_01_clauses = rule_01_clauses;
        this.rule_02_pure_literals = rule_02_pure_literals;
        this.rule_03_variables = rule_03_variables;
        this.proof = proof;
    }
}
