/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.utils.DateTimeUtil;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
@Path("/shop-order")
public class ShopOrderService implements IDynoService
{
    @Override
    public Response get()
    {
        List<ShopOrderModelJson> list = new ArrayList<>();
        GenericEntity<List<ShopOrderModelJson>> entity;
        
        List<ShopOrderOperationModelJson> operations = new ArrayList<ShopOrderOperationModelJson>();
        ShopOrderOperationModelJson operation = new ShopOrderOperationModelJson("S01", 1, 1, "WC1", "Milling", "Test", 0, 1, 0, DateTime.now(), DateTime.now(), DateTime.now().plusDays(2), DateTime.now().plusDays(2), 0, DataModelEnums.OperationStatus.Created);
        operations.add(operation);
        
        list.add(new ShopOrderModelJson("SO1", "Test1", DateTime.now().toString(DateTimeUtil.getDateTimeFormatJson()), "P1", "SR1", "RR1", DateTime.now(), DateTime.now(), DateTime.now(), DataModelEnums.ShopOrderSchedulingDirection.Backward, "C1", DataModelEnums.ShopOrderStatus.Created, DataModelEnums.ShopOrderPriority.Trivial, operations));
        list.add(new ShopOrderModelJson("SO2", "Test2", DateTime.now().toString(DateTimeUtil.getDateTimeFormatJson()), "P2", "SR2", "RR2", DateTime.now(), DateTime.now(), DateTime.now(), DataModelEnums.ShopOrderSchedulingDirection.Backward, "C2", DataModelEnums.ShopOrderStatus.Created, DataModelEnums.ShopOrderPriority.Trivial, operations));
        
        entity = new GenericEntityImpl(list);
        return Response.ok(entity).build();
    }    

    private static class GenericEntityImpl extends GenericEntity<List<ShopOrderModelJson>>
    {
        public GenericEntityImpl(List<ShopOrderModelJson> entity)
        {
            super(entity);
        }
    }
}

@XmlRootElement
class ShopOrderModelJson
{
    public String orderNo;
    public String description;
    public String createdDate;
    public String partNo;
    public String structureRevision;
    public String routingRevision;
    public String requiredDate;
    public String startDate;
    public String finishDate;
    public String schedulingDirection;
    public String customerNo;
    public String schedulingStatus;
    public String shopOrderStatus;
    public String priority;
    public List<ShopOrderOperationModelJson> operations;
    
     public ShopOrderModelJson(String orderNo, String description, String createdDate, String partNo, String structureRevision, String routingRevision, DateTime requiredDate,
            DateTime startDate, DateTime finishDate, DataModelEnums.ShopOrderSchedulingDirection schedulingDirection, String customerNo, DataModelEnums.ShopOrderStatus shopOrderStatus, DataModelEnums.ShopOrderPriority priority, List<ShopOrderOperationModelJson> operations)
    {
        this.orderNo = orderNo;
        this.description = description;
        this.createdDate = createdDate;
        this.partNo = partNo;
        this.structureRevision = structureRevision;
        this.routingRevision = routingRevision;
        this.requiredDate = "2017-04-01T08:00:00";
        this.startDate = "2017-04-01T08:00:00";
        this.finishDate = "2017-04-01T08:00:00";
        this.schedulingDirection = schedulingDirection.toString();
        this.customerNo = customerNo;
        this.shopOrderStatus = shopOrderStatus.toString();
        this.priority = priority.toString();
        this.operations = operations;
    }
    
    public ShopOrderModelJson() {}
}

@XmlRootElement
class ShopOrderOperationModelJson
{
    public String orderNo;
    public int operationId;
    public int operationNo;
    public String workCenterNo;
    public String workCenterType;
    public String operationDescription;
    public int operationSequence;
    public int precedingOperationId;
    public int workCenterRuntimeFactor;
    public int workCenterRuntime;
    public int laborRuntimeFactor;
    public int laborRunTime;
    public String opStartDate;
    public String opStartTime;
    public String opFinishDate;
    public String opFinishTime;
    public String latestOpFinishDate;
    public String latestOpFinishTime;
    public int quantity;
    public DataModelEnums.OperationStatus operationStatus;
    
    public ShopOrderOperationModelJson() {}
    
    public ShopOrderOperationModelJson(String orderNo, int operationId, int operationNo, String workCenterNo, String workCenterType, String operationDescription, int operationSequence,
                int workCenterRunTime, int laborRunTime, DateTime opStartDate, DateTime opStartTime, DateTime opFinishDate, DateTime opFinishTime, int quantity, DataModelEnums.OperationStatus operationStatus)
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
        this.opStartDate = "2017-04-01T08:00:00";
        this.opStartTime = "2017-04-01T08:00:00";
        this.opFinishDate = "2017-04-01T08:00:00";
        this.opFinishTime = "2017-04-01T08:00:00";
    }
}