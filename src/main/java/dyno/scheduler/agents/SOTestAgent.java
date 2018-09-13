/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import dyno.scheduler.utils.StringUtil;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Prabash
 */
public class SOTestAgent extends Agent
{
    private static final long serialVersionUID = 8846265486536139525L;

    // shop order handled by the agent
    private transient ShopOrderModel shopOrder;

    transient DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();
    transient DateTimeFormatter dateTimeFormat = DateTimeUtil.getDateTimeFormat();

    @Override
    protected void setup()
    {
        super.setup(); //To change body of generated methods, choose Tools | Templates.

        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            shopOrder = (ShopOrderModel) args[0];

        } else
        {
            System.out.println("Error with the Shop Order arguments");
        }
        addBehaviour(new BProcessOperationQueue(shopOrder.getOperations()));
        addBehaviour(new BStartOperationScheduler(this));

        System.out.println("the Shop Order Agent " + this.getLocalName() + " is started");
    }
    
    public ShopOrderOperationModel getOperationById(String operationId)
    {
        ShopOrderOperationModel returnOp = null;
        for (ShopOrderOperationModel operation : shopOrder.getOperations())
        {
            System.out.println(" +++++ operation id : " + operationId);
            System.out.println(" +++++ primary key : " + operation.getPrimaryKey());
            if (operation.getPrimaryKey().equals(operationId))
            {
                returnOp = operation;
                break;
            }
                
        }
        return returnOp;
        //return shopOrder.getOperations().stream().filter(rec -> rec.getPrimaryKey().equals(operationId)).collect(Collectors.toList()).get(0);
    }
    
    public DateTime targetOpStartDate(String operationId)
    {
        return shopOrder.getOperationTargetStartDate(operationId);
    }
    
    public void updateShopOrderOperation(ShopOrderOperationModel operationOb)
    {
        shopOrder.updateOperation(operationOb);
    }
}

class BProcessOperationQueue extends Behaviour
{
    private static final long serialVersionUID = 5767062419481143156L;
    private boolean operationsQueued = false;
    
    // The list of known workcenter agents
    private AID[] managerAgents;

    // shop order operations will be added to the queue to be processed sequentially
    transient List<ShopOrderOperationModel> operations = new ArrayList<>();

    public BProcessOperationQueue(List<ShopOrderOperationModel> operations)
    {
        this.operations.clear();
        this.operations.addAll(operations);
    }

    @Override
    public void action()
    {
        for (ShopOrderOperationModel operation : operations)
        {
            // Update the list of seller agents
            DFAgentDescription template = new DFAgentDescription();
            ServiceDescription serviceDesc = new ServiceDescription();
            // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
            // therefore this should be dynamically set.
            serviceDesc.setType("manager-agent");
            serviceDesc.setName("manager-agent-service");

            template.addServices(serviceDesc);
            try
            {
                // find the agents belonging to the certain work center type
                DFAgentDescription[] result = DFService.search(myAgent, template);
                managerAgents = new AID[result.length];
                for (int i = 0; i < result.length; ++i)
                {
                    managerAgents[i] = result[i].getName();
                    System.out.println(managerAgents[i].getName());
                }

                // Send the cfp (Call for Proposal) message for the operation to the manager agent
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.REQUEST);
                // since there's currently only one manager, this will be 0
                for (int i = 0; i < managerAgents.length; ++i)
                {
                    cfpMessage.addReceiver(managerAgents[i]);
                }
                cfpMessage.setContent(StringUtil.generateMessageContent(operation.getPrimaryKey()));
                myAgent.send(cfpMessage);

            } catch (FIPAException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }
        }
        operationsQueued = true;
    }

    @Override
    public boolean done()
    {
        return operationsQueued;
    }
}

  
class BStartOperationScheduler extends CyclicBehaviour
{
    private final SOTestAgent currentAgent;
    private static final long serialVersionUID = 3149611413717448878L;
    
    final String CONVERSATION_ID = "work-center-request";
    transient ShopOrderOperationModel currentOperation;
    
    // The target date that the operation should be started (FS) or Ended (BS)
    private DateTime targetOperationStartDate = null;
        
