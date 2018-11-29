package lehrtools.formula.parser.lexing;

import java.util.LinkedList;

import lehrtools.formula.parser.parsing.ParserException;

/**
 * Handles the tokenizing of a given input string.
 * The constructor takes the symbols representing
 * the operation and, or and not and uses these to
 * divide the input string into tokens.
 * The tokens are later used by the parser to create 
 * the formula.
 *
 */


public class Lexer_Formula {
	/**
	 * Class in charge of dividing the string in
	 * Tokens using the regular expressions set on
	 * the constructor
	 */
	private Tokenizer tokenizer;
	
	
	/**
	 * Constructor takes symbols that will represent the operations
	 * and, or and not as arguments.
	 * @param and : symbol that will represent the operation and.
	 * @param or  : symbol that will represent the operation or.
	 * @param neg : symbol that will represent the operation not.
	 */
	public Lexer_Formula(String and, 
				 String or, 
				 String neg)
	{
		
		  tokenizer = new Tokenizer();
		  tokenizer.add("\\(", 5); // open bracket
		  tokenizer.add("\\)", 4); // close bracket
		  tokenizer.add(neg, 3); // negation		 
		  tokenizer.add(or, 1); //or
		  tokenizer.add(and, 2); // and
		  tokenizer.add("[a-zA-Z0-9][a-zA-Z0-9_]*", 6); // variable
		
	}
	/**
	 * Method that turns a String representing a formula into a list of Tokens
	 * needed by the parser to generate the instance of Formula it represents.
	 * @param str : String representing a Formula
	 * @return : return a list of Tokens
	 * @throws ParserException : exception thrown when a symbol that is not allowed 
	 * 			is found.
	 */
	public LinkedList<Token> tokenize(String str) throws ParserException
	{
		tokenizer.tokenize(str);
		return tokenizer.getTokens();
	}
	

}

