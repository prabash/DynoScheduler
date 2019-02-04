/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.DataModelEnums.OperationStatus;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import dyno.scheduler.utils.MySqlUtil;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class DataWriter
{
    public static boolean updateWorkCenterAllocData(List<WorkCenterOpAllocModel> workCenterOpAllocations)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(workCenterOpAllocations, DataModelEnums.DataModelType.WorkCenterAllocationFinite);
    }
    
    public static boolean updateShopOrderOperationData(List<ShopOrderOperationModel> shopOrderOperations)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(shopOrderOperations, DataModelEnums.DataModelType.ShopOrderOperation);
    }
    
    public static boolean addShopOrderOperationData(List<ShopOrderOperationModel> shopOrderOperations)
    {
        return DataFactory.getDataWriteManagerInstance().addData(shopOrderOperations, DataModelEnums.DataModelType.ShopOrderOperation);
    }
    
    public static int addShopOrderOperation(ShopOrderOperationModel shopOrderOperation)
    {
        String tableName = MySqlUtil.getStorageName(DataModelEnums.DataModelType.ShopOrderOperation);
        return DataFactory.getDataWriteManagerInstance().addShopOrderOperation(shopOrderOperation, tableName);
    }
    
    public static boolean replacePrecedingOperationId(int precedingOperationId, int replaceById, int exceptOpId, String orderNo)
    {
        return DataFactory.getDataWriteManagerInstance().replacePrecedingOperationId(precedingOperationId, replaceById, exceptOpId, orderNo);
    }
    
    public static boolean updateOperationStatus(int operationId, OperationStatus operationStatus)
    {
        return DataFactory.getDataWriteManagerInstance().changeOperationStatus(operationId, operationStatus);
    }
}
