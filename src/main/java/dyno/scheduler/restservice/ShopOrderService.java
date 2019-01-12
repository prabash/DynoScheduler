/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import dyno.scheduler.data.DataReader;
import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
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
        List<ShopOrderModel> shopOrders = DataReader.getShopOrderDetails(true);

        List<ShopOrderModelJson> list = new ArrayList<>();
        GenericEntity<List<ShopOrderModelJson>> entity;

        for (ShopOrderModel shopOrder : shopOrders)
        {
            List<ShopOrderOperationModelJson> operations = new ArrayList<ShopOrderOperationModelJson>();
            for (ShopOrderOperationModel operation : shopOrder.getOperations())
            {

                //ShopOrderOperationModelJson operation = new ShopOrderOperationModelJson("S01", 1, 1, "WC1", "Milling", "Test", 0, 1, 0, DateTime.now(), DateTime.now(), DateTime.now().plusDays(2), DateTime.now().plusDays(2), 0, DataModelEnums.OperationStatus.Created);
                DateTime opStartDateTime = null;
                DateTime opFinishDateTime = null;

                if (operation.getOpStartDate() != null && operation.getOpStartTime() != null)
                {
                    opStartDateTime = DateTimeUtil.concatenateDateTime(operation.getOpStartDate(), operation.getOpStartTime());
                }
                if (operation.getOpFinishDate() != null && operation.getOpFinishTime() != null)
                {
                    opFinishDateTime = DateTimeUtil.concatenateDateTime(operation.getOpFinishDate(), operation.getOpFinishTime());
                }

                ShopOrderOperationModelJson shopOrderOpJsonObj = new ShopOrderOperationModelJson(
                        operation.getOrderNo(),
                        operation.getOperationId(),
                        operation.getOperationNo(),
                        operation.getWorkCenterNo(),
                        operation.getWorkCenterType(),
                        operation.getOperationDescription(),
                        operation.getOperationSequence(),
                        operation.getPrecedingOperationId(),
                        operation.getWorkCenterRuntimeFactor(),
                        operation.getWorkCenterRuntime(),
                        operation.getLaborRuntimeFactor(),
                        operation.getLaborRunTime(),
                        opStartDateTime != null ? opStartDateTime.toString(DateTimeUtil.getDateTimeFormatJson()) : "",
                        opFinishDateTime != null ? opFinishDateTime.toString(DateTimeUtil.getDateTimeFormatJson()) : "",
                        0,
                        DataModelEnums.OperationStatus.Created);
                operations.add(shopOrderOpJsonObj);
            }

            ShopOrderModelJson shopOrderJsonObj = new ShopOrderModelJson(
                    shopOrder.getOrderNo(),
                    shopOrder.getDescription(),
                    shopOrder.getCreatedDate() != null ? shopOrder.getCreatedDate().toString(DateTimeUtil.getDateTimeFormatJson()) : "",
                    shopOrder.getPartNo(),
                    shopOrder.getStructureRevision(),
                    shopOrder.getRoutingRevision(),
                    shopOrder.getRequiredDate() != null ? shopOrder.getRequiredDate().toString(DateTimeUtil.getDateTimeFormatJson()) : "",
                    shopOrder.getStartDate() != null ? shopOrder.getStartDate().toString(DateTimeUtil.getDateTimeFormatJson()) : "",
                    shopOrder.getFinishDate() != null ? shopOrder.getFinishDate().toString(DateTimeUtil.getDateTimeFormatJson()) : "",
                    shopOrder.getSchedulingDirection().toString(),
                    shopOrder.getCustomerNo(),
                    shopOrder.getSchedulingStatus().toString(),
                    shopOrder.getShopOrderStatus().toString(),
                    shopOrder.getPriority().toString(),
                    shopOrder.getRevenueValue(),
                    operations);

            list.add(shopOrderJsonObj);
        }
//
//        list.add(new ShopOrderModelJson("SO1", "Test1", DateTime.now().toString(DateTimeUtil.getDateTimeFormatJson()), "P1", "SR1", "RR1", DateTime.now(), DateTime.now(), DateTime.now(), DataModelEnums.ShopOrderSchedulingDirection.Backward, "C1", DataModelEnums.ShopOrderStatus.Created, DataModelEnums.ShopOrderPriority.Trivial, operations));
//        list.add(new ShopOrderModelJson("SO2", "Test2", DateTime.now().toString(DateTimeUtil.getDateTimeFormatJson()), "P2", "SR2", "RR2", DateTime.now(), DateTime.now(), DateTime.now(), DataModelEnums.ShopOrderSchedulingDirection.Backward, "C2", DataModelEnums.ShopOrderStatus.Created, DataModelEnums.ShopOrderPriority.Trivial, operations));

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
    public int revenueValue;
    public List<ShopOrderOperationModelJson> operations;

    public ShopOrderModelJson(String orderNo, String description, String createdDate, String partNo, String structureRevision, String routingRevision, String requiredDate,
            String startDate, String finishDate, String schedulingDirection, String customerNo, String schedulingStatus, String shopOrderStatus, String priority, int revenueValue, List<ShopOrderOperationModelJson> operations)
    {
        this.orderNo = orderNo;
        this.description = description;
        this.createdDate = createdDate;
        this.partNo = partNo;
        this.structureRevision = structureRevision;
        this.routingRevision = routingRevision;
        this.requiredDate = requiredDate;
        this.startDate = startDate;
        this.finishDate = finishDate;
        this.schedulingDirection = schedulingDirection;
        this.customerNo = customerNo;
        this.schedulingStatus = schedulingStatus;
        this.shopOrderStatus = shopOrderStatus;
        this.priority = priority;
        this.revenueValue = revenueValue;
        this.operations = operations;
    }

    public ShopOrderModelJson()
    {
    }
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
    public double operationSequence;
    public int precedingOperationId;
    public int workCenterRuntimeFactor;
    public int workCenterRuntime;
    public int laborRuntimeFactor;
    public int laborRunTime;
    public String opStartDateTime;
    public String opFinishDateTime;
    public String latestOpFinishDate;
    public String latestOpFinishTime;
    public int quantity;
    public String operationStatus;

    public ShopOrderOperationModelJson()
    {
    }

    public ShopOrderOperationModelJson(String orderNo, int operationId, int operationNo, String workCenterNo, String workCenterType, String operationDescription, double operationSequence, int precedingOperationId,
            int workCenterRuntimeFactor, int workCenterRunTime, int laborRuntimeFactor, int laborRunTime, String opStartDateTime, String opFinishDateTime, int quantity, DataModelEnums.OperationStatus operationStatus)
    {
        this.orderNo = orderNo;
        this.operationId = operationId;
        this.operationNo = operationNo;
        this.workCenterNo = workCenterNo;
        this.workCenterType = workCenterType;
        this.operationDescription = operationDescription;
        this.operationSequence = operationSequence;
        this.precedingOperationId = precedingOperationId;
        this.workCenterRuntimeFactor = workCenterRuntimeFactor;
        this.workCenterRuntime = workCenterRunTime;
        this.laborRuntimeFactor = laborRuntimeFactor;
        this.laborRunTime = laborRunTime;
        this.opStartDateTime = opStartDateTime;
        this.opFinishDateTime = opFinishDateTime;
        this.quantity = quantity;
        this.operationStatus = operationStatus.toString();
    }
}
