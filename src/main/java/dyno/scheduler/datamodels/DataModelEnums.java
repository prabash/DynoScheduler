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
    public enum ShopOrderPriority
    {
        Critical,
        High,
        Medium,
        Low,
        Trivial
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
        Reserved,
        Issued,
        InProgress,
        Completed
    }

}
