package br.ufrgs.inf;

import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.ContainerController;
import jade.util.ExtendedProperties;

public class MainContainer {

    private ContainerController mainContainer;

    public MainContainer() {

        Runtime rt = Runtime.instance();
        Properties p = new ExtendedProperties();
        //p.setProperty("gui", "true");
        ProfileImpl profile = new ProfileImpl(p);
        mainContainer = rt.createMainContainer(profile);

    }

    public ContainerController getContainer() { return mainContainer; }

}
