/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.utils;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.DataModelEnums.StoredProcedures;

/**
 *
 * @author Prabash
 */
public class MySqlUtil
{

    public static String getStorageName(DataModelEnums.DataModelType dataModel)
    {
        switch (dataModel)
        {
            case ShopOrder:
                return getDbName() + "shop_order_tab";
            case ShopOrderOperation:
                return getDbName() + "shop_order_operation_tab";
            case WorkCenter:
                return getDbName() + "work_center_tab";
            case WorkCenterAllocationFinite:
                return getDbName() + "work_center_op_alloc_finite_tab";
            default:
                return "";
        }
    }

    public static String getDbName()
    {
        return "dynoschedule_test.";
    }

    public static String getStoredProcedureName(StoredProcedures procedure)
    {
        switch (procedure)
        {
            case OperationScheduledTimeBlockFinite:
                return "get_operation_scheduled_time_block_finite";
            case InterruptedOperaitonDetails:
                return "get_interrupted_operation_details";
            case UnscheduledOrders:
                return "get_unscheduled_orders";
            case UnschedledOperationWorkCenters:
                return "get_unscheduled_operation_workcenters";
            case AllOperationsOrdered:
                return "get_all_shop_order_operations_ordered";
            case ByOrderNoOperationsOrdered:
                return "get_shop_order_operations_ordered";
            case LowerPriorityBlockerShopOrders:
                return "get_lower_priority_blocker_shop_orders";    
            default:
                return "";
        }
    }
}
