package lehrtools.model;

import lehrtools.formula.Variable;
import lehrtools.model.state.*;
import lehrtools.viewmodel.ModelEvent;
import lehrtools.viewmodel.Observer;
import lehrtools.viewmodel.Subject;

import java.util.EventObject;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * This class implements the the DP60 algorithm to test if a formula in CNF is
 * satisfiable. It uses the functionality implemented in the DP60_Utility.
 * This class represnts the model in a MVVM pattern, so it interacts with the viewmodel
 * through an observer pattern. This is implement by extending the abstract class subject.
 * Through this the class offers following methods to interact with an observer: attach(observer),
 * execute(Event Object) and getState().
 *
 *
 */
public class DP60 extends Subject
{

    /**
     * Store each Step instance resulting of executing a step of the  backward Dual Resolution
     * algorithm.
     */
    private LinkedList<Step> _steps;
    /**
     * Describes the state the algorithm.
     */
    private ModelState _state;
    /**
     * Only used when a step back is executed. Used to tell the
     * DP60ViewModel the state DP60 is in, since the field _state is used
     * to tell the DP60ViewModel that step back was executed.
     */
    private ModelState _back_state;
    /**
     * Represents the actual state of the given formula.
     */
    private Formula_NF _formula;
    /**
     * Represents teh actual formula after a step is executed.
     */
    private Formula_NF _result;
    /**
     * Set of unit clauses contained in _formula and available for
     * a Unit Clause step.
     */
    private HashSet<Clause> _rule_01_clauses;
    /**
     * Set of Pure Literals contained in _formula and available for a Pure Literal step.
     */
    private HashSet<Literal> _rule_02_pure_literals;
    /**
     * Set of variables from _formula available for a Variableelimination step.
     */
    private HashSet<Variable> _rule_03_variables;
    /**
     * at the end of the calculations it will describe if the formula is satisfiable(1)
     * or unsatisfiable(0), during calculations satisfiability is undetermined(-1)
     */
    private int _proof;
    /**
     * Keeps an ordered list of the states the algorithm went through. Its used to set
     * the state of the algorithm back to a previous one.
     */
    private LinkedList<DP60_Log> _logger;
    /**
     * Constructor
     * @param formula formula to be tested.
     */
    public DP60(Formula_NF formula)
    {
        _steps = new LinkedList<>();
        _state = ModelState.START;
        _back_state = ModelState.START;
        _formula = formula;
        _result = new Formula_NF();
        _rule_01_clauses = new HashSet<>();
        _rule_02_pure_literals = new HashSet<>();
        _rule_03_variables = new HashSet<>();
        _proof = -1;
        _steps.add(new Step_DP( _state,
                                _formula,
                                _result,
                                new LinkedList<>(),
                                new LinkedList<>()
                              ));
        _logger = new LinkedList<>();
        update_state();
        generate_log();


    }


    @Override
    public void execute(EventObject event)
    {

        ModelEvent _event = (ModelEvent) event;
        switch(_event.state())
        {
            case RULE_01: execute_rule_01();
                 break;
            case RULE_01_OVER: execute_rule_01_over(_event.information());
                break;
            case RULE_02: execute_rule_02();
                 break;
            case RULE_02_OVER: execute_rule_02_over(_event.information());
                break;
            case RULE_03: execute_rule_03(_event.information());
                 break;
            case BACK: execute_step_back();
                       break;
            default:

        }


    }

    @Override
    public State getState()
    {
        return new State_DP(_steps,
                            _state,
                            _back_state,
                            _rule_01_clauses,
                            _rule_02_pure_literals,
                            _rule_03_variables,
                _proof);
    }

    @Override
    public void attach(Observer observer)
    {
        _observer = observer;
        update();
    }

    /**
     * Applies the Unit Clause step. It repeats this step until there isn't
     * any unit clause present in _formula. Each step is executed by a call
     * of execute_rule_01_over(...)
     */
    private void execute_rule_01()
    {
        if(_proof != -1) return;
        if(_rule_01_clauses.isEmpty()) return;
        if(!_formula.contains_unit_clause()) return;

        while(_formula.contains_unit_clause())
        {
            Clause unit_clause = _formula.get_unit_clause();
            execute_rule_01_over(unit_clause.toString());
        }

    }

    /**
     * Executes the Unit Clause step over _formula for a given unit clause.
     * @param unit_clause_string  A Clause instance representing unit clause.
     */
    private void execute_rule_01_over(String unit_clause_string)
    {
        if(_proof != -1) return;
        Clause clause = get_unit_clause(unit_clause_string);
        LinkedList<S_DP_Calculation> calculations = new LinkedList<>();
        //Invariant : should not throw an exception as long as a value
        //            the available options are literals from the formula
        _result = DP60_Utility.rule_01(_formula,clause ,calculations);

        _steps.add(new Step_DP(ModelState.RULE_01_OVER,
                _formula,
                _result,
                clause.getUnitLit(),
                calculations
        ));
        _formula = _result;
        _state = ModelState.RULE_01_OVER;
        update_state();
        update();

        test_end();
        generate_log();

    }

    /**
     * Retrieves the Literal instance in the field _formula corresponding to a given String. If no Literal
     * is found the  value null is returned.
     * @param literal_string string representation of a literal.
     * @return  Clause instance or null.
     */
    private Literal get_literal(String literal_string)
    {
        for(Literal literal: DP60_Utility.get_literals(_formula))
            if(literal_string.equals(literal.toString())) return literal;
        return null;
    }