    int step = 0; // is used in the switch statement inside the action method.
    AID bestWorkCenter; // work center that provides the best target date
    DateTime bestOfferedDate; // The best possible start date if forward scheduling / end date if backward scheduling
    int repliesCount = 0; // The counter of replies from work center agents
    MessageTemplate msgTemplate; // The template to receive replies
    ACLMessage reply;
    ACLMessage replyMgr;
    
    // The list of known workcenter agents
    private AID[] workCenterAgents;
    
    public BStartOperationScheduler(SOTestAgent curreAgent)
    {
        this.currentAgent = curreAgent;
    }
    
    @Override
    public void action()
    {
        MessageTemplate mt = MessageTemplate.MatchConversationId("OPERATION_PROCESSING_QUEUE");
        ACLMessage msg = myAgent.receive(mt);
        
        if (msg != null)
        {
            if (msg.getPerformative() == ACLMessage.PROPAGATE)
            {
                replyMgr = msg.createReply();
                String opIdToSchedule = msg.getContent();
                currentOperation = currentAgent.getOperationById(opIdToSchedule);
                if (currentOperation != null)
                {
                    // get the target operation start date before scheduling
                    targetOperationStartDate = currentAgent.targetOpStartDate(currentOperation.getPrimaryKey());
                    getWorkCenterAgents();
                    
                    step = 0;
                    repliesCount = 0;
                    bestOfferedDate = null;
                    scheduleOperation();


    //                try
    //                {
    //                    System.out.println(currentOperation.getPrimaryKey() + " is scheduling");
    //                    Thread.sleep(5000L);
    //                   
    //                } catch (InterruptedException ex)
    //                {
    //                    Logger.getLogger(BStartOperationScheduler.class.getName()).log(Level.SEVERE, null, ex);
    //                }
    //                notifyManagerAgent();
                }
            }
            else
            {
                reply = msg;
                scheduleOperation();
            }
        }
    }
    
