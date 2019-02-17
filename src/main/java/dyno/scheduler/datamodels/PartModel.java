/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.datamodels;

import dyno.scheduler.utils.LogUtil;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.poi.ss.usermodel.Row;

/**
 *
 * @author Prabash
 */
public class PartModel extends DataModel
{
    // <editor-fold defaultstate="collapsed" desc="properties">
    
    private int id;
    private String partNo; 
    private String partDescription;
    private String vendor;
    
    // </editor-fold>
    
    // <editor-fold defaultstate="collapsed" desc="constructors"> 
    
    public PartModel(int id, String partNo, String partDescription, String vendor)
    {
        this.id = id;
        this.partNo = partNo;
        this.partDescription = partDescription;
        this.vendor = vendor;
    }
    
    public PartModel()
    {
    }
    
    // </editor-fold>

    //<editor-fold defaultstate="collapsed" desc="getters/setters">

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getPartNo()
    {
        return partNo;
    }

    public void setPartNo(String partNo)
    {
        this.partNo = partNo;
    }

    public String getPartDescription()
    {
        return partDescription;
    }

    public void setPartDescription(String partDescription)
    {
        this.partDescription = partDescription;
    }

    public String getVendor()
    {
        return vendor;
    }

    public void setVendor(String vendor)
    {
        this.vendor = vendor;
    }
    
    //</editor-fold>

    // <editor-fold defaultstate="collapsed" desc="overriden methods"> 
    
    @Override
    public PartModel getModelObject(Object row)
    {
        if (row instanceof Row)
        {
        }
        else
        {
            ResultSet resultSetRow = (ResultSet) row;
            int i = 0;
            try
            {
                this.setId(resultSetRow.getInt(++i));
                this.setPartNo(resultSetRow.getString(++i));
                this.setPartDescription(resultSetRow.getString(++i));
                this.setVendor(resultSetRow.getString(++i));
                
            } catch (SQLException ex)
            {
                LogUtil.logSevereErrorMessage(this, ex.getMessage(), ex);
            }
            
        }
        return this;
    }

    @Override
    public String getPrimaryKey()
    {
        return String.valueOf(this.getId());
    }

    @Override
    public String getClassName()
    {
        return PartModel.class.getName();
    }
    
    //</editor-fold>
}
