/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.datamodels.DataModelEnums.ShopOrderPriority;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderScheduleStatus;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderSchedulingDirection;
import dyno.scheduler.datamodels.DataModelEnums.ShopOrderStatus;
import dyno.scheduler.utils.DateTimeUtil;
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
public class ShopOrderModel extends DataModel
{
    // <editor-fold desc="properties"> 

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
        return operations;
    }

    public void setOperations(List<ShopOrderOperationModel> operations)
    {
        this.operations = operations;
    }

    // </editor-fold> 
    
    // <editor-fold desc="overriden methods"> 
    
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
            Row excelRow = (Row)row;
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

    // </editor-fold> 
}
