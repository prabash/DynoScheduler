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
}
