package br.ufrgs.inf;

import br.ufrgs.inf.agent.Infra;
import br.ufrgs.inf.agent.Packet;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;

public class MainAppli {

    ContainerController mainContainer, container;
    private AgentController producerAgent;

    public static void main(String[] args) throws StaleProxyException {
        new MainAppli();
    }

    public MainAppli() throws StaleProxyException {
        mainContainer = new MainContainer().getContainer();
        container = new JadeContainer().getContainer();
        producerAgent = container
                .createNewAgent("Infra", Infra.class.getName(), new Object[]{});

        producerAgent.start();

    }

}
