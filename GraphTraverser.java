/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Stack;

/**
 *
 * @author khalil, awilson40, jcanida
 */
//* Traversing the Graph
public class GraphTraverser {

    public static Set<String> TraverseGraph(NFA nfa, int maxLenAccept) {
        LinkedHashSet<State> q = new LinkedHashSet<>();
        Map<Integer, HashSet<String>> stateValues = new HashMap();
        LinkedList<State> graph = nfa.getGraph();

        q.add(graph.getFirst());
        //getting the state ids and transitions
        while (!q.isEmpty()) {
            Iterator<State> i = q.iterator();
            State currentState = i.next();
            i.remove();

            Map<Character, Set<State>> currentStateTransitions = currentState.getTransitions();

            //Check for the transitions and states
            if (currentStateTransitions != null) {
                for (char transitionChar : currentStateTransitions.keySet()) {

                    HashSet<String> valuesToPushToNextStates = createPushValues(stateValues.get(currentState.getID()), transitionChar, maxLenAccept);
                    //System.out.println("Preparing to push from " + currentState.getID());
                    q.addAll(pushThemValues(valuesToPushToNextStates, currentStateTransitions, stateValues, transitionChar));
                }
            }
        }
        if (maxLenAccept == 0) {
            for (State state : graph) {
                if (state.isAccepting()) {
                    //System.out.println("Returning values.");
                    if (stateValues.get(state.getID()).contains("~")) {
                        return new HashSet<>(Collections.singletonList("~"));
                    }
                }
            }
        } else {
            for (State state : graph) {
                if (state.isAccepting()) {
                    //System.out.println("Returning values.");
                    return stateValues.get(state.getID());
                }
            }
        }
        return null;
    }

    private static HashSet<String> createPushValues(HashSet<String> currentStateValues, char transitionChar, int maxLenAccept) {
        //Doing modifications on the values to push.
        HashSet<String> result = new HashSet<String>();
        if (currentStateValues == null) {
            //System.out.println("Starting new input string.");
            result.add(Character.toString(transitionChar));

        } else if (transitionChar != '~' || (!currentStateValues.isEmpty())) {
            for (String value : new HashSet<>(currentStateValues)) {
                String valueToAdd = value;

                if (transitionChar != '~') {
                    valueToAdd += transitionChar;
                }

                if ("~".equals(value)) {
                    result.remove(value);
                    if (maxLenAccept != 0) {
                        result.add(Character.toString(transitionChar));
                    }
                } else if (valueToAdd.length() <= maxLenAccept) {
                    //Add the transition char to each set
                    result.remove(value);
                    result.add(valueToAdd);
                    //System.out.println("Setting up " + value + " to be added.");
                } else {
                    //Over the length so we won't push it along.
                    //System.out.println("Value is too long for a transition.");
                    result.remove(value);
                }
            }
        }
        return result;
    }

    private static List<State> pushThemValues(HashSet<String> valuesToPush, Map<Character, Set<State>> currentStateTransitions, Map<Integer, HashSet<String>> valueMap, char transitionChar) {
        boolean addStateToQueue = false;
        List<State> result = new Stack<>();

        //System.out.println("Trying to push to states.");
        //Try to push to each state with the current transition character
        for (State currentTransitionState : currentStateTransitions.get(transitionChar)) {
            addStateToQueue = false;
            HashSet<String> thisStateValue = valueMap.get(currentTransitionState.getID());
            if (thisStateValue == null) {
                thisStateValue = new HashSet<>();
            }
            //check to see if valuesToPush are contained in the set or are empty
            for (String pushValue : valuesToPush) {
                if (pushValue != null) {
                    if (!thisStateValue.contains(pushValue)) {
                        //add values
                        //System.out.println("Pushing " + pushValue + " to state " + currentTransitionState.getID());
                        if (!valueMap.containsKey(currentTransitionState.getID())) {
                            valueMap.put(currentTransitionState.getID(), new HashSet<>());
                        }
                        valueMap.get(currentTransitionState.getID()).add(pushValue);
                        addStateToQueue = true;
                    }
                }
            }
            //put states on queue if changed
            if (addStateToQueue || (transitionChar == '~' && (!valuesToPush.contains("~")))) {
                result.add(currentTransitionState);
            }
        }
        return result;
    }

}
