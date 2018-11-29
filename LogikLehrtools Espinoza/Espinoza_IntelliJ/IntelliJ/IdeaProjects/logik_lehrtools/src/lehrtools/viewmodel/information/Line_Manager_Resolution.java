package lehrtools.viewmodel.information;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import lehrtools.model.Clause;
import lehrtools.model.state.*;

/**
 * Class used by the ResolutionViewModel to update the Lineinstances offered to the View
 * and to set which clauses are to be displayed as selected when a calculation is clicked.
 */
public class Line_Manager_Resolution {
    /**
     * Stores the calculations made by the model.
     */
	private ArrayList<Step> _steps;
    /**
     * List that syncs content with View.
     */
	private ObservableList<Line> _lines;
    /**
     * Map used to retrieve a Clause instance when the String representation is known.
     * Every Clause from every step is added here .
     */
    private HashMap<String, Clause> _clauses_string_map;
    /**
     * Map used to retrieve a S_Calculation instance when the String representation is known.
     * Every Calculation from every step is added here .
     */
    private HashMap<String, S_Calculation> _calculation_string_map;
    /**
     * Keeps a Map from a S_Calculation and the Line that contains the S_Calculation.
     * Since al lines are stored in the List _lines, the calculation is paired to the index of the
     * Line it is in.
     */
    private HashMap<S_Calculation, Integer> _calculation_line_index_map;
    /**
     * Class used to generate the Line instances Froma Step.
     */
	private Line_Factory _line_factory;
    /**
     * Used to find the Line containing a clause that is part of a resolution
     * or subsumption calculation. For teh resolution is the index of the line in _lines that
     * contains the resolvent.
     */
	private int selected_resolvent_line_index;
    /**
     * Used to find the Line containing a clause that is part of a resolution
     * or subsumption calculation. For the resolution is the index of the line in _lines that
     * contains the parents.
     */
    private int selected_parents_line_index;
    /**
     * The index the a selected has in the _clauses field of the Line instances that
     * it is selected.
     */
    private int selected_resolvent_index;
    /**
     * The index the a selected has in the _clauses field of the Line instances that
     * it is selected.
     */
    private int selected_parent1_index;
    /**
     * The index the a selected has in the _clauses field of the Line instances that
     * it is selected.
     */
    private int selected_parent2_index;
    /**
     * String representation of the S_Calculation instance that is actually selected.
     */
	private String _selected_calculation;

    /**
     * Constructor
     * @param lines a reference to the List offered to the View by the ResolutionViewModel
     */
	public Line_Manager_Resolution(ObservableList<Line> lines)
	{
		_steps = new ArrayList<>();
		_lines = lines;
		_clauses_string_map = new HashMap<>();
		_calculation_string_map = new HashMap<>();
		_calculation_line_index_map = new HashMap<>();
		_line_factory = new Line_Factory();
		selected_resolvent_line_index = -1;
		selected_parents_line_index = -1;
		selected_resolvent_index = -1;
		selected_parent1_index =-1;
		selected_parent2_index = -1;
		_selected_calculation = "";

		
	}

    /**
     * Used by the ResolutionViewModle to add new Step instances.
     * For each new Step instance a new Line is generates, as well as the entries in
     * _clauses_string_map, calculation_string_map and calculation_line_index_map.
     * @param step A new Step instance.
     */
	public void add(Step step)
	{
		_lines.add(_line_factory.get_resolution_line(step));
		_steps.add(step);
		process_step(step);
		
	}

    /**
     * Given a String representation of a clasue it retrieves the Clause instance.
     * @param clause  Sting representation of a Clause.
     * @return the corresponding Clause instance to the given String.
     */
    public Clause getClause(String clause){ return _clauses_string_map.containsKey(clause) ?_clauses_string_map.get(clause) : new Clause();}

    /**
     * This method retrieves the Lines for the clauses involved in a calculation.
     * Then finds the indexes they have in the clauses list and set the respective values so that the
     * View can display those clauses as selceted.
     * @param calculation_string string representation of a calculation .
     */
    public void select_calculation(String calculation_string)
    {
        reset_seletion();
        if(calculation_string.equals(_selected_calculation))
        {
            _selected_calculation = "";
            return;
        }
        _selected_calculation = calculation_string;

        S_Calculation calculation;
        if(_calculation_string_map.containsKey(calculation_string))
            calculation = _calculation_string_map.get(calculation_string);
        else return;

        if(calculation instanceof S_Res_Calculation)
            select_resolution_calculation((S_Res_Calculation) calculation);
        else
            select_subsumption_calculation((S_Sub_Calculation) calculation);
    }

