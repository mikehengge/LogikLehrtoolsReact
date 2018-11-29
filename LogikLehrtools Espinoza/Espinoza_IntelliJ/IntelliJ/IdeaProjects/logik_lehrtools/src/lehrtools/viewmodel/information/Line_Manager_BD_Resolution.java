package lehrtools.viewmodel.information;

import java.util.ArrayList;
import java.util.HashMap;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
import lehrtools.model.Clause;
import lehrtools.model.state.*;

public class Line_Manager_BD_Resolution {
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

	private int selected_resolvent_line_index, selected_parent1_line_index, selected_parent2_line_index,selected_resolvent_index, selected_parent1_index, selected_parent2_index;
	private String _selected_calculation;
	/**
	 * Constructor
	 * @param lines : A reference to the content that will be displayed in the view.
	 */
	public Line_Manager_BD_Resolution(ObservableList<Line> lines)
	{
		_steps = new ArrayList<>();
		_lines = lines;
		_clauses_string_map = new HashMap<>();
		_calculation_string_map = new HashMap<>();
		_calculation_line_index_map = new HashMap<>();
		_line_factory = new Line_Factory();
		selected_resolvent_line_index = -1;
		selected_parent1_line_index = -1;
		selected_parent2_line_index = -1;
		selected_resolvent_index = -1;
		selected_parent1_index =-1;
		selected_parent2_index = -1;
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
		switch(step.type)
		{
			case START:
			case RESOLUTION: process_resolution((Step_Resolution) step);
						 break;
			case SUBSUMPTION_FW : process_fw_subsumption((Step_Subsumption) step);
							  break;
			case SUBSUMPTION_BW : process_bw_subsumption((Step_Subsumption) step);
							  break;
			default :
		}
	}

