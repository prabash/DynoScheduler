/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.utils.DateTimeUtil;
import java.util.HashMap;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormatter;

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
    
    public void addToTimeBlockAllocation(String timBlockName, int operationId)
    {
        this.timeBlockAllocation.put(timBlockName, operationId);
    }

    // </editor-fold>
    
    // <editor-fold desc="overriden methods"> 
    /**
     * get WorkCenterOpAllocModel object by passing Excel or MySql table row
     *
     * @param rowData relevant data object
     * @return WorkCenterOpAllocModel object
     */
    @Override
    public WorkCenterOpAllocModel getModelObject(Object row)
    {
         // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();
        DateTimeFormatter dateFormat = DateTimeUtil.getDateFormat();
        int noOfTimeBlocks = 8;

        if (row instanceof Row)
        {
            Row excelRow = (Row)row;
            int i = -1;

            this.setWorkCenterNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setOperationDate(excelRow.getCell(++i) == null ? null : dateFormat.parseDateTime(dataFormatter.formatCellValue(excelRow.getCell(i))));
            this.setTimeBlockAllocation(new HashMap<>());
            int timeBlockId = 1;
            for (int j = ++i; j < i + noOfTimeBlocks; j++)
            {
                if (excelRow.getCell(j) != null)
                {
                    this.addToTimeBlockAllocation("TB" + timeBlockId, Integer.parseInt(dataFormatter.formatCellValue(excelRow.getCell(j))));
                } else
                {
                    this.addToTimeBlockAllocation("TB" + timeBlockId, 0);
                }
                timeBlockId++;
            }
            return this;

        } else
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
        }
    }

    @Override
    public String getPrimaryKey()
    {
        // TODO: should change this
        return getWorkCenterNo() + getOperationDate().toString();
    }

    @Override
    public String getClassName()
    {
        return WorkCenterOpAllocModel.class.getName();
    }

    // </editor-fold> 
    
    public DateTime getTimeBlockValue(String timeBlock)
    {
        DateTime timeBlockValue = null;

        DateTimeFormatter timeFormat = DateTimeUtil.getTimeFormat();
        switch (timeBlock)
        {
            case "TB1":
                timeBlockValue = timeFormat.parseDateTime("08:00:00");
                break;
            case "TB2":
                timeBlockValue = timeFormat.parseDateTime("09:00:00");
                break;
            case "TB3":
                timeBlockValue = timeFormat.parseDateTime("10:00:00");
                break;
            case "TB4":
                timeBlockValue = timeFormat.parseDateTime("11:00:00");
                break;
            case "TB5":
                timeBlockValue = timeFormat.parseDateTime("13:00:00");
                break;
            case "TB6":
                timeBlockValue = timeFormat.parseDateTime("14:00:00");
                break;
            case "TB7":
                timeBlockValue = timeFormat.parseDateTime("15:00:00");
                break;
            case "TB8":
                timeBlockValue = timeFormat.parseDateTime("16:00:00");
                break;
            default:
                break;
        }
        return timeBlockValue;
    }

    public String getTimeBlockName(LocalTime startTime)
    {
        String timeBlockValue = null;
        String startTimeString = startTime.toString();
        switch (startTimeString)
        {
            case "08:00:00.000":
                timeBlockValue = "TB1";
                break;
            case "09:00:00.000":
                timeBlockValue = "TB2";
                break;
            case "10:00:00.000":
                timeBlockValue = "TB3";
                break;
            case "11:00:00.000":
                timeBlockValue = "TB4";
                break;
            case "13:00:00.000":
                timeBlockValue = "TB5";
                break;
            case "14:00:00.000":
                timeBlockValue = "TB6";
                break;
            case "15:00:00.000":
                timeBlockValue = "TB7";
                break;
            case "16:00:00.000":
                timeBlockValue = "TB8";
                break;
            default:
                break;
        }
        return timeBlockValue;
    }
}
