/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.data.DataEnums.DataGetMethod;
import dyno.scheduler.utils.GeneralSettings;

/**
 *
 * @author Prabash
 */
public class DataFactory
{
    /**
     * return a new instance of the DataReadManager depending on the current data get method
     * available in the general settings
     * @return DataReadManager instance
     */
    public static DataReadManager getDataReadManagerInstance()
    {
        DataGetMethod dataGetMethod = GeneralSettings.getDataGetMethod();
        switch (dataGetMethod)
        {
            case Database:
                return new MysqlReadManager();
            case Excel:
                return new ExcelReadManager();
            default:
                return null;
        }
    }
}
