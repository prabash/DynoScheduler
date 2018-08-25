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
    
    private static final String HOST_NAME = "127.0.0.1";
    private static final String EXCEL_FILE = "data.xlsx";
    private static final DataGetMethod DATA_GET_METHOD = DataEnums.DataGetMethod.Excel;
    

    // </editor-fold>
    
    // <editor-fold desc="getters/setters">
    
    public static String getHostName()
    {
        return HOST_NAME;
    }
    
    public static DataGetMethod getDataGetMethod()
    {
        return DATA_GET_METHOD;
    }
    
    public static String getDefaultExcelFile()
    {
        return EXCEL_FILE;
    }
    
    // </editor-fold>

}
