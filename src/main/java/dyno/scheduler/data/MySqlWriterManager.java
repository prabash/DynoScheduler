/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModel;
import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.DataModelEnums.OperationStatus;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import dyno.scheduler.utils.DateTimeUtil;
import dyno.scheduler.utils.LogUtil;
import dyno.scheduler.utils.MySqlUtil;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
public class MySqlWriterManager extends DataWriteManager
{

    @Override
    public boolean addData(List<? extends DataModel> dataList, DataModelEnums.DataModelType dataModelType)
    {
        try
        {
            String tableName = MySqlUtil.getStorageName(dataModelType);
            switch (dataModelType)
            {
                case ShopOrder:
                {
                    return addShopOrderData((List<ShopOrderModel>) dataList, tableName);
                }
                case ShopOrderOperation:
                {
                    return addShopOrderOperationData((List<ShopOrderOperationModel>) dataList, tableName);
                }
                case WorkCenter:
                {
                    return addWorkCenterData((List<WorkCenterModel>) dataList, tableName);
                }
                case WorkCenterAllocationFinite:
                {
                    return addWorkCenterOpALlocData((List<WorkCenterOpAllocModel>) dataList, tableName);
                }
                default:
                {
                    return false;
                }

            }
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean updateData(List<? extends DataModel> dataList, DataModelEnums.DataModelType dataModelType)
    {
        try
        {
            String tableName = MySqlUtil.getStorageName(dataModelType);
            switch (dataModelType)
            {
                case ShopOrder:
                {
                    return updateShopOrderData((List<ShopOrderModel>) dataList, tableName);
                }
                case ShopOrderOperation:
                {
                    return updateShopOrderOperationData((List<ShopOrderOperationModel>) dataList, tableName);
                }
                case WorkCenter:
                {
                    return updateWorkCenterData((List<WorkCenterModel>) dataList, tableName);
                }
                case WorkCenterAllocationFinite:
                {
                    return updateWorkCenterOpAllocData((List<WorkCenterOpAllocModel>) dataList, tableName);
                }
                default:
                {
                    return false;
                }

            }
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean addShopOrderData(List<ShopOrderModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addShopOrderOperationData(List<ShopOrderOperationModel> dataList, String storageName)
    {
        try
        {
            int precedingOpId = 0;
            for (ShopOrderOperationModel shopOrderOperation : dataList.stream().sorted(new ShopOrderOperationModel()).collect(Collectors.toList()))
            {
                if(shopOrderOperation.getPrecedingOperationId() < 0)
                {
                    shopOrderOperation.setPrecedingOperationId(precedingOpId);
                }
                precedingOpId = addShopOrderOperation(shopOrderOperation, storageName);
            }
            return true;

        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public int addShopOrderOperation(ShopOrderOperationModel shopOrderOperation, String storageName)
    {
        int precedingOpId;
        String query = "INSERT INTO " + storageName + " " +
                    "(operation_no," + "order_no," + "operation_description," + "operation_sequence," + "preceding_operation_id," +
                    "wc_runtime_factor," + "wc_runtime," + "labor_runtime_factor," + "labor_runtime," + "op_start_date," +
                    "op_start_time," + "op_finish_date," + "op_finish_time," + "quantity," + "work_center_type," +
                    "work_center_no," + "operation_status)" +
                    "VALUES"+
                    "(?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?,?,?,?,"
                    + "?,?)";
        
        HashMap<Integer, Object> columnValues = new HashMap<>();
        int i = 0;
        columnValues.put(++i, shopOrderOperation.getOperationNo());
        columnValues.put(++i, shopOrderOperation.getOrderNo());
        columnValues.put(++i, shopOrderOperation.getOperationDescription());
        columnValues.put(++i, shopOrderOperation.getOperationSequence());
        columnValues.put(++i, shopOrderOperation.getPrecedingOperationId());
        
        columnValues.put(++i, shopOrderOperation.getWorkCenterRuntimeFactor());
        columnValues.put(++i, shopOrderOperation.getWorkCenterRuntime());
        columnValues.put(++i, shopOrderOperation.getLaborRuntimeFactor());
        columnValues.put(++i, shopOrderOperation.getLaborRunTime());
        
        columnValues.put(++i, shopOrderOperation.getOpStartDate() != null ? DateTimeUtil.convertDatetoSqlDate(shopOrderOperation.getOpStartDate()) : new Date(0));
        columnValues.put(++i, shopOrderOperation.getOpStartTime() != null ? DateTimeUtil.convertTimetoSqlTime(shopOrderOperation.getOpStartTime()) : new Time(0));
        columnValues.put(++i, shopOrderOperation.getOpFinishDate() != null ? DateTimeUtil.convertDatetoSqlDate(shopOrderOperation.getOpFinishDate()) : new Date(0));
        columnValues.put(++i, shopOrderOperation.getOpFinishDate() != null ? DateTimeUtil.convertTimetoSqlTime(shopOrderOperation.getOpFinishTime()) : new Time(0));
        
        columnValues.put(++i, shopOrderOperation.getQuantity());
        columnValues.put(++i, shopOrderOperation.getWorkCenterType());
        columnValues.put(++i, shopOrderOperation.getWorkCenterNo());
        columnValues.put(++i, shopOrderOperation.getOperationStatus().toString());

        precedingOpId = new MySqlWriter().WriteToTable(query, columnValues);
        return precedingOpId;
    }

    @Override
    public boolean addWorkCenterData(List<WorkCenterModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean addWorkCenterOpALlocData(List<WorkCenterOpAllocModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateShopOrderData(List<ShopOrderModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateShopOrderOperationData(List<ShopOrderOperationModel> dataList, String storageName)
    {
        try
        {
            String query = "UPDATE " + storageName + " "
                    + "SET "
                    + "operation_no = ?, "
                    + "order_no =?, "
                    + "operation_description = ?, "
                    + "operation_sequence = ?, "
                    + "preceding_operation_id = ?, "
                    + "wc_runtime_factor = ?, "
                    + "wc_runtime = ?, "
                    + "labor_runtime_factor = ?, "
                    + "labor_runtime = ?, "
                    + "op_start_date = ?, "
                    + "op_start_time = ?, "
                    + "op_finish_date = ?, "
                    + "op_finish_time = ?, "
                    + "quantity = ?, "
                    + "work_center_type = ?, "
                    + "work_center_no = ?, "
                    + "operation_status = ? "
                    + "WHERE id = ?";

            for (ShopOrderOperationModel shopOrderOperation : dataList)
            {
                HashMap<Integer, Object> columnValues = new HashMap<>();
                int i = 0;
                columnValues.put(++i, shopOrderOperation.getOperationNo());
                columnValues.put(++i, shopOrderOperation.getOrderNo());
                columnValues.put(++i, shopOrderOperation.getOperationDescription());
                columnValues.put(++i, shopOrderOperation.getOperationSequence());
                columnValues.put(++i, shopOrderOperation.getPrecedingOperationId());
                columnValues.put(++i, shopOrderOperation.getWorkCenterRuntimeFactor());
                columnValues.put(++i, shopOrderOperation.getWorkCenterRuntime());
                columnValues.put(++i, shopOrderOperation.getLaborRuntimeFactor());
                columnValues.put(++i, shopOrderOperation.getLaborRunTime());
                columnValues.put(++i, DateTimeUtil.convertDatetoSqlDate(shopOrderOperation.getOpStartDate()));
                columnValues.put(++i, DateTimeUtil.convertTimetoSqlTime(shopOrderOperation.getOpStartTime()));
                columnValues.put(++i, DateTimeUtil.convertDatetoSqlDate(shopOrderOperation.getOpFinishDate()));
                columnValues.put(++i, DateTimeUtil.convertTimetoSqlTime(shopOrderOperation.getOpFinishTime()));
                columnValues.put(++i, shopOrderOperation.getQuantity());
                columnValues.put(++i, shopOrderOperation.getWorkCenterType());
                columnValues.put(++i, shopOrderOperation.getWorkCenterNo());
                columnValues.put(++i, shopOrderOperation.getOperationStatus().toString());
                columnValues.put(++i, shopOrderOperation.getOperationId());

                new MySqlWriter().WriteToTable(query, columnValues);
            }
            return true;

        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean updateWorkCenterData(List<WorkCenterModel> dataList, String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean updateWorkCenterOpAllocData(List<WorkCenterOpAllocModel> dataList, String storageName)
    {
        try
        {
            for (WorkCenterOpAllocModel workCenterOpAlloc : dataList)
            {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("UPDATE ").append(storageName).append(" SET ");
                HashMap<Integer, Object> columnValues = new HashMap<>();
                int i = 0;

                int size = workCenterOpAlloc.getTimeBlockAllocation().entrySet().size();
                int counter = 1;
                for (Map.Entry<String, Integer> timeBlockEntry : workCenterOpAlloc.getTimeBlockAllocation().entrySet())
                {
                    if (counter < size)
                    {
                        queryBuilder.append(timeBlockEntry.getKey()).append(" = ?, ");
                    } else if (counter == size)
                    {
                        queryBuilder.append(timeBlockEntry.getKey()).append(" = ? ");
                    }

                    columnValues.put(++i, timeBlockEntry.getValue());
                    counter++;
                }

                queryBuilder.append("WHERE work_center_no = ? ");
                columnValues.put(++i, workCenterOpAlloc.getWorkCenterNo());

                queryBuilder.append("AND operation_date = ? ");
                columnValues.put(++i, DateTimeUtil.convertDatetoSqlDate(workCenterOpAlloc.getOperationDate()));

                new MySqlWriter().WriteToTable(queryBuilder.toString(), columnValues);
            }
            return true;
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean unscheduleOperations(List<ShopOrderOperationModel> operationsList, String storageName)
    {
        try
        {
            for (ShopOrderOperationModel shopOrderOperation : operationsList)
            {
                StringBuilder queryBuilder = new StringBuilder();
                queryBuilder.append("UPDATE ").append(storageName).append(" SET ");
                HashMap<Integer, Object> columnValues = new HashMap<>();
                int i = 0;

                queryBuilder.append("operation_status = ? ");
                columnValues.put(++i, OperationStatus.Interrupted.toString());

                queryBuilder.append("WHERE id = ? ");
                columnValues.put(++i, shopOrderOperation.getOperationId());

                new MySqlWriter().WriteToTable(queryBuilder.toString(), columnValues);
            }
            return true;
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public boolean unscheduleAllOperationsFrom(ShopOrderOperationModel operation, String storageName)
    {
        return unscheduleOperations(DataReader.getSubsequentOperations(operation), storageName);
    }

    @Override
    public boolean interruptWorkCenter(String workCenterNo, DateTime startTime, DateTime endTime)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean replacePrecedingOperationId(int precedingOperationId, int replacedById, int exceptOpId, String orderNo)
    {
        ArrayList<Object> parameters = new ArrayList<>();
        String storedProcedure = MySqlUtil.getStoredProcedureName(DataModelEnums.StoredProcedures.ReplacePrecedingOperationIDs);
        parameters.add(precedingOperationId);
        parameters.add(replacedById);
        parameters.add(exceptOpId);
        parameters.add(orderNo);

        int result;
        try
        {
            result = new MySqlWriter().invokeUpdateStoredProcedure(storedProcedure, parameters);
            
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return true;
    }

    @Override
    public boolean changeOperationStatus(int operationId, OperationStatus operationStatus)
    {
        String tableName = MySqlUtil.getStorageName(DataModelEnums.DataModelType.ShopOrderOperation);
        try
        {
            StringBuilder queryBuilder = new StringBuilder();
            queryBuilder.append("UPDATE ").append(tableName).append(" SET ");
            HashMap<Integer, Object> columnValues = new HashMap<>();
            int i = 0;

            queryBuilder.append("operation_status = ? ");
            columnValues.put(++i, operationStatus.toString());

            queryBuilder.append("WHERE id = ? ");
            columnValues.put(++i, operationId);

            new MySqlWriter().WriteToTable(queryBuilder.toString(), columnValues);
            return true;
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        }
    }
    
    public void UpdateTestColumn()
    {
        try
        {
            String query = "UPDATE gantt_tasks SET test_column = ? WHERE id = 1";
            HashMap<Integer, Object> columnValues = new HashMap<>();

            java.sql.Date date = java.sql.Date.valueOf("2018-08-08");
            columnValues.put(1, date);

            new MySqlWriter().WriteToTable(query, columnValues);

        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
    }

}
