/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.data.DataReader;
import java.util.List;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 */
public class WorkCenterUtil
{
    public static void interruptWorkCenter(DateTime interruptionStartDateTime, DateTime interruptionEndDateTime, String workCenterNo)
    {
        List<InterruptedOpDetailsDataModel> interruptionDetails = DataReader.getInterruptedOperationDetails(interruptionStartDateTime, interruptionStartDateTime, interruptionEndDateTime, interruptionEndDateTime, workCenterNo);
        for (InterruptedOpDetailsDataModel interruptionDetail : interruptionDetails)
        {
            ShopOrderModel shopOrder = DataReader.getShopOrderByPrimaryKey(interruptionDetail.getOrderNo());
            DateTime interruptionOnOpEndDateTime = WorkCenterOpAllocModel.incrementTime(interruptionDetail.getInterruptionOnOpStartDateTime(), interruptionDetail.getInterruptedRunTime());
            shopOrder.unscheduleOperationsOnInterruption(interruptionDetail.getInterruptionOnOpStartDateTime(), interruptionOnOpEndDateTime, DataModelEnums.InerruptionType.Interruption);
        }
    }
    
    public static void interruptWorkCenterOnPartUnavailability(DateTime partUnavailableStartDateTime, DateTime partUnavailableEndDateTime, String workCenterNo)
    {
        List<InterruptedOpDetailsDataModel> interruptionDetails = DataReader.getInterruptedOperationDetails(partUnavailableStartDateTime, partUnavailableStartDateTime, partUnavailableEndDateTime, partUnavailableEndDateTime, workCenterNo);
        for (InterruptedOpDetailsDataModel interruptionDetail : interruptionDetails)
        {
            ShopOrderModel shopOrder = DataReader.getShopOrderByPrimaryKey(interruptionDetail.getOrderNo());
            DateTime interruptionOnOpEndDateTime = WorkCenterOpAllocModel.incrementTime(interruptionDetail.getInterruptionOnOpStartDateTime(), interruptionDetail.getInterruptedRunTime());
            shopOrder.unscheduleOperationsOnInterruption(interruptionDetail.getInterruptionOnOpStartDateTime(), interruptionOnOpEndDateTime, DataModelEnums.InerruptionType.PartUnavailable);
        }
    }
}
