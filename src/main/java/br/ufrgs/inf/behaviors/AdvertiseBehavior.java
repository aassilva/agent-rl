package br.ufrgs.inf.behaviors;

import br.ufrgs.inf.agent.Infra;
import br.ufrgs.inf.agent.Packet;
import br.ufrgs.inf.beans.Banda;
import jade.core.AID;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdvertiseBehavior extends OneShotBehaviour {

    @Override
    public void action() {

        Infra infra = (Infra)myAgent;
        Banda banda = infra.getBanda();
        List<String> packets = new ArrayList<>();

        infra.getAllPackets().forEach(packet -> packets.add(packet.getName()));

        ACLMessage message = new ACLMessage(ACLMessage.INFORM);
        packets.forEach(packet ->  message.addReceiver(new AID(packet, AID.ISLOCALNAME)));

        try {
            message.setContentObject(banda);
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        message.setOntology("commerce");
        myAgent.send(message);

    }

}
