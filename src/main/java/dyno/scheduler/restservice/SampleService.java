/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dyno.scheduler.restservice;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Path;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.xml.bind.annotation.XmlRootElement;
import org.joda.time.DateTime;

/**
 *
 * @author Prabash
 * sample service class used to test service
 * TODO: Remove after the implementation
 */
@Path("/sample")
public class SampleService implements IDynoService
{
    @Override
    public Response get()
    {
        List<DataObj> list = new ArrayList<>();
        
        List<SubDataObj> subList = new ArrayList<>();
        subList.add(new SubDataObj("1"));
        
        GenericEntity<List<DataObj>> entity;
        
        list.add(new DataObj("Prabash", "28", TestEnum.test1, DateTime.now(), subList));
        list.add(new DataObj("Shehan", "25", TestEnum.test2, DateTime.now(), subList));
        
        entity = new GenericEntity<List<DataObj>>(list){};
        return Response.ok(entity).build();
    }
}

@XmlRootElement
class DataObj
{
    private String name;
    private String age;
    private TestEnum enumVal;
    private DateTime currentDate;
    private List<SubDataObj> subData;
    
    public DataObj(String name, String age, TestEnum enumVal, DateTime currentDate, List<SubDataObj> subData)
    {
        this.name = name; 
        this.age = age;
        this.enumVal = enumVal;
        this.currentDate = currentDate;
        this.subData = subData;
    }
    
    public DataObj() {}

    /**
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the age
     */
    public String getAge()
    {
        return age;
    }

    /**
     * @param age the age to set
     */
    public void setAge(String age)
    {
        this.age = age;
    }

    /**
     * @return the enumVal
     */
    public TestEnum getEnumVal()
    {
        return enumVal;
    }

    /**
     * @param enumVal the enumVal to set
     */
    public void setEnumVal(TestEnum enumVal)
    {
        this.enumVal = enumVal;
    }

    /**
     * @return the currentDate
     */
    public DateTime getCurrentDate()
    {
        return currentDate;
    }

    /**
     * @param currentDate the currentDate to set
     */
    public void setCurrentDate(DateTime currentDate)
    {
        this.currentDate = currentDate;
    }

    /**
     * @return the subData
     */
    public List<SubDataObj> getSubData()
    {
        return subData;
    }

    /**
     * @param subData the subData to set
     */
    public void setSubData(List<SubDataObj> subData)
    {
        this.subData = subData;
    }
}

@XmlRootElement
class SubDataObj
{
    private String subId;
    
    public SubDataObj() {}
    
    public SubDataObj(String subId)
    {
        this.subId = subId;
    }

    /**
     * @return the subId
     */
    public String getSubId()
    {
        return subId;
    }

    /**
     * @param subId the subId to set
     */
    public void setSubId(String subId)
    {
        this.subId = subId;
    }
}


enum TestEnum
{
    test1, test2
}