    public void getWorkCenterAgents()
    {
        // Update the list of seller agents
        DFAgentDescription template = new DFAgentDescription();
        ServiceDescription serviceDesc = new ServiceDescription();
        // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
        // therefore this should be dynamically set.
        serviceDesc.setType("work-center-" + currentOperation.getWorkCenterType());
        serviceDesc.setName("schedule-work-center-service");

        template.addServices(serviceDesc);
        try
        {
            // find the agents belonging to the certain work center type
            DFAgentDescription[] result = DFService.search(myAgent, template);
            System.out.println("Found the WorkCenterAgents :");
            workCenterAgents = new AID[result.length];
            for (int i = 0; i < result.length; ++i)
            {
                workCenterAgents[i] = result[i].getName();
                System.out.println(workCenterAgents[i].getName());
            }
        } catch (FIPAException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
    }
    
    public void scheduleOperation()
    {
        switch (step)
        {
            case 0:
            {
                // Send the cfp (Call for Proposal) to all sellers
                ACLMessage cfpMessage = new ACLMessage(ACLMessage.CFP);
                for (int i = 0; i < workCenterAgents.length; ++i)
                {
                    cfpMessage.addReceiver(workCenterAgents[i]);
                }
                cfpMessage.setContent(StringUtil.generateMessageContent(targetOperationStartDate.toString(DateTimeUtil.getDateTimeFormat()), String.valueOf(currentOperation.getWorkCenterRuntime())));
                cfpMessage.setConversationId(CONVERSATION_ID);
                cfpMessage.setReplyWith("cfp" + System.currentTimeMillis()); // Unique value (can be something with the Shop Order No + operation No. and time)
                myAgent.send(cfpMessage);

                // Prepare the template to get proposals
                msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(CONVERSATION_ID),
                        MessageTemplate.MatchInReplyTo(cfpMessage.getReplyWith()));
                step = 1;
                break;
            }
            case 1:
            {
                // Date offered by the work center agent
                DateTime offeredDate;
                if (reply != null)
                {
                    // Reply received
                    if (reply.getPerformative() == ACLMessage.PROPOSE)
                    {
                        // This is an offer, recieved with the date and the time
                        offeredDate = DateTimeUtil.getDateTimeFormat().parseDateTime(reply.getContent());

                        System.out.println("++++++ offeredDate : " + offeredDate + " by Work Center Agent : " + reply.getSender());
                        System.out.println("++++++ targetOperationDate : " + targetOperationStartDate);
                        System.out.println("++++++ bestOfferedDate : " + bestOfferedDate);

                        // if forward scheduling the offered date should be the earliest date/time that comes on or after the target date
                        if (bestWorkCenter == null || ((offeredDate.equals(targetOperationStartDate) || offeredDate.isAfter(targetOperationStartDate)) && offeredDate.isBefore(bestOfferedDate)))
                        {
                            // This is the best offer at present
                            bestOfferedDate = offeredDate;
                            bestWorkCenter = reply.getSender();
                            System.out.println("Current best offered time : " + bestOfferedDate + " by Work Center Agent : " + bestWorkCenter);
                        }

                        repliesCount++;
                    }

                    if (repliesCount >= workCenterAgents.length)
                    {
                        // We received all replies
                        step = 2;
                        System.out.println("++++++ RECEIVED ALL OFFERES! ++++++++");
                        
                        // Send the confirmation to the work center that sent the best date
                        ACLMessage order = new ACLMessage(ACLMessage.ACCEPT_PROPOSAL);
                        order.addReceiver(bestWorkCenter);
                        order.setContent(StringUtil.generateMessageContent(String.valueOf(currentOperation.getOperationId()), String.valueOf(currentOperation.getWorkCenterRuntime())));
                        order.setConversationId(CONVERSATION_ID);
                        order.setReplyWith("setOperation" + System.currentTimeMillis());
                        myAgent.send(order);
                        // Prepare the template to get the purchase order reply
                        msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(CONVERSATION_ID),
                                MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                        step = 3;
                        break;
                    }
                } else
                {
                    block();
                }
                break;
            }
            case 2:
            {
                
            }
            case 3:
            {
                if (reply != null)
                {
                    // confirmation reply received
                    if (reply.getPerformative() == ACLMessage.INFORM)
                    {

                        // the next possible op start date and the work center no. is sent by the work center agent in the reply
                        String [] msgContent = StringUtil.readMessageContent(reply.getContent());
                        targetOperationStartDate = DateTime.parse(msgContent[0], DateTimeUtil.getDateTimeFormat());
                        String workCenterNo = msgContent[1];

                        // current operation details should be updated on the database
                        updateOperationDetails(bestOfferedDate, targetOperationStartDate, workCenterNo, DataModelEnums.OperationStatus.Scheduled);
                        // update operation details on the current shop order object
                        currentAgent.updateShopOrderOperation(currentOperation);

                        // Date set successfully. We can terminate
                        System.out.println("Operation " + currentOperation.getOperationId() + " was successfully scheduled on " + bestOfferedDate + " at work center : " + reply.getSender().getName());
                        System.out.println("______________________________________________________________________________________________________________________________________");

                    } else
                    {
                        System.out.println("Operation " + currentOperation.getOperationId() + " could not be scheduled on " + bestOfferedDate + " at work center : " + reply.getSender().getName());
                    }
                    
                    notifyManagerAgent();
                    step = 4;
                    
                } else
                {
                    block();
                }
                break;
            }
            default:
                break;
        }
    }
    
    private void updateOperationDetails(DateTime opStartDate, DateTime opFinishDate, String workCenterNo, DataModelEnums.OperationStatus opStatus)
    {
        currentOperation.setOpStartDate(opStartDate);
        currentOperation.setOpStartTime(opStartDate);
        currentOperation.setOpFinishDate(opFinishDate);
        currentOperation.setOpFinishTime(opFinishDate);
        currentOperation.setWorkCenterNo(workCenterNo);
        currentOperation.setOperationStatus(opStatus);
        currentOperation.updateOperationDetails();
    }
    
    private void notifyManagerAgent()
    {
        if (replyMgr != null)
        {
            replyMgr.setPerformative(ACLMessage.AGREE);
            replyMgr.setContent("Release Lock");
            myAgent.send(replyMgr);
        }
    }
}

