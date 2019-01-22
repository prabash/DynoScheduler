/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Prabash
 */
@Path("/work-center")
public class WorkCenterService
{
    @POST
    @Path("/interrupt")
    @Consumes(MediaType.TEXT_PLAIN)
    public Response interruptWorkCenter(String workCenter, String interruptionStartDate, String interruptionEndDate)
    {
        System.out.println(workCenter);
        System.out.println(interruptionStartDate);
        System.out.println(interruptionEndDate);
        return Response.status(200).entity(workCenter + " " + interruptionStartDate + " " + interruptionEndDate + " ").build();
    }
}
