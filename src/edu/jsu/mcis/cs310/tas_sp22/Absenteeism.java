package edu.jsu.mcis.cs310.tas_sp22;
import java.time. *; 

public class Absenteeism {
    private String badgeid; 
    private LocalDate payPeriod; 
    private double percentage; 
    
    public Absenteeism (String string, LocalDate date, double percent)
    {
        this.badgeid = string; 
        this.payPeriod = date;
        this.percentage = percent; 
    }
    
    public String getBadgeid()
    {
        return badgeid; 
    }
    
    public LocalDate getPayPeriod()
    {
        return payPeriod; 
    }
    
    public double getPercentage()
    {
        return percentage; 
    }
    
    @Override 
    public String toString()
    {
        StringBuilder sb = new StringBuilder(); 
        sb.append("#").append(badgeid).append(" (Pay Period Starting ").append(payPeriod).append("): ");
        sb.append(percentage).append("%"); 
        return sb.toString(); 
    }
    
}
