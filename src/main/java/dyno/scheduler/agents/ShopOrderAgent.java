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
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.domain.FIPAException;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Prabash
 */
public class ShopOrderAgent extends Agent
{
    private static final long serialVersionUID = 3537448666946171304L;

    // The list of known workcenter agents
    private AID[] workCenterAgents;
    // The target date that the operation should be started (FS) or Ended (BS)
    private DateTime targetOperationDate = null;
    // ID pf the operation that is being scheduled
    private int targetOperationID = 0;
    // shop order handled by the agent
    private transient ShopOrderModel shopOrder;

    transient DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();
    transient DateTimeFormatter dateTimeFormat = DateTimeUtil.getDateTimeFormat();
    
    // shop order operations will be added to the queue to be processed sequentially
    private transient final Queue<ShopOrderOperationModel> operationsQueue = new ConcurrentLinkedQueue<>();

    // <editor-fold desc="overriden methods" defaultstate="collapsed">
    /**
     * setup method
     */
    @Override
    protected void setup()
    {
        super.setup(); //To change body of generated methods, choose Tools | Templates.

        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            shopOrder = (ShopOrderModel) args[0];
            // add all the operations to the queue to be processed one by one
            operationsQueue.addAll(shopOrder.getOperations());

            addBehaviour(new TickerBehaviour(this, 1000)
            {
                private static final long serialVersionUID = 3035804743025458174L;

                @Override
                protected void onTick()
                {
                    ShopOrderOperationModel operation = operationsQueue.poll();
                    if (operation != null)
                    {
                        // if the targetOperationDate is null (initially), set the SO created date as the startdate and concatenate with 8.00AM as the starting time
                        // if the targetOperationDate is available use it as it is (since it will already have the time portion available)
                        // TODO: if currently created date is taken assuming forward scheduling. If the SO is backward scheduled, should calculate the start date before this point.
                        targetOperationDate = targetOperationDate == null ? DateTimeUtil.concatenateDateTime(shopOrder.getCreatedDate().toString(dateFormat), "08:00:00") : targetOperationDate;
                        targetOperationID = operation.getOperationId();

                        System.out.println("Trying to schedule operation : " + operation.getOperationId() + " on " + targetOperationDate);

                        // Update the list of seller agents
                        DFAgentDescription template = new DFAgentDescription();
                        ServiceDescription serviceDesc = new ServiceDescription();
                        // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
                        // therefore this should be dynamically set.
                        serviceDesc.setType("work-center-" + operation.getWorkCenterType());
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

                        // Perform the request
                        myAgent.addBehaviour(new BWorkCenterAgentHandler(operation));
                    }
                }
            });

        } else
        {
            System.out.println("Error with the Shop Order arguments");
        }

        System.out.println("the Shop Order Agent " + this.getLocalName() + " is started");
    }

    /**
     * takeDown method
     */
    @Override
    protected void takeDown()
    {
        super.takeDown();
        System.out.println("ShopOrderAgent is down!");
    }

    // </editor-fold>
    
    // <editor-fold desc="behaviors" defaultstate="collapsed"> 
    
    // <editor-fold desc="BWorkCenterRequestHandler behavior - Communication with WorkCenterAgent" defaultstate="collapsed"> 
    
    /**
     * this behaviour sends proposals to the WorkCenterAgent through different
     * steps 1. broadcast the date requirement to all WorkCenterAgents 2. Get
     * the best date offer by a WorkCenterAgent 3. Accept the best date offer
     * and Send Acknowledgement to the selected WorkCenterAgent 4. Receives
     * acknowledgement back from the WorkCenterAgent for the transaction
     * completion
     */
    class BWorkCenterAgentHandler extends Behaviour
    {

        private static final long serialVersionUID = 1863729905737637656L;

        private int step = 0; // is used in the switch statement inside the action method.
        private AID bestWorkCenter; // work center that provides the best target date
        private DateTime bestOfferedDate; // The best possible start date if forward scheduling / end date if backward scheduling
        private int repliesCount = 0; // The counter of replies from work center agents
        private MessageTemplate msgTemplate; // The template to receive replies
        private ACLMessage reply;
        private final static String CONVERSATION_ID = "work-center-request";
        private final transient ShopOrderOperationModel currentOperation;

        public BWorkCenterAgentHandler(ShopOrderOperationModel operation)
        {
            currentOperation = operation;
        }

        // <editor-fold desc="overriden methods" defaultstate="collapsed">
        @Override
        public void action()
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
                    cfpMessage.setContent(StringUtil.generateMessageContent(targetOperationDate.toString(DateTimeUtil.getDateTimeFormat()), String.valueOf(currentOperation.getWorkCenterRuntime())));
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
                    // Receive all proposals/refusals from seller agents
                    reply = myAgent.receive(msgTemplate);
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
                            System.out.println("++++++ targetOperationDate : " + targetOperationDate);
                            System.out.println("++++++ bestOfferedDate : " + bestOfferedDate);

                            // if forward scheduling the offered date should be the earliest date/time that comes on or after the target date
                            if (bestWorkCenter == null || ((offeredDate.equals(targetOperationDate) || offeredDate.isAfter(targetOperationDate)) && offeredDate.isBefore(bestOfferedDate)))
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
                        }
                    } else
                    {
                        block();
                    }
                    break;
                }
                case 2:
                {
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
                case 3:
                {
                    // Receive the confirmation reply
                    reply = myAgent.receive(msgTemplate);
                    if (reply != null)
                    {
                        // confirmation reply received
                        if (reply.getPerformative() == ACLMessage.INFORM)
                        {
                            
                            // the next possible op start date and the work center no. is sent by the work center agent in the reply
                            String [] msgContent = StringUtil.readMessageContent(reply.getContent());
                            targetOperationDate = DateTime.parse(msgContent[0], DateTimeUtil.getDateTimeFormat());
                            String workCenterNo = msgContent[1];
                            
                            // current operation details should be updated
                            updateOperationDetails(bestOfferedDate, targetOperationDate, workCenterNo, DataModelEnums.OperationStatus.Scheduled);
                            
                            // Date set successfully. We can terminate
                            System.out.println("Operation " + currentOperation.getOperationId() + " was successfully scheduled on " + bestOfferedDate + " at work center : " + reply.getSender().getName());
                            System.out.println("______________________________________________________________________________________________________________________________________");

                        } else
                        {
                            System.out.println("Operation " + currentOperation.getOperationId() + " could not be scheduled on " + bestOfferedDate + " at work center : " + reply.getSender().getName());
                        }

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

        @Override
        public boolean done()
        {
            if (step == 2 && bestWorkCenter == null)
            {
                System.out.println("Attempt failed: there are no work centers available for the date" + targetOperationDate);
            }
            return ((step == 2 && bestWorkCenter == null) || step == 4);
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

        // </editor-fold>
    }
    
    // </editor-fold>

    // </editor-fold>
}
