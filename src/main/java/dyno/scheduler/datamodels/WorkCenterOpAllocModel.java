/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.data.DataEnums;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.GeneralSettings;
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

    public WorkCenterOpAllocModel()
    {
        timeBlockAllocation = new HashMap<>();
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
            Row excelRow = (Row) row;
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
    public LocalTime getTimeBlockValue(String timeBlock)
    {
        DateTime timeBlockValue;

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
                return null;
        }
        return timeBlockValue.toLocalTime();
    }

    public String getTimeBlockName(LocalTime startTime)
    {
        String timeBlockValue = null;
        String startTimeString = startTime.toString();
        switch (GeneralSettings.getCapacityType())
        {
            case FiniteCapacity:
            {
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
                    // special case: 12.00 should be taken as the next time block
                    case "12:00:00.000":
                        timeBlockValue = "TB5";
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
                break;
            }
            case InfiniteCapacity:
            {
                switch (startTimeString)
                {
                    case "00:00:00.000":
                        timeBlockValue = "TB1";
                        break;
                    case "01:00:00.000":
                        timeBlockValue = "TB2";
                        break;
                    case "02:00:00.000":
                        timeBlockValue = "TB3";
                        break;
                    case "03:00:00.000":
                        timeBlockValue = "TB4";
                        break;
                    case "04:00:00.000":
                        timeBlockValue = "TB5";
                        break;
                    case "05:00:00.000":
                        timeBlockValue = "TB6";
                        break;
                    case "06:00:00.000":
                        timeBlockValue = "TB7";
                        break;
                    case "07:00:00.000":
                        timeBlockValue = "TB8";
                        break;
                    case "08:00:00.000":
                        timeBlockValue = "TB9";
                        break;
                    case "09:00:00.000":
                        timeBlockValue = "TB10";
                        break;
                    case "10:00:00.000":
                        timeBlockValue = "TB11";
                        break;
                    case "11:00:00.000":
                        timeBlockValue = "TB12";
                        break;
                    case "12:00:00.000":
                        timeBlockValue = "TB13";
                        break;
                    case "13:00:00.000":
                        timeBlockValue = "TB14";
                        break;
                    case "14:00:00.000":
                        timeBlockValue = "TB15";
                        break;
                    case "15:00:00.000":
                        timeBlockValue = "TB16";
                        break;
                    case "16:00:00.000":
                        timeBlockValue = "TB17";
                        break;
                    case "17:00:00.000":
                        timeBlockValue = "TB18";
                        break;
                    case "18:00:00.000":
                        timeBlockValue = "TB19";
                        break;
                    case "19:00:00.000":
                        timeBlockValue = "TB20";
                        break;
                    case "20:00:00.000":
                        timeBlockValue = "TB21";
                        break;
                    case "21:00:00.000":
                        timeBlockValue = "TB22";
                        break;
                    case "22:00:00.000":
                        timeBlockValue = "TB23";
                        break;
                    case "23:00:00.000":
                        timeBlockValue = "TB24";
                        break;

                    default:
                        break;
                }
                break;
            }
        }
        return timeBlockValue;
    }

    /**
     * this method is used to increment a given time block value by an integer.
     * if the timeblock TB4 of a given day is incremented by 13, the
     * newTimeBlock value is 17 (more than 8) and if the capacity type is finite
     * (only 8 hrs), then after incrementing, the new time block should be the
     * TB1 after 2 days (4 + 8 + "1"). Therefore returns newTimeBlock value as
     * T1 and daysAdded as 2 in a list
     *
     * @param timeBlock current time block
     * @param incrementBy the value to be incremented by
     * @return a list: first element is the new Time block name, second element
     * is the daysAdded after incrementing
     */
    public HashMap<String, Object> incrementTimeBlock(String timeBlock, int incrementBy)
    {
        HashMap<String, Object> returnList = new HashMap<>();
        int currentTimeBlock = Integer.parseInt(timeBlock.substring(2));
        int newTimeBlock = currentTimeBlock + incrementBy;
        int daysAdded = 0;

        if (newTimeBlock > 8 && GeneralSettings.getCapacityType() == DataEnums.CapacityType.FiniteCapacity)
        {
            while (newTimeBlock >= 8)
            {
                newTimeBlock = newTimeBlock - 8;
                daysAdded++;
            }
        } else if (newTimeBlock > 24 && GeneralSettings.getCapacityType() == DataEnums.CapacityType.InfiniteCapacity)
        {
            while (newTimeBlock >= 24)
            {
                newTimeBlock = newTimeBlock - 24;
                daysAdded++;
            }
        }

        String newTimeBlockName = "TB" + newTimeBlock;

        returnList.put(GeneralSettings.getStrTimeBlockName(), newTimeBlockName);
        returnList.put(GeneralSettings.getStrDaysAdded(), daysAdded);

        return returnList;
    }
}
