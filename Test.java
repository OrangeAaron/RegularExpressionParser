/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author JC
 */
public class Test {
    
    private static String getRandomString( Set<String> stringSet)
    {
	Random rand = new Random();
	
	int rand_index = 0;
	
	if(stringSet.size() > 0)
	{
	    rand_index = rand.nextInt(stringSet.size());
	}
	
	for(String string : stringSet)
	{
	    --rand_index;
	    if(rand_index == 0)
	    {
		return string;
	    }
	}
	
	return null;
    }
    
    private static Character getRandomChar()
    {
	Random rand = new Random();
	String alphabet = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	int rand_char_index = rand.nextInt(alphabet.length() - 1);
	return alphabet.charAt(rand_char_index);
    }
   
    
    public static void printDetails(String s, String regex, int maxSize)
    {
	boolean sizeGood = false;
	System.out.print(s);
	System.out.print(" : ");
	System.out.println(s.matches(regex));
	if(s.length() <= maxSize) sizeGood = true;
	else sizeGood = false;
	System.out.println("Size: " + sizeGood);
    }
    
    public static void TestRegex(String regex, Set<String> stringSet, int maxSize, boolean printAll)
    {
	if(printAll)
	{
	    
	    System.out.println("Checking original set.");
	    for(String string : stringSet)
	    {
		printDetails(string, regex, maxSize);	
	    }
	}
	else
	{
	    System.out.println("Checking original set.");
	    for(String string : stringSet)
	    {
		if(!string.matches(regex))
		{
		    System.out.println(string + " does not match regex!");
		}
		if(string.length() > maxSize)
		{
		    System.out.println(string + "length " + string.length() + " is greater than " + maxSize);
		}
			
	    }
	}
	
    }
	    
    
}
