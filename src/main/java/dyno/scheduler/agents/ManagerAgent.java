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
import dyno.scheduler.utils.StringUtil;
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
import java.util.Comparator;
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
            ContainerController container = (ContainerController) args[0];

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
        addBehaviour(new BProcessOperationScheduleQueue(this, 5000L));
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
        {
            return OPERATION_SCHEDULING_REQUESTS_QUEUE.isEmpty();
        } else
        {
            return true;
        }
    }

    public static ACLMessage getNextFromScheduleOperationsQueue()
    {
        if (OPERATION_SCHEDULING_REQUESTS_QUEUE != null)
        {
            if (!OPERATION_SCHEDULING_REQUESTS_QUEUE.isEmpty())
            {
                return OPERATION_SCHEDULING_REQUESTS_QUEUE.poll();
            } else
            {
                return null;
            }
        } else
        {
            return null;
        }
    }

    public static Queue<ACLMessage> getQueueSnapshot()
    {
        return OPERATION_SCHEDULING_REQUESTS_QUEUE;
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
                System.out.println("+++ Operation " + msg.getContent() + " from " + msg.getSender() + "  is queued!");
            } else
            {
                block();
            }
        }
        // </editor-fold>
    }

    private static boolean OPERATION_SCHEDULED;

    private static final Object OPERATION_SCHEDULE_LOCK = new Object();

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
            System.out.println("operation queue timer thread locked!");
            Thread thread = new Thread(new ProcessOperationScheduleQueue(myAgent, this));
            thread.start();
        }
    }

    static class ProcessOperationScheduleQueue implements Runnable
    {

        Agent myAgent;
        BProcessOperationScheduleQueue tickerInstance;
        ArrayList<ACLMessage> processingQueue;

        public ProcessOperationScheduleQueue(Agent agent, BProcessOperationScheduleQueue tickerInstance)
        {
            this.myAgent = agent;
            this.tickerInstance = tickerInstance;
        }

        @Override
        public void run()
        {
            // remove the ticker behavior when processing the queue, so the queue processing wont overlap
            if (!ManagerAgent.scheduleOperationsQueueIsEmpty())
            {
                myAgent.removeBehaviour(tickerInstance);
            }
            processingQueue = new ArrayList<>();
            while (!ManagerAgent.scheduleOperationsQueueIsEmpty())
            {
                ACLMessage request = ManagerAgent.getNextFromScheduleOperationsQueue();
                processingQueue.add(request);
            }
            
            processingQueue.sort(new ShopOrderACLMessageComparator());
            for (ACLMessage request : processingQueue)
            {
                scheduleOperation(request);
            }
            
            System.out.println(" %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% NO OPERATIONS TO BE SCHEDULED!! ");
            myAgent.addBehaviour(tickerInstance);
        }

        public boolean scheduleOperation(ACLMessage request)
        {
            OPERATION_SCHEDULED = false;
            synchronized (OPERATION_SCHEDULE_LOCK)
            {
                try
                {
                    AID shopOrderAgent = request.getSender();
                    // get the string array of message content
                    String[] msgContent = StringUtil.readMessageContent(request.getContent());
                    // second index of the message content array will have the operation id
                    String operationId = msgContent[2];

                    System.out.println(" ++++++  Shop Order Agent : " + shopOrderAgent);
                    System.out.println(" ++++++  operationId : " + operationId);

                    ACLMessage startOpScheduleMsg = new ACLMessage(ACLMessage.PROPAGATE);
                    startOpScheduleMsg.setConversationId("OPERATION_PROCESSING_QUEUE");
                    startOpScheduleMsg.addReceiver(shopOrderAgent);
                    startOpScheduleMsg.setContent(operationId);

                    if (!OPERATION_SCHEDULED)
                    {
                        myAgent.send(startOpScheduleMsg);
                        OPERATION_SCHEDULE_LOCK.wait();

                        System.out.println("operation queue locked to schedule operation : " + operationId);
                    }
                } catch (InterruptedException ex)
                {
                    LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
                }
            }

            return OPERATION_SCHEDULED;
        }
    }

    static class ShopOrderACLMessageComparator implements Comparator<ACLMessage>
    {
        @Override
        public int compare(ACLMessage o1, ACLMessage o2)
        {
            String[] o1_msgContent = StringUtil.readMessageContent(o1.getContent());
            String[] o2_msgContent = StringUtil.readMessageContent(o2.getContent());

            // Sort first by the importance
            Double o1_importance = Double.parseDouble(o1_msgContent[1]);
            Double o2_importance = Double.parseDouble(o2_msgContent[1]);
            int importanceResult = o2_importance.compareTo(o1_importance);
            if (importanceResult != 0)
            {
                return importanceResult;
            }

            // Sort second by the shop order no.
            Integer o1_shopOrderNo = Integer.parseInt(o1_msgContent[0]);
            Integer o2_shopOrderNo = Integer.parseInt(o2_msgContent[0]);
            int shopOrderNoResult = o1_shopOrderNo.compareTo(o2_shopOrderNo);
            if (shopOrderNoResult != 0)
            {
                return shopOrderNoResult;
            }

            // Sort last by the operation sequence.
            Double o1_operationSequence = Double.parseDouble(o1_msgContent[3]);
            Double o2_operationSequence = Double.parseDouble(o2_msgContent[3]);
            return o1_operationSequence.compareTo(o2_operationSequence);
        }

    }

    static class BNotifyOperationScheduleQueue extends CyclicBehaviour
    {

        private static final long serialVersionUID = -8707253852585581218L;

        @Override
        public void action()
        {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.AGREE);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null)
            {
                try
                {
                    synchronized (OPERATION_SCHEDULE_LOCK)
                    {
                        System.out.println(msg.getContent());
                        System.out.println("Notify queue unlock");

                        OPERATION_SCHEDULED = true;
                        OPERATION_SCHEDULE_LOCK.notifyAll();
                    }

                } catch (Exception ex)
                {
                    LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
                }
            }
        }
    }
}
