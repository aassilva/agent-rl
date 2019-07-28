package br.ufrgs.inf.behaviors;

import br.ufrgs.inf.agent.Infra;
import br.ufrgs.inf.agent.Packet;
import br.ufrgs.inf.beans.Banda;
import br.ufrgs.inf.beans.Order;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

import java.io.IOException;

public class ReceiveBehavior extends CyclicBehaviour {

    @Override
    public void action() {

        ACLMessage message = receiveMessage(ACLMessage.INFORM);

        if (message != null) {
            Order order = generateOrderForTheProduct(message);
            if (order != null) sendOrder(message, order, ACLMessage.REQUEST);
        } else {
            block();
        }

    }

    private ACLMessage receiveMessage(int performative) {

        MessageTemplate template =
                MessageTemplate.and(MessageTemplate.MatchPerformative(performative),
                        MessageTemplate.MatchOntology("commerce"));
        ACLMessage message = myAgent.receive(template);
        return message;
    }

    private Order generateOrderForTheProduct(ACLMessage message) {

        try {
            Banda product = (Banda) message.getContentObject();
            Packet consumer = (Packet) myAgent;
            int quantity = consumer.getDesiredQuantityOf(product);
            return new Order(product, quantity);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void sendOrder(ACLMessage message, Order order, int performative) {

        ACLMessage reply = message.createReply();
        reply.setPerformative(performative);

        try {
            reply.setContentObject(order);
        } catch (IOException e) {
            e.printStackTrace();
        }

        myAgent.send(reply);
    }

}
