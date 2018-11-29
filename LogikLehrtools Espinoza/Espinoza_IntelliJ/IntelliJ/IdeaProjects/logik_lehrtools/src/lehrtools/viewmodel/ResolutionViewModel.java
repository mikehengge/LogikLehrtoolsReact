package lehrtools.viewmodel;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;
import java.util.Set;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lehrtools.model.Clause;
import lehrtools.model.state.ModelState;
import lehrtools.model.state.ResolutionState;
import lehrtools.model.state.Step;
import lehrtools.viewmodel.information.Line;
import lehrtools.viewmodel.information.Line_Manager_Resolution;
import lehrtools.formula.Variable;

/***
 * Class that corresponds to the ViewModel in the MVVM Pattern. It is in charge of managing user input from the View,
 * here ResolutionController, and updates fromt eh model , here Resolution class.
 */
public class ResolutionViewModel extends Observer{
	/**
	 * List with te information of the steps the View needs to display tehm correctly.
	 */
	public ObservableList<Line> _steps;
	/**
	 * BooleanProperty that controls the visibility of the button_resolution.
	 */
	public BooleanProperty _resolution;
	/**
	 * BooleanProperty that controls the visibility of the menu_resolution_over.
	 */
	public BooleanProperty _resolution_over;
	/**
	 * BooleanProperty that controls the visibility of the button_fw_subsumption.
	 */
	public BooleanProperty _forward;
	/**
	 * BooleanProperty that controls the visibility of the button_bw_subsumption.
	 */
	public BooleanProperty _backward;
	/**
	 * BooleanProperty that controls the visibility of the button_end.
	 */
	public BooleanProperty _end;
	/**
	 * BooleanProperty that controls the visibility of the deduction tree.
	 */
	public BooleanProperty _binary_tree_visible;
	/**
	 * takes value 1 if the formula is satisfiable, 0 when it is unsatisfiable and -1
	 * if there is no result.
	 */
	public int _proof;
	/**
	 * List containing the Variables a Resolution over step can be executed.
	 */
	public ObservableList<String> _vars;
	/**
	 * Clause the actual deduction tree was calculate on.
	 */
	public Clause _selected_clause;
	/**
	 * Value that triggers a refres of the content in the View.
	 */
	public BooleanProperty _redraw;

	/**
	 * Processes the steps into Lines for the view and sets selected clauses.
	 */
	private Line_Manager_Resolution _manage;

	/**
	 * Constructor
	 * @param model Takes the Subject it Observes as an argument.
	 */
	
	public ResolutionViewModel(Subject model)
	{
		_subject = model;
		_steps = FXCollections.observableList(new ArrayList<Line>());
		_resolution = new SimpleBooleanProperty(true);
		_resolution_over = new SimpleBooleanProperty(true);
		_forward = new SimpleBooleanProperty(false);
		_backward = new SimpleBooleanProperty(false);
		_end = new SimpleBooleanProperty(false);
		_binary_tree_visible = new SimpleBooleanProperty(false);
		_proof = -1;
		_vars = FXCollections.observableList(new ArrayList<String>());
		_manage = new Line_Manager_Resolution(_steps);
		_selected_clause = new Clause();
		_redraw = new SimpleBooleanProperty(false);
				
	}

	/**
	 * Getter
	 * @return Subject
	 */
	public Subject model() { return _subject;}

	/**
	 * Recieves the user input from the View.
	 * @param event EventObject instance
	 */
	public void execute(EventObject event)
	{ 
		switch(((ModelEvent)event).state() )
		{
			case SHOW_TREE : state_tree_show( ((ModelEvent) event).information() );
							 break;
			case HIDE_TREE : state_tree_hide();
							 break;
			case SHOW_CALCULATION: _manage.select_calculation(((ModelEvent) event).information());
								   _redraw.set(true);
								   _redraw.set(false);
								  break;
		default: _subject.execute(event);
							 
		}
		
	}
	

	@Override
	public void update() {
		ResolutionState state = (ResolutionState)_subject.getState();
		
		switch(state.state)
		{
		case RESOLUTION : 
		case RESOLUTION_OVER : state_resolution(state);
								break;
		case SUBSUMPTION :		state_subsumption(state);
								break;
		case END : 	state_end(state);
					break;
		case BACK : state_back(state);
					break;
		default:
							  
		}
		
		
	}

	/**
	 * Update _manager with a new step and sets the State of teh View accordingly.
	 * @param state Actual state of the Model
	 */
	private void state_resolution(ResolutionState state)
	{
		_manage.add(state.steps.getLast());
		_resolution.setValue(true);
		_resolution_over.setValue(true);
		_forward.setValue(false);
		_backward.setValue(false);
		_end.setValue(false);
		_proof = state.proof;
		_vars.clear();
		_vars.addAll(toList(state.vars));
		_binary_tree_visible.setValue(false);
		
	}


	/**
	 * Update _manager witha new step and sets the State of teh View accordingly.
	 * @param state Actual state of the Model
	 */
	private void state_subsumption(ResolutionState state)
	{
		_manage.add(state.steps.getLast());
		_resolution.setValue(false);
		_resolution_over.setValue(false);
		_forward.setValue(!state.forward_subsumption);
		_backward.setValue(!state.backward_subsumption);
		_end.setValue(false);
		_proof = state.proof;
		_vars.clear();
		_binary_tree_visible.setValue(false);
	}
	/**
	 * Update _manager witha new step and sets the State of teh View accordingly.
	 * @param state Actual state of the Model
	 */
	private void state_end(ResolutionState state)
	{
		_manage.add(state.steps.getLast());
		_resolution.setValue(false);
		_resolution_over.setValue(false);
		_forward.setValue(false);
		_backward.setValue(false);
		_end.setValue(true);
		_proof = state.proof;
		_vars.clear();
		_binary_tree_visible.setValue(false);

	}

	/**
	 * Triggers a display of the Canvas containing a deduction tree.
	 * @param clause Clause for which the Deduction tree is generated.
	 */
	private void state_tree_show(String clause)
	{
		_selected_clause = _manage.getClause(clause);
		if(_binary_tree_visible.get())
			state_tree_hide();
		_binary_tree_visible.setValue(true);
	}

	/**
	 * Causes the view to Hide the Canvas with the Deduction tree.
	 */
	private void state_tree_hide()
	{
		_binary_tree_visible.setValue(false);
	}

	/**
	 * Updates _manager and acttual state of
	 * the algorithm after a step was removed.
	 * @param state Actual State of the Model.
	 */
	private void state_back(ResolutionState state)
	{
		_end.setValue(false);
		_binary_tree_visible.setValue(false);
		_proof = state.proof;
		_vars.clear();
		_vars.addAll(toList(state.vars));
		if(state.back_step_state == ModelState.SUBSUMPTION)
		{
			_resolution.setValue(false);
			_resolution_over.setValue(false);
			_forward.setValue(!state.forward_subsumption);
			_backward.setValue(!state.backward_subsumption);
		}
		else
		{
			_resolution.setValue(true);
			_resolution_over.setValue(true);
			_forward.setValue(false);
			_backward.setValue(false);
		}
		
		_steps.clear();
		_manage = new Line_Manager_Resolution(_steps);
		
		for(Step step : state.steps)
			_manage.add(step);
	}

	/**
	 * Generates a List from a Set of Variables.
	 * @param set Set of Variables
	 * @return List of Variables.
	 */
	private List<String> toList(Set<Variable> set)
	{
		List<String> vars = new ArrayList<>();
		for(Variable var : set)
			vars.add(var.toString());
		return vars;
	}

}
