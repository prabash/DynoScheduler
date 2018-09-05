/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class DataWriter
{
    public static boolean updateWorkCenterAllocData(List<WorkCenterOpAllocModel> workCenterOpAllocations)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(workCenterOpAllocations, DataModelEnums.DataModelType.WorkCenterAllocation);
    }
    
    public static boolean updateWorkShopOrderOperationData(List<ShopOrderOperationModel> shopOrderOperations)
    {
        return DataFactory.getDataWriteManagerInstance().updateData(shopOrderOperations, DataModelEnums.DataModelType.ShopOrderOperation);
    }
}
