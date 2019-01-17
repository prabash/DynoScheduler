/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.*;
import dyno.scheduler.datamodels.DataModelEnums.DataModelType;
import java.util.List;
import org.joda.time.DateTime;

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
    
    /**
     * Get the operation scheduled time block details
     * @param operationId
     * @return  
     */
    protected abstract List<OperationScheduleTimeBlocksDataModel> getOperationScheduledTimeBlockDetails(int operationId);
    
    /**
     * Get interrupted operation details of a work center
     * @param interruptionStartDate
     * @param interruptionStartTime
     * @param interruptionEndDate
     * @param interruptionEndTime
     * @param workCenterNo
     * @return 
     */
    protected abstract List<InterruptedOpDetailsDataModel> getInterruptedOperationDetails(DateTime interruptionStartDate, DateTime interruptionStartTime, DateTime interruptionEndDate, DateTime interruptionEndTime, String workCenterNo);
    
    /**
     * Get Unscheduled Shop Orders List
     * @return 
     */
    protected abstract List<ShopOrderModel> getUnscheduledShopOrders();
    
    /**
     * Get Work Centres related unscheduled operations
     * @return 
     */
    protected abstract List<WorkCenterModel> getUnscheduledOperationWorkCenters();
}
