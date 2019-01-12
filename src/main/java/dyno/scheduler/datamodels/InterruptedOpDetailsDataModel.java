/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
public class InterruptedOpDetailsDataModel extends DataModel
{
     // <editor-fold defaultstate="collapsed" desc="properties"> 
    
    private String WorkCenterNo;
    private int operationId;
    private DateTime operationStartDateTime;
    private int interruptedRunTime;
    
    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters/setters">
    public int getOperationId()
    {
        return operationId;
    }

    public void setOperationId(int operationId)
    {
        this.operationId = operationId;
    }

    public DateTime getOperationStartDateTime()
    {
        return operationStartDateTime;
    }

    public void setOperationStartDateTime(DateTime operationStartDateTime)
    {
        this.operationStartDateTime = operationStartDateTime;
    }

    public String getWorkCenterNo()
    {
        return WorkCenterNo;
    }

    public void setWorkCenterNo(String WorkCenterNo)
    {
        this.WorkCenterNo = WorkCenterNo;
    }

    public int getInterruptedRunTime()
    {
        return interruptedRunTime;
    }

    public void setInterruptedRunTime(int interruptedRunTime)
    {
        this.interruptedRunTime = interruptedRunTime;
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="overriden methods"> 
    @Override
    public InterruptedOpDetailsDataModel getModelObject(Object row)
    {
        if (row instanceof ResultSet)
        {
            ResultSet resultSetRow = (ResultSet) row;
            int i = 0;
            try
            {
                this.setWorkCenterNo(resultSetRow.getString(++i));
                this.setOperationStartDateTime(resultSetRow.getDate(++i) == null ? null : DateTimeUtil.convertSqlTimestampToDateTime(resultSetRow.getTimestamp(i)));
                this.setOperationId(resultSetRow.getInt(++i));
                this.setInterruptedRunTime(resultSetRow.getInt(++i));
                
            } catch (SQLException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }
        }
        return this;
    }

    @Override
    public String getPrimaryKey()
    {
        return getOperationStartDateTime().toString(DateTimeUtil.getDateTimeFormat());
    }

    @Override
    public String getClassName()
    {
        return InterruptedOpDetailsDataModel.class.getName();
    }
    // </editor-fold>
}
