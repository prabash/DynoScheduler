/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import dyno.scheduler.data.DataReader;
import dyno.scheduler.datamodels.DataModel;
import dyno.scheduler.jade.AgentsManager;
import dyno.scheduler.jade.ISchedulerAgent;
import dyno.scheduler.utils.LogUtil;
import jade.core.Agent;
import static jade.core.Agent.MSG_QUEUE_CLASS;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 *
 * @author Prabash
 */
public class ManagerAgent extends Agent implements ISchedulerAgent
{
    private static final long serialVersionUID = 3369137004053108334L;
    private transient List<AgentController> agentList;// agents's ref
    
    private static Queue<ACLMessage> operationScheduleRequests;

    @Override
    protected void setup()
    {
        super.setup();
        
        registerAgentService();        
        
        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            ContainerController container = (ContainerController)args[0];
            
            agentList = new ArrayList<>();
            agentList.addAll(AgentsManager.createAgentsFromData(container, DataReader.getShopOrderDetails(false)));
            agentList.addAll(AgentsManager.createAgentsFromData(container, DataReader.getWorkCenterDetails(false)));
        }
        
        try
        {
            System.out.println("Press a key to start the agents");
            System.in.read();
        } catch (IOException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        addBehaviour(new BQueueScheduleOperationRequests());
        AgentsManager.startAgents(agentList);
    }

    @Override
    protected void takeDown()
    {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void registerAgentService()
    {
        // Register the book-selling service in the yellow pages
        DFAgentDescription dfAgentDesc = new DFAgentDescription();
        dfAgentDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();
        
        // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
        // therefore this should be dynamically set.
        serviceDescription.setType("manager-agent");
        serviceDescription.setName("manager-agent-service");
        dfAgentDesc.addServices(serviceDescription);
        try
        {
            // register the work center agent service
            DFService.register(this, dfAgentDesc);
        } catch (FIPAException fe)
        {
            LogUtil.logSevereErrorMessage(this, MSG_QUEUE_CLASS, fe);
        }
    }

    @Override
    public void registerAgentService(DataModel obj)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void addScheduleOperationRequest(ACLMessage scheduleOpRequest)
    {
        if (operationScheduleRequests == null)
        {
            operationScheduleRequests = new ConcurrentLinkedQueue<>();
        }
        operationScheduleRequests.add(scheduleOpRequest);
    }
    
    public static void clearScheduleOperationRequestsQueue()
    {
        operationScheduleRequests.clear();
    }
}
 
class BQueueScheduleOperationRequests extends CyclicBehaviour
{

    private static final long serialVersionUID = 8948436530894606064L;

    // <editor-fold desc="overriden methods" defaultstate="collapsed">

    /**
     * action overridden method
     */
    @Override
    public void action()
    {

        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null)
        {
            ManagerAgent.addScheduleOperationRequest(msg);
            System.out.println("+++ Operation queued!");
        } else
        {
            block();
        }
    }

    // </editor-fold>
}