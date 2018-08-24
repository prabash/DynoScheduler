/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

/**
 *
 * @author Prabash
 */
public class WorkCenterModel extends DataModel
{
    // <editor-fold desc="properties"> 
    
    private String workCenterNo;
    private String workCenterType;
    private String workCenterDescription;
    private String workCenterCapacity;
    
    public WorkCenterModel()
    {
        AGENT_PREFIX = "WORK_CENTER_AGENT";
    }

    public String getWorkCenterNo()
    {
        return workCenterNo;
    }

    public void setWorkCenterNo(String workCenterNo)
    {
        this.workCenterNo = workCenterNo;
    }

    public String getWorkCenterType()
    {
        return workCenterType;
    }

    public void setWorkCenterType(String workCenterType)
    {
        this.workCenterType = workCenterType;
    }

    public String getWorkCenterDescription()
    {
        return workCenterDescription;
    }

    public void setWorkCenterDescription(String workCenterDescription)
    {
        this.workCenterDescription = workCenterDescription;
    }

    public String getWorkCenterCapacity()
    {
        return workCenterCapacity;
    }

    public void setWorkCenterCapacity(String workCenterCapacity)
    {
        this.workCenterCapacity = workCenterCapacity;
    }
    
    // </editor-fold>
    
    // <editor-fold desc="overriden methods"> 
    
    /**
     * get WorkCenterModel object by passing Excel or MySql table row
     * @param rowData relevant data object
     * @return WorkCenterModel object
     */
    @Override
    public WorkCenterModel getModelObject(Object rowData)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    @Override
    public String getPrimaryKey()
    {
        return getWorkCenterNo();
    }

    @Override
    public String getClassName()
    {
        return WorkCenterModel.class.getName();
    }
    
    @Override
    public String getAgentPrefix()
    {
        return this.AGENT_PREFIX;
    }
    
    // </editor-fold> 
}
