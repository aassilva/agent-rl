package br.ufrgs.inf.agent;

import br.ufrgs.inf.beans.State;
import br.ufrgs.inf.useful.Configuration;

import java.util.ArrayList;

public class Environment {

    private double resourcePool = 0;
    private double cumulativeNegativeResources = 0;

    public double getResourcePool() {

        return resourcePool;
    }

    public double getCumulativeNegativeResources() {

        return cumulativeNegativeResources;
    }

    public void trial() {

        ArrayList<ArrayList<Integer>> rewards = new ArrayList<ArrayList<Integer>>();
        ArrayList<ArrayList<Integer>> numberChosen = new ArrayList<ArrayList<Integer>>();

        for (State s : State.getAllStates()) {

            ArrayList<Integer> r = new ArrayList<Integer>();
            ArrayList<Integer> c = new ArrayList<Integer>();

            for (int i = 0; i < s.getActions().size(); i++) {

                r.add(0);
                c.add(0);
            }

            rewards.add(r);
            numberChosen.add(c);
        }

        for (Packet a : Packet.getAllPackets()) {

            State s = a.getCurrentState();
            int index = s.getStateNumber();
            int choice = a.makeChoice(index);
            int reward = s.getActions().get(choice).giveReward();
            numberChosen.get(index).set(choice, numberChosen.get(index).get(choice) + 1);
            a.addFitness(reward, choice);
            resourcePool += a.getResourceShare()*reward;
            rewards.get(index).set(choice, rewards.get(index).get(choice) + reward);
        }

        for (Packet a : Packet.getAllPackets()) {

            a.updateChoices(numberChosen, rewards);
            a.setCurrentState(a.getCurrentState().getActions().get(a.getChoice()).getNextState());
        }

        double totalSharedCost = 0;

        for (Packet a : Packet.getAllPackets()) {

            totalSharedCost += a.getResourceShare()* Configuration.costOfLiving;
        }

        if (resourcePool < totalSharedCost) totalSharedCost = resourcePool;

        for (Packet a : Packet.getAllPackets()) {

            if (a.getResourceShare()*Configuration.costOfLiving
                    < totalSharedCost/ Packet.getAllPackets().size()) {

                resourcePool -= a.getResourceShare()*Configuration.costOfLiving;
                a.addRemoveFitnessNoShare((a.getResourceShare() - 1)*Configuration.costOfLiving);
            } else {

                resourcePool -= totalSharedCost/ Packet.getAllPackets().size();
                a.addRemoveFitnessNoShare(totalSharedCost/ Packet.getAllPackets().size()
                        - Configuration.costOfLiving);
            }
        }

        for (int i = 0; i < Packet.getAllPackets().size(); i++) {

            if (Packet.getAllPackets().get(i).getFitness() < 0) {

                cumulativeNegativeResources += Packet.getAllPackets().get(i).getFitness();
                Packet.getAllPackets().remove(i--);
            }
        }
    }

}
