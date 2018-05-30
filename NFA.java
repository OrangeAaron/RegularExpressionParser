/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author JC
 */
public class NFA{
    
    public final static char epsilon = '~';
    private LinkedList<State> graph;
    private static int id = 0; // used to keep track of the state numbers. Static so that when NFA's are merged in concatenation, the states still have unique ids.
        
    
    public NFA()
    {
	this.graph = new LinkedList<State>();
	//this.graph = null;
    }
    
    private void setGraph(LinkedList<State> graph)
    {
	
	this.graph = graph;
    }
    
    public LinkedList<State> getGraph()
    {
	return this.graph;
    }
    
    private State createState()
    {
	State state = new State(id++);
	
	return state;
    }
    
    private void mergeWith(NFA nfaToMergeWith)
    {
        LinkedList<State> result = new LinkedList<>();
        result.addAll(this.graph);
        result.addAll(nfaToMergeWith.getGraph());
        this.setGraph(result);
    }
    
    public void print(){
	if(graph == null) {System.out.println("NFA has not been created!");}
    	else
	{
            System.out.println("Printing out GraphViz compatible state list");
	    for(State state : graph)
	    {
		state.print();
	    }
	    
	}
    }
    
    // generates the NFA
    public void generate(String postfixRegex){
	Stack<NFA> NFAStack  = new Stack<>();
	
	// make sure generate always uses empty stacks
	NFAStack.clear();
	
	for(int i = 0; i < postfixRegex.length(); ++i)
	{
            Parser.Operator op = Parser.getOperator(postfixRegex.charAt(i));
	    if(Parser.inAlpha(postfixRegex.charAt(i)))
	    {
		NFA basic = this.createBasicNFA(postfixRegex.charAt(i));
		NFAStack.push(basic);
		//System.out.println("Creating Basic NFA: " + regex.charAt(i));
	    }
	    else if(op == Parser.Operator.KLEENE)
	    {
		NFA nfa = this.createRepetition(NFAStack.pop());
		NFAStack.push(nfa);
	    }
            else if(op == Parser.Operator.CONCATENATION)
            {
                NFA rightSide = NFAStack.pop();
                NFA leftSide = NFAStack.pop();
                NFA nfa = this.createConcat(leftSide, rightSide);
                NFAStack.push(nfa);
            }
            else if(op == Parser.Operator.ALTERNATION){
                NFA rightSide = NFAStack.pop();
                NFA leftSide = NFAStack.pop();
                NFA nfa = this.createAltern(leftSide, rightSide);
                NFAStack.push(nfa);
            }
	}
	
        if(NFAStack.size() > 1)  throw new RuntimeException("The regular expression resulted in more than one nondeterministic finite automaton.");
        else if(NFAStack.size() < 1) throw new RuntimeException("The regular expression could not resolve to a nondeterministic finite automaton.");
        
	setGraph(NFAStack.pop().getGraph());
	
	this.getGraph().getLast().setAcceptingState(true);
        print();
    }
    
    // create concatenation
    public NFA createConcat(NFA lNFA, NFA rNFA){
        
        State linkedFromState = lNFA.getGraph().getLast();
        State linkedToState = rNFA.getGraph().getFirst();
        linkedFromState.addTransition(epsilon, linkedToState);
        lNFA.mergeWith(rNFA);
        return lNFA;
    }
    
        // create alternation
    public NFA createAltern(NFA lNFA, NFA rNFA){
        
        State firstStateOfLeft = lNFA.getGraph().getFirst();
        State firstStateOfRight = rNFA.getGraph().getFirst();
        State lastStateOfLeft = lNFA.getGraph().getLast();
        State lastStateOfRight = rNFA.getGraph().getLast();
        
	State start = this.createState();
	State end = this.createState();
	
        start.addTransition(epsilon, firstStateOfLeft);
        start.addTransition(epsilon, firstStateOfRight);
        
        lastStateOfLeft.addTransition(epsilon, end);
        lastStateOfRight.addTransition(epsilon, end);
        
        lNFA.mergeWith(rNFA);
        
        lNFA.getGraph().addFirst(start);
        lNFA.getGraph().addLast(end);
        
        return lNFA;
    }
    
    // create repetition
    public NFA createRepetition(NFA nfa){
	
	State start = this.createState();
	State end = this.createState();
	
	// add epsilon transition from start to end
	start.addTransition(epsilon, end);
	// add epsilon transition from start to first state on stack
	start.addTransition(epsilon, nfa.getGraph().getFirst());
	
	// add epsilon from last state of NFA on stack to first state of NFA
	// on stack
	nfa.getGraph().getLast().addTransition(epsilon, nfa.getGraph().getFirst());
	
	// add epsilon transition from last state of NFA on stack to last state
	nfa.getGraph().getLast().addTransition(epsilon, end);
	
	
	// add the new states to the NFA that was on the stack
	nfa.getGraph().addFirst(start);
	nfa.getGraph().addLast(end);
	
	// return the altered NFA
	return nfa;
    }
    

    // This function should create a base NFA for single characters and push on
    // on the NFA stack
    public NFA createBasicNFA(char transition)
    {
	State s0 = this.createState();
	State s1 = this.createState();
	
	s0.addTransition(transition, s1);
	
	NFA nfa = new NFA();
	
	// add states to new NFA
	nfa.getGraph().addLast(s0);
	nfa.getGraph().addLast(s1);
	
	return nfa;
    }
    
}
