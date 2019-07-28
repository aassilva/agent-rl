package br.ufrgs.inf.agent;

import br.ufrgs.inf.beans.Banda;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;

import java.util.ArrayList;
import java.util.List;

public class Infra extends Agent {

    private Banda banda;
    private static List<Packet> allPackets = new ArrayList<Packet>();

    @Override
    protected void setup() {

        addBehaviour(new OneShotBehaviour() {
            @Override
            public void action() {}
        });
    }

    public Banda getBanda() {

        return banda;
    }

    public static List<Packet> getAllPackets() {

        return allPackets;
    }

    public static void setAllPackets(List<Packet> allPackets) {

        Infra.allPackets = allPackets;
    }
}
