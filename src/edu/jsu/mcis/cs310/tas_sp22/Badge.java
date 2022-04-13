package edu.jsu.mcis.cs310.tas_sp22;
import java.util.HashMap; 


public class Badge 
{
    private String id, description;
    
    public Badge(HashMap<String, String> params)
    {
        this.id = params.get("id");
        this.description = params.get("description");
    }

    public String getId()
    {
        return id;
    }

    public String getDescription() 
    {
        return description;          
    }

    @Override
    public String toString()
    {
        StringBuilder result =  new StringBuilder(); 
        result.append("#").append(id).append(" (").append(description).append(")");
        
        return result.toString();
    }
 }
