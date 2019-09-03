package generators.cryptography.helpers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Tokenizer {

	
	/**
	 * tokenizes the given formula 
	 * 
	 * @param formula
	 * 					given formula as a string
	 * @param ops
	 * 					given operations
	 * @param funcs
	 * 					given functions
	 * 
	 * @return tokenized list
	 */
     public List<Token> tokenize(String formula, Set<String> ops, Set<String> funcs)
     {
        int index = 0;
        int length = formula.length();
        List<Token> result = new ArrayList<Token>();
 
        Set<String> opsAndFuncs = new HashSet<String>();
        opsAndFuncs.addAll(ops);
        opsAndFuncs.addAll(funcs);
 
        while(index < length){
        	// current char
            char curr = formula.charAt(index);
 
            // do not need white space
            if(!Character.isWhitespace(curr))
            {
                if(curr == '(')
                {
                    result.add(new Token('('));
                    index++;
                }
                else if(curr == ')')
                {
                    result.add(new Token(')'));
                    index++;
                }
                // if there is a number, get full number
                else if(Character.isDigit(curr) || curr == '.')
                {
                    int end = index+1;
                    boolean pointSeen = curr == '.';
 
                    while(end < length)
                    {
                        char next = formula.charAt(end);
                        if(Character.isDigit( next))
                            end++;
                        else if(next == '.' && !pointSeen)
                        {
                            pointSeen = true;
                            end++;
                        }
                        else
                            break;
                    }
 
                    result.add(new Token(formula.substring(index, end), true));
                    index = end;
                }
                else
                {
                    int signLength = 0;
                    String sign = null;
 
                    for(String check : opsAndFuncs)
                    {
                        if(formula.startsWith( check, index))
                        {
                            if(check.length() > signLength)
                            {
                                signLength = check.length();
                                sign = check;
                            }
                        }
                    }
 
                    if( sign == null )
                        throw new IllegalArgumentException("there is something wrong");
 
                    index += signLength;
                    result.add(new Token(sign, false)); 
                }
            }
            else
                index++;
        }
        return result;
    }
}
