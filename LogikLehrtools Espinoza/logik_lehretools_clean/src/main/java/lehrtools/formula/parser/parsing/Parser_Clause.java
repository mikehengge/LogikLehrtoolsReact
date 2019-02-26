package lehrtools.formula.parser.parsing;

import java.util.LinkedList;

import lehrtools.formula.Variable;
import lehrtools.formula.parser.lexing.Token;
import lehrtools.model.Clause;
import lehrtools.model.Formula_NF;
import lehrtools.model.Literal;
/**
 * Parses a given list of token based on the following grammar:
 * <ul>
 * <li>NORMALFORM -&gt; open_brace CLAUSE CLAUSES .
 * <li>CLAUSES -&gt; comma CLAUSE CLAUSES .
 *<li> CLAUSES -&gt; close_brace .
 *<li> CLAUSE -&gt; open_brace LITERAL  LITERALS .
 *<li> LITERALS -&gt; comma LITERAL LITERALS .
 * <li>LITERALS -&gt; close_brace .
 * <li>LITERAL -&gt; not variable.
 * <li>LITERAL -&gt; variable.
 * </ul>
 * The method parse(...) will start the generation of the Formula.
 */
public class Parser_Clause {
	
	/**
	   * List of tokens representing the given input.
	   */
	  private LinkedList<Token> tokens;
	  /**
	   * next token to be processed.
	   */
	  private Token lookahead;
	  
	  /**
	  *  <p>
	  *  Starts the generation of the Formula_NF corresponding to the given 
	  *  input string.
	  *  Should the input not be a valid Formula then an ParserException is raised.
	  *  </p>
	  * @param tokens : 
	  * 				List of tokens generated by the Lexer_Clause class.
	  * @return : 
	  * 			An instance of Formula representing the given input.
	  */
		@SuppressWarnings("unchecked")
		public Formula_NF parse(LinkedList<Token> tokens)
		  {
		    this.tokens = (LinkedList<Token>) tokens.clone();
		    lookahead = this.tokens.getFirst();
		    
		    Formula_NF formula = normalform();

		    if (lookahead.token != Token.EPSILON)
		      throw new ParserException("Unexpected symbol " +  lookahead.sequence + " found");
		    
		    return formula;
		  }
		/**
		 * Method used to retrieve the next token to be processed by the parser,
		 * if the end of the list is reached a EPSILON token will be set, to signal that
		 * the end. 
		 */
		private void nextToken()
		  {
		    tokens.pop();
		    // at the end of input we return an epsilon token
		    if (tokens.isEmpty())
		      lookahead = new Token(Token.EPSILON, "");
		    else
		      lookahead = tokens.getFirst();
		  }
		
		/**
		 * Applies the rules LITERAL -&gt; not variable | variable.
		 * Creates a positive literal instance if the lookahead.token of 
		 * the type Variable or negative literal instance in case the 
		 * lookahead token is of typeNOT. The Variable is named after
		 * the sequence stored in the token.
		 * 
		 * Should the lookahead.token not match any of the above
		 * mentioned types, then an ParserEsception is raised.
		 * 
		 * @return Literal representing the given input.
		 */
		private Literal literal()
		{
			 if(lookahead.token == Token.VARIABLE)
			  {
				 
				 Literal lit  = new Literal(new Variable(lookahead.sequence), true);
				 nextToken();
				 return lit;
			  }
			 if(lookahead.token == Token.NOT)
			 {
				 nextToken();
				 Literal lit  = new Literal(new Variable(lookahead.sequence), false);
				 nextToken();
				 return lit;
				 
			 }
			 
			 if (lookahead.token == Token.EPSILON)
				    throw new ParserException("Unexpected end of input");
			 else
				    throw new ParserException("Unexpected symbol %s found", lookahead);
		}
		/**
		 * Applies the rule  LITERALS -&gt; comma LITERAL LITERALS | close_brace.
		 * If the lookahead.token is of type COMMA then a literal is
		 * parsed and the through recursion is checked if there are more literals 
		 * or if the clause ends there.
		 * If the lookahead.token is of type CLOSE_BRACES, then the end of the 
		 * clause is reached and nothing mores is parsed.
		 * 
		 * Should the lookahead.token not match any of the above
		 * mentioned types, then an ParserEsception is raised.
		 * 
		 * @param clause : stores all the literals that have been found.
		 * @return a set of literals contained in a Clause class. 
		 */
		private Clause literals(Clause clause)
		{
			if(lookahead.token == Token.COMMA)
			{
				nextToken();
				clause.addLiteral(literal());
				return literals(clause);
			}
			if(lookahead.token == Token.CLOSE_BRACES)
			{
				nextToken();
				return clause;
			}
			
			if (lookahead.token == Token.EPSILON)
			    throw new ParserException("Unexpected end of input");
			else
			    throw new ParserException("Unexpected symbol %s found", lookahead);
		}
		/**
		 * Applies the rule CLAUSE -&gt; open_brace LITERAL LITERALS
		 * If the lookahead.token is of type OEN_BRACES, then it will 
		 * try to parse a clause by looking of all the literals in a 
		 * recursive manner.
		 * 
		 * Should the lookahead.token not match any of the above
		 * mentioned types, then an ParserEsception is raised.
		 * 
		 * @return a Clause
		 */
		private Clause clause()
		{
			if(lookahead.token == Token.OPEN_BRACES)
			{
				nextToken();
				Clause clause = new Clause();
				clause.addLiteral(literal());
				return literals(clause);
			}
			
			if (lookahead.token == Token.EPSILON)
			    throw new ParserException("Unexpected end of input");
			else
			    throw new ParserException("Unexpected symbol %s found", lookahead);
			
		}
		/**
		 * Applies the rule CLAUSES -&gt; comma CLAUSE CLAUSES | close_brace;
		 * If lookahead.token is of type COMMA then a Clause is parsed,and 
		 * through recursion it will look for more Clauses.
		 * If the lookahead.token if of type CLOSE_BRACES then the end of 
		 * the end of the formula has been reached.
		 * 
		 * Should the lookahead.token not match any of the above
		 * mentioned types, then an ParserEsception is raised.
		 * 
		 * @param formula that contains already found clauses
		 * @return formula contaning the found clauses.
		 */
		private Formula_NF clauses(Formula_NF formula)
		{
			if(lookahead.token == Token.COMMA)
			{
				nextToken();
				formula.add_clause(clause());
				return clauses(formula);
			}
			if(lookahead.token == Token.CLOSE_BRACES)
			{
				nextToken();
				return formula;
			}
			
			if (lookahead.token == Token.EPSILON)
			    throw new ParserException("Unexpected end of input");
			else
			    throw new ParserException("Unexpected symbol %s found", lookahead);
		}
		/**
		 * Applies the rule NORMALFORM -&gt; open_brace CLAUSE CLAUSES
		 * This method starts all the calculations by trying to parse a first 
		 * clause, and then through recursion more clauses.
		 * @return a formula representing the given input.
		 */
		private Formula_NF normalform()
		{
			if(lookahead.token == Token.OPEN_BRACES)
			{
				nextToken();
				Formula_NF formula = new Formula_NF();
				formula.add_clause(clause());
				return clauses(formula);
			}
			
			if (lookahead.token == Token.EPSILON)
			    throw new ParserException("Unexpected end of input");
			else
			    throw new ParserException("Unexpected symbol %s found", lookahead);
		}

}
