package lehrtools.formula.parser.lexing;

import java.util.LinkedList;

import lehrtools.formula.parser.parsing.ParserException;

/**
 * Handles the tokenizing of a given input string representing
 * a formula in normal form.
 * The tokens are later used by the parser to create the formula.
 */
public class Lexer_Clause  {
	/**
	 * Class in charge of divinding the string in
	 * Tokens using the regular expressions set on
	 * the constructor
	 */
	private Tokenizer tokenizer;
	/**
	 * Sets up the regular expressions expected to 
	 * be contained in the string representing the 
	 * input. 
	 */
	public Lexer_Clause()
	{
		
		  tokenizer = new Tokenizer();
		  tokenizer.add("-", 3); //negation
		  tokenizer.add("\\{", 7); // open brace
		  tokenizer.add("\\}", 8); // close brace
		  tokenizer.add(",", 9); // comma	 
		  tokenizer.add("[a-zA-Z0-9][a-zA-Z0-9_]*", 6); // variable
		
	}
	/**
	 * Method that turns a String representing a formula into a list of Tokens
	 * needed by the parser to generate the instance of Formula_NF it represents.
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