	/**
	 * This method retrieves the Lines for the clauses involved in a calculation.
	 * Then finds the indexes they have in the clauses list and set the respective values so that the
	 * View can display those clauses as selceted.
	 * @param calculation_string string representation of a calculation .
	 */
	public void select_calculation(String calculation_string)
	{
		reset_selection();
		if(calculation_string.equals(_selected_calculation))
		{
			_selected_calculation = "";
			Line last_line = _lines.get(_lines.size()-1);
			_lines.remove(last_line);
			_lines.add(last_line);
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
	 * Adds a new line to the offered content.
	 * @param step : Contains information about the resolvents that were calculated.
	 */
	private void process_resolution(Step_Resolution step)
	{
		_lines.add(_line_factory.get_bd_resolution_line(step));
		for(Clause clause : step.resolvents.get_clauses())
			_clauses_string_map.put(clause.toString(),clause);
		for(S_Calculation calculation : step.steps)
		{
			_calculation_string_map.put(string_list_toString(calculation.toStringList()), calculation);
			_calculation_line_index_map.put(calculation, _steps.indexOf(step));
		}
	}
	/**
	 * Sets the active value of the absorbed clauses to true.
	 * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
	 * for a Step_Subsumption instance originated in a forward subsumption step.
	 * @param step Step instance.
	 */
	private void process_fw_subsumption(Step_Subsumption step)
	{
		for(S_Calculation subsumption : step.steps)
		{

			Line_Double last_line = (Line_Double) _lines.get(_lines.size()-1);
			if(contains_clause(subsumption.clause_1.toString(), last_line))
			{
				int index = get_index(subsumption.clause_1.toString(), last_line);
				last_line.active.get(index).set(false);
			}

			_calculation_string_map.put(string_list_toString(subsumption.toStringList()), subsumption);
			_calculation_line_index_map.put(subsumption, _steps.indexOf(step));

		}

		_lines.add(_line_factory.get_bd_resolution_line(step));
	}
	/**
	 * Sets the active value of the absorbed clauses to true.
	 * Creates entries in  _clauses_string_map, calculation_string_map and calculation_line_index_map
	 * for a Step_Subsumption instance originated in a backward subsumption step.
	 * @param step Step instance.
	 */
	private void process_bw_subsumption(Step_Subsumption step)
	{

		for(S_Calculation subsumption : step.steps)
		{
			for(Line line : _lines)
			{
				Line_Double bd_line = (Line_Double) line;
				if(contains_clause(subsumption.clause_1.toString(), bd_line) && (line.label == "Resolution" || line.label == "Start"))
				{
					int clause_index = bd_line.clauses.indexOf( ( (S_Sub_Calculation) subsumption).clause_1.toString());
					bd_line.active.get(clause_index).set(false);
					break;
				}
				
			}

			_calculation_string_map.put(string_list_toString(subsumption.toStringList()), subsumption);
			_calculation_line_index_map.put(subsumption, _steps.indexOf(step));
		}
		_lines.add(_line_factory.get_bd_resolution_line(step));

	}
	/**
	 * Helper Class for the method select calculation that , selects the clauses of a resolution step.
	 * @param calculation string representation of a calculation .
	 */
	private void select_resolution_calculation(S_Res_Calculation calculation)
	{
		// get index from the line the calculation is found at
		selected_resolvent_line_index = _calculation_line_index_map.get(calculation);
		Line_Double resolvent_line = (Line_Double) _lines.get(selected_resolvent_line_index);
		// get line index for the parents
		selected_parent1_line_index = get_line_index(calculation.parent_1.toString());
		selected_parent2_line_index = get_line_index(calculation.parent_2.toString());
		//get lines for the parents
		Line_Double parent1_line = (Line_Double) _lines.get(selected_parent1_line_index);
		Line_Double parent2_line = (Line_Double) _lines.get(selected_parent2_line_index);




		selected_resolvent_index = resolvent_line.clauses.indexOf(calculation.clause_1.toString());
		selected_parent1_index = get_index(calculation.parent_1.toString(), parent1_line);
		selected_parent2_index = get_index(calculation.parent_2.toString(), parent2_line);

		if(selected_resolvent_index  != -1)
			resolvent_line.selected.set(selected_resolvent_index, new SimpleBooleanProperty(true));
		parent1_line.selected.set(selected_parent1_index, new SimpleBooleanProperty(true));
		parent2_line.selected.set(selected_parent2_index, new SimpleBooleanProperty(true));

		Line last_line = _lines.get(_lines.size()-1);
		_lines.remove(last_line);
		_lines.add(last_line);

	}
	/**
	 * Helper Class for the method select calculation that , selects the clauses of a absorption step.
	 * @param calculation string representation of a calculation .
	 */
	private void select_subsumption_calculation(S_Sub_Calculation calculation)
	{
		// get index from the line the calculation is found at
		selected_resolvent_line_index = get_line_index(calculation.clause_1.toString());
		// get line index for the parents
		selected_parent1_line_index = get_line_index(calculation.clause_1.toString());
		selected_parent2_line_index = get_line_index(calculation.clause_2.toString());
		//get lines for the parents
		Line_Double parent1_line = (Line_Double) _lines.get(selected_parent1_line_index);
		Line_Double parent2_line = (Line_Double) _lines.get(selected_parent2_line_index);




		selected_resolvent_index = -1;
		selected_parent1_index = get_index(calculation.clause_1.toString(), parent1_line);
		selected_parent2_index = get_index(calculation.clause_2.toString(), parent2_line);

		parent1_line.selected.set(selected_parent1_index, new SimpleBooleanProperty(true));
		parent2_line.selected.set(selected_parent2_index, new SimpleBooleanProperty(true));

		Line last_line = _lines.get(_lines.size()-1);
		_lines.remove(last_line);
		_lines.add(last_line);
	}
	/**
	 * Removes the selection of all the selected clauses.
	 */
	private void reset_selection()
	{

		if(selected_resolvent_line_index != -1)
		{

			Line_Double resolvent_line = (Line_Double) _lines.get(selected_resolvent_line_index);
			Line_Double parent1_line = (Line_Double) _lines.get(selected_parent1_line_index);
			Line_Double parent2_line = (Line_Double) _lines.get(selected_parent2_line_index);

			if(selected_resolvent_index  != -1)
				resolvent_line.selected.set(selected_resolvent_index, new SimpleBooleanProperty(false));
			parent1_line.selected.set(selected_parent1_index, new SimpleBooleanProperty(false));
			parent2_line.selected.set(selected_parent2_index, new SimpleBooleanProperty(false));
		}
	}

	/**
	 * Method that checks is String is contained in the clauses field of a Line_Double instance.
	 * @param clause String to be looked for.
	 * @param line Line instances that is supposed to contain the String.
	 * @return true if the String was found.
	 */
	private boolean contains_clause(String clause , Line_Double line)
	{
		for(String _clause : line.clauses)
			if(clause.equals(_clause))
				return true;
		return false;
			
	}

	/**
	 * Method get the index a String has in its field clauses.
	 * It is recomend to use this method toghether wit the method contains_clause().
	 * @param clause String for the index needs to be retrieved.
	 * @param line Line instances that is supposed to contain the String.
	 * @return index of the String in clauses.
	 */
	private int get_index(String clause, Line_Double  line)
	{
		for(int i = 0; i< line.clauses.size(); i++)
			if(clause.equals(line.clauses.get(i)))
				return i;
		return -1;
		
	}

	/**
	 * Finds the index of the Line in the field _lines that contains clause.
	 * @param clause String
	 * @return index
	 */
	private int get_line_index(String clause)
	{
		for(int index = 0; index < _lines.size(); index++)
		{
			Line_Double line = (Line_Double) _lines.get(index);
			if(contains_clause (clause, line))
				return index;
		}
		return -1;
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

}
