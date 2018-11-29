package lehrtools.viewmodel;

import java.util.EventObject;

import lehrtools.model.state.State;

/**
 * Class that implements the Subject in an Observer pattern.
 */
public abstract class Subject {
	/**
	 * reference to the Observer
	 */
	protected Observer _observer;

	/**
	 * Method used by te observer ot retrieve the subjects State.
	 * @return An instance of a class containing the information to be updated.
	 */
	public abstract State getState();

	/**
	 * Method used by the Observer tocomunicate with the subject using Events.
	 * @param event Event instance.
	 */
	public abstract void execute(EventObject event);

	/**
	 * Method that registers an Observer with the Subject.
	 * @param observer Observer instance.
	 */
	public abstract void attach(Observer observer);

}
