/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.data.DataReader;
import dyno.scheduler.data.DataWriter;
import dyno.scheduler.datamodels.DataModelEnums.InerruptionType;
import dyno.scheduler.datamodels.DataModelEnums.OperationStatus;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormatter;

/**
 *
 * @author Prabash
 */
@XmlRootElement
public class ShopOrderOperationModel extends DataModel implements Comparator<ShopOrderOperationModel>
{
    // <editor-fold defaultstate="collapsed" desc="properties"> 

    private String orderNo;
    private int operationId;
    private int operationNo;
    private String workCenterNo;
    private String workCenterType;
    private String operationDescription;
    private double operationSequence;
    private int precedingOperationId;
    private int workCenterRuntimeFactor;
    private int workCenterRuntime;
    private int laborRuntimeFactor;
    private int laborRunTime;
    private DateTime opStartDate;
    private DateTime opStartTime;
    private DateTime opFinishDate;
    private DateTime opFinishTime;
    private DateTime latestOpFinishDate;
    private DateTime latestOpFinishTime;
    private int quantity;
    private OperationStatus operationStatus;

    // </editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructors">
    public ShopOrderOperationModel()
    {
    }

    public ShopOrderOperationModel(String orderNo, int operationId, int operationNo, String workCenterNo, String workCenterType, String operationDescription, double operationSequence,
            int workCenterRunTime, int laborRunTime, DateTime opStartDate, DateTime opStartTime, DateTime opFinishDate, DateTime opFinishTime, int quantity, OperationStatus operationStatus)
    {
        this.orderNo = orderNo;
        this.operationId = operationId;
        this.operationNo = operationNo;
        this.workCenterNo = workCenterNo;
        this.workCenterType = workCenterType;
        this.operationDescription = operationDescription;
        this.operationSequence = operationSequence;
        this.workCenterRuntime = workCenterRunTime;
        this.laborRunTime = laborRunTime;
        this.opStartDate = opStartDate;
        this.opStartTime = opStartTime;
        this.opFinishDate = opFinishDate;
        this.opFinishTime = opFinishTime;
    }

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="getters/setters">
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

    public double getOperationSequence()
    {
        return operationSequence;
    }

    public void setOperationSequence(double operationSequence)
    {
        this.operationSequence = operationSequence;
    }

    public int getPrecedingOperationId()
    {
        return precedingOperationId;
    }

    public void setPrecedingOperationId(int precedingOperationId)
    {
        this.precedingOperationId = precedingOperationId;
    }

    public int getWorkCenterRuntimeFactor()
    {
        return workCenterRuntimeFactor;
    }

    public void setWorkCenterRuntimeFactor(int workCenterRuntimeFactor)
    {
        this.workCenterRuntimeFactor = workCenterRuntimeFactor;
    }

    public int getWorkCenterRuntime()
    {
        return workCenterRuntime;
    }

    public void setWorkCenterRuntime(int workCenterRuntime)
    {
        this.workCenterRuntime = workCenterRuntime;
    }

    public int getLaborRuntimeFactor()
    {
        return laborRuntimeFactor;
    }

    public void setLaborRuntimeFactor(int laborRuntimeFactor)
    {
        this.laborRuntimeFactor = laborRuntimeFactor;
    }

    public int getLaborRunTime()
    {
        return laborRunTime;
    }

    public void setLaborRunTime(int laborRunTime)
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

    public DateTime getLatestOpFinishDate()
    {
        return latestOpFinishDate;
    }

    public void setLatestOpFinishDate(DateTime latestOpFinishDate)
    {
        this.latestOpFinishDate = latestOpFinishDate;
    }

    public DateTime getLatestOpFinishTime()
    {
        return latestOpFinishTime;
    }

