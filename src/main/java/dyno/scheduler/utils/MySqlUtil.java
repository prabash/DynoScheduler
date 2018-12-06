/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.utils;

import dyno.scheduler.datamodels.DataModelEnums;

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
                return "shop_order_tab";
            case ShopOrderOperation:
                return "shop_order_operation_tab";
            case WorkCenter:
                return "work_center_tab";
            case WorkCenterAllocationFinite:
                return "work_center_op_alloc_finite_tab";
            default:
                return "";
        }
    }
}