    /**
     * Helper Class for the method select_calculation that , selects the clauses of a resolution step.
     * @param calculation string representation of a calculation .
     */
    private void select_resolution_calculation(S_Res_Calculation calculation)
    {
        // get index from the line the calculation is found at
        selected_resolvent_line_index = _calculation_line_index_map.get(calculation);
        Line_Double resolvent_line = (Line_Double) _lines.get(selected_resolvent_line_index);
        // get index from the step the parent are found at
        selected_parents_line_index = find_parents(selected_resolvent_line_index);
        //Convert parent line into Line_Composite(For Resolution)
        Line_Composite parents_line = (Line_Composite) _lines.get(selected_parents_line_index);
        S_Res_Calculation res_calculation = calculation;


        selected_resolvent_index = resolvent_line.clauses.indexOf(calculation.clause_1.toString());
        selected_parent1_index = parents_line.clauses.indexOf(res_calculation.parent_1.toString());
        selected_parent2_index = parents_line.clauses.indexOf(res_calculation.parent_2.toString());

        if(selected_resolvent_index  != -1)
            resolvent_line.selected.set(selected_resolvent_index, new SimpleBooleanProperty(true));
        parents_line.clause_selected.set(selected_parent1_index, new SimpleBooleanProperty(true));
        parents_line.clause_selected.set(selected_parent2_index, new SimpleBooleanProperty(true));

    }
    /**
     * Helper Class for the method select_calculation that , selects the clauses of a subsumption step.
     * @param calculation string representation of a calculation .
     */
    @SuppressWarnings("Duplicates")
    private void select_subsumption_calculation(S_Sub_Calculation calculation)
    {
        //get index from the line the calculation is found at
        int selected_line_index = _calculation_line_index_map.get(calculation);
        //get line that contains the calclation
        Step step = _steps.get(selected_line_index);
        //In case of forward subsumption do
        if(step.type == ModelState.SUBSUMPTION_FW)
        {
            //find line index for the absorbed clause
            int counter = selected_line_index-1;
            while(counter > 0)
            {
                Step absorber_step = _steps.get(counter);
                if(absorber_step.type == ModelState.RESOLUTION)
                {
                    selected_resolvent_line_index = counter;
                    break;
                }
                counter--;
            }
            //find line index of absorber Clause
            selected_parents_line_index = find_parents(counter);
            //retrieve both lines
            Line_Double absorbed_line = (Line_Double) _lines.get(selected_resolvent_line_index);
            Line_Composite absorber_line = (Line_Composite) _lines.get(selected_parents_line_index);
            //retrieve index of both clauses
            selected_resolvent_index = absorbed_line.clauses.indexOf(calculation.clause_1.toString());
            selected_parent1_index = absorber_line.clauses.indexOf(calculation.clause_2.toString());
            selected_parent2_index = -1;

            absorbed_line.selected.set(selected_resolvent_index, new SimpleBooleanProperty(true));
            absorber_line.clause_selected.set(selected_parent1_index, new SimpleBooleanProperty(true));

        }
        else
        {
            //find line index for the absorber clause
            int counter = selected_line_index-1;
            while(counter > 0)
            {
                Step absorber_step = _steps.get(counter);
                if(absorber_step.type == ModelState.RESOLUTION)
                {
                    selected_parents_line_index = counter;
                    break;
                }
                counter--;
            }
            //find line index of absorbed Clause
            selected_resolvent_line_index = find_parents(counter);

            //retrieve both lines
            Line_Composite absorbed_line = (Line_Composite) _lines.get(selected_resolvent_line_index);
            Line_Double absorber_line = (Line_Double) _lines.get(selected_parents_line_index);
            //retrieve index of both clauses
            selected_resolvent_index = absorbed_line.clauses.indexOf(calculation.clause_1.toString());
            selected_parent1_index = absorber_line.clauses.indexOf(calculation.clause_2.toString());
            selected_parent2_index = -1;

            absorbed_line.clause_selected.set(selected_resolvent_index, new SimpleBooleanProperty(true));
            absorber_line.selected.set(selected_parent1_index, new SimpleBooleanProperty(true));
        }


    }

