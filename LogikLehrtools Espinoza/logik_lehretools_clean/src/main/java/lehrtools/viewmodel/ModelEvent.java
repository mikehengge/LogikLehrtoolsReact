package lehrtools.viewmodel;

import java.util.EventObject;

import lehrtools.model.state.ModelState;

/**
 * Class used to pass information about user input to the ViewModel or the Model.
 */
@SuppressWarnings("serial")
public class ModelEvent extends EventObject {
	/**
	 * Represents the type of the user input, usually as a form of a command ot
	 * be executed.
	 */
	private ModelState _state;
	/**
	 * Additional information needed to execute the expected input.
	 */
	private String _information;

	/**
	 * Constructoe
	 * @param o Object that created the ModelEvent.
	 * @param state State representing the command to be executed.
	 * @param information Additional information needed to execute the expected input.
	 */
	public ModelEvent(Object o, ModelState state, String information)
	{
		super(o);
		_state = state;
		_information = information;
	}
	
	public ModelState state() { return _state;}
	public String information() { return _information;}

}
