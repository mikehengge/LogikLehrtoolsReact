package lehrtools.formula.parser.lexing;

/**
 * Stores information about the tokens that have been found. 
 * It holds the token code that identifies the regular 
 * expression and the string storing associated information.
 */
public class Token
{

	/** Token id for the epsilon terminal */
	public static final int EPSILON = 0;
	/** Token id for or */
	public static final int OR = 1;
	/** Token id for and */
	public static final int AND = 2;
	/** Token id for not */
	public static final int NOT = 3;
	/** Token id for opening brackets */
	public static final int OPEN_BRACKET = 5;
	/** Token id for closing brackets */
	public static final int CLOSE_BRACKET = 4;
	/** Token id for variable names */
	public static final int VARIABLE = 6;
	/** Token id for opening braces */
	public static final int OPEN_BRACES = 7;
	/** Token id for closing braces */
	public static final int CLOSE_BRACES = 8;
	/** Token id for a comma */
	public static final int COMMA = 9;
	  
	/**
	 * Unique identifier that characterizes the Token
	 */
	public final int token;
	/**
	 * Information associated to the token. For this implementation
	 * it will only be used to store the Variable name.
	 */
	public final String sequence;
	/**
	 * Constructor that takes 2 paramenters, token and sequence.
	 * @param token : integer identifying what type of token it is.
	 * @param sequence : additional information associated to the token.
	 */
	public Token(int token, String sequence)
	{
  	
  	
		super();
		this.token = token;
		this.sequence = sequence;
	}
  
}