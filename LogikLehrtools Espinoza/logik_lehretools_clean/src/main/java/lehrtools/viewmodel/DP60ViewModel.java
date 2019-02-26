package lehrtools.viewmodel;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lehrtools.formula.Variable;
import lehrtools.model.Clause;
import lehrtools.model.Literal;
import lehrtools.model.state.ModelState;
import lehrtools.model.state.State_DP;
import lehrtools.model.state.Step;
import lehrtools.viewmodel.information.Line_Manager_DP60;
import lehrtools.viewmodel.information.Line;

import java.util.ArrayList;
import java.util.EventObject;
/***
 * Class that corresponds to the ViewModel in the MVVM Pattern. It is in charge of managing user input from the View,
 * here DP60_Controller, and updates fromt eh model , here DP60 class.
 */
public class DP60ViewModel extends Observer {

    /**
     * List with te information of the steps the View needs to display tehm correctly.
     */
    public ObservableList<Line> lines;
    /**
     * Set of Unit Clauses offered to the View
     */
    public ObservableList<String> rule_01;
    /**
     * Set of Pure Literals  offered to the View
     */
    public ObservableList<String> rule_02;
    /**
     * Set of Variables offered to the View
     */
    public ObservableList<String> rule_03;
    /**
     * Sets the visibility of button_rule_1 and menu_rule_1_over
     */
    public BooleanProperty rule_1_visible;
    /**
     * Sets the visibility of button_rule_2 and menu_rule_2_over
     */
    public BooleanProperty rule_2_visible;
    /**
     * Sets the visibility of menu_rule_3_over
     */
    public BooleanProperty rule_3_visible;
    /**
     * Sets the visibility of button_end
     */
    public BooleanProperty end;
    /**
     * Value that triggers a refres of the content in the View.
     */
    public BooleanProperty _redraw;
    /**
     * Processes the steps into Lines for the view and sets selected clauses.
     */
    private Line_Manager_DP60 _line_manager;



    /**
     * Constructor
     * @param subject Takes the Subject it Observes as an argument.
     */

    public DP60ViewModel(Subject subject)
    {
        _subject = subject;
        final ArrayList<Line> line_list = new ArrayList<>();
        lines = FXCollections.observableList(line_list);
        final ArrayList<String> rule_01_list = new ArrayList<>();
        rule_01 = FXCollections.observableList(rule_01_list);
        final ArrayList<String> rule_02_list = new ArrayList<>();
        rule_02 = FXCollections.observableList(rule_02_list);
        final ArrayList<String> rule_03_list = new ArrayList<>();
        rule_03 = FXCollections.observableList(rule_03_list);

        rule_1_visible = new SimpleBooleanProperty(true);
        rule_2_visible = new SimpleBooleanProperty(true);
        rule_3_visible = new SimpleBooleanProperty(true);
        end = new SimpleBooleanProperty(false);

        _redraw = new SimpleBooleanProperty(false);

        _line_manager = new Line_Manager_DP60(lines);
    }
    /**
     * Getter
     * @return Subject
     */
    public Subject model() { return _subject;}
    public Line_Manager_DP60 line_manager () {return _line_manager; }
    public void execute(EventObject event)
    {
        switch(((ModelEvent)event).state() )
        {
           case SHOW_CALCULATION: _line_manager.select_calculation(((ModelEvent) event).information());
                _redraw.set(true);
                _redraw.set(false);
                break;
            default: _subject.execute(event);

        }
    }

    @Override
    public void update() {

        State_DP state = (State_DP) _subject.getState();
        if(state.state == ModelState.BACK)
        {
            lines.clear();
            _line_manager = new Line_Manager_DP60(lines);
            for(Step step : state.steps)
                _line_manager.add(step);
            end.setValue(false);

        }
        else
        {
            _line_manager.add(state.steps.getLast());
            if(state.state == ModelState.END)
                end.setValue(true);
        }

        update_rule_01(state);
        update_rule_02(state);
        update_rule_03(state);

    }

    /**
     * Updates the Set of Unit Clauses offered to the View
     * @param state Actual state of the Model
     */
    private void update_rule_01(State_DP state)
    {
        rule_01.clear();
        for(Clause clause : state.rule_01_clauses)
            rule_01.add(clause.toString());
        if(rule_01.isEmpty() || state.state == ModelState.END)
            rule_1_visible.setValue(false);
        else
            rule_1_visible.setValue(true);
    }
    /**
     * Updates the Set of Pure Literals  offered to the View
     * @param state Actual state of the Model
     */
    private void update_rule_02(State_DP state)
    {
        rule_02.clear();
        for(Literal literal : state.rule_02_pure_literals)
            rule_02.add(literal.toString());
        if(rule_02.isEmpty()|| state.state == ModelState.END)
            rule_2_visible.setValue(false);
        else
            rule_2_visible.setValue(true);
    }
    /**
     * Updates the Set of Variables offered to the View
     * @param state Actual state of the Model
     */
    private void update_rule_03(State_DP state)
    {
        rule_03.clear();
        for(Variable variable : state.rule_03_variables)
            rule_03.add(variable.toString());
        if(rule_03.isEmpty()|| state.state == ModelState.END)
            rule_3_visible.setValue(false);
        else
            rule_3_visible.setValue(true);
    }
}
