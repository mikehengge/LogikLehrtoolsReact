package lehrtools.viewmodel.information;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import lehrtools.model.state.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

public class Line_Manager_DP60
{
    /**
     * Stores the calculations made by the model.
     */
    private ArrayList<Step> _steps;
    /**
     * A reference to the content offered to the view.
     */
    private ObservableList<Line> _lines;
    /**
     * Class used to create a new line from a given step.
     */
    private Line_Factory _line_factory;
    /**
     * Map that associates the displayed calculations string with
     * its respective instance.
     */
    private HashMap<String, S_Calculation> _calculation_string_map;
    /**
     * Map that associates each calculation with the index of the tep they are stored in.
     */
    private HashMap<S_Calculation, Integer> _calculation_line_index_map;

    private int _calculation_line_index;
    private int _clause_1_index;
    private int _clause_2_index;
    private int _clause_3_index;
    private String _selected_calculation;

    /**
     * Constructor
     * @param lines : A reference to the content that will be displayed in the view.
     */
    public Line_Manager_DP60(ObservableList<Line> lines)
    {
        _steps = new ArrayList<>();
        _lines = lines;
        _line_factory = new Line_Factory();
        _calculation_string_map = new HashMap<>();
        _calculation_line_index_map = new HashMap<>();
        _calculation_line_index = -1;
        _clause_1_index = -1;
        _clause_2_index = -1;
        _clause_3_index = -1;
        _selected_calculation = "";
    }

