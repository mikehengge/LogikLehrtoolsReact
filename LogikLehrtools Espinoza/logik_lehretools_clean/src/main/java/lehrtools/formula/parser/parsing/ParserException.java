package lehrtools.formula.parser.parsing;

import lehrtools.formula.parser.lexing.Token;

/**
 * Custom Runtime Exception that is raised when the lexer or parser encounter
 * an invalid Input. This class inherits from the class RuntimeException. This includes 
 * a string that  can containing a message.
 */
@SuppressWarnings("serial")
public class ParserException extends RuntimeException {
	/**
	 * Stores tha Token that caused the exception during the execution
	 * of the parser.
	 * If the exception is raised b the lexer then it will remain null.
	 */
	private Token _token ;
	/**
	 * Constructor used by the lexer to when it needs to raise an exception
	 * @param msg : string containing information about what causes the 
	 * 				the exception.
	 */
	public ParserException(String msg) {
		super(msg);
		_token = null;
	}
	/**
	 * Constructor used by the parser to raise when it needs to raise an exception.
	 * 
	 * @param msg : string containing information about the nature of the error.
	 * @param token : contains information about the token that raised the exception.
	 */
	public ParserException(String msg, Token token)
	{
		super(msg);
		_token = token;
	}
	/**
	 * Method that returns a message containing the information stored
	 * int the class.
	 */
	public String getMessage()
	{
		
	    String msg = super.getMessage();
	    if (_token != null)
	    {
	      msg = "Syntax Fehler "+ _token.sequence;
	    }
	    return msg;
	 }

  
  
}