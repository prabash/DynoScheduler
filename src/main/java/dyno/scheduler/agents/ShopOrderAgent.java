/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
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
            for (ShopOrderOperationModel operation : shopOrder.getOperations())
            {
                // if the targetOperationDate is null (initially), set the SO created date as the startdate and concatenate with 8.00AM as the starting time
                // if the targetOperationDate is available use it as it is (since it will already have the time portion available)
                targetOperationDate = targetOperationDate == null ? DateTimeUtil.concatenateDateTime(shopOrder.getCreatedDate().toString(dateFormat), "08:00:00") : targetOperationDate;
                targetOperationID = operation.getOperationId();

                System.out.println("Target operation date is " + targetOperationDate.toString(dateTimeFormat));

                // Add a TickerBehaviour that schedules a request to seller agents every minute
                addBehaviour(new TickerBehaviour(this, 5000)
                {
                    private static final long serialVersionUID = 3035804743025458174L;

                    @Override
                    protected void onTick()
                    {
                        System.out.println("Trying to schedule operation : " + targetOperationDate);

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
                            System.out.println("Found the following seller agents:");
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
                });
            }

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
                    cfpMessage.setContent(targetOperationDate.toString(DateTimeUtil.getDateTimeFormat()));
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

                            System.out.println("++++++ offeredDate : " + offeredDate);
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
                        }
                        repliesCount++;
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
                    order.setContent(String.valueOf(targetOperationID));
                    order.setConversationId(CONVERSATION_ID);
                    order.setReplyWith("setOperation" + System.currentTimeMillis());
                    myAgent.send(order);
                    // Prepare the template to get the purchase order reply
                    msgTemplate = MessageTemplate.and(MessageTemplate.MatchConversationId(CONVERSATION_ID),
                            MessageTemplate.MatchInReplyTo(order.getReplyWith()));

                    // after accepting the offer, the new targetOperationDate should be the accepted date, for the next operation to begin
                    targetOperationDate = bestOfferedDate;

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
                            // Date set successfully. We can terminate
                            System.out.println("Operation " + currentOperation.getOperationId() + " was successfully scheduled on " + targetOperationDate + " at work center" + reply.getSender().getName());
                            System.out.println("Set Date = " + bestOfferedDate);

                        } else
                        {
                            System.out.println("Operation " + currentOperation.getOperationId() + " could not be scheduled on " + targetOperationDate + " at work center" + reply.getSender().getName());
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

        // </editor-fold>
    }
    
    // </editor-fold>

    // </editor-fold>
}
