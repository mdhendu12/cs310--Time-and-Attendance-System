package edu.jsu.mcis.cs310.tas_sp22;

import java.time.*;
import java.util.HashMap;

public class Employee 
{
    private String badgeid, firstname, middlename, lastname; 
    private int employeetypeid, departmentid, shiftid; 
    private LocalDate active, inactive; 
    
    public Employee(HashMap<String, String> s, HashMap<String, LocalDate> l,HashMap<String, Integer> i)
    {
        this.employeetypeid = i.get("employeetypeid");
        this.departmentid = i.get("departmentid"); 
        this.shiftid = i.get("shiftid"); 
        this.badgeid = s.get("badgeid"); 
        this.firstname = s.get("firstname"); 
        this.middlename = s.get("middlename"); 
        this.lastname = s.get("lastname"); 
        this.active = l.get("active"); 
        this.inactive = l.get("inactive"); 
    }

    public int getEmployeetypeid()
    {
        return employeetypeid;
    }

    public int getDepartmentid()
    {
        return departmentid; 
    }

    public int getShiftid()
    {
        return shiftid;
    }

    public String getBadge()
    {
        return badgeid; 
    }

    public String getFirst()
    {
        return firstname; 
    }

    public String getMiddle()
    {
        return middlename;
    }

    public String getLast()
    {
        return lastname; 
    }

    public LocalDate getActive()
    {
        return active;
    }

    public LocalDate getInactive()
    {
        return inactive;
    }

    @Override
    public String toString()
    {
        StringBuilder result =  new StringBuilder(); 
        result.append("#").append(badgeid).append(" (").append(lastname).append(", ").append(firstname);
        result.append(" ").append(middlename).append("): employeetypeid: ").append(employeetypeid);
        result.append(", departmentid: ").append(departmentid).append(", shiftid: ").append(shiftid); 
        result.append(", active: ").append(active).append(", inactive: ");
        
        if (inactive == null)
        {
            result.append("none");
        }
        else 
        {
            result.append(inactive); 
        }
        
        return result.toString();
    }
}
