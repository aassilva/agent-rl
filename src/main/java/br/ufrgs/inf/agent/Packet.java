package br.ufrgs.inf.agent;

import br.ufrgs.inf.beans.Banda;
import br.ufrgs.inf.beans.State;
import br.ufrgs.inf.behaviors.ReceiveBehavior;
import br.ufrgs.inf.useful.Configuration;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;
import java.util.List;

public class Packet extends Agent {

    private static List<Packet> allPackets = new ArrayList<Packet>();
    private long fitness = 0;
    private List<List<Double>> expectedRewards = new ArrayList<>();
    private double selfWeight = 1;
    private double timeWeight = .1;
    private double resourceShare = 0;
    private State currentState = null;
    private int choice = 0;
    private double newReward = 0;


    @Override
    protected void setup() {

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {}
        });

        addBehaviour(new ReceiveBehavior());
    }

    public Packet(int[] dimensions, State state) {

        for (int i = 0; i < dimensions.length; i++) {

            List<Double> e = new ArrayList<Double>();

            for (int j = 0; j < dimensions[i]; j++) {

                e.add(State.getAllStates().get(i).getExpectedReward());
            }

            expectedRewards.add(e);
        }

        setCurrentState(state);
        allPackets.add(this);
    }

    public Packet(int[] dimensions, double W, double T, double R, State state, int fitness) {

        for (int i = 0; i < dimensions.length; i++) {

            List<Double> e = new ArrayList<Double>();

            for (int j = 0; j < dimensions[i]; j++) {

                e.add(State.getAllStates().get(i).getExpectedReward());
            }

            expectedRewards.add(e);
        }

        selfWeight = W;
        timeWeight = T;
        resourceShare = R;
        setCurrentState(state);
        this.fitness = fitness;
        allPackets.add(this);
    }

    public int getDesiredQuantityOf(Banda product) {

        int bandaTemp = product.getLarguraBanda();
        int q = makeChoice(bandaTemp);
        return q>0 ? q:0;
    }

    public static List<Packet> getAllPackets() {

        return allPackets;
    }


    public long getFitness() {

        return fitness;
    }

    public double getResourceShare() {

        return resourceShare;
    }

    public double getNewReward() {

        return newReward;
    }

    public List<Double> getExpectedRewards(int index) {

        return expectedRewards.get(index);
    }

    public State getCurrentState() {

        return currentState;
    }

    public int getChoice() {

        return choice;
    }

    public int makeChoice(int state) {

        double z = 0;

        for (double reward : expectedRewards.get(state)) {

            z += Math.pow(Math.E, reward/ Configuration.temperature);
        }

        double pVal = Math.random();

        for (int i = 0; i < expectedRewards.get(state).size(); i++) {

            pVal -= Math.pow(Math.E,
                    expectedRewards.get(state).get(i)/Configuration.temperature)/z;

            if (pVal < 0) {

                return i;
            }
        }

        return expectedRewards.get(state).size() - 1;
    }

    public void addFitness(double value, int choice) {

        fitness += (1 - resourceShare)*value;
        newReward = value;
        this.choice = choice;
    }

    public void addRemoveFitnessNoShare(double value) {

        fitness += value;
    }

    public void setCurrentState(State newState) {

        currentState = newState;
    }

    public void updateChoices(ArrayList<ArrayList<Integer>> numbers,
                              ArrayList<ArrayList<Integer>> rewards) {

        int index = currentState.getStateNumber();
        int rTot = rewards.get(index).get(choice);
        int cTot = numbers.get(index).get(choice);
        double newER = expectedRewards.get(index).get(choice);

        if (selfWeight == 0) {

            newER *= 1 - timeWeight;
            newER += (double)rTot/cTot * timeWeight;
        } else {

            if (cTot > 1) {

                newER = (double) (rTot - newReward) / (cTot - 1);
                newER *= 1 - selfWeight;
                newER += selfWeight*newReward;
                newER *= timeWeight;
                newER += (1 - timeWeight)*expectedRewards.get(index).get(choice);
            } else {

                newER *= 1 - timeWeight;
                newER += newReward*timeWeight;
            }

        }

        expectedRewards.get(index).set(choice, newER);

        for (int i = 0; i < expectedRewards.size(); i++) {

            for (int j = 0; j < expectedRewards.get(i).size(); j++) {

                if ((i != index) || (j != choice)) {

                    rTot = rewards.get(i).get(j);
                    cTot = numbers.get(i).get(j);
                    if (cTot > 0) {

                        newER = (double)rTot/cTot;
                        newER *= 1-selfWeight;
                        newER += selfWeight*expectedRewards.get(i).get(j);
                        newER *= timeWeight;
                        newER += (1 - timeWeight)*expectedRewards.get(i).get(j);
                        expectedRewards.get(i).set(j, newER);
                    }
                }
            }

        }

    }

    @Override
    public String toString() {

        return "Packet [fitness=" + fitness + ", expectedRewards=" + expectedRewards
                + ", selfWeight=" + selfWeight + ", timeWeight=" + timeWeight
                + "]";
    }

}