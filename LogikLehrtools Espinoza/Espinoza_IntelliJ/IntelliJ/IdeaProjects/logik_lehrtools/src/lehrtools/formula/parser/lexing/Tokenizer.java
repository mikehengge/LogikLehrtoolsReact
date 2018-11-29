package lehrtools.formula.parser.lexing;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lehrtools.formula.parser.parsing.ParserException;

/**
 * In charge of  separating the input string into short bits that 
 * represent the basic entities in the expression. 
 * java.util.regex package was used to do the work. This allows a 
 * general implementation that can be used for any kind of grammar.
 * The class contains a inner class 
 * 		TokenInfo that contains the information about the individual 
 * 		tokens that are allowed as input.
 * 		 
 * 
 */
public class Tokenizer
{
  
  /**
   * Stores the information that is allowed as input
   */
  private LinkedList<TokenInfo> tokenInfos;
  /**
   * List that stores the token sequence representing the given string.
   */
  private LinkedList<Token> tokens;
  
  public Tokenizer()
  {
    tokenInfos = new LinkedList<TokenInfo>();
    tokens = new LinkedList<Token>();
  }
  
  /**
   *  Takes regular expression string and a token code as parameters. 
   *  The method will then the "^" character to the user supplied regular expression. 
   *  It causes the regular expression to match only the beginning of a string.
   *  
   * @param regex : string representing the regular expression.
   * @param token : the token code.
   */
  public void add(String regex, int token)
  {
    tokenInfos.add(new TokenInfo(Pattern.compile("^("+regex+")"), token));
  }
  /**
   * Method that divides input into tokens.
   * Should the input not be a valid string it will raise an exception.
   * @param str : string to be tokenized
   * @exception ParserException : Custom RuntimeException
   */
  public void tokenize(String str) throws ParserException
  {
    String s = str.trim();
    tokens.clear();
    while (!s.equals(""))
    {
      boolean match = false;
      for (TokenInfo info : tokenInfos)
      {
        Matcher m = info.regex.matcher(s);
        if (m.find())
        {
          match = true;
          String tok = m.group().trim();
          s = m.replaceFirst("").trim();
          tokens.add(new Token(info.token, tok));
          break;
        }
      }
      if (!match) throw new ParserException("Syntaxfehler. Unbekanntes Zeichen.: "+s);
    }
  }
  /**
   * Method in charge of retrieving the processed input
   * 
   * @return : LinkedList containing the tokens that were found.
   */
  public LinkedList<Token> getTokens()
  {
    return tokens;
  }
  /**
   *  TokenInfo contains two fields. 
   *  The regular expression that is used to match the input string against the token is stored in the Pattern regex. 
   *  Pattern objects are stored instead of the regular expression string to improve performance. Regular expressions 
   *  have to be compiled which takes time. Pattern stores the regular expression in compiled form. 
   *  The code of the token is given by the integer value token. Each type of token should has its own code.
   */
  
  private class TokenInfo
  {
    public final Pattern regex;
    public final int token;

    public TokenInfo(Pattern regex, int token)
    {
      super();
      this.regex = regex;
      this.token = token;
    }
    
  }
  

  
}
