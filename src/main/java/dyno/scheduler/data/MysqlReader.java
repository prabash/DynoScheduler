/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.data;

import dyno.scheduler.utils.LogUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Prabash
 */
public class MysqlReader
{

    public void testConnection()
    {

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;

        try
        {
            connection = new MysqlConnection().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM shop_order_tab");
            while (resultSet.next())
            {
                System.out.printf("%s\t%s\t%s\t%s\t",
                        resultSet.getString(1),
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDate(4));
            }

        } catch (SQLException ex)
        {
            LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
        } finally
        {
            try
            {
                if (resultSet != null)
                {
                    resultSet.close();
                }
                if (statement != null)
                {
                    statement.close();
                }
                if (connection != null)
                {
                    connection.close();
                }

            } catch (SQLException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }
        }
    }
}
