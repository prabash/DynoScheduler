/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModel;
import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.InterruptedOpDetailsDataModel;
import dyno.scheduler.datamodels.OperationScheduleTimeBlocksDataModel;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import dyno.scheduler.utils.MySqlUtil;
import dyno.scheduler.utils.TableUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
public class MySqlReadManager extends DataReadManager
{

    @Override
    public List<? extends DataModel> getData(DataModelEnums.DataModelType dataModelType)
    {

        try
        {
            String tableName = MySqlUtil.getStorageName(dataModelType);
            switch (dataModelType)
            {
                case ShopOrder:
                {
                    return getShopOrderData(tableName);
                }
                case ShopOrderOperation:
                {
                    return getShopOrderOperationData(tableName);
                }
                case WorkCenter:
                {
                    return getWorkCenterData(tableName);
                }
                case WorkCenterAllocationFinite:
                {
                    return getWorkCenterOpAllocData(tableName);
                }
                default:
                {
                    return null;
                }

            }
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return null;
        }
    }

    @Override
    protected List<ShopOrderModel> getShopOrderData(String storageName)
    {
        List<ShopOrderModel> shopOrders = new ArrayList<>();
        ArrayList<ArrayList<String>> filters = null;
        ArrayList<String> orderBy = null;
        ResultSet results;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                ShopOrderModel shopOrderObj = new ShopOrderModel().getModelObject(results);
                shopOrders.add(shopOrderObj);
            }

        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }

        return shopOrders;
    }

    @Override
    protected List<ShopOrderOperationModel> getShopOrderOperationData(String storageName)
    {
        List<ShopOrderOperationModel> shopOrderOperations = new ArrayList<>();
        ArrayList<ArrayList<String>> filters = null;
        ArrayList<String> orderBy = null;
        ResultSet results;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                ShopOrderOperationModel shopOrderOpObj = new ShopOrderOperationModel().getModelObject(results);
                shopOrderOperations.add(shopOrderOpObj);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return shopOrderOperations;
    }

    @Override
    protected List<WorkCenterModel> getWorkCenterData(String storageName)
    {
        List<WorkCenterModel> workCenters = new ArrayList<>();
        ArrayList<ArrayList<String>> filters = null;
        ArrayList<String> orderBy = null;
        ResultSet results;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                WorkCenterModel workCenter = new WorkCenterModel().getModelObject(results);
                workCenters.add(workCenter);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return workCenters;
    }

    @Override
    protected List<WorkCenterOpAllocModel> getWorkCenterOpAllocData(String storageName)
    {
        List<WorkCenterOpAllocModel> workCenterOpAllocs = new ArrayList<>();
        ArrayList<ArrayList<String>> filters = null;
        ArrayList<String> orderBy = null;
        ResultSet results;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                WorkCenterOpAllocModel workCenterOpAlloc = new WorkCenterOpAllocModel().getModelObject(results);
                workCenterOpAllocs.add(workCenterOpAlloc);
            }

        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return workCenterOpAllocs;
    }

    @Override
    protected List<ShopOrderOperationModel> getSubsequentOperations(ShopOrderOperationModel shopOrderOperation)
    {
        List<ShopOrderOperationModel> shopOrderOperations = new ArrayList<>();
        // filter by operation sequence and order_no
        ArrayList<ArrayList<String>> filters = new ArrayList<>();
        filters.add(TableUtil.createTableFilter("operation_sequence", ">=", String.valueOf(shopOrderOperation.getOperationSequence())));
        filters.add(TableUtil.createTableFilter("order_no", "=", shopOrderOperation.getOrderNo()));

        // order by opretaion sequence
        ArrayList<String> orderBy = TableUtil.createOrderByFilters("operation_sequence");;

        // get the table name
        String tableName = MySqlUtil.getStorageName(DataModelEnums.DataModelType.ShopOrderOperation);

        ResultSet results;
        try
        {
            results = new MySqlReader().ReadTable(tableName, filters, orderBy);
            while (results.next())
            {
                ShopOrderOperationModel shopOrderOpObj = new ShopOrderOperationModel().getModelObject(results);
                shopOrderOperations.add(shopOrderOpObj);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return shopOrderOperations;
    }

    @Override
    protected List<OperationScheduleTimeBlocksDataModel> getOperationScheduledTimeBlockDetails(int operationId)
    {
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<OperationScheduleTimeBlocksDataModel> scheduledTimeBlocks = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.OperationScheduledTimeBlockFinite);
        parameters.add(operationId);

        ResultSet results;
        try
        {
            results = new MySqlReader().invokeGetStoreProcedure(storedProcedure, parameters);
            while (results.next())
            {
                OperationScheduleTimeBlocksDataModel scheduledTimeBlock = new OperationScheduleTimeBlocksDataModel().getModelObject(results);
                scheduledTimeBlocks.add(scheduledTimeBlock);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return scheduledTimeBlocks;
    }

    @Override
    protected List<InterruptedOpDetailsDataModel> getInterruptedOperationDetails(DateTime interruptionStartDate, DateTime interruptionStartTime, DateTime interruptionEndDate, DateTime interruptionEndTime, String workCenterNo)
    {
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<InterruptedOpDetailsDataModel> interruptedOpDetails = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.InterruptedOperaitonDetails);
        parameters.add(DateTimeUtil.convertDatetoSqlDate(interruptionStartDate));
        parameters.add(DateTimeUtil.convertTimetoSqlTime(interruptionStartTime));
        parameters.add(DateTimeUtil.convertDatetoSqlDate(interruptionEndDate));
        parameters.add(DateTimeUtil.convertTimetoSqlTime(interruptionEndTime));
        parameters.add(workCenterNo);

        ResultSet results;
        try
        {
            results = new MySqlReader().invokeGetStoreProcedure(storedProcedure, parameters);
            while (results.next())
            {
                InterruptedOpDetailsDataModel interruptedOp = new InterruptedOpDetailsDataModel().getModelObject(results);
                interruptedOpDetails.add(interruptedOp);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return interruptedOpDetails;
    }

    @Override
    protected List<ShopOrderModel> getUnscheduledShopOrders()
    {
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<ShopOrderModel> unscheduledOrders = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.UnscheduledOrders);
        ResultSet results;
        try
        {
            results = new MySqlReader().invokeGetStoreProcedure(storedProcedure, parameters);
            while (results.next())
            {
                ShopOrderModel shopOrder = new ShopOrderModel().getModelObject(results);
                // set the list of operations
                shopOrder.setOperations(getShopOrderOperationsByOrderNo(shopOrder.getOrderNo()));
                // assign latest finish time for operations
                shopOrder.assignEstimatedLatestFinishTimeForOperations();
                unscheduledOrders.add(shopOrder);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return unscheduledOrders;
    }

    @Override
    protected List<WorkCenterModel> getUnscheduledOperationWorkCenters()
    {
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<WorkCenterModel> unscheduledOpWorkCenters = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.UnschedledOperationWorkCenters);
        ResultSet results;
        try
        {
            results = new MySqlReader().invokeGetStoreProcedure(storedProcedure, parameters);
            while (results.next())
            {
                WorkCenterModel workCenter = new WorkCenterModel().getModelObject(results);
                unscheduledOpWorkCenters.add(workCenter);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return unscheduledOpWorkCenters;
    }

    @Override
    protected List<ShopOrderOperationModel> getShopOrderOperationsByOrderNo(String orderNo)
    {
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<ShopOrderOperationModel> shopOrderOperations = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.ByOrderNoOperationsOrdered);

        parameters.add(orderNo);

        ResultSet results;
        try
        {
            results = new MySqlReader().invokeGetStoreProcedure(storedProcedure, parameters);
            while (results.next())
            {
                ShopOrderOperationModel shopOrderOperation = new ShopOrderOperationModel().getModelObject(results);
                shopOrderOperations.add(shopOrderOperation);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return shopOrderOperations;
    }

    @Override
    protected List<ShopOrderModel> getLowerPriorityBlockerShopOrders(DateTime fromDate, DateTime fromTime, String workCenterType, Double currentPriority)
    {
        ArrayList<Object> parameters = new ArrayList<>();
        ArrayList<ShopOrderModel> lowerPriorityBlockerOrders = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.LowerPriorityBlockerShopOrders);
        parameters.add(DateTimeUtil.convertDatetoSqlDate(fromDate));
        parameters.add(DateTimeUtil.convertTimetoSqlTime(fromTime));
        parameters.add(workCenterType);
        parameters.add(currentPriority);

        ResultSet results;
        try
        {
            results = new MySqlReader().invokeGetStoreProcedure(storedProcedure, parameters);
            while (results.next())
            {
                ShopOrderModel lowerPriorityBlockerOrder = new ShopOrderModel().getModelObject(results);
                // set the list of operations
                lowerPriorityBlockerOrder.setOperations(getShopOrderOperationsByOrderNo(lowerPriorityBlockerOrder.getOrderNo()));
                // assign latest finish time for operations
                lowerPriorityBlockerOrder.assignEstimatedLatestFinishTimeForOperations();
                
                lowerPriorityBlockerOrders.add( lowerPriorityBlockerOrder);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return lowerPriorityBlockerOrders;
    }

    @Override
    protected WorkCenterModel getWorkCenterByPrimaryKey(String workCenterNo)
    {
        List<WorkCenterModel> workCenters = new ArrayList<>();
        ArrayList<ArrayList<String>> filters = new ArrayList<>();
        ArrayList<String> orderBy = null;
        ResultSet results;
        String storageName =  MySqlUtil.getStorageName(DataModelEnums.DataModelType.WorkCenter);
        
        filters.add(TableUtil.createTableFilter("work_center_no", "=", workCenterNo));
        
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                WorkCenterModel workCenter = new WorkCenterModel().getModelObject(results);
                workCenters.add(workCenter);
            }
        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return workCenters.get(0);
    }
}