    /**
     * Removes the selection of all the selected clauses.
     */
    private void reset_seletion()
    {

        if(selected_resolvent_line_index != -1)
        {
            Line resolvent_line = _lines.get(selected_resolvent_line_index);
            Line parents_line = _lines.get(selected_parents_line_index);
            if(selected_resolvent_index  != -1)
                if(resolvent_line instanceof Line_Double)
                    ((Line_Double)resolvent_line).selected.set(selected_resolvent_index, new SimpleBooleanProperty(false));
                else
                    ((Line_Composite)resolvent_line).clause_selected.set(selected_resolvent_index, new SimpleBooleanProperty(false));
            if(parents_line instanceof Line_Composite)
            {
                ((Line_Composite) parents_line).clause_selected.set(selected_parent1_index, new SimpleBooleanProperty(false));
                if (selected_parent2_index != -1)
                    ((Line_Composite)parents_line).clause_selected.set(selected_parent2_index, new SimpleBooleanProperty(false));
            }
            else
            {
                ((Line_Double) parents_line).selected.set(selected_parent1_index, new SimpleBooleanProperty(false));
                if (selected_parent2_index != -1)
                    ((Line_Double)parents_line).selected.set(selected_parent2_index, new SimpleBooleanProperty(false));
            }

        }
    }

    /**
     * Method that creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for the clauses and calculation present ina Step instance.
     * @param step Step instance.
     */
	private void process_step(Step step)
	{
		switch(step.type)
		{
		case START : process_step_start(step); 
					 break;
		case RESOLUTION: 
		case RESOLUTION_OVER: process_step_resolution(step);
							  break;
		case SUBSUMPTION_FW:  process_step_fw_subsumption(step); 
							  break;
		case SUBSUMPTION_BW:  process_step_bw_subsumption(step);
							  break;
		case END : 
				   break;
		default: break;
		}
	}

    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_Start instance.
     * @param step Step instance.
     */
	private void process_step_start(Step step)
	{
        Step_Start s_step = (Step_Start) step;
        for(Clause clause : s_step.formula.get_clauses())
            _clauses_string_map.put(clause.toString(),clause);
	}
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_Resolution instance.
     * @param step Step instance.
     */
	private void process_step_resolution(Step step)
	{

		Step_Resolution r_step = (Step_Resolution) step;
		for(Clause clause : r_step.resolvents.get_clauses())
		    _clauses_string_map.put(clause.toString(),clause);
		for(S_Calculation calculation : r_step.steps)
		{
            _calculation_string_map.put(string_list_toString(calculation.toStringList()), calculation);
            _calculation_line_index_map.put(calculation, _steps.indexOf(step));
        }
	}
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_Subsumption instance originated in a forward subsumption step.
     * @param step Step instance.
     */
	private void process_step_fw_subsumption(Step step)
	{
	    Step_Subsumption s_step = (Step_Subsumption) step;
        for(S_Calculation calculation : s_step.steps)
        {
            ArrayList<String> calculation_string = calculation.toStringList();
            calculation_string.set(3," subsumiert die Klausel ");
            _calculation_string_map.put(string_list_toString(calculation_string), calculation);
            _calculation_line_index_map.put(calculation, _steps.indexOf(s_step));
        }
	}
    /**
     * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
     * for a Step_Subsumption instance originated in a backward subsumption step.
     * @param step Step instance.
     */
	private void process_step_bw_subsumption(Step step)
	{
        Step_Subsumption s_step = (Step_Subsumption) step;
        for(S_Calculation calculation : s_step.steps)
        {
            ArrayList<String> calculation_string = calculation.toStringList();
            calculation_string.set(3," subsumiert die Klausel ");
            _calculation_string_map.put(string_list_toString(calculation_string), calculation);
            _calculation_line_index_map.put(calculation,_steps.indexOf(s_step));
        }
	}

    /**
     * Takes the stringlist representaion of a S_Calculation and merges is toghether
     * into one String.
     * @param list ArrayList<String> respresenting a S_calcualtion instance.
     * @return String composed of all elelments of list.
     */
	private String string_list_toString(ArrayList<String> list)
    {
        String merged_string = "";
        for(String string : list)
            merged_string = merged_string + string;
        return merged_string;
    }

    /**
     * Finds the  Line instance containing the parents of a resolution.
     * Used as well to find the forward subsumption and backward subusmption.
     * @param index index of the clasue resultion from the calculation.
     * @return index of the Line.
     */
    private int find_parents(int index)
    {
        int counter = index - 1;
        while(counter >= 0)
        {
            Line line = _lines.get(counter);
            if(line instanceof Line_Composite)
                break;
            counter--;

        }

        return counter;

    }



}
