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
        addBehaviour(new BStartOperationScheduler());

        System.out.println("the Shop Order Agent " + this.getLocalName() + " is started");
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
    private static final long serialVersionUID = -1131845958921042476L;
    
    @Override
    public void action()
    {
        MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.PROPAGATE);
        ACLMessage msg = myAgent.receive(mt);
        if (msg != null)
        {
            System.out.println(msg.getContent() + " will be scheduled!");
            ACLMessage reply = msg.createReply();
            
            try
            {
                Thread.sleep(2000L);
            } catch (InterruptedException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }
            
            reply.setPerformative(ACLMessage.INFORM);

            reply.setContent("Release Lock");

            myAgent.send(reply);
        }
    }        
}