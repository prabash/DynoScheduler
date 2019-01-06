/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.utils.LogUtil;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Prabash
 */
public class MySqlWriter
{

    public boolean WriteToTable(String query, HashMap<Integer, Object> columnValues)
    {
        Connection connection = null;
        PreparedStatement preparedStmt = null;

        try
        {
            connection = MySqlConnection.getConnection();
            // create the java mysql update preparedstatement
            preparedStmt = connection.prepareStatement(query);
            for (Map.Entry<Integer, Object> entry : columnValues.entrySet())
            {
                if(entry.getValue() instanceof Integer)
                {
                    preparedStmt.setInt(entry.getKey(), (int)entry.getValue());
                }
                else if (entry.getValue() instanceof String)
                {
                    preparedStmt.setString(entry.getKey(), (String)entry.getValue());
                }
                else if (entry.getValue() instanceof Date)
                {
                    Date date = (Date)entry.getValue();
                    preparedStmt.setObject(entry.getKey(), date);
                }
                else if (entry.getValue() instanceof Time)
                {
                    Time time = (Time)entry.getValue();
                    preparedStmt.setTime(entry.getKey(), time);
                }
            }

            // execute the java preparedstatement
            preparedStmt.executeUpdate();

        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return false;
        } finally
        {
            try
            {
                if (preparedStmt != null)
                {
                    preparedStmt.close();
                }

            } catch (SQLException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }

            return true;
        }
    }
}
