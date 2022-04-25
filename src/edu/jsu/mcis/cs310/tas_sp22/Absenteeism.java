package edu.jsu.mcis.cs310.tas_sp22;
import java.time. *; 
import java.time.format.DateTimeFormatter;

public class Absenteeism {
    private String badgeid; 
    private LocalDate payPeriod; 
    private double percentage; 
    
    public Absenteeism (String badgeid, LocalDate date, double percent)
    {
        this.badgeid = badgeid; 
        this.payPeriod = date;
        this.percentage = percent; 
    }
    
    public Absenteeism (Badge b, LocalDate date, double percent) {
        this.badgeid = b.getId();
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
        sb.append("#").append(badgeid).append(" (Pay Period Starting ").append(DateTimeFormatter.ofPattern("MM-dd-yyyy").format(payPeriod)).append("): ");
        sb.append(String.format("%.2f%%", percentage)); 
        return sb.toString(); 
    }
    
}
