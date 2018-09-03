/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.data.DataReader;
import dyno.scheduler.data.DataWriter;
import dyno.scheduler.utils.DateTimeUtil;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalTime;

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
    public WorkCenterModel getModelObject(Object row)
    {
        // Create a DataFormatter to format and get each cell's value as String
        DataFormatter dataFormatter = new DataFormatter();

        if (row instanceof Row)
        {
            Row excelRow = (Row)row;
            int i = -1;
            
            this.setWorkCenterNo(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setWorkCenterType(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setWorkCenterDescription(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            this.setWorkCenterCapacity(dataFormatter.formatCellValue(excelRow.getCell(++i)));
            
            return this;

        } else
        {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
        }
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
    
    /**
     * *
     * this method will return the best date time offer for the work center no
     * and the required date TODO: should also incorporate scheduling direction
     * and time factors
     *
     * @param requiredDate
     * @return
     */
    public DateTime getBestDateTimeOffer(DateTime requiredDate)
    {
        DateTime bestDateTimeOffer = null;
        // get the allocation for the work center. Set refresh to true so that it will always get the latest values from the DB
        // TODO: if the requiredDate allocation is full next possible date should be taken
        List<WorkCenterOpAllocModel> workCenterAlloc = DataReader.getWorkCenterOpAllocDetails(true).stream()
                .filter(rec -> rec.getWorkCenterNo().equals(this.getWorkCenterNo()))
                .collect(Collectors.toList());

        if (workCenterAlloc != null)
        {
            // sort the work center allocations by date on the ascending order
            Collections.sort(workCenterAlloc, (WorkCenterOpAllocModel o1, WorkCenterOpAllocModel o2) -> o1.getOperationDate().compareTo(o2.getOperationDate()));

            bestDateTimeOffer = getBestDateOffer(workCenterAlloc, requiredDate);
        }
        return bestDateTimeOffer;
    }
    

    private DateTime getBestDateOffer(List<WorkCenterOpAllocModel> workCenterAlloc, DateTime requiredDate)
    {
        DateTime bestDate = null;
        // convert the required date to joda datetime
        DateTime requiredDateTime = new DateTime(requiredDate);

        for (WorkCenterOpAllocModel workCenterOpAlloc : workCenterAlloc)
        {
            // convert the work center date to joda datetime
            DateTime workCenterOpDate = new DateTime(workCenterOpAlloc.getOperationDate());
            if (workCenterOpDate.toLocalDate().equals(requiredDateTime.toLocalDate()) || workCenterOpDate.toLocalDate().isAfter(requiredDateTime.toLocalDate()))
            {
                bestDate = getBestTimeOffer(workCenterOpAlloc, requiredDateTime.toLocalTime());
                break;
            }
        }

        return bestDate;
    }
    

    /**
     * *
     * this method will get the date allocation and return the earliest
     * available timeblock value
     *
     * @param allocation
     * @param reqTime
     * @return
     */
    public DateTime getBestTimeOffer(WorkCenterOpAllocModel allocation, LocalTime reqTime)
    {
        DateTime bestTimeOffer = null;

        // get the hashmap with timeblock assignment details
        HashMap<String, Integer> timeBlockDetails = allocation.getTimeBlockAllocation();

        //sort the hashmap by its keys
        SortedSet<String> keys = new TreeSet<>(timeBlockDetails.keySet());
        for (String key : keys)
        {
            LocalTime timeBlockStartTime = new DateTime(allocation.getTimeBlockValue(key)).toLocalTime();
            if ((timeBlockStartTime.equals(reqTime) || timeBlockStartTime.isAfter(reqTime)) && timeBlockDetails.get(key) == 0)
            {
                bestTimeOffer = DateTimeUtil.concatenateDateTime(allocation.getOperationDate(), allocation.getTimeBlockValue(key));
                break;
            }
        }
        return bestTimeOffer;
    }
    
    public void updateWorkCenterOpAllocDetails(String workCenterNo, DateTime bestOfferedDate, int operationId, int workCenterRuntime)
    {
        List<WorkCenterOpAllocModel> workCenterOpAllocations = new ArrayList<>();
        WorkCenterOpAllocModel alloc = new WorkCenterOpAllocModel();
        // bestOfferedDate value also has the time portion in it. Therefore, convert it to string and only add the Date portion
        alloc.setOperationDate(DateTimeUtil.getDateFormat().parseDateTime(bestOfferedDate.toString(DateTimeUtil.getDateFormat())));
        alloc.setWorkCenterNo(workCenterNo);
        
        // time blocks should be updated for the given runtime factor
        for (int i = 0; i < workCenterRuntime; i++)
        {
            alloc.addToTimeBlockAllocation(alloc.getTimeBlockName(new DateTime(bestOfferedDate.plusHours(i)).toLocalTime()), operationId);
        }
        workCenterOpAllocations.add(alloc);

        // update work center allocation data with provided information
        DataWriter.updateWorkCenterAllocData(workCenterOpAllocations, workCenterNo);
    }
    
    // </editor-fold> 
}
