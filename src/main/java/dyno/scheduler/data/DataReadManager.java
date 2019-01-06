/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.*;
import dyno.scheduler.datamodels.DataModelEnums.DataModelType;
import java.util.List;

/**
 *
 * @author Prabash
 */
public abstract class DataReadManager
{
    public abstract List<? extends DataModel> getData(DataModelType dataModel);

    protected abstract List<ShopOrderModel> getShopOrderData(String storageName);

    protected abstract List<ShopOrderOperationModel> getShopOrderOperationData(String storageName);

    protected abstract List<WorkCenterModel> getWorkCenterData(String storageName);

    protected abstract List<WorkCenterOpAllocModel> getWorkCenterOpAllocData(String storageName);
    
    /**
     * Get the subsequent operations of a given operation ordered by operation sequence
     * @param shopOrderOperation operation to find the subsequent operations
     * @param storageName table name
     * @return list of all the subsequent operations
     */
    protected abstract List<ShopOrderOperationModel> getSubsequentOperations(ShopOrderOperationModel shopOrderOperation);
}
