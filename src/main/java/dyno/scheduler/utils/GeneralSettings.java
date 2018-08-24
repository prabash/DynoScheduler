/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.utils;

import dyno.scheduler.data.DataEnums;
import dyno.scheduler.data.DataEnums.DataGetMethod;

/**
 *
 * @author Prabash
 */
public class GeneralSettings
{
    // <editor-fold desc="properties">
    
    private static final String hostName = "127.0.0.1";
    private static final DataGetMethod dataGetMethod = DataEnums.DataGetMethod.Excel;

    // </editor-fold>
    
    // <editor-fold desc="getters/setters">
    
    public static String getHostName()
    {
        return hostName;
    }
    
    public static DataGetMethod getDataGetMethod()
    {
        return dataGetMethod;
    }
    
    // </editor-fold>
}
