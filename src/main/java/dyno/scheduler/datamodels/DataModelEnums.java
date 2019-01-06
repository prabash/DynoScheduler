/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

/**
 *
 * @author Prabash
 */
public class DataModelEnums
{
    /**
     * species the priority of a shop order
     */
    public enum ShopOrderPriority
    {
        Critical(1),
        High(2),
        Medium(3),
        Low(4),
        Trivial(5);
        
        private final int value;

        public int getValue()
        {
            return value;
        }
        private ShopOrderPriority(int value)        
        {
            this.value = value;
        }
    }

    public enum ShopOrderScheduleStatus
    {
        Scheduled,
        Unscheduled
    }

    public enum ShopOrderSchedulingDirection
    {
        Backward,
        Forward,
    }

    public enum ShopOrderStatus
    {
        Created,
        Started,
        InProgress,
        Delivered,
        Closed
    }
    
    public enum OperationStatus
    {
        Created,
        Scheduled,
        Unscheduled,
        Reserved,
        Issued,
        InProgress,
        Completed
    }
    
    public enum DataModelType
    {
        ShopOrder,
        ShopOrderOperation,
        WorkCenter,
        WorkCenterAllocationFinite
    }
    
    public enum TimeBlockParamType
    {
        TimeBlockValue,
        TimeBlockName
    }
}
