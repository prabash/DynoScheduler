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
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.TickerBehaviour;
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
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Prabash
 */
public class ManagerAgent extends Agent implements ISchedulerAgent
{
    private static final long serialVersionUID = 3369137004053108334L;
    private transient List<AgentController> agentList;// agents's ref
    
    private static final Queue<ACLMessage> OPERATION_SCHEDULING_REQUESTS_QUEUE = new ConcurrentLinkedQueue<>();

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
        addBehaviour(new BProcessOperationScheduleQueue(this, 1000L));
        addBehaviour(new BNotifyOperationScheduleQueue());
        
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
            LogUtil.logSevereErrorMessage(this, fe.getMessage(), fe);
        }
    }

    @Override
    public void registerAgentService(DataModel obj)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public static void addScheduleOperationRequest(ACLMessage scheduleOpRequest)
    {
        OPERATION_SCHEDULING_REQUESTS_QUEUE.add(scheduleOpRequest);
    }
    
    public static void clearScheduleOperationRequestsQueue()
    {
        OPERATION_SCHEDULING_REQUESTS_QUEUE.clear();
    }
    
    public static boolean scheduleOperationsQueueIsEmpty()
    {
        if (OPERATION_SCHEDULING_REQUESTS_QUEUE != null)
            return OPERATION_SCHEDULING_REQUESTS_QUEUE.isEmpty();
        else 
            return true;
    }
    
    public static ACLMessage getNextFromScheduleOperationsQueue()
    {
        if (OPERATION_SCHEDULING_REQUESTS_QUEUE != null)
        {
            if (!OPERATION_SCHEDULING_REQUESTS_QUEUE.isEmpty())
                return OPERATION_SCHEDULING_REQUESTS_QUEUE.poll();
            else
                return null;
        }
        else
            return null;
    }
    
    static class BQueueScheduleOperationRequests extends CyclicBehaviour
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

    private static boolean OPERATION_SCHEDULED;
    private static final ReentrantLock OPERATION_SCHEDULE_QUEUE_LOCK = new ReentrantLock();
    
    static class BProcessOperationScheduleQueue extends TickerBehaviour 
    {
        private static final long serialVersionUID = -6980306431467493127L;

        public BProcessOperationScheduleQueue(Agent agent, long period)
        {
            super(agent, period);
        }

        @Override
        protected void onTick()
        {
            if(!OPERATION_SCHEDULE_QUEUE_LOCK.isLocked())
            {
                if(OPERATION_SCHEDULE_QUEUE_LOCK.tryLock())
                {
                    Thread thread = new Thread(new ProcessOperationScheduleQueue(myAgent));
                    thread.start();
                }
            }
        }
    }
    
    static class ProcessOperationScheduleQueue implements Runnable
    {
        Agent myAgent;
        
        public ProcessOperationScheduleQueue(Agent agent)
        {
            this.myAgent = agent;
        }

        @Override
        public void run()
        {
            if (!ManagerAgent.scheduleOperationsQueueIsEmpty())
            {
                ACLMessage request = ManagerAgent.getNextFromScheduleOperationsQueue();
                scheduleOperation(request);
            }
        }
        
        public boolean scheduleOperation(ACLMessage request)
        {
            try
            {
                AID shopOrderAgent = request.getSender();
                String operationId = request.getContent();
                ACLMessage startOpScheduleMsg = new ACLMessage(ACLMessage.PROPAGATE);
                startOpScheduleMsg.addReceiver(shopOrderAgent);
                startOpScheduleMsg.setContent(operationId);

                myAgent.send(startOpScheduleMsg);
                System.out.println("operation queue locked to schedule operation : " + operationId);
            }
            catch (Exception ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }

            return OPERATION_SCHEDULED;
        }
    }
    
    
    static class BNotifyOperationScheduleQueue extends CyclicBehaviour
    {
        private static final long serialVersionUID = -8707253852585581218L;
        @Override
        public void action()
        {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null)
            {
                try
                {
                    System.out.println(msg.getContent());
                    
                    OPERATION_SCHEDULED = true;
                    System.out.println("Notify thread unlock");

                    OPERATION_SCHEDULE_QUEUE_LOCK.unlock();
                }
                catch (Exception ex)
                {
                    LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
                }
            }
        }
    }
}
 

