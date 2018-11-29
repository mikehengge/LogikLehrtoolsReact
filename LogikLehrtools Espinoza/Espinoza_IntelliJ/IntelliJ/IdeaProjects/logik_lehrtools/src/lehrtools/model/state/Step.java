package lehrtools.model.state;

/**
 * Contains all the information needed to describe a step for any of the procedures offered by
 * the application. It usually  is the result of the calculation after a user input.
 */
public abstract class Step {
	/**
	 * Decribes the step that was executed.
	 */
	public final ModelState type;
	/**
	 * Used in the Resolution and Back Dual Resolution an represents the
	 * iteration number of the algorithm.
	 */
	public final int index;

	/**
	 * Constructor
	 * @param type The  step that was executed
	 * @param index The iteration number of the algorithm.
	 */
	public Step(ModelState type, int index)
	{
		this.type = type;
		this.index = index;
		
	}
	
	
	

}
