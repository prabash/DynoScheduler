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
import java.util.List;

/**
 *
 * @author Prabash
 */
public class MysqlReadManager extends DataReadManager
{
    @Override
    public List<DataModel> getData(DataModelEnums.DataModelType dataModel)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected String getStorageName(DataModelEnums.DataModelType dataModel)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<ShopOrderModel> getShopOrderData(String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<ShopOrderOperationModel> getShopOrderOperationData(String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<WorkCenterModel> getWorkCenterData(String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected List<WorkCenterOpAllocModel> getWorkCenterOpAllocData(String storageName)
    {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
