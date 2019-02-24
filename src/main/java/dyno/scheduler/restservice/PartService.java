/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import dyno.scheduler.data.DataReader;
import dyno.scheduler.data.DataWriter;
import dyno.scheduler.datamodels.PartModel;
import dyno.scheduler.datamodels.PartUnavailabilityModel;
import dyno.scheduler.utils.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
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
@Path("/part-details")
public class PartService implements IDynoGetService
{
    @Override
    public Response get()
    {
        List<PartModel> partDetails = DataReader.getPartDetails();
        
        List<PartModelJson> list = new ArrayList<>();
        GenericEntity<List<PartModelJson>> entity;
        
        for (PartModel partDetail : partDetails)
        {
            List<PartUnavailabilityModelJson> unavailabilityDetails = new ArrayList<>();
            for (PartUnavailabilityModel partUnavailability : partDetail.getPartUnavailabilityDetails())
            {
                PartUnavailabilityModelJson unavailabilityJsonObj = new PartUnavailabilityModelJson(
                        partUnavailability.getId(), 
                        partUnavailability.getPartNo(), 
                        partUnavailability.getUnavailableFromDate()!= null ? partUnavailability.getUnavailableFromDate().toString(DateTimeUtil.getDateFormat()) : "", 
                        partUnavailability.getUnavailableFromTime() != null? partUnavailability.getUnavailableFromTime().toString(DateTimeUtil.getTimeFormat()) : "", 
                        partUnavailability.getUnavailableToDate()!= null ? partUnavailability.getUnavailableToDate().toString(DateTimeUtil.getDateFormat()) : "",
                        partUnavailability.getUnavailableToTime()!= null? partUnavailability.getUnavailableToTime().toString(DateTimeUtil.getTimeFormat()) : "");
                unavailabilityDetails.add(unavailabilityJsonObj);
            }
            PartModelJson partModelJsonObj = new PartModelJson(
                    partDetail.getId(), 
                    partDetail.getPartNo(), 
                    partDetail.getPartDescription(), 
                    partDetail.getVendor(), 
                    unavailabilityDetails);
            
            list.add(partModelJsonObj);
        }
        
        entity = new GenericEntityImpl(list);
        return Response.ok(entity).build();
    }
    
    private static class GenericEntityImpl extends GenericEntity<List<PartModelJson>>
    {
        public GenericEntityImpl(List<PartModelJson> entity)
        {
            super(entity);
        }
    }
    
    @POST
    @Path("/addpart")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPart(PartModelJson partDetailsJson)
    {
        PartModel partDetail = new PartModel();
        partDetail.setPartNo(partDetailsJson.partNo);
        partDetail.setPartDescription(partDetailsJson.partDescription);
        partDetail.setVendor(partDetailsJson.vendor);
        
        DataWriter.addPartDetails(partDetail);
        
        return Response.status(200).entity("Successfully Added").build();
    }
    
    
    @POST
    @Path("/updatepart")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updatePart(PartModelJson partDetailsJson)
    {
        PartModel partDetail = new PartModel();
        partDetail.setId(partDetailsJson.id);
        partDetail.setPartNo(partDetailsJson.partNo);
        partDetail.setPartDescription(partDetailsJson.partDescription);
        partDetail.setVendor(partDetailsJson.vendor);
        
        DataWriter.updatePartDetails(partDetail);
        
        return Response.status(200).entity("Successfully Updated").build();
    }
    
    @POST
    @Path("/addPartUnavailability")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addPartUnavailability(PartUnavailabilityModelJson partUnavailabilityJson)
    {
        PartUnavailabilityModel partUnavailability = new PartUnavailabilityModel();
        partUnavailability.setPartNo(partUnavailabilityJson.partNo);
        partUnavailability.setUnavailableFromDate(DateTimeUtil.convertStringDateToDateTime(partUnavailabilityJson.unavailableFromDate));
        partUnavailability.setUnavailableFromTime(DateTimeUtil.convertStringTimeToDateTime(partUnavailabilityJson.unavailableFromTime));
        partUnavailability.setUnavailableToDate(DateTimeUtil.convertStringDateToDateTime(partUnavailabilityJson.unavailableToDate));
        partUnavailability.setUnavailableToTime(DateTimeUtil.convertStringTimeToDateTime(partUnavailabilityJson.unavailableToTime));
        
        DataWriter.addPartUnavailabilityDetails(partUnavailability);
        
        return Response.status(200).entity("Successfully Updated").build();
    }
}

@XmlRootElement
class PartModelJson
{
    public int id;
    public String partNo; 
    public String partDescription;
    public String vendor;
    public List<PartUnavailabilityModelJson> partUnavailabilityDetails;

    public PartModelJson()
    {
    }

    public PartModelJson(int id, String partNo, String partDescription, String vendor, List<PartUnavailabilityModelJson> partUnavailabilityDetails)
    {
        this.id = id;
        this.partNo = partNo;
        this.partDescription = partDescription;
        this.vendor = vendor;
        this.partUnavailabilityDetails = partUnavailabilityDetails;
    }
}

@XmlRootElement
class PartUnavailabilityModelJson
{
    public int id;
    public String partNo; 
    public String unavailableFromDate;
    public String unavailableFromTime;
    public String unavailableToDate;
    public String unavailableToTime;

    public PartUnavailabilityModelJson()
    {
    }

    public PartUnavailabilityModelJson(int id, String partNo, String unavailableFromDate, String unavailableFromTime, String unavailableToDate, String unavailableToTime)
    {
        this.id = id;
        this.partNo = partNo;
        this.unavailableFromDate = unavailableFromDate;
        this.unavailableFromTime = unavailableFromTime;
        this.unavailableToDate = unavailableToDate;
        this.unavailableToTime = unavailableToTime;
    }
}