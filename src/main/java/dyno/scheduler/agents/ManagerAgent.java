/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import dyno.scheduler.data.DataHandler;
import dyno.scheduler.jade.AgentsManager;
import dyno.scheduler.utils.LogUtil;
import jade.core.Agent;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class ManagerAgent extends Agent
{
    private static final long serialVersionUID = 3369137004053108334L;
    private transient List<AgentController> agentList;// agents's ref

    @Override
    protected void setup()
    {
        super.setup();
        
        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            ContainerController container = (ContainerController)args[0];
            
            agentList = new ArrayList<>();
            agentList.addAll(AgentsManager.createAgentsFromData(container, DataHandler.getShopOrderDetails()));
            agentList.addAll(AgentsManager.createAgentsFromData(container, DataHandler.getWorkCenterDetails()));
        }
        
        try
        {
            System.out.println("Press a key to start the agents");
            System.in.read();
        } catch (IOException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        
        AgentsManager.startAgents(agentList);
    }

    @Override
    protected void takeDown()
    {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
    }
}
