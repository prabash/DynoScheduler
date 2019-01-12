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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Prabash
 */
public class MySqlWriter
{

    public int WriteToTable(String query, HashMap<Integer, Object> columnValues)
    {
        Connection connection = null;
        PreparedStatement preparedStmt = null;
        int key = -1;
        try
        {
            connection = MySqlConnection.getConnection();
            // create the java mysql update preparedstatement
            preparedStmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            for (Map.Entry<Integer, Object> entry : columnValues.entrySet())
            {
                if(entry.getValue() instanceof Integer)
                {
                    preparedStmt.setInt(entry.getKey(), (int)entry.getValue());
                }
                else if (entry.getValue() instanceof Double)
                {
                    preparedStmt.setDouble(entry.getKey(), (double)entry.getValue());
                }
                else if (entry.getValue() instanceof String)
                {
                    preparedStmt.setString(entry.getKey(), (String)entry.getValue());
                }
                else if (entry.getValue() instanceof Date)
                {
                    Date date = (Date)entry.getValue();
                    if (date.equals(new Date(0)))
                    {
                        preparedStmt.setNull(entry.getKey(), java.sql.Types.DATE);
                    }
                    else
                    {
                        preparedStmt.setObject(entry.getKey(), date);
                    }
                }
                else if (entry.getValue() instanceof Time)
                {
                    Time time = (Time)entry.getValue();
                    if (time.equals(new Time(0)))
                    {
                        preparedStmt.setNull(entry.getKey(), java.sql.Types.TIME);
                    }
                    else
                    {
                        preparedStmt.setTime(entry.getKey(), time);
                    }
                }
            }

            // execute the java preparedstatement
            preparedStmt.executeUpdate();
            ResultSet generatedKey = preparedStmt.getGeneratedKeys();
            if (generatedKey.next())
            {
                key = generatedKey.getInt(1);
            }

        } catch (Exception ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            return key;
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

            return key;
        }
    }
}
