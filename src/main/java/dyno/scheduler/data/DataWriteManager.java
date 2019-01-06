/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.*;
import java.util.List;

/**
 *
 * @author Prabash
 */
public abstract class DataWriteManager
{
    public abstract boolean addData(List<? extends DataModel> dataList, DataModelEnums.DataModelType dataModelType);
    
    public abstract boolean updateData(List<? extends DataModel> dataList, DataModelEnums.DataModelType dataModelType);
    
    public abstract boolean addShopOrderData(List<ShopOrderModel> dataList, String storageName);
    
    public abstract boolean addShopOrderOperationData(List<ShopOrderOperationModel> dataList, String storageName);
    
    public abstract boolean addWorkCenterData(List<WorkCenterModel> dataList, String storageName);
    
    public abstract boolean addWorkCenterOpALlocData(List<WorkCenterOpAllocModel> dataList, String storageName);
    
    public abstract boolean updateShopOrderData(List<ShopOrderModel> dataList, String storageName);
    
    public abstract boolean updateShopOrderOperationData(List<ShopOrderOperationModel> dataList, String storageName);
    
    public abstract boolean updateWorkCenterData(List<WorkCenterModel> dataList, String storageName);
    
    public abstract boolean updateWorkCenterOpAllocData(List<WorkCenterOpAllocModel> dataList, String storageName);
    
    /**
     * This method will unschedule all the given operations available in the operationsList
     * @param operationsList operations to be unscheduled
     * @param storageName table name
     * @return true if operation is successful
     */
    public abstract boolean unscheduleOperations(List<ShopOrderOperationModel> operationsList, String storageName);
    
    /**
     * This method will unschedule all the subsequent operations (ordered by the operation sequence) starting from the given operation
     * @param operation operation to be unscheduled from
     * @param storageName table name
     * @return true if operation is successful
     */
    public abstract boolean unscheduleAllOperationsFrom(ShopOrderOperationModel operation, String storageName);
}
