/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.utils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author prabash.dharshanapri
 */
public class LogManager
{
    /**
     * log info messages
     * @param className name of the class
     * @param message message to be logged
     */
    public static void logInfoMessage(String className, String message)
    {
        Logger.getLogger(className).log(Level.INFO, message);
    }
    
    /**
     * log error messages
     * @param className name of the class
     * @param message message to be logged
     * @param ex exception
     */
    public static void logSevereErrorMessage(String className, String message, Exception ex)
    {
        Logger.getLogger(className).log(Level.SEVERE, message, ex);
    }
}