    /**
     * Retrieves the Clause instance in the field _formula corresponding to a given String.The Clause returned is a unit clause.
     * If no Clause is found the  value null is returned.
     * @param unit_clause_string the string representation of a unit clause.
     * @return a Clause instance that is a unit clause or null.
     */
    private Clause get_unit_clause(String unit_clause_string)
    {
        for(Clause clause : get_unit_clauses())
            if(clause.toString().equals(unit_clause_string)) return clause;

        return null;
    }

    /**
     * Executes the Pure Literal step.
     * Eliminates all pure literals contained in _formula.
     * It repeats this step until no more pure literals can be found.
     */
    private void execute_rule_02()
    {
        if(_proof != -1) return;
        if(_rule_02_pure_literals.isEmpty())return;
        Set<Literal> pure_literals = new HashSet<>();
        _result = _formula;

        while(DP60_Utility.get_pure_literal(_result, pure_literals))
        {
            for(Literal pure_literal : pure_literals)
                execute_rule_02_over(pure_literal.toString());
            pure_literals = new HashSet<>();
        }
    }

    /**
     * Applies the Pure Literal step for a given pure literal.
     * @param literal_String string representation of the pure literal to be eliminated.
     */
    private void execute_rule_02_over(String literal_String)
    {
        if(_proof != -1) return;
        Literal literal = get_literal(literal_String);
        LinkedList<S_DP_Calculation> calculations = new LinkedList<>();

        _result = DP60_Utility.rule_02(_formula,literal, calculations);

        _steps.add(new Step_DP(ModelState.RULE_02_OVER,
                _formula,
                _result,
                literal,
                calculations
        ));

        _formula = _result;
        _state = ModelState.RULE_02_OVER;
        update_state();
        update();

        test_end();
        generate_log();

    }

    /**
     * Performs the variable elimination step for a given variable. .
     * @param variable_string string representation of the variable to be eliminated.
     */
    private void execute_rule_03(String variable_string)
    {
        if(_proof != -1) return;
       Variable variable = new Variable(variable_string);
       LinkedList<S_Calculation> calculations = new LinkedList<>();

       _result = DP60_Utility.rule_03(_formula,variable, calculations );
       _result = clean_up_double(_result);

        _steps.add(new Step_DP(ModelState.RULE_03,
                _formula,
                _result,
                variable,
                calculations
        ));

        _formula = _result;
        _state = ModelState.RULE_03;
        update_state();
        update();

        test_end();
        generate_log();

    }
    /**
     * Sets the state of the algorithm one step back.
     */
    @SuppressWarnings("unchecked")
    private void execute_step_back()
    {
        if(_logger.size() == 1) return;
        _logger.removeLast();
        DP60_Log log = _logger.getLast();
        _steps = (LinkedList<Step>) log.steps.clone();
        _state = ModelState.BACK;
        _back_state = log.state;
        _formula = log.formula;
        _result = log.result;
        _rule_01_clauses = (HashSet<Clause>) log.rule_01_clauses.clone();
        _rule_02_pure_literals = (HashSet<Literal>) log.rule_02_pure_literals.clone();
        _rule_03_variables = (HashSet<Variable>) log.rule_03_variables.clone();
        _proof = log.proof;
        update();
        _state = log.back_state;
    }
    /**
     * Triggers an update of the actual state of the class to the
     * DP60ViewModel.
     */
    private void update()
    {
        if(_observer != null) _observer.update();
    }

    /**
     * Updates the unit clauses , pure literals and variables available
     * for a step.
     */
    private void update_state()
    {
        _rule_01_clauses = get_unit_clauses();
        _rule_02_pure_literals = new HashSet<>();
        Set<Literal> pure_literals = new HashSet<>();
        if(DP60_Utility.get_pure_literal(_formula,pure_literals) )
            _rule_02_pure_literals.addAll(pure_literals);
        _rule_03_variables = (HashSet<Variable>) _formula.vars();
    }

    /**
     * Retreives all unit clauses available for a Unit Clause Step.
     * @return HashSet<Clause> containing unit clauses.
     */
    private HashSet<Clause> get_unit_clauses()
    {
        Formula_NF formula = new Formula_NF(_formula);
        HashSet<Clause> unit_clauses = new HashSet<>();
        while(formula.contains_unit_clause())
        {
            Clause unit_clause = formula.get_unit_clause();
            unit_clauses.add(unit_clause);
            formula.remove_clause(unit_clause);
        }
        return unit_clauses;
    }

    /**
     * It cleans up all duplicate clauses.
     * @param _formula formula fromwhich al the doubles are to be removes.
     * @return Formula_NF
     */
    private Formula_NF clean_up_double(Formula_NF _formula)
    {
        Formula_NF cleaned_formula = new Formula_NF();
        for(Clause clause : _formula.get_clauses())
            if(!DP60_Utility.is_contained(cleaned_formula,clause))
                cleaned_formula.add_clause(clause);
        return cleaned_formula;
    }

    /**
     * Test if teh conditions to en the DP60 algorithm are met.
     */
    private void test_end()
    {
        if(_formula.contains_empty_clause())
            _proof = 0;
        else if(_formula.is_empty())
            _proof = 1;
        else return;
        _state = ModelState.END;
        _steps.add(new Step_End(_state,0, _proof, _formula.get_empty_clause()));
        update();
        //generate_log();

    }
    /**
     * Creats a DP60Log instance saving the state of the class and adding it to _logger.
     */
    public void generate_log()
    {
        _logger.add(new DP60_Log( _steps,
                                  _state,
                                  _back_state,
                                  _formula,
                                  _result,
                                  _rule_01_clauses,
                                  _rule_02_pure_literals,
                                  _rule_03_variables,
                _proof));
    }

}
