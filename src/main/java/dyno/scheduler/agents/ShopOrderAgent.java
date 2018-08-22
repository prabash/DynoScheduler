/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;

/**
 *
 * @author Prabash
 */
public class ShopOrderAgent extends Agent
{
    private static final long serialVersionUID = 3537448666946171304L;
        
    // <editor-fold desc="overriden methods" defaultstate="collapsed">
    
    /**
     * setup method
     */
    @Override
    protected void setup()
    {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
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
     * this behaviour sends proposals to the WorkCenterAgent through different steps
     * 1. broadcast the date requirement to all WorkCenterAgents 
     * 2. Get the best date offer by a WorkCenterAgent
     * 3. Accept the best date offer and Send Acknowledgement to the selected WorkCenterAgent
     * 4. Receives acknowledgement back from the WorkCenterAgent for the transaction completion
     */
    private static class BWorkCenterAgentHandler extends Behaviour
    {
        private static final long serialVersionUID = 1516429306105478383L;
        
        // <editor-fold desc="overriden methods" defaultstate="collapsed">
        
        @Override
        public void action()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public boolean done()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        // </editor-fold>
    }
    // </editor-fold>
    
    // </editor-fold>
}
