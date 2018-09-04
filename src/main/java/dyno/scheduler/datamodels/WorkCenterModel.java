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
                if(checkConsecutiveTimeBlockAvailability(currentDate, currentTimeBlockName, workCenterRuntime))
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
    
    List<WorkCenterOpAllocModel> workCenterOpAllocUpdate = new ArrayList<>();
    
    public void updateWorkCenterOpAllocDetails(DateTime bestOfferedDate, int operationId, int workCenterRuntime)
    {
        workCenterOpAllocUpdate = new ArrayList<>();
        String bestOfferStartTimeBlock = new WorkCenterOpAllocModel().getTimeBlockName(bestOfferedDate.toLocalTime());
        
        getWorkCenterOpAllocObjectForUpdate(bestOfferedDate, bestOfferStartTimeBlock, operationId, workCenterRuntime);
        
        // update work center allocation data with provided information
        DataWriter.updateWorkCenterAllocData(workCenterOpAllocUpdate);
    }
    
    /**
     * this method will recursively add WorkCenterOpAllocModel objects to the workCenterOpAllocUpdate list
     * @param currentDate initially this should be the bestOfferedDate, afterwards, the subsequent days depending on the workCenterRuntime
     * @param timeBlockName startingTimeBlock name for the day
     * @param operationId operationId that should be allocated for
     * @param workCenterRuntime workCenterRuntime
     */
    private void getWorkCenterOpAllocObjectForUpdate(DateTime currentDate, String timeBlockName, int operationId, int workCenterRuntime)
    {
        WorkCenterOpAllocModel allocObj = new WorkCenterOpAllocModel();
        
        // bestOfferedDate value also has the time portion in it. Therefore, convert it to string and only add the Date portion
        allocObj.setOperationDate(DateTimeUtil.getDateFormat().parseDateTime(currentDate.toString(DateTimeUtil.getDateFormat())));
        allocObj.setWorkCenterNo(this.getWorkCenterNo());
        
        for (int i = workCenterRuntime; i > 0; i--)
        {
            // add the timeblock to the allocObj
            allocObj.addToTimeBlockAllocation(timeBlockName, operationId);
            
            // increment the timeblock by 1
            List<Object> incrementDetails = new WorkCenterOpAllocModel().incrementTimeBlock(timeBlockName, 1);
            timeBlockName =  incrementDetails.get(0).toString();
            // if days are added when incrementing the timeblock, recursively call this method again
            // by sending the currentDate as the currentDate+daysAdded, currentTimeBlockName, and the remaining workCenterRuntime (i)
            int daysAdded = Integer.parseInt(incrementDetails.get(1).toString());
            if (daysAdded > 0)
            {
                workCenterOpAllocUpdate.add(allocObj);
                getWorkCenterOpAllocObjectForUpdate(currentDate.plusDays(daysAdded), timeBlockName, operationId, i);
            }
        }
        workCenterOpAllocUpdate.add(allocObj);
    }
    
    /**
     * This method will check if the set of TimeBlocks are available from the given given date and the timeBlock Name for the amount of
     * work center runtime
     * @param currentDate the date to be checked
     * @param timeBlockName the starting timeblock name
     * @param workCenterRuntime amount of timeblocks to be checked is taken from the workCenterRuntime
     * @return return true if available, false if not.
     */
    private boolean checkConsecutiveTimeBlockAvailability(LocalDate currentDate, String timeBlockName, int workCenterRuntime)
    {
        boolean timeBlocksAvailable = true;
        
        WorkCenterOpAllocModel workCenterOpAlloc = currentWorkCenterOpAllocs.stream().filter(aloc -> aloc.getOperationDate().toLocalDate().equals(currentDate)).
                                                                    collect(Collectors.toList()).get(0);
        // get timeBlock allocation for the currentDate
        HashMap<String, Integer> timeBlockAllocation = workCenterOpAlloc.getTimeBlockAllocation();
        
        for (int i = workCenterRuntime; i > 0; i--)
        {
            // timeblock is allocated to an operation, therefore time blocks are not consecutively available to be allocated
            // for the workCenterRuntime of the operation 
            if(timeBlockAllocation.get(timeBlockName) != 0)
            {
                timeBlocksAvailable = false;
                // break the loop since there's no need of checking the consequent timeblocks
                break;
            }
            // timeblock is free, increment the timeblock by one and check for the next time block
            else
            {
                // increment the timeblock by 1
                List<Object> incrementDetails = new WorkCenterOpAllocModel().incrementTimeBlock(timeBlockName, 1);
                timeBlockName =  incrementDetails.get(0).toString();
                // if days are added when incrementing the timeblock, recursively call this method again
                // by sending the currentDate as the currentDate+daysAdded, currentTimeBlockName, and the remaining workCenterRuntime (i)
                int daysAdded = Integer.parseInt(incrementDetails.get(1).toString());
                if (daysAdded > 0)
                {
                    timeBlocksAvailable = checkConsecutiveTimeBlockAvailability(currentDate.plusDays(daysAdded), timeBlockName, i);
                }
            }
        }
        
        return timeBlocksAvailable;
    }
    
    // </editor-fold> 

}
