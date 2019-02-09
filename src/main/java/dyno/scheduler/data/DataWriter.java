/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.DataModelEnums.OperationStatus;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import dyno.scheduler.utils.MySqlUtil;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class DataWriter
{
    
    //<editor-fold defaultstate="collapsed" desc="Shop Order Methods">
    
    public static boolean addShopOrder(ShopOrderModel shopOrder)
    {
        List<ShopOrderModel> shopOrders = new ArrayList<>();
        shopOrders.add(shopOrder);
        return addShopOrderData(shopOrders);
    }
     
    public static boolean addShopOrderData(List<ShopOrderModel> shopOrders)
    {
        return DataFactory.getDataWriteManagerInstance().addData(shopOrders, DataModelEnums.DataModelType.ShopOrder);
    }
    
    public static boolean updateShopOrderData(List<ShopOrderModel> shopOrders)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(shopOrders, DataModelEnums.DataModelType.ShopOrder);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Shop Order Operation Methods">
    
    public static boolean addShopOrderOperationData(List<ShopOrderOperationModel> shopOrderOperations)
    {
        return DataFactory.getDataWriteManagerInstance().addData(shopOrderOperations, DataModelEnums.DataModelType.ShopOrderOperation);
    }
    
    public static int addShopOrderOperation(ShopOrderOperationModel shopOrderOperation)
    {
        String tableName = MySqlUtil.getStorageName(DataModelEnums.DataModelType.ShopOrderOperation);
        return DataFactory.getDataWriteManagerInstance().addShopOrderOperation(shopOrderOperation, tableName);
    }
    
    public static boolean updateShopOrderOperationData(List<ShopOrderOperationModel> shopOrderOperations)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(shopOrderOperations, DataModelEnums.DataModelType.ShopOrderOperation);
    }
    
    public static boolean replacePrecedingOperationId(int precedingOperationId, int replaceById, int exceptOpId, String orderNo)
    {
        return DataFactory.getDataWriteManagerInstance().replacePrecedingOperationId(precedingOperationId, replaceById, exceptOpId, orderNo);
    }
    
    public static boolean updateOperationStatus(int operationId, OperationStatus operationStatus)
    {
        return DataFactory.getDataWriteManagerInstance().changeOperationStatus(operationId, operationStatus);
    }
    
    public static boolean removeScheduledOperationData(int operationId, OperationStatus operationStatus)
    {
        return DataFactory.getDataWriteManagerInstance().removeOperationScheduleData(operationId, operationStatus);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Work Center Methods">
    
    public static boolean addWorkCenter(WorkCenterModel workCenter)
    {
        List<WorkCenterModel> workCenters = new ArrayList<>();
        workCenters.add(workCenter);
        return addWorkCenterData(workCenters);
    }
    
    public static boolean addWorkCenterData(List<WorkCenterModel> workCenters)
    {
        return DataFactory.getDataWriteManagerInstance().addData(workCenters, DataModelEnums.DataModelType.WorkCenter);
    }
    
    public static boolean updateWorkCenterData(List<WorkCenterModel> workCenters)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(workCenters, DataModelEnums.DataModelType.WorkCenter);
    }
    
    //</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Work Center Op Alloc Methods">
    
    public static boolean addWorkCenterOpAlloc(WorkCenterOpAllocModel workCenterOpAlloc)
    {
        List<WorkCenterOpAllocModel> workCenterOpAllocs = new ArrayList<>();
        workCenterOpAllocs.add(workCenterOpAlloc);
        return addWorkCenterOpAllocData(workCenterOpAllocs);
    }
    
    public static boolean addWorkCenterOpAllocData(List<WorkCenterOpAllocModel> workCenterOpAlloc)
    {
        return DataFactory.getDataWriteManagerInstance().addData(workCenterOpAlloc, DataModelEnums.DataModelType.WorkCenterAllocationFinite);
    }
    
    public static boolean updateWorkCenterAllocData(List<WorkCenterOpAllocModel> workCenterOpAllocations)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(workCenterOpAllocations, DataModelEnums.DataModelType.WorkCenterAllocationFinite);
    }
    
    //</editor-fold>
    
    
    
}
