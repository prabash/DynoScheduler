/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import java.util.HashMap;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
public class WorkCenterOpAllocModel extends DataModel
{
    // <editor-fold desc="properties"> 
    
    private String workCenterNo;
    private DateTime operationDate;
    private HashMap<String, Integer> timeBlockAllocation;

    public String getWorkCenterNo()
    {
        return workCenterNo;
    }

    public void setWorkCenterNo(String workCenterNo)
    {
        this.workCenterNo = workCenterNo;
    }

    public DateTime getOperationDate()
    {
        return operationDate;
    }

    public void setOperationDate(DateTime operationDate)
    {
        this.operationDate = operationDate;
    }

    public HashMap<String, Integer> getTimeBlockAllocation()
    {
        return timeBlockAllocation;
    }

    public void setTimeBlockAllocation(HashMap<String, Integer> timeBlockAllocation)
    {
        this.timeBlockAllocation = timeBlockAllocation;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="overriden methods"> 
    
    /**
     * get WorkCenterOpAllocModel object by passing Excel or MySql table row
     * @param rowData relevant data object
     * @return WorkCenterOpAllocModel object
     */
    @Override
    public WorkCenterOpAllocModel getModelObject(Object rowData)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    // </editor-fold> 
}
