/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author Prabash
 */
public class DataReader
{
    private static List<ShopOrderModel> shopOrderDetails;
    private static List<ShopOrderOperationModel> shopOrderOperationDetails;
    private static List<WorkCenterModel> workCenterDetails;
    private static List<WorkCenterOpAllocModel> workCenterOpAllocDetails;

    /**
     * this method will return all the list of shop orders available
     * @return list of shop orders
     */
    public synchronized static List<ShopOrderModel> getShopOrderDetails()
    {
        if (shopOrderDetails == null)
        {
            // get shop order details 
            populateShopOrderDetails();
            // get shop order operation details
            populateShopOrderOperationDetails();

            // for each shop order, add the related shop order operation details
            shopOrderDetails.forEach((shopOrder) ->
            {
                List<ShopOrderOperationModel> relatedOperations = shopOrderOperationDetails.stream()
                        .filter(op -> op.getOrderNo().equals(shopOrder.getOrderNo()))
                        .collect(Collectors.toList());
                shopOrder.setOperations(relatedOperations);
            });
        }
        return shopOrderDetails;
    }

    public synchronized static List<ShopOrderOperationModel> getShopOrderOperationDetails()
    {
        if (shopOrderOperationDetails == null)
        {
            populateShopOrderOperationDetails();
        }
        return shopOrderOperationDetails;
    }

    public synchronized static List<WorkCenterModel> getWorkCenterDetails()
    {
        if (workCenterDetails == null)
        {
            populateWorkCenterDetails();
        }
        return workCenterDetails;
    }

    public synchronized static List<WorkCenterOpAllocModel> getWorkCenterOpAllocDetails()
    {
        if (workCenterOpAllocDetails == null)
        {
            populateWorkCenterOpAllocDetails();
        }
        return workCenterOpAllocDetails;
    }

    private static void populateShopOrderDetails()
    {
        shopOrderDetails = (List<ShopOrderModel>) DataFactory.getDataReadManagerInstance().getData(DataModelEnums.DataModelType.ShopOrder);
    }

    private static void populateShopOrderOperationDetails()
    {
        shopOrderOperationDetails = (List<ShopOrderOperationModel>) DataFactory.getDataReadManagerInstance().getData(DataModelEnums.DataModelType.ShopOrderOperation);
    }

    private static void populateWorkCenterDetails()
    {
        workCenterDetails = (List<WorkCenterModel>) DataFactory.getDataReadManagerInstance().getData(DataModelEnums.DataModelType.WorkCenter);
    }

    private static void populateWorkCenterOpAllocDetails()
    {
        workCenterOpAllocDetails = (List<WorkCenterOpAllocModel>) DataFactory.getDataReadManagerInstance().getData(DataModelEnums.DataModelType.WorkCenterAllocation);
    }
}