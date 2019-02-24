/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import dyno.scheduler.data.DataReader;
import dyno.scheduler.datamodels.WorkCenterInterruptionsModel;
import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.main.Main;
import dyno.scheduler.utils.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Prabash
 */
@Path("/work-center")
public class WorkCenterService implements IDynoGetService
{

    @POST
    @Path("/interrupt")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response interruptWorkCenter(InterruptWorkCenterParams params)
    {
        System.out.println(params.workCenter);
        System.out.println(params.interruptionStartDate);
        System.out.println(params.interruptionEndDate);
        
        Main.InterruptWorkCenterTest();
        
        return Response.status(200).entity(params.workCenter + " " + params.interruptionStartDate + " " + params.interruptionEndDate + " ").build();
    }
    
    @GET
    @Path("/interruptwc")
    public Response interruptWorkCenter()
    {
        return Response.status(200).entity("Successfully Interrupted").build();
    }

    @Override
    public Response get()
    {
        List<WorkCenterModel> workCenterDetails = DataReader.getWorkCenterDetails(true);
        
        List<WorkCenterModelJson> list = new ArrayList<>();
        GenericEntity<List<WorkCenterModelJson>> entity;
        
        for (WorkCenterModel workCenterDetail : workCenterDetails)
        {
            List<WorkCenterInterruptionsModelJson> interruptionDetails = new ArrayList<>();
            for (WorkCenterInterruptionsModel workCenterInterruption : workCenterDetail.getWorkCenterInterruptions())
            {
                WorkCenterInterruptionsModelJson interruptionJsonObj = new WorkCenterInterruptionsModelJson(
                        workCenterInterruption.getId(), 
                        workCenterInterruption.getWorkCenterNo(), 
                        workCenterInterruption.getInterruptionFromDate() != null ? workCenterInterruption.getInterruptionFromDate().toString(DateTimeUtil.getDateFormat()) : "", 
                        workCenterInterruption.getInterruptionFromTime() != null? workCenterInterruption.getInterruptionFromTime().toString(DateTimeUtil.getTimeFormat()) : "", 
                        workCenterInterruption.getInterruptionToDate()!= null ? workCenterInterruption.getInterruptionToDate().toString(DateTimeUtil.getDateFormat()) : "",
                        workCenterInterruption.getInterruptionToTime()!= null? workCenterInterruption.getInterruptionToDate().toString(DateTimeUtil.getTimeFormat()) : "");
                interruptionDetails.add(interruptionJsonObj);
            }
            WorkCenterModelJson workCenterJsonObj = new WorkCenterModelJson(
                    workCenterDetail.getId(), 
                    workCenterDetail.getWorkCenterNo(), 
                    workCenterDetail.getWorkCenterType(), 
                    workCenterDetail.getWorkCenterDescription(), 
                    workCenterDetail.getWorkCenterCapacity(), 
                    interruptionDetails);
            
            list.add(workCenterJsonObj);
        }
        
        entity = new GenericEntityImpl(list);
        return Response.ok(entity).build();
    }
    
    private static class GenericEntityImpl extends GenericEntity<List<WorkCenterModelJson>>
    {
        public GenericEntityImpl(List<WorkCenterModelJson> entity)
        {
            super(entity);
        }
    }
}

@XmlRootElement
class InterruptWorkCenterParams
{
    public String workCenter;
    public String interruptionStartDate;
    public String interruptionEndDate;
    
    public InterruptWorkCenterParams()
    {
    }

    public InterruptWorkCenterParams(String workCenter, String interruptionStartDate, String interruptionEndDate)
    {
        this.workCenter = workCenter;
        this.interruptionStartDate = interruptionStartDate;
        this.interruptionEndDate = interruptionEndDate;
    }
}

@XmlRootElement
class WorkCenterModelJson
{
    public int id;
    public String workCenterNo;
    public String workCenterType;
    public String workCenterDescription;
    public String workCenterCapacity;
    public List<WorkCenterInterruptionsModelJson> workCenterInterruptions;

    public WorkCenterModelJson()
    {
    }

    public WorkCenterModelJson(int id, String workCenterNo, String workCenterType, String workCenterDescription, String workCenterCapacity, List<WorkCenterInterruptionsModelJson> workCenterInterruptions)
    {
        this.id = id;
        this.workCenterNo = workCenterNo;
        this.workCenterType = workCenterType;
        this.workCenterDescription = workCenterDescription;
        this.workCenterCapacity = workCenterCapacity;
        this.workCenterInterruptions = workCenterInterruptions;
    }
}


@XmlRootElement
class WorkCenterInterruptionsModelJson
{
    public int id;
    public String workCenterNo; 
    public String interruptionFromDate;
    public String interruptionFromTime;
    public String interruptionToDate;
    public String interruptionToTime;

    public WorkCenterInterruptionsModelJson()
    {
    }

    public WorkCenterInterruptionsModelJson(int id, String workCenterNo, String interruptionFromDate, String interruptionFromTime, String interruptionToDate, String interruptionToTime)
    {
        this.id = id;
        this.workCenterNo = workCenterNo;
        this.interruptionFromDate = interruptionFromDate;
        this.interruptionFromTime = interruptionFromTime;
        this.interruptionToDate = interruptionToDate;
        this.interruptionToTime = interruptionToTime;
    }
}