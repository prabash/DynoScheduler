/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.datamodels.WorkCenterOpAllocModel;
import java.util.List;

/**
 *
 * @author Prabash
 */
public class DataWriter
{
    public static boolean updateWorkCenterAllocData(List<WorkCenterOpAllocModel> dataList, String storageName)
    {
        return DataFactory.getDataWriteManagerInstance().updateWorkCenterOpALlocData(dataList, storageName);
    }
}
