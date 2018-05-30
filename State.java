/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

//* Library
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author JC
 */
public class State {
    
    // * Variables : State's id and Transitions
    private int id; 
    private Map<Character, Set<State>> transitions;
    private boolean acceptingState;
    
    public State(int id)
    {
	this.id = id;
	this.acceptingState = false;
	this.transitions = new HashMap<>();
    }
    
    public boolean isAccepting()
    {
	return this.acceptingState;
    }
   
   
    public void print(){
	if(!transitions.isEmpty()){
	    for(Character c : transitions.keySet())
	    {
		for(State state : transitions.get(c)){
		    System.out.print(this.id + " -> " + state.getID() + " [ label =  \"" + c + "\" ];");
                    System.out.println("");
		}
	    }
	}
	
    }
    public void addTransition(char input, State state){
	
	
	// if this state does not already have a transition for
	// the supplied input
	if(!transitions.containsKey(input)){
	    // create a new set
	    Set set = new HashSet();
	    // add the state to the set
	    set.add(state);
	    // put the set with one state as a transition
	    this.transitions.put(input, set);
	}
	else // there is already a transition 
	{
	    this.transitions.get(input).add(state);
	}
    }
    
    // set transitions for the state
    public void setTransitions(Map<Character, Set<State>> transitions)
    {
	this.transitions = transitions;
    }
    
    // get the id of the state
    public int getID(){
	return this.id;
    }
    // get all transitions from the state
    public Map<Character, Set<State>> getTransitions(){
	
	return this.transitions;
    }
    
    // get transitions for a specific input
    public Set<State> getTransitions(char c){
	if(this.transitions.get(c) == null)
	{
	    return new HashSet<> ();
	}
	else{
	    return this.transitions.get(c);
	}
	    
    }
    
    // set whether or not the state is an accepting state
    public void setAcceptingState(boolean isAccepting){
	this.acceptingState = isAccepting;
    }
    
    // set the id for the state
    public void setStateID(int id){
	this.id = id;
    }
    
   
    
}
