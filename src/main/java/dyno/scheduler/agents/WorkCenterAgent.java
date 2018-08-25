/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
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
public class WorkCenterAgent extends Agent
{
    private static final long serialVersionUID = 263288753945767774L;
    private transient WorkCenterModel workCenter;
    
    // the target date requested by the shop order agent for a particular operation
    DateTime requestedOpDate;
    // the best date and timeblock available to the work center agent
    DateTime bestOfferedDate;

    // <editor-fold desc="overriden methods" defaultstate="collapsed">
    
    /**
     * takeDown overridden method
     */
    @Override
    protected void takeDown()
    {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * setup overridden method
     */
    @Override
    protected void setup()
    {
        // Register the book-selling service in the yellow pages
        DFAgentDescription dfAgentDesc = new DFAgentDescription();
        dfAgentDesc.setName(getAID());

        ServiceDescription serviceDescription = new ServiceDescription();

        //get the parameters given into the object[]
        final Object[] args = getArguments();
        if (args[0] != null)
        {
            workCenter = (WorkCenterModel) args[0];

            // each agent belonging to a certain work center type will have "work-center-<TYPE>" in here.
            // therefore this should be dynamically set.
            serviceDescription.setType("work-center-" + workCenter.getWorkCenterType());
            serviceDescription.setName("schedule-work-center-service");
            dfAgentDesc.addServices(serviceDescription);
            try
            {
                // register the work center agent service
                DFService.register(this, dfAgentDesc);
            } catch (FIPAException fe)
            {
                LogUtil.logSevereErrorMessage(this, MSG_QUEUE_CLASS, fe);
            }

        } else
        {
            System.out.println("Error with the Work Center arguments");
        }

        //Add the behaviours
        //addBehaviour(new ReceiveMessage(this));
        addBehaviour(new BOfferBestAvailableDate());
        addBehaviour(new BAssignOperationToWorkCenter());

        System.out.println("the Work Center agent " + this.getLocalName() + " is started");
    }

    // </editor-fold>
    
    // <editor-fold desc="behaviors" defaultstate="collapsed"> 
    
    // <editor-fold desc="BOfferBestAvailableDate behavior" defaultstate="collapsed"> 
    
    /**
     * This behaviour offer the best available date for a specific operation date request
     * sent in by the ShopOrderAgent
     */
    private class BOfferBestAvailableDate extends CyclicBehaviour
    {
        private static final long serialVersionUID = -7860101940083496148L;
        
        DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();
        DateTimeFormatter dateTimeFormat = DateTimeUtil.getDateTimeFormat();
        
        // <editor-fold desc="overriden methods" defaultstate="collapsed">
        
        /**
         * action overridden method
         */
        @Override
        public void action()
        {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.CFP);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null)
            {
                // CFP Message received. Process it
                requestedOpDate = dateTimeFormat.parseDateTime(msg.getContent());
                ACLMessage reply = msg.createReply();

                // you should get the date related to the work center that is the earliest date after the target date
                bestOfferedDate = workCenter.getBestDateTimeOffer(requestedOpDate);

                if (bestOfferedDate != null)
                {
                    // reply with the earliest available date/timeblock that comes after the target date
                    reply.setPerformative(ACLMessage.PROPOSE);

                    // offer should be included with the time as well, therefore the dateTimeFormat is used
                    reply.setContent(bestOfferedDate.toString(dateTimeFormat));
                }
                myAgent.send(reply);
                
            } else
            {
                block();
            }
        }
        
        // </editor-fold>
    }
    
    // </editor-fold>
    
    // <editor-fold desc="BAssignOperationToWorkCenter behavior" defaultstate="collapsed"> 
    
    /**
     * This behaviour gets the acknowledgement for the offered date from the ShopOrderAgent
     * and schedules the operation on the offered date/time
     */
    private class BAssignOperationToWorkCenter extends CyclicBehaviour
    {
        private static final long serialVersionUID = 4660381226186754715L;
        
        // <editor-fold desc="overriden methods" defaultstate="collapsed">
        
        /**
         * action overridden method
         */
        @Override
        public void action()
        {
            MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.ACCEPT_PROPOSAL);
            ACLMessage msg = myAgent.receive(mt);
            if (msg != null)
            {
                // ACCEPT_PROPOSAL Message received with the OperationNo
                String operationId = msg.getContent();
                ACLMessage reply = msg.createReply();

                //Integer price = (Integer) catalogue.remove(title);
                if (bestOfferedDate != null)
                {
                    reply.setPerformative(ACLMessage.INFORM);
                    
                    workCenter.updateWorkCenterOpAllocDetails(workCenter.getWorkCenterNo(), bestOfferedDate, Integer.parseInt(operationId));
                    //update the excel sheet with the date
                    System.out.println("WC --> SCHEDULED OPERATION " + Integer.valueOf(operationId) + " ON " + bestOfferedDate);
                }
                
                myAgent.send(reply);
            } else
            {
                block();
            }
        }
        
        // </editor-fold>
    }
    
    // </editor-fold>
    
    // </editor-fold>
}