    /**
     * Starts the processing of calculations in form of a step.
     * Step_Resolution will result in a new line.
     * Step_Subsumption will add or alter information to the existing lines.
     * @param step : Contains information about the calculations made by the model.
     */
    public void add(Step step)
    {
        _steps.add(step);
        _lines.add(_line_factory.get_dp60_line(step));
        switch(step.type)
        {
            case RULE_01: execute_rule_01((Step_DP) step);
                          break;
            case RULE_01_OVER: execute_rule_01_over((Step_DP) step);
                          break;
            case RULE_02: execute_rule_02((Step_DP) step);
                          break;
            case RULE_02_OVER: execute_rule_02_over((Step_DP) step);
                          break;
            case RULE_03: execute_rule_03((Step_DP) step);
                          break;
            default :
        }
    }
    /**
     * This method retrieves the Lines where for clauses involved in a calculation.
     * Then finds the indexes they have in the clauses list and set the respective values so that the
     * View can display those clauses as selceted.
     * @param calculation_string string representation of a calculation .
     */
    public void select_calculation(String calculation_string)
    {
        reset_selection();
        if(calculation_string.equals(_selected_calculation))
        {
            _selected_calculation="";
            return;
        }
        _selected_calculation = calculation_string;
        S_Calculation calculation = _calculation_string_map.get(calculation_string);
        _calculation_line_index = _calculation_line_index_map.get(calculation);
        if(calculation instanceof  S_DP_Calculation)
        {
            switch( ((S_DP_Calculation)calculation).type )
            {
                case "Rule 01 Clause" : select_rule_1_clause((S_DP_Calculation) calculation);
                                        break;
                case "Rule 01 Variable": select_rule_1_variable((S_DP_Calculation) calculation);
                                         break;
                case "Rule 02" : select_rule_2_variable( (S_DP_Calculation) calculation);
                                 break;
                default:
            }
        }
        else
            select_rule_3_variable((S_Res_Calculation) calculation);

    }

    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_DP instance .
     * @param step Step instance.
     */
    private void execute_rule_01(Step_DP step){ register_string_calculation_step_dp(step.calculations);  }
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_DP instance .
     * @param step Step instance.
     */
    private void execute_rule_01_over(Step_DP step)
    {
        register_string_calculation_step_dp(step.calculations);
    }
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_DP instance .
     * @param step Step instance.
     */
    private void execute_rule_02(Step_DP step)
    {
        register_string_calculation_step_dp(step.calculations);
    }
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_DP instance .
     * @param step Step instance.
     */
    private void execute_rule_02_over(Step_DP step)
    {
        register_string_calculation_step_dp(step.calculations);
    }
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_DP instance .
     * @param step Step instance.
     */
    private void execute_rule_03(Step_DP step){ register_string_calculation_rule3(step.rule_03_calculations);  }
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for the calculations resulting from a Variable Elimination step .
     * @param calculations  List of calculations.
     */
    @SuppressWarnings("Duplicates")
    private void register_string_calculation_rule3(LinkedList<S_Calculation> calculations)
    {
        for(S_Calculation calculation : calculations) {
            String merged_string = "";
            for (String string : calculation.toStringList())
                merged_string = merged_string + string;
            _calculation_string_map.put(merged_string, calculation);
            _calculation_line_index_map.put(calculation, _steps.size() - 1);
        }
    }
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for the calculations resulting from a Unit Clause or a Pure Literal step .
     * @param calculations  List of calculations.
     */
    private void register_string_calculation_step_dp(LinkedList<S_DP_Calculation> calculations)
    {
        for(S_DP_Calculation calculation : calculations) {
            String merged_string = "";
            for (String string : calculation.toStringList())
                merged_string = merged_string + string;
            _calculation_string_map.put(merged_string, calculation);
            _calculation_line_index_map.put(calculation, _steps.size() - 1);
        }
    }
    /**
     * Helper Class for the method select_calculation that , selects the clauses of a Variable Elimination step.
     * @param calculation string representation of a calculation .
     */
    @SuppressWarnings("Duplicates")
    private void select_rule_3_variable(S_Res_Calculation calculation)
    {
        _clause_1_index = ((Line_DP60)_lines.get(_calculation_line_index) ).clauses.indexOf(calculation.clause_1.toString());
        if(_calculation_line_index ==1)
        {
            _clause_2_index =  ((Line_Simple)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.parent_1.toString());
            _clause_3_index =  ((Line_Simple)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.parent_2.toString());
        }
        else {
            _clause_2_index = ((Line_DP60) _lines.get(_calculation_line_index - 1)).clauses.indexOf(calculation.parent_1.toString());
            _clause_3_index = ((Line_DP60) _lines.get(_calculation_line_index - 1)).clauses.indexOf(calculation.parent_2.toString());
        }

        if(_clause_1_index != -1)
            ((Line_DP60)_lines.get(_calculation_line_index) ).selected.set(_clause_1_index, new SimpleBooleanProperty(true));

        if(_calculation_line_index ==1)
        {
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_3_index, new SimpleBooleanProperty(true));
        }
        else {
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_3_index, new SimpleBooleanProperty(true));
        }
    }
    /**
     * Helper Class for the method select_calculation that , selects the clauses of a Unit Clause step
     * where a Clause was removed.
     * @param calculation string representation of a calculation .
     */
    @SuppressWarnings("Duplicates")
    private void select_rule_1_clause(S_DP_Calculation calculation)
    {
        _clause_1_index = -1;
        if(_calculation_line_index ==1)
        {
            _clause_2_index =  ((Line_Simple)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.clause_1.toString());
            _clause_3_index =  ((Line_Simple)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.unit_clause.toString());
        }
        else {
            _clause_2_index = ((Line_DP60) _lines.get(_calculation_line_index - 1)).clauses.indexOf(calculation.clause_1.toString());
            _clause_3_index = ((Line_DP60)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.unit_clause.toString());
        }
        if(_calculation_line_index ==1) {
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_3_index, new SimpleBooleanProperty(true));
        }
        else {
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_3_index, new SimpleBooleanProperty(true));
        }

    }
    /**
     * Helper Class for the method select_calculation that , selects the clauses of a Unit Clause step
     * where a Literal  was removed.
     * @param calculation string representation of a calculation .
     */
    @SuppressWarnings("Duplicates")
    private  void select_rule_1_variable(S_DP_Calculation calculation)
    {
        _clause_1_index = ((Line_DP60) _lines.get(_calculation_line_index)).clauses.indexOf(calculation.reduced_clause.toString());
        if(_calculation_line_index ==1)
        {
            _clause_2_index =  ((Line_Simple)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.clause_1.toString());
            _clause_3_index =  ((Line_Simple)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.unit_clause.toString());
        }
        else {
            _clause_2_index = ((Line_DP60) _lines.get(_calculation_line_index - 1)).clauses.indexOf(calculation.clause_1.toString());
            _clause_3_index = ((Line_DP60)_lines.get(_calculation_line_index -1) ).clauses.indexOf(calculation.unit_clause.toString());
        }
        if(_clause_1_index != -1)
            ((Line_DP60) _lines.get(_calculation_line_index)).selected.set(_clause_1_index, new SimpleBooleanProperty(true));
        if(_calculation_line_index ==1) {
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_3_index, new SimpleBooleanProperty(true));
        }
        else {
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_3_index, new SimpleBooleanProperty(true));
        }
    }
    /**
     * Helper Class for the method select_calculation that , selects the clauses of a Pure Literal step
     * where a Clause was removed.
     * @param calculation string representation of a calculation .
     */
    private void select_rule_2_variable(S_DP_Calculation calculation)
    {

        if(_calculation_line_index == 1)
        {
            _clause_2_index = ((Line_Simple) _lines.get(_calculation_line_index - 1)).clauses.indexOf(calculation.clause_1.toString());
            ((Line_Simple) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
        }
        else
        {
            _clause_2_index = ((Line_DP60) _lines.get(_calculation_line_index - 1)).clauses.indexOf(calculation.clause_1.toString());
            ((Line_DP60) _lines.get(_calculation_line_index - 1)).selected.set(_clause_2_index, new SimpleBooleanProperty(true));
        }
        _clause_1_index = -1;
        _clause_3_index = -1;
    }

    /**
     * Removes the selection of all the selected clauses.
     */
    private void reset_selection()
    {
        if(_clause_1_index != -1)
                ((Line_DP60)_lines.get(_calculation_line_index) ).selected.set(_clause_1_index, new SimpleBooleanProperty(false));
        if(_calculation_line_index == 1)
        {
            if(_clause_2_index != -1)
                ((Line_Simple)_lines.get(_calculation_line_index -1) ).selected.set(_clause_2_index, new SimpleBooleanProperty(false));
            if(_clause_3_index != -1)
                ((Line_Simple)_lines.get(_calculation_line_index -1) ).selected.set(_clause_3_index, new SimpleBooleanProperty(false));
        }
        else
        {
            if(_clause_2_index != -1)
                ((Line_DP60)_lines.get(_calculation_line_index -1) ).selected.set(_clause_2_index, new SimpleBooleanProperty(false));
            if(_clause_3_index != -1)
                ((Line_DP60)_lines.get(_calculation_line_index -1) ).selected.set(_clause_3_index, new SimpleBooleanProperty(false));
        }
        _calculation_line_index = -1;
        _clause_1_index = -1;
        _clause_2_index = -1;
        _clause_3_index = -1;


    }


}
