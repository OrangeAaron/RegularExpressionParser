/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Stack;

/**
 *
 * @author khalil, awilson40, jcanida
 */
public class Parser {
    
	private Stack<Character> AlphaStack;
	private Stack<Character> OpStack;
	
	Parser() {
	    AlphaStack = new Stack();
	    OpStack = new Stack();
	}   
	
        // Possible Operators
	public enum Operator{
	    NONOPERATOR, ALTERNATION, CONCATENATION, KLEENE   
	}
	public static boolean inAlpha(char c)
	{
	    return Character.isLetterOrDigit(c);
	}
	// Concatenation
        public boolean isConcat(char previous, char current)
        {
           if (inAlpha(current) || current == '(')
           {
               if(inAlpha(previous) || previous == ')' || previous == '*') return true;
           }
           return false;
        }
        
	public String formatRegEx(String input){
	               
	    StringBuffer formattedString = new StringBuffer(input.length());
	    
	    for(int i = 0; i < input.length(); ++i)
	    {
		
		if(Character.isWhitespace(input.charAt(i)))
		{
		    continue;
		}
		else if(formattedString.length() > 0 && isConcat(formattedString.charAt(formattedString.length() - 1),input.charAt(i)))
		{
		    formattedString.append('.');
		    formattedString.append(input.charAt(i));
		}
		else
		{
		    formattedString.append(input.charAt(i));
		}
		
		
	    }
		
	   return  formattedString.toString();
	}
	
	public static Operator getOperator(char c){
	    
	    switch (c)
	    {
		case '.': return Operator.CONCATENATION;
		case '*': return Operator.KLEENE;
		case '|': return Operator.ALTERNATION;
		default:  return Operator.NONOPERATOR;
	    }
	}
	
        public String ParseRegExString(String input){
	    
	    input = formatRegEx(input);
	    StringBuffer output = new StringBuffer();
	    if(input == null){
		throw new IllegalArgumentException("No regular expression found.");
	    }
	    
	    for(int i = 0; i < input.length(); ++i){
		char c = input.charAt(i);
		if(inAlpha(c)){
		    output.append(c);
		}
		else if(getOperator(c) == Operator.KLEENE)
		{
		    output.append(c);
		}
		else if(getOperator(c) != Operator.NONOPERATOR)
		{
		    while(!OpStack.empty()
			  && 
			  getOperator(c).compareTo( getOperator(OpStack.peek())) <= 0
			  && 
			  (OpStack.peek() != '('))
			  
		    {
			output.append(OpStack.pop());
		    }   
		    OpStack.push(c);
		}
		else if(c == '(')
		{
		    OpStack.push(c);
		}
		else if(c == ')')
		{
		    try{		    
			while(OpStack.peek() != '(')
			{
			    output.append(OpStack.pop());
			}
		    }catch(Exception e)
		    {
			System.out.println("Mismatch Parentheses");
			throw e;
		    }
		    OpStack.pop();		    
		}
	    }
	    
	    while(!OpStack.empty())
	    {
		output.append(OpStack.pop());
	    }
	    
        
        return output.toString();
    }
    
}
