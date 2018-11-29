package lehrtools.viewmodel;

/**
 * Abstract Class to impplment an Observer
 * for an Observer pattern.
 */
public abstract class Observer {
	/**
	 *  A reference to the object that is being observed
	 */
	protected Subject _subject;

	/**
	 * Method the Subject can trigger when its state changes.
	 */
	public abstract void update();

}
