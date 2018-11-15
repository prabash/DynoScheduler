/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.data.DataEnums;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderPriority;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderScheduleStatus;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderSchedulingDirection;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderStatus;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.GeneralSettings;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
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
public class ShopOrderModel extends DataModel
{

    //<editor-fold defaultstate="collapsed" desc="properties">
    private String orderNo;
    private String description;
    private DateTime createdDate;
    private String partNo;
    private String structureRevision;
    private String routingRevision;
    private DateTime requiredDate;
    private DateTime startDate;
    private DateTime finishDate;
    private ShopOrderSchedulingDirection schedulingDirection;
    private String customerNo;
    private ShopOrderScheduleStatus schedulingStatus;
    private ShopOrderStatus shopOrderStatus;
    private ShopOrderPriority priority;
    private List<ShopOrderOperationModel> operations;

    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="constructors">
    public ShopOrderModel()
    {
        AGENT_PREFIX = "SHOP_ORDER_AGENT";
    }

    public ShopOrderModel(String orderNo, String description, DateTime createdDate, String partNo, String structureRevision, String routingRevision, DateTime requiredDate,
            DateTime startDate, DateTime finishDate, ShopOrderSchedulingDirection schedulingDirection, String customerNo, ShopOrderStatus shopOrderStatus, ShopOrderPriority priority, List<ShopOrderOperationModel> operaitons)
    {
        this.orderNo = orderNo;
        this.description = description;
        this.createdDate = createdDate;
        this.partNo = partNo;
        this.structureRevision = structureRevision;
        this.routingRevision = routingRevision;
        this.requiredDate = requiredDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.schedulingDirection = schedulingDirection;
        this.customerNo = customerNo;
        this.shopOrderStatus = shopOrderStatus;
        this.priority = priority;
        this.operations = operaitons;
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

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public DateTime getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(DateTime createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getPartNo()
    {
        return partNo;
    }

    public void setPartNo(String partNo)
    {
        this.partNo = partNo;
    }

    public String getStructureRevision()
    {
        return structureRevision;
    }

    public void setStructureRevision(String structureRevision)
    {
        this.structureRevision = structureRevision;
    }

    public String getRoutingRevision()
    {
        return routingRevision;
    }

    public void setRoutingRevision(String routingRevision)
    {
        this.routingRevision = routingRevision;
    }

    public DateTime getRequiredDate()
    {
        return requiredDate;
    }

    public void setRequiredDate(DateTime requiredDate)
    {
        this.requiredDate = requiredDate;
    }

    public DateTime getStartDate()
    {
        return startDate;
    }

    public void setStartDate(DateTime startDate)
    {
        this.startDate = startDate;
    }

    public DateTime getFinishDate()
    {
        return finishDate;
    }

    public void setFinishDate(DateTime finishDate)
    {
        this.finishDate = finishDate;
    }

    public ShopOrderSchedulingDirection getSchedulingDirection()
    {
        return schedulingDirection;
    }

    public void setSchedulingDirection(ShopOrderSchedulingDirection schedulingDirection)
    {
        this.schedulingDirection = schedulingDirection;
    }

    public String getCustomerNo()
    {
        return customerNo;
    }

    public void setCustomerNo(String customerNo)
    {
        this.customerNo = customerNo;
    }

    public ShopOrderScheduleStatus getSchedulingStatus()
    {
        return schedulingStatus;
    }

    public void setSchedulingStatus(ShopOrderScheduleStatus schedulingStatus)
    {
        this.schedulingStatus = schedulingStatus;
    }

    public ShopOrderStatus getShopOrderStatus()
    {
        return shopOrderStatus;
    }

    public void setShopOrderStatus(ShopOrderStatus thisStatus)
    {
        this.shopOrderStatus = thisStatus;
    }

    public ShopOrderPriority getPriority()
    {
        return priority;
    }

    public void setPriority(ShopOrderPriority priority)
    {
        this.priority = priority;
    }

    public List<ShopOrderOperationModel> getOperations()
    {
        Collections.sort(operations, (ShopOrderOperationModel o1, ShopOrderOperationModel o2) -> Integer.compare(o1.getOperationSequence(), o2.getOperationSequence()));
        return operations;
    }

    public void setOperations(List<ShopOrderOperationModel> operations)
    {
        this.operations = operations;
    }
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="overriden methods"> 
    /**
     * get ShopOrderModel object by passing Excel or MySql table row
     *
     * @param row relevant data object
     * @return ShopOrderModel object
     */
    @Override
    public ShopOrderModel getModelObject(Object row)
    {
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();

        if (row instanceof Row)
        {
            Row excelRow = (Row) row;
            int i = -1;

            this.setOrderNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setDescription(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setCreatedDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
            this.setPartNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setStructureRevision(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setRoutingRevision(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setRequiredDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
            this.setStartDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
            this.setFinishDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
            this.setSchedulingDirection(ShopOrderSchedulingDirection.valueOf(dataFormatter.formatCellValue(excelRow.getCell(++i))));
            this.setCustomerNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setSchedulingStatus(ShopOrderScheduleStatus.valueOf(dataFormatter.formatCellValue(excelRow.getCell(++i))));
            this.setShopOrderStatus(ShopOrderStatus.valueOf(dataFormatter.formatCellValue(excelRow.getCell(++i))));
            this.setPriority(ShopOrderPriority.valueOf(dataFormatter.formatCellValue(excelRow.getCell(++i))));

            return this;
        } else
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
        }
    }

    @Override
    public String getPrimaryKey()
    {
        return getOrderNo();
    }

    @Override
    public String getClassName()
    {
        return ShopOrderModel.class.getName();
    }

    @Override
    public String getAgentPrefix()
    {
        return this.AGENT_PREFIX;
    }

    /**
     * this method is used to get a target start date for the operation by
     * sending its primary key (operation id) If the precedingOperation ID is 0,
     * get the shopOrder start date or else get the precedingOperation ID's
     * operation finish date/time
     *
     * @param opPrimaryKey primary key
     * @return target operation start date
     */
    public DateTime getOperationTargetStartDate(String opPrimaryKey)
    {
        List<ShopOrderOperationModel> currentOperations = getOperations();
        DateTime opTargetStartDate = null;
        
        for (ShopOrderOperationModel operation : currentOperations)
        {
            if (operation.getPrimaryKey().equals(opPrimaryKey))
            {
                // first operation sequence
                if (operation.getPrecedingOperationId() == 0)
                {
                    // set the shop order created date as the opTargetStartDate for the first operation     
                    opTargetStartDate = getShopOrderStartDateTime();
                    break;
                } // subsequent operations
                else
                {
                    // the finish datetime of the Preceding Operation Id, should be taken as the target start date/time of the operations
                    ShopOrderOperationModel precedingOp = currentOperations.stream().
                            filter(rec -> rec.getPrimaryKey().equals(String.valueOf(operation.getPrecedingOperationId()))).
                            collect(Collectors.toList()).get(0);
                    opTargetStartDate = DateTimeUtil.concatenateDateTime(precedingOp.getOpFinishDate(), precedingOp.getOpFinishTime());
                    break;
                }
            }
        }

        return opTargetStartDate;
    }

    /**
     * Return the startDate of a Shop Order by considering various parameters
     * @return Starting Date Time of the Shop Order
     */
    public DateTime getShopOrderStartDateTime()
    {
        DateTime shopOrderStartDateTime = null;
        if (getSchedulingDirection() == ShopOrderSchedulingDirection.Forward)
        {
            shopOrderStartDateTime = getStartTimeOnCapacityType(getCreatedDate());
        }
        else if (getSchedulingDirection() == ShopOrderSchedulingDirection.Backward)
        {
            double bufferPercentage = 20.0; // percentage of the total runtime duration that will be a buffer, minimum buffer value is 1 day
            
            int runtimeInDays = getShopOrderTotalRunTimeDays();
            int bufferDays = Math.round((float)Math.ceil(runtimeInDays * bufferPercentage / 100.0));
            // set the minimum buffer days
            if (bufferDays < 1)
            {
                bufferDays = 1;
            }
            
            // total runtime and the buffer will be reducted from the required date and calculate the start date
            DateTime shopOrderStartDate = getRequiredDate().minusDays(runtimeInDays + bufferDays);
            shopOrderStartDateTime = getStartTimeOnCapacityType(shopOrderStartDate);
        }
        
        return shopOrderStartDateTime;
    }
    
    /**
     * Get the latest possible start date of the order in order to meet the required date
     * @return latestPossibleStartDate
     */
    private DateTime getLatestOrderStartDate()
    {
        int runtimeInDays = getShopOrderTotalRunTimeDays();
        DateTime shopOrderStartDate = getRequiredDate().minusDays(runtimeInDays);
        return getStartTimeOnCapacityType(shopOrderStartDate);
    }
    
    /**
     * this will concatenate a start-time to a given date based on the capacity type
     * @param date the date to which the time should be concatenated
     * @return 
     */
    private DateTime getStartTimeOnCapacityType(DateTime date)
    {
        // Calcualate the start datetime depending on the capacity type
        if (GeneralSettings.getCapacityType() == DataEnums.CapacityType.FiniteCapacity)
        {
            // for finite capacity starting time of the day is 0800HRS
            return DateTimeUtil.concatenateDateTime(date.toString(DateTimeUtil.getDateFormat()), "08:00:00");
        } 
        else
        {
            // for finite capacity starting time of the day is 0000HRS
            return DateTimeUtil.concatenateDateTime(date.toString(DateTimeUtil.getDateFormat()), "00:00:00");
        }
    }
    
    /**
     * This method is used assign the latest finish  times for operations.
     */
    public void assignLatestFinishTimeForOperations()
    {
        List<ShopOrderOperationModel> currentOperations = getOperations();
        DateTime latestOrderStartDate = getLatestOrderStartDate();
        DateTime latestOpEndDateTime = null;
        
        for (ShopOrderOperationModel operation : currentOperations)
        {
            // if the operation is the starting operation/s
            if (operation.getPrecedingOperationId() == 0)
            {
                // increment the order start datetime (starting date time of the first operation) by the first operation's work center run time
                latestOpEndDateTime = WorkCenterOpAllocModel.incrementTime(latestOrderStartDate, operation.getWorkCenterRuntime());
                
                operation.setLatestOpFinishDate(latestOpEndDateTime);
                operation.setLatestOpFinishTime(latestOpEndDateTime);
            }
            // else get the previous operation latest finish date time and add the workcenter runtime
            else
            {
                // the finish datetime of the Preceding Operation Id, should be taken as the target start date/time of the operations
                ShopOrderOperationModel precedingOp = currentOperations.stream().
                        filter(rec -> rec.getPrimaryKey().equals(String.valueOf(operation.getPrecedingOperationId()))).
                        collect(Collectors.toList()).get(0);
                // calculate the current op latestStartDate by concatenating the prev. op finish date and time
                DateTime previousOpLatestFinishDateTime = DateTimeUtil.concatenateDateTime(precedingOp.getLatestOpFinishDate(), precedingOp.getLatestOpFinishTime());
                
                 // increment the previous operation latest finish datetime by current operation work center runtime.
                latestOpEndDateTime = WorkCenterOpAllocModel.incrementTime(previousOpLatestFinishDateTime, operation.getWorkCenterRuntime());
                operation.setLatestOpFinishDate(latestOpEndDateTime);
                operation.setLatestOpFinishTime(latestOpEndDateTime);
            }
            System.out.println("SO : " + operation.getOrderNo() + " Op No : " + operation.getOperationId() + " Latest Op End Date : " +
                    DateTimeUtil.concatenateDateTime(operation.getLatestOpFinishDate(), operation.getLatestOpFinishTime()).toString(DateTimeUtil.getDateTimeFormat()));
        }
        System.out.println("Completed assigning latest finish times for operations");
    }
    
    
    public int getShopOrderTotalRunTimeDays()
    {
        int totalRuntTimeInHours = 0;
        int totalRuntimeDays = 0;
        // calculate the total number of runtime hours 
        totalRuntTimeInHours = getOperations().stream().map((operation) -> operation.getWorkCenterRuntime()).reduce(totalRuntTimeInHours, (accumulator, _item) -> accumulator + _item);
        
        // for finite capacity, get the number of runtime in days by dividing the no. of hours by 8
        if (GeneralSettings.getCapacityType() == DataEnums.CapacityType.FiniteCapacity)
        {
            totalRuntimeDays = Math.round((float)Math.ceil(totalRuntTimeInHours / 8.0));
        }
        // for infinite capacity, get the number of runtime in days by dividing the no. of hours by 24
        else if (GeneralSettings.getCapacityType() == DataEnums.CapacityType.InfiniteCapacity)
        {
            totalRuntimeDays = Math.round((float)Math.ceil(totalRuntTimeInHours / 24.0));
        }
        
        return totalRuntimeDays;
    }

    /**
     * this method is used to update the operations of the current shop order
     * object by providing the operation object
     *
     * @param operationOb operation object
     */
    public void updateOperation(ShopOrderOperationModel operationOb)
    {
        List<ShopOrderOperationModel> currentOperations = getOperations();
        int index = 0;
        // for each of the available operations
        for (ShopOrderOperationModel operation : currentOperations)
        {
            // check if the current operationId matches with the sent operation's operationId
            // and if so break the loop, keeping the index value;
            if (operation.getPrimaryKey().equals(operationOb.getPrimaryKey()))
            {
                break;
            }

            // increment index value
            index++;
        }

        // replace the operation in the index by the nex operation object
        currentOperations.set(index, operationOb);

        // update the operations list
        setOperations(currentOperations);
    }

    // </editor-fold> 
}
