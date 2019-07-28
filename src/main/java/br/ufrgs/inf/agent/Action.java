package br.ufrgs.inf.agent;

import br.ufrgs.inf.beans.State;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Action {

    private static List<Action> allActions = new ArrayList<Action>();
    private List<Integer> rewards = new ArrayList<Integer>();
    private State nextState = null;
    private int expectedReward = 0;
    private Random prng = null;

    public Action(List<Integer> rew) {

        rewards = rew;

        for (int reward : rewards) {

            expectedReward += reward;
        }

        expectedReward = expectedReward / rewards.size();
        allActions.add(this);
        prng = new Random();
    }

    public static List<Action> getAllActions() {

        return allActions;
    }

    public State getNextState() {

        return nextState;
    }

    public void setNextState(State state) {

        nextState = state;
    }

    public int giveReward() {

        int i = (int) Math.floor(rewards.size()*prng.nextDouble());
        return rewards.get(i);
    }

    public double expectedReward() {

        return expectedReward;
    }

}
