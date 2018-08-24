/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.datamodels.DataModelEnums.OperationStatus;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
public class ShopOrderOperationModel extends DataModel
{
    // <editor-fold desc="properties"> 
    
    private String orderNo;
    private int operationId;
    private int operationNo;
    private String workCenterNo;
    private String workCenterType;
    private String operationDescription;
    private int operationSequence;
    private double workCenterRuntime;
    private double laborRunTime;
    private DateTime opStartDate;
    private DateTime opStartTime;
    private DateTime opFinishDate;
    private DateTime opFinishTime;
    private int quantity;
    private OperationStatus operationStatus;

    public String getOrderNo()
    {
        return orderNo;
    }

    public void setOrderNo(String orderNo)
    {
        this.orderNo = orderNo;
    }

    public int getOperationId()
    {
        return operationId;
    }

    public void setOperationId(int operationId)
    {
        this.operationId = operationId;
    }

    public int getOperationNo()
    {
        return operationNo;
    }

    public void setOperationNo(int operationNo)
    {
        this.operationNo = operationNo;
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

    public String getOperationDescription()
    {
        return operationDescription;
    }

    public void setOperationDescription(String operationDescription)
    {
        this.operationDescription = operationDescription;
    }

    public int getOperationSequence()
    {
        return operationSequence;
    }

    public void setOperationSequence(int operationSequence)
    {
        this.operationSequence = operationSequence;
    }

    public double getWorkCenterRuntime()
    {
        return workCenterRuntime;
    }

    public void setWorkCenterRuntime(double workCenterRuntime)
    {
        this.workCenterRuntime = workCenterRuntime;
    }

    public double getLaborRunTime()
    {
        return laborRunTime;
    }

    public void setLaborRunTime(double laborRunTime)
    {
        this.laborRunTime = laborRunTime;
    }

    public DateTime getOpStartDate()
    {
        return opStartDate;
    }

    public void setOpStartDate(DateTime opStartDate)
    {
        this.opStartDate = opStartDate;
    }

    public DateTime getOpStartTime()
    {
        return opStartTime;
    }

    public void setOpStartTime(DateTime opStartTime)
    {
        this.opStartTime = opStartTime;
    }

    public DateTime getOpFinishDate()
    {
        return opFinishDate;
    }

    public void setOpFinishDate(DateTime opFinishDate)
    {
        this.opFinishDate = opFinishDate;
    }

    public DateTime getOpFinishTime()
    {
        return opFinishTime;
    }

    public void setOpFinishTime(DateTime opFinishTime)
    {
        this.opFinishTime = opFinishTime;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    public OperationStatus getOperationStatus()
    {
        return operationStatus;
    }

    public void setOperationStatus(OperationStatus operationStatus)
    {
        this.operationStatus = operationStatus;
    }
    
    // </editor-fold> 
    
    // <editor-fold desc="overriden methods"> 
    
    /**
     * get ShopOrderOperationModel object by passing Excel or MySql table row
     * @param rowData relevant data object
     * @return ShopOrderOperationModel object
     */
    @Override
    public ShopOrderOperationModel getModelObject(Object rowData)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public String getPrimaryKey()
    {
        return String.valueOf(getOperationId());
    }

    @Override
    public String getClassName()
    {
        return ShopOrderOperationModel.class.getName();
    }
    
    // </editor-fold> 
}
