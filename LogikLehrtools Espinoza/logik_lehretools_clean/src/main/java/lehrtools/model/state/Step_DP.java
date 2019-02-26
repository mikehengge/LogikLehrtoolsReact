package lehrtools.model.state;

import lehrtools.formula.Variable;
import lehrtools.model.Formula_NF;
import lehrtools.model.Literal;

import java.util.LinkedList;

/**
 * Contains all the information needed to describe a step for the DP60 algorithm
 * It usually  is the result of the calculation after a user input.
 */
public class Step_DP extends Step
{
    /**
     * The formula the Step is applied to.
     */
    public final Formula_NF original;
    /**
     * The formula after the execution of the step.
     */
    public final Formula_NF result;
    /**
     * The Literal or literals involved.
     */
    public final LinkedList<Literal> literals;
    /**
     * If the Step executed was a Unit Clause or a Pure Literal the calculations are stored into this field.
     */
    public final LinkedList<S_DP_Calculation> calculations;
    /**
     * If the step was a Variable Elimination step the calculations are stored into this field.
     */
    public final LinkedList<S_Calculation> rule_03_calculations;

    /**
     * Constructor usually used to store given formula, i.e. it the first step.
     * @param type Decribes the step that was executed.
     * @param original The formula the Step is applied to.
     * @param result The formula after the execution of the step.
     * @param literals The Literal or literals involved.
     * @param calculations If the step was a Variable Elimination step the calculations are stored into this field.
     */
    public Step_DP(ModelState type,
                   Formula_NF original,
                   Formula_NF result,
                   LinkedList<Literal> literals,
                   LinkedList<S_DP_Calculation> calculations) {
        super(type, 0);
        this.original = original;
        this.result = result;
        this.literals = literals;
        this.calculations = calculations;
        rule_03_calculations = new LinkedList<>();
    }

    /**
     * Constructor for a Step originating in a Unit Clause Step or Pure Literal Step.
     * @param type Describes the step that was executed.
     * @param original The formula the Step is applied to.
     * @param result The formula after the execution of the step.
     * @param literal The Pure Literal or the Literal contained in the Unit Clause.
     * @param calculations  Calculations executed during the step.
     */
    public Step_DP(ModelState type,
                   Formula_NF original,
                   Formula_NF result,
                   Literal literal,
                   LinkedList<S_DP_Calculation> calculations) {
        super(type, 0);
        this.original = original;
        this.result = result;
        this.literals = new LinkedList<>();
        literals.add(literal);
        this.calculations = calculations;
        rule_03_calculations = new LinkedList<>();
    }

    /**
     * Constructor for a Step originating in a Variable Elimination step.
     * @param type Describes the step that was executed.
     * @param original The formula the Step is applied to.
     * @param result The formula after the execution of the step.
     * @param variable The Variable involved in the Variable Elimination step.
     * @param calculations  Calculations executed during the step.
     */
    public Step_DP(ModelState type,
                   Formula_NF original,
                   Formula_NF result,
                   Variable variable,
                   LinkedList<S_Calculation> calculations) {
        super(type, 0);
        this.original = original;
        this.result = result;
        this.literals = new LinkedList<>();
        literals.add(new Literal(variable, true));
        this.calculations = new LinkedList<>();
        rule_03_calculations = calculations;
    }



}
