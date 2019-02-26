package lehrtools.formula.parser;

/**
 * Construct that stores the information of an input language
 * for a propositional formula.
 *
 */
public class Input_Language {
	/**
	 * The name that describes the language
	 */
	public final String name;
	/**
	 * A short description of the Language
	 */
	public final String description;
	/**
	 * Symbol used as the and operator.
	 */
	public final String and;
	/**
	 * Symbol used as the or operator.
	 */
	public final String or;
	/**
	 * Symbol used as the negation
	 */
	public final String neg;
	/**
	 * And operator as plain String
	 */
	public final String and_as_string;
	
	/**
	 * Constructor for Input_Language.
	 * @param name : Name of the Language
	 * @param description : A brief description of the LAnguage.
	 * @param and : Symbol representing the and operator.
	 * @param or : Symbol representing the or operator.
	 * @param neg : Symbol representing the negation operator.
	 */
	public Input_Language(String name,
						  String description,
						  String and,
						  String or,
						  String neg,
						  String and_as_string)
	{
		this.name = name;
		this.description = description;
		this.and = and;
		this.or = or;
		this.neg = neg;
		this.and_as_string = and_as_string;
		
	}

}
