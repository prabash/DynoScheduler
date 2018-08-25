/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.utils;

import dyno.scheduler.data.DataEnums;
import dyno.scheduler.data.DataEnums.DataAccessMethod;

/**
 *
 * @author Prabash
 */
public class GeneralSettings
{
    // <editor-fold desc="properties">
    
    private static final String HOST_NAME = "127.0.0.1";
    private static final String EXCEL_FILE = "data.xlsx";
    private static final DataAccessMethod DATA_ACCESS_METHOD = DataEnums.DataAccessMethod.Excel;
    

    // </editor-fold>
    
    // <editor-fold desc="getters/setters">
    
    public static String getHostName()
    {
        return HOST_NAME;
    }
    
    public static DataAccessMethod getDataAccessMethod()
    {
        return DATA_ACCESS_METHOD;
    }
    
    public static String getDefaultExcelFile()
    {
        return EXCEL_FILE;
    }
    
    // </editor-fold>

}
