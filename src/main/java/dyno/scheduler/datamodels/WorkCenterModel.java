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
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

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
    
    
    private List<WorkCenterOpAllocModel> currentWorkCenterOpAllocs;
    private String currentTimeBlockName;
    private LocalDate currentDate;
    /**
     * *
     * this method will return the best date time offer for the work center no
     * and the required date TODO: should also incorporate scheduling direction
     * and time factors
     *
     * @param requiredDateTime
     * @return
     */
    public DateTime getBestDateTimeOffer(DateTime requiredDateTime, int workCenterRuntime)
    {
        DateTime bestDateTimeOffer = null;
        
        // get the date and the time portion of the required datetime
        currentDate = requiredDateTime.toLocalDate();
        currentTimeBlockName = new WorkCenterOpAllocModel().getTimeBlockName(requiredDateTime.toLocalTime());
        
        // get the work center allocation details, filter it by the current work center no.
        // TODO: The filteration should happen when taking from the database.
        currentWorkCenterOpAllocs = DataReader.getWorkCenterOpAllocDetails(true).stream()
                .filter(rec -> rec.getWorkCenterNo().equals(this.getWorkCenterNo()))
                .collect(Collectors.toList());
        
        // sort the work center allocations by date on the ascending order
        Collections.sort(currentWorkCenterOpAllocs, (WorkCenterOpAllocModel o1, WorkCenterOpAllocModel o2) -> o1.getOperationDate().compareTo(o2.getOperationDate()));
        
        while(bestDateTimeOffer == null)
        {
            // get the WorkCenterOpAllocModel object from the list, related to the currentDate
            WorkCenterOpAllocModel workCenterOpAlloc = currentWorkCenterOpAllocs.stream().filter(aloc -> aloc.getOperationDate().toLocalDate().equals(currentDate)).
                                                                    collect(Collectors.toList()).get(0);
            
            // get timeBlock allocation for the given currentDate
            HashMap<String, Integer> timeBlockAllocation = workCenterOpAlloc.getTimeBlockAllocation();
            
            // if the currentTimeBlock is not allocated
            if(timeBlockAllocation.get(currentTimeBlockName) == 0) 
            {
                // check if there's enough consecutive time available in the work center to allocate the workCenterRuntime
                if(checkConsecutiveTimeBlockAvailability(workCenterRuntime))
                {
                    bestDateTimeOffer = DateTimeUtil.concatenateDateTime(currentDate, new WorkCenterOpAllocModel().getTimeBlockValue(currentTimeBlockName));
                }
                else
                {
                    // increment the time by workCenterRuntime factor and get the timeblock name and assign it;
                    // the reason for incrementing by the workCenterRuntime rather than evaluatin each and every timeblock, is to increase the performance
                    List<Object> incrementDetails = new WorkCenterOpAllocModel().incrementTimeBlock(currentTimeBlockName, workCenterRuntime);
                    currentTimeBlockName = incrementDetails.get(0).toString();
                    currentDate = currentDate.plusDays(Integer.parseInt(incrementDetails.get(1).toString()));
                }
            }
            // if the currentTimeBlock is already allocated 
            else
            {
                // increment the time by 1 (an hour) and get the timeblock name and assign it;
                List<Object> incrementDetails = new WorkCenterOpAllocModel().incrementTimeBlock(currentTimeBlockName, 1);
                currentTimeBlockName = incrementDetails.get(0).toString();
                currentDate = currentDate.plusDays(Integer.parseInt(incrementDetails.get(1).toString()));
            }
        }
        
        return bestDateTimeOffer;
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
    
    private boolean checkConsecutiveTimeBlockAvailability(int workCenterRuntime)
    {
        return true;
    }
    
    // </editor-fold> 

}
