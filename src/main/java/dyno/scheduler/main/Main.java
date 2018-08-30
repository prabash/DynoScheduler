package dyno.scheduler.main;

import dyno.scheduler.agents.ManagerAgent;
import dyno.scheduler.jade.AgentsManager;
import dyno.scheduler.restservice.RESTServiceHandler;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 
 * Hello world!
 *
 */
public class Main
{
    private static Runtime platformRuntime;
    private static List<AgentController> agentList;// agents's ref
    
    /**
     * starting point of the Application
     * @param args 
     */
    public static void main(String[] args)
    {
        // start the rest services
        startRESTService();
        
        // get the platform
        platformRuntime = AgentsManager.getRuntimeInstance();
        
        // create the main container
        ContainerController mainContainer = AgentsManager.createMainContainer(platformRuntime);
        
        // create other containers (currently only one)
        // TODO: should check in to the possibility of supporting multiple containers
        List<String> containerNames = new ArrayList<>();
        containerNames.add("Container0");
        Map<String, ContainerController> createdContainers = AgentsManager.createContainers(platformRuntime, containerNames);
        
        // create monitoring agtents and added them to the main container
        AgentsManager.createMonitoringAgents(mainContainer);
        
        // create other agents and add them to the other container
        agentList = new ArrayList<>();
        ContainerController otherContainer = createdContainers.get(containerNames.get(0));
        
        // create manager agent
        Object [] initData = new Object [] {
            otherContainer
        };
        agentList.add(AgentsManager.createAgent(otherContainer, "ManagerAgent", ManagerAgent.class.getName(), initData));
        
        // start the manager agent
        AgentsManager.startAgents(agentList);
        
    }
    
    private static void startRESTService()
    {
        Thread restServiceHandler = new Thread(new RESTServiceHandler());
        restServiceHandler.start();
    }
}