    public void setLatestOpFinishTime(DateTime latestOpFinishTime)
    {
        this.latestOpFinishTime = latestOpFinishTime;
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

    //</editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="overriden methods"> 
    /**
     * get ShopOrderOperationModel object by passing Excel or MySql table row
     *
     * @param row relevant data object
     * @return ShopOrderOperationModel object
     */
    @Override
    public ShopOrderOperationModel getModelObject(Object row)
    {
        if (row instanceof Row)
        {
            // Create a DataFormatter to format and get each cell's value as String
            DataFormatter dataFormatter = new DataFormatter();
            DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();
            DateTimeFormatter timeFormat = DateTimeUtil.getTimeFormat();

            Row excelRow = (Row) row;
            if (excelRow.getLastCellNum() > 0)
            {
                int i = -1;

                this.setOrderNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
                this.setOperationId(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setOperationNo(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setOperationDescription(dataFormatter.formatCellValue(excelRow.getCell(++i)));
                this.setOperationSequence(Double.parseDouble(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setPrecedingOperationId(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setWorkCenterRuntimeFactor(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setWorkCenterRuntime(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setLaborRuntimeFactor(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setLaborRunTime(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setOpStartDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
                this.setOpStartTime(excelRow.getCell(++i) == null ? null : timeFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
                this.setOpFinishDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
                this.setOpFinishTime(excelRow.getCell(++i) == null ? null : timeFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
                this.setQuantity(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
                this.setWorkCenterType(dataFormatter.formatCellValue(excelRow.getCell(++i)));
                this.setWorkCenterNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
                this.setOperationStatus(OperationStatus.valueOf(dataFormatter.formatCellValue(excelRow.getCell(++i))));
            }
        } else
        {
            ResultSet resultSetRow = (ResultSet) row;
            int i = 0;
            try
            {
                this.setOperationId(resultSetRow.getInt(++i));
                this.setOperationNo(resultSetRow.getInt(++i));
                this.setOrderNo(resultSetRow.getString(++i));
                this.setOperationDescription(resultSetRow.getString(++i));
                this.setOperationSequence(resultSetRow.getDouble(++i));
                this.setPrecedingOperationId(resultSetRow.getInt(++i));
                this.setWorkCenterRuntimeFactor(resultSetRow.getInt(++i));
                this.setWorkCenterRuntime(resultSetRow.getInt(++i));
                this.setLaborRuntimeFactor(resultSetRow.getInt(++i));
                this.setLaborRunTime(resultSetRow.getInt(++i));
                this.setOpStartDate(resultSetRow.getDate(++i) == null ? null : DateTimeUtil.convertSqlDatetoDateTime(resultSetRow.getDate(i)));
                this.setOpStartTime(resultSetRow.getTime(++i) == null ? null : DateTimeUtil.convertSqlTimetoDateTime(resultSetRow.getTime(i)));
                this.setOpFinishDate(resultSetRow.getDate(++i) == null ? null : DateTimeUtil.convertSqlDatetoDateTime(resultSetRow.getDate(i)));
                this.setOpFinishTime(resultSetRow.getTime(++i) == null ? null : DateTimeUtil.convertSqlTimetoDateTime(resultSetRow.getTime(i)));
                this.setQuantity(resultSetRow.getInt(++i));
                this.setWorkCenterType(resultSetRow.getString(++i));
                this.setWorkCenterNo(resultSetRow.getString(++i));
                this.setOperationStatus(OperationStatus.valueOf(resultSetRow.getString(++i)));
            } catch (SQLException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }
        }
        return this;
    }

    public boolean updateOperationDetails()
    {
        List<ShopOrderOperationModel> updateList = new ArrayList<>();
        updateList.add(this);
        return DataWriter.updateShopOrderOperationData(updateList);
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
    
    //<editor-fold defaultstate="collapsed" desc="custom methods">
    
    @Override
    public ShopOrderOperationModel clone()
    {
        ShopOrderOperationModel shopOrderOperationModel = new ShopOrderOperationModel();
        shopOrderOperationModel.setOrderNo(this.getOrderNo());
        shopOrderOperationModel.setOperationId(this.getOperationId());
        shopOrderOperationModel.setOperationNo(this.getOperationNo());
        shopOrderOperationModel.setWorkCenterNo(this.getWorkCenterNo());
        shopOrderOperationModel.setWorkCenterType(this.getWorkCenterType());
        shopOrderOperationModel.setOperationDescription(this.getOperationDescription());
        shopOrderOperationModel.setOperationSequence(this.getOperationSequence());
        shopOrderOperationModel.setPrecedingOperationId(this.getPrecedingOperationId());
        shopOrderOperationModel.setWorkCenterRuntimeFactor(this.getWorkCenterRuntimeFactor());
        shopOrderOperationModel.setWorkCenterRuntime(this.getWorkCenterRuntime());
        shopOrderOperationModel.setLaborRuntimeFactor(this.getLaborRuntimeFactor());
        shopOrderOperationModel.setLaborRunTime(this.getLaborRunTime());
        shopOrderOperationModel.setOpStartDate(this.getOpStartDate());
        shopOrderOperationModel.setOpStartTime(this.getOpStartTime());
        shopOrderOperationModel.setOpFinishDate(this.getOpFinishDate());
        shopOrderOperationModel.setOpFinishTime(this.getOpFinishTime());
        shopOrderOperationModel.setLatestOpFinishDate(this.getLatestOpFinishDate());
        shopOrderOperationModel.setLatestOpFinishTime(this.getLatestOpFinishTime());
        shopOrderOperationModel.setQuantity(this.getQuantity());
        shopOrderOperationModel.setOperationStatus(this.getOperationStatus());
        
        return shopOrderOperationModel;
    }
    
    /***
     * This method will split an operation by getting only the interruption start date time, and split the remainder of the operation to a separate operation
     * @param interruptionStartDateTime
     * @return 
     */
    public boolean splitInterruptedOperation(DateTime interruptionStartDateTime, InerruptionType interruptionType)
    {
        return splitInterruptedOperation(interruptionStartDateTime, DateTimeUtil.concatenateDateTime(getOpFinishDate(), getOpFinishTime()), interruptionType);
    }
    
    /**
     * This method will split an operation within the given start and end time and create new operations accordingly
     * @param interruptionStartDateTime
     * @param interruptionEndDateTime
     * @return 
     */
    public boolean splitInterruptedOperation(DateTime interruptionStartDateTime, DateTime interruptionEndDateTime, InerruptionType interruptionType)
    {
        List<OperationScheduleTimeBlocksDataModel> operationScheduledTimeBlocks = DataReader.getOperationScheduledTimeBlockDetails(this.getOperationId());
        
        int beforeInterruptionRuntime = 0;
        int withinInterruptionRuntime = 0;
        int afterInterruptionRuntime = 0;
        
        if (interruptionStartDateTime.isEqual(DateTimeUtil.concatenateDateTime(getOpStartDate(), getOpStartTime())) && 
                interruptionEndDateTime.isEqual(DateTimeUtil.concatenateDateTime(getOpFinishDate(), getOpFinishTime())))
        {
            System.out.println("Interruption start and end date times are equal to the operation start and end times, therefore no splitting required");
            return false;
        }
        
        for (OperationScheduleTimeBlocksDataModel operationScheduledTimeBlock : operationScheduledTimeBlocks)
        {
            DateTime timeBlockStartDateTime = DateTimeUtil.concatenateDateTime(operationScheduledTimeBlock.getOperationDate(), operationScheduledTimeBlock.getTimeBlockStartTime());
            // if timeblock comes before the interruption start time, increment beforeInterruptionRuntime variable
            if (timeBlockStartDateTime.isBefore(interruptionStartDateTime))
            {
                beforeInterruptionRuntime++;
            }
            // if timeblock comes in between the interruption start and end time, increment withinInterruptionRuntime
            else if (timeBlockStartDateTime.isEqual(interruptionStartDateTime) || timeBlockStartDateTime.isBefore(interruptionEndDateTime))
            {
                withinInterruptionRuntime++;
            }
            // if timeblock comes on or after the interruption end time, increment afterInterruptionRuntime
            else if (timeBlockStartDateTime.isEqual(interruptionEndDateTime) || timeBlockStartDateTime.isAfter(interruptionEndDateTime))
            {
                afterInterruptionRuntime++;
            }
        }
        
        double incOperationSequence = 0.00;
        ShopOrderOperationModel operationBeforeInterruption = null;
        ShopOrderOperationModel operationWithinInterruption = null;
        ShopOrderOperationModel operationAfterInterruption = null;

        ArrayList<ShopOrderOperationModel> operationToUpdate = new ArrayList<>();
        ArrayList<ShopOrderOperationModel> operationsToAdd = new ArrayList<>();

        if(beforeInterruptionRuntime > 0)
        {
            operationBeforeInterruption = clone();
            //update the current operation with the reduced runtime
            operationBeforeInterruption.setWorkCenterRuntime(beforeInterruptionRuntime);
            
            // this operation will end at the time where the interruption starts
            operationBeforeInterruption.setOpFinishDate(interruptionStartDateTime);
            operationBeforeInterruption.setOpFinishTime(interruptionStartDateTime);
            
            operationToUpdate.add(operationBeforeInterruption);
        }
        
        if (beforeInterruptionRuntime == 0 && withinInterruptionRuntime > 0)
        {
            operationWithinInterruption = clone();
            //update the current operation with the reduced runtime
            operationWithinInterruption.setWorkCenterRuntime(withinInterruptionRuntime);
            operationWithinInterruption.setOperationStatus(OperationStatus.Interrupted);
            
            operationToUpdate.add(operationWithinInterruption);
        }
        else if (beforeInterruptionRuntime > 0 && withinInterruptionRuntime > 0)
        {
            operationWithinInterruption = clone();
            operationWithinInterruption.setWorkCenterRuntime(withinInterruptionRuntime);
            incOperationSequence += 0.01;
            operationWithinInterruption.setOperationSequence(operationWithinInterruption.getOperationSequence() + incOperationSequence);
            operationWithinInterruption.setOperationDescription("Spltted From Operation ID " + operationWithinInterruption.getOperationId());
            // WorkCenter Runtime Factor and quantity is set to -1 for splitted operations, those should be referred to from the original operation
            operationWithinInterruption.setWorkCenterRuntimeFactor(-1);
            operationWithinInterruption.setQuantity(-1);
            operationWithinInterruption.setOperationStatus(OperationStatus.Interrupted);

            operationWithinInterruption.setOpStartDate(interruptionStartDateTime);
            operationWithinInterruption.setOpStartTime(interruptionStartDateTime);
            operationWithinInterruption.setOpFinishDate(null);
            operationWithinInterruption.setOpFinishTime(null);
            
            // if the previous operation was updated, set its operation Id as the preceding operation id of this.
            operationWithinInterruption.setPrecedingOperationId(operationBeforeInterruption.getOperationId());

            operationsToAdd.add(operationWithinInterruption);
        }
        
        if (afterInterruptionRuntime > 0)
        {
            operationAfterInterruption = clone();
            operationAfterInterruption.setWorkCenterRuntime(afterInterruptionRuntime);
            incOperationSequence += 0.01;
            operationAfterInterruption.setOperationSequence(operationAfterInterruption.getOperationSequence() + incOperationSequence);
            operationAfterInterruption.setOperationDescription("Spltted From Operation ID " + operationAfterInterruption.getOperationId());
            // WorkCenter Runtime Factor and quantity is set to -1 for splitted operations, those should be referred to from the original operation
            operationAfterInterruption.setWorkCenterRuntimeFactor(-1);
            operationAfterInterruption.setQuantity(-1);
            // Operation after the interruption should be in the Unscheduled status
            operationAfterInterruption.setOperationStatus(OperationStatus.Unscheduled);

            // if the previous operation has been only update
            if (beforeInterruptionRuntime == 0 && withinInterruptionRuntime > 0)
            {
                // set the previous operation id as the preceding operation id for this operation
                operationAfterInterruption.setPrecedingOperationId(operationWithinInterruption.getOperationId());
            }
            // if the previous operation has been added newly
            else if (beforeInterruptionRuntime > 0 && withinInterruptionRuntime > 0)
            {
                // set 0 as the preceding operation id so that it will be handled using the operation sequence
                operationAfterInterruption.setPrecedingOperationId(0);
            }
            
            // this operation should start when the interruption ends and finishes at the previous operation finish time
            operationAfterInterruption.setOpStartDate(interruptionEndDateTime);
            operationAfterInterruption.setOpStartTime(interruptionEndDateTime);

            // the operations that comes after will always be splitted and added
            operationsToAdd.add(operationAfterInterruption);
        }

        DataWriter.updateShopOrderOperationData(operationToUpdate);
        DataWriter.addShopOrderOperationData(operationsToAdd);
        
        // After creating new operations by splitting the existing operations, for each of the created operations, the work center should be interrupted
        for (ShopOrderOperationModel addedOperation : operationsToAdd)
        {
            WorkCenterModel workCenter = DataReader.getWorkCenterByPrimaryKey(getWorkCenterNo());
            
            // if the current addedOperation is the operation that falls between the interrupted time, check if the given interruptionType is normal interruption, or 
            // priority unschedule, and call the respective method to unschedule work center accordingly
            if(addedOperation.getOperationStatus().equals(OperationStatus.Interrupted))
            {
                if(interruptionType.equals(InerruptionType.Interruption))
                {
                    workCenter.unscheduleWorkCenterOnInterruption(DateTimeUtil.concatenateDateTime(addedOperation.getOpStartDate(), addedOperation.getOpStartTime()), 
                        addedOperation.getWorkCenterRuntime());
                }
                else
                {
                    workCenter.unscheduleWorkCenterOnPriority(DateTimeUtil.concatenateDateTime(addedOperation.getOpStartDate(), addedOperation.getOpStartTime()), 
                        addedOperation.getWorkCenterRuntime());
                }
            }
            // if the operation comes after the interrupted time, the work center should anyway be able to utilize that time, hence it will be unscheduled as a priority
            // unschedule of the work center
            else
            {
                workCenter.unscheduleWorkCenterOnPriority(DateTimeUtil.concatenateDateTime(addedOperation.getOpStartDate(), addedOperation.getOpStartTime()), 
                        addedOperation.getWorkCenterRuntime());
            }
        }
        
        return true;
    }
    
    
    /**
     * This method will update the current operation status as unscheduled and update the work center schedule accordingly
     * @return 
     */
    public boolean unscheduleOperation()
    {
        ArrayList<ShopOrderOperationModel> operationToUpdate = new ArrayList<>();
        
        // Set operation status to unschedule and update the database
        this.setOperationStatus(OperationStatus.Unscheduled);
        operationToUpdate.add(this);
        DataWriter.updateShopOrderOperationData(operationToUpdate);
        
        // update the work center allocations as priority unschedule so that the available time can be utilized later
        WorkCenterModel workCenter = DataReader.getWorkCenterByPrimaryKey(getWorkCenterNo());
        workCenter.unscheduleWorkCenterOnPriority(DateTimeUtil.concatenateDateTime(this.getOpStartDate(), this.getOpStartTime()), 
                        this.getWorkCenterRuntime());
        
        return true;
    }
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="comparator implementation"> 
    
    @Override
    public int compare(ShopOrderOperationModel o1, ShopOrderOperationModel o2)
    {
        int returnVal = o1.getOperationSequence() > o2.getOperationSequence() ? 1 : -1;
        return returnVal;
    }
    
    // </editor-fold> 
}
