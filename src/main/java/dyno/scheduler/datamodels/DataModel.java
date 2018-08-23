/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

/**
 *
 * @author Prabash
 */
public abstract class DataModel
{
    /**
     * this method is used to return the DataModel or sub-type object by passing 
     * rowData acquired by reading an Excel or MySql table row
     * @param <T> type of DataModel
     * @param rowData data to be passed to populate the object
     * @return return an object of the DataModel sub-type
     */
    public abstract <T> T getModelObject(Object rowData);
}
