/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.jade;

import dyno.scheduler.datamodels.DataModel;
import dyno.scheduler.utils.GeneralSettings;
import dyno.scheduler.utils.LogManager;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.ControllerException;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Prabash
 */
public class AgentsManager
{
    private static final String className = AgentsManager.class.getName();

    /**
     * return an instance of the runtime
     * @return the runtime instance
     */
    public static Runtime getRuntimeInstance()
    {
        return Runtime.instance();
    }
    
    /**
     * This method will create the main container for  the runtime platform
     * 
     * @param runtime runtime on which the main container should be created
     * @return created main ContainerController reference
     */
    public static ContainerController createMainContainer(Runtime runtime)
    {
        // Create a platform (main container + DF + AMS)
        Profile mainProf = new ProfileImpl(GeneralSettings.getHostName(), 8888, null);
        ContainerController mainContainerRef = runtime.createMainContainer(mainProf); // Including DF and AMS

        return mainContainerRef;
    }
    
    /**
     * Create the containers used to hold the agents
     *
     * @param runtime The reference to the main container
     * @param containerNames the list of container names that should be created
     * @return an HashMap associating the name of a container and its object
     * reference.
     */
    public static Map<String, ContainerController> createContainers(Runtime runtime, List<String> containerNames)
    {
        HashMap<String, ContainerController> containerList = new HashMap<>();

        containerNames.stream().forEach(container ->
        {
            ProfileImpl pContainer = new ProfileImpl(null, 8888, null);
            // ContainerController replace AgentContainer in the new versions of Jade.
            ContainerController containerRef = runtime.createAgentContainer(pContainer);
            containerList.put(container, containerRef);
        });

        LogManager.logInfoMessage(className, "Launching containers done");
        return containerList;
    }

    /**
     * this method is used to create work center agents depending on a set of
     * data
     *
     * @param container the container to which the agents should be added
     * @param dataSet agents will be added for each of the objects in the
     * @return the set of agents created
     */
    public static List<AgentController> createAgentsFromData(ContainerController container, List<DataModel> dataSet)
    {
        String agentName;
        List<AgentController> agentsList = new ArrayList();

        for (DataModel data : dataSet)
        {
            agentName = data.getAgentPrefix() + data.getPrimaryKey();
            try
            {
                Object[] initInfo = new Object[]
                {
                    data
                };//used to give informations to the agent

                AgentController agentController = container.createNewAgent(agentName, data.getClassName(), initInfo);
                agentsList.add(agentController);
                System.out.println(agentName + " launched");
            }
            catch (StaleProxyException ex)
            {
                LogManager.logSevereErrorMessage(className, ex.getMessage(), ex);
            }
        }
        return agentsList;
    }

    /**
     * Start the agents
     *
     * @param agentList the list of agents that should be started
     */
    public static void startAgents(List<AgentController> agentList)
    {
        agentList.stream().forEach((ac) ->
        {
            try
            {
                ac.start();
            }
            catch (StaleProxyException ex)
            {
                LogManager.logSevereErrorMessage(className, null, ex);
            }
        });
        LogManager.logInfoMessage(className, "Agents started...");
    }

    /**
     * Create default monitoring agents provided by Jade
     *
     * @param container container on which the agents are created
     */
    public static void createMonitoringAgents(ContainerController container)
    {
        try
        {
            AgentController rmaAgent;
            rmaAgent = container.createNewAgent("rma", "jade.tools.rma.rma", new Object[0]);
            rmaAgent.start();

            LogManager.logInfoMessage(className, "Launched RMA Agent on " + container.getContainerName());
        }
        catch (StaleProxyException ex)
        {
            LogManager.logSevereErrorMessage(className, "Launching of RMA Agent failed", ex);
        }
        catch (ControllerException ex)
        {
            LogManager.logSevereErrorMessage(className, ex.getMessage(), ex);
        }

        try
        {
            AgentController snifferAgent;
            snifferAgent = container.createNewAgent("sniffeur", "jade.tools.sniffer.Sniffer", new Object[0]);
            snifferAgent.start();

            LogManager.logInfoMessage(className, "Launched Sinffer Agent on " + container.getContainerName());
        }
        catch (StaleProxyException ex)
        {
            LogManager.logSevereErrorMessage(className, "Launching of Sinffer failed", ex);
        }
        catch (ControllerException ex)
        {
            LogManager.logSevereErrorMessage(className, ex.getMessage(), ex);
        }
    }

}
