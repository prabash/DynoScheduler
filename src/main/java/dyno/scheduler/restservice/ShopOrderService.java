/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import dyno.scheduler.datamodels.DataModelEnums;
import dyno.scheduler.datamodels.ShopOrderModel;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
@Path("/shop-order")
public class ShopOrderService implements IDynoService
{
    @Override
    public Response get()
    {
        List<ShopOrderModel> list = new ArrayList<>();
        GenericEntity<List<ShopOrderModel>> entity;
        
        list.add(new ShopOrderModel("SO1", "Test1", DateTime.now(), "P1", "SR1", "RR1", DateTime.now(), DateTime.now(), DateTime.now(), DataModelEnums.ShopOrderSchedulingDirection.Backward, "C1", DataModelEnums.ShopOrderStatus.Created, DataModelEnums.ShopOrderPriority.Trivial, null));
        list.add(new ShopOrderModel("SO2", "Test2", DateTime.now(), "P2", "SR2", "RR2", DateTime.now(), DateTime.now(), DateTime.now(), DataModelEnums.ShopOrderSchedulingDirection.Backward, "C2", DataModelEnums.ShopOrderStatus.Created, DataModelEnums.ShopOrderPriority.Trivial, null));
        
        entity = new GenericEntityImpl(list);
        return Response.ok(entity).build();
    }    

    private static class GenericEntityImpl extends GenericEntity<List<ShopOrderModel>>
    {
        public GenericEntityImpl(List<ShopOrderModel> entity)
        {
            super(entity);
        }
    }
}
