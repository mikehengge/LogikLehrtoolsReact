package lehrtools.model.state;

import lehrtools.formula.Variable;
import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;
import lehrtools.model.Literal;

import java.util.HashSet;
import java.util.LinkedList;
/**
 * Takes a snapshot of the State of all variables relevant to the
 * DP60 procedure, so that it can be restored to that specific state at a later
 * point in time.
 */
public class DP60_Log
{
    public final LinkedList<Step> steps;
    public final ModelState state;
    public final ModelState back_state;
    public final Formula_NF formula;
    public final Formula_NF result;
    public final HashSet<Clause> rule_01_clauses;
    public final HashSet<Literal> rule_02_pure_literals;
    public final HashSet<Variable> rule_03_variables;
    public final int proof;

    @SuppressWarnings("unchecked")
    public DP60_Log(LinkedList<Step> steps,
                    ModelState state,
                    ModelState back_state,
                    Formula_NF formula,
                    Formula_NF result,
                    HashSet<Clause> rule_01_clauses,
                    HashSet<Literal> rule_02_pure_literals,
                    HashSet<Variable> rule_03_variables,
                    int _proof)
    {
        this.steps = (LinkedList<Step>) steps.clone();
        this.state = state;
        this.back_state = back_state;
        this.formula = new Formula_NF(formula);
        this.result = new Formula_NF(result);
        this.rule_01_clauses = (HashSet<Clause>) rule_01_clauses.clone();
        this.rule_02_pure_literals = (HashSet<Literal>) rule_02_pure_literals.clone();
        this.rule_03_variables = (HashSet<Variable>) rule_03_variables.clone();
        this.proof = _proof;
    }
}
