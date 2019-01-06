/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.DataModel;
import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.ShopOrderModel;
import dyno.scheduler.datamodels.ShopOrderOperationModel;
import dyno.scheduler.datamodels.WorkCenterModel;
import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import dyno.scheduler.utils.LogUtil;
import dyno.scheduler.utils.MySqlUtil;
import dyno.scheduler.utils.TableUtil;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        ResultSet results = null;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                ShopOrderModel shopOrderObj = new ShopOrderModel().getModelObject(results);
                shopOrders.add(shopOrderObj);
            }

        } catch (Exception ex)
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
        ResultSet results = null;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                ShopOrderOperationModel shopOrderOpObj = new ShopOrderOperationModel().getModelObject(results);
                shopOrderOperations.add(shopOrderOpObj);
            }
        } catch (Exception ex)
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
        ResultSet results = null;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                WorkCenterModel workCenter = new WorkCenterModel().getModelObject(results);
                workCenters.add(workCenter);
            }
        } catch (Exception ex)
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
        ResultSet results = null;
        try
        {
            results = new MySqlReader().ReadTable(storageName, filters, orderBy);
            while (results.next())
            {
                WorkCenterOpAllocModel workCenterOpAlloc = new WorkCenterOpAllocModel().getModelObject(results);
                workCenterOpAllocs.add(workCenterOpAlloc);
            }

        } catch (Exception ex)
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
        
        ResultSet results = null;
        try
        {
            results = new MySqlReader().ReadTable(tableName, filters, orderBy);
            while (results.next())
            {
                ShopOrderOperationModel shopOrderOpObj = new ShopOrderOperationModel().getModelObject(results);
                shopOrderOperations.add(shopOrderOpObj);
            }
        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        }
        return shopOrderOperations;
    }
}
