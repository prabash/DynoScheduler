/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.data.DataWriter;
import dyno.scheduler.datamodels.DataModelEnums.OperationStatus;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
public class ShopOrderOperationModel extends DataModel
{
    // <editor-fold defaultstate="collapsed" desc="properties"> 

    private String orderNo;
    private int operationId;
    private int operationNo;
    private String workCenterNo;
    private String workCenterType;
    private String operationDescription;
    private int operationSequence;
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

    public ShopOrderOperationModel(String orderNo, int operationId, int operationNo, String workCenterNo, String workCenterType, String operationDescription, int operationSequence,
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

    public int getOperationSequence()
    {
        return operationSequence;
    }

    public void setOperationSequence(int operationSequence)
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
                this.setOperationSequence(Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(++i))));
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
                this.setOperationSequence(resultSetRow.getInt(++i));
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
        return DataWriter.updateWorkShopOrderOperationData(updateList);
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
