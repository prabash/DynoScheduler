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
    public DataManager getDataManagerInstance()
    {
        DataGetMethod dataGetMethod = GeneralSettings.getDataGetMethod();
        switch (dataGetMethod)
        {
            case Database:
                return new MysqlManager();
            case Excel:
                return new ExcelManager();
            default:
                return null;
        }
    }
}
