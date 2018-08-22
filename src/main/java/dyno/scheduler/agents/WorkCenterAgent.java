/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;

/**
 *
 * @author Prabash
 */
public class WorkCenterAgent extends Agent
{
    private static final long serialVersionUID = 263288753945767774L;

    // <editor-fold desc="overriden methods" defaultstate="collapsed">
    
    @Override
    protected void takeDown()
    {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected void setup()
    {
        super.setup(); //To change body of generated methods, choose Tools | Templates.
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
        // <editor-fold desc="overriden methods" defaultstate="collapsed">
        
        @Override
        public void action()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
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
        // <editor-fold desc="overriden methods" defaultstate="collapsed">
        
        @Override
        public void action()
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
        
        // </editor-fold>
    }
    
    // </editor-fold>
    
    // </editor-fold>
}
