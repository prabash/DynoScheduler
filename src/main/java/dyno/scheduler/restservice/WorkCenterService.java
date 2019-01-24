/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import dyno.scheduler.main.Main;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Prabash
 */
@Path("/work-center")
public class WorkCenterService
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
