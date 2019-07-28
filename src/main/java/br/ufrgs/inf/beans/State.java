package br.ufrgs.inf.beans;

import br.ufrgs.inf.agent.Action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class State implements Serializable {

    private static List<State> allStates = new ArrayList<State>();
    private int stateNumber = 0;
    private List<Action> actions = new ArrayList<Action>();
    private double expectedReward = 0;

    public State(List<Action> act) {

        actions = act;

        for (Action action : actions) {

            expectedReward += action.expectedReward();
        }

        expectedReward = expectedReward / actions.size();
        stateNumber = allStates.size();
        allStates.add(this);
    }


    public static List<State> getAllStates() {

        return allStates;
    }

    public int getStateNumber() {

        return stateNumber;
    }

    public List<Action> getActions() {

        return actions;
    }

    public double getExpectedReward() {

        return expectedReward;
    }

    public void setActions(List<Action> newActions) {

        actions = newActions;

        for (Action action : actions) {

            expectedReward += action.expectedReward();
        }

        expectedReward = expectedReward / actions.size();
    }

}