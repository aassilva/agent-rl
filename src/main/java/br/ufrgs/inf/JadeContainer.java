package br.ufrgs.inf;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.ContainerController;

public class JadeContainer {

    private ContainerController container;

    public JadeContainer() {

        Runtime rt = Runtime.instance();
        ProfileImpl profile = new ProfileImpl(false);
        profile.setParameter(ProfileImpl.MAIN_HOST, "localhost");
        profile.setParameter(ProfileImpl.CONTAINER_NAME, "Infraestrutura");
        container = rt.createAgentContainer(profile);
    }

    public ContainerController getContainer() { return container; }
}
