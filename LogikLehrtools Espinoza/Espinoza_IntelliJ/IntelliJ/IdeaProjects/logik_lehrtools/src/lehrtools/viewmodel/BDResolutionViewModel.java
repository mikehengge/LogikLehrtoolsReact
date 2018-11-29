
package lehrtools.viewmodel;

import java.util.ArrayList;
import java.util.EventObject;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lehrtools.model.Clause;
import lehrtools.model.state.*;
import lehrtools.viewmodel.information.Line_Manager_BD_Resolution;
import lehrtools.viewmodel.information.Line;
import lehrtools.viewmodel.information.Line_Manager_Resolution;
/***
 * Class that corresponds to the ViewModel in the MVVM Pattern. It is in charge of managing user input from the View,
 * here BD_Resolution_Controller, and updates fromt eh model , here BDResolution class.
 */
public class BDResolutionViewModel extends Observer {

	/**
	 * List with te information of the steps the View needs to display tehm correctly.
	 */
	public ObservableList<Line>  _lines;
	/**
	 * BooleanProperty that controls the visibility of the button_resolution.
	 */
	public BooleanProperty _resolution;
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
	 * Processes the steps into Lines for the view and sets selected term.
	 */
	private Line_Manager_BD_Resolution _line_manager;

	/**
	 * Constructor
	 * @param subject Takes the Subject it Observes as an argument.
	 */
	public BDResolutionViewModel(Subject subject)
	{
		_subject = subject;
		
		final ArrayList<Line> line_list = new ArrayList<>();
		_lines = FXCollections.observableList(line_list);
		
		_line_manager = new Line_Manager_BD_Resolution(_lines);
		
		_resolution = new SimpleBooleanProperty(true);
		_forward = new SimpleBooleanProperty(false);
		_backward = new SimpleBooleanProperty(false);
		_end = new SimpleBooleanProperty(false);
		
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
		if(((ModelEvent)event).state() == ModelState.SHOW_CALCULATION)
			_line_manager.select_calculation(((ModelEvent) event).information());
		else
			_subject.execute(event);
	}
	
	
	@Override
	public void update() {

		ResolutionState state = (ResolutionState)_subject.getState();
		
		switch(state.state)
		{
		case BEFORE_RESOLUTION : change_state_resolution(state);
								 break;
		case RESOLUTION : state_resolution(state);
						  break;
		case BEFORE_SUBSUMPTION : change_state_subsumption(state);
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
	 * Adds a Resolution step to the _line_manager.
	 * @param state Actual state fo teh model
	 */
	private void state_resolution(ResolutionState state)
	{
		_line_manager.add(state.steps.getLast());
	}
	/**
	 * Adds a Resolution step to the _line_manager and adjusts the
	 * visibility of the ssubsumption buttons.
	 * @param state Actual state fo teh model
	 */
	private void state_subsumption(ResolutionState state)
	{
		_line_manager.add(state.steps.getLast());
		_forward.set(!state.forward_subsumption);
		_backward.set(!state.backward_subsumption);
	}

	/**
	 * Displays the End button and hides  the resolution and subsumption buttons.
	 * @param state Actual state fo teh model
	 */
	private void state_end(ResolutionState state)
	{
		_resolution.setValue(false);
		_forward.setValue(false);
		_backward.setValue(false);
		_end.setValue(true);
		
	}

	/**
	 * Tells the View to display the buttons for the resolution step and
	 * hide the buttons for the subsumption.
	 * @param state Actual state fo teh model
	 */
	private void change_state_resolution(ResolutionState state)
	{
		_resolution.setValue(true);
		_forward.setValue(false);
		_backward.setValue(false);
		_end.setValue(false);
	}
	/**
	 * Tells the View to display the buttons for the subsumption step and
	 * hide the buttons for the resolution.
	 * @param state Actual state fo teh model
	 */
	private void change_state_subsumption(ResolutionState state)
	{
		_resolution.setValue(false);
		_forward.setValue(true);
		_backward.setValue(true);
		_end.setValue(false);
	}
	/**
	 * Updates _line_manager and acttual state of
	 * the algorithm after a step was removed.
	 * @param state Actual State of the Model.
	 */
	private void state_back(ResolutionState state)
	{
		_end.setValue(false);
		if(state.back_step_state == ModelState.SUBSUMPTION)
		{
			_resolution.setValue(false);
			_forward.setValue(!state.forward_subsumption);
			_backward.setValue(!state.backward_subsumption);
		}
		else
		{
			_resolution.setValue(true);
			_forward.setValue(false);
			_backward.setValue(false);
		}

		_lines.clear();
		_line_manager = new Line_Manager_BD_Resolution(_lines);

		for(Step step : state.steps)
			_line_manager.add(step);
	}
	
}
