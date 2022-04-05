package edu.jsu.mcis.cs310.tas_sp22;
import java.util.*;
import java.time.*;
import org.junit.Before;

public class TAS {
    
    public static void main(String[] args) {
        
    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        
        int totalMinutes = 0;
        Punch firstPunch = null;
        Punch secondPunch = null;
        boolean clockout = true;
        Duration minutes;
        Iterator<Punch> it = dailypunchlist.iterator();
        
        if (dailypunchlist.size() > 1) {
            
            while (it.hasNext()) {

                if (clockout) {
                    firstPunch = it.next();
                }

                else {
                    clockout = true;
                }

                if (it.hasNext()) {secondPunch = it.next();}
                else {break;}
                
                if (secondPunch.getPunchtype().toString() == "CLOCK IN") {
                    firstPunch = secondPunch;
                    clockout = false;
                }

                else {

                    if (secondPunch.toString() == "TIME OUT") {break;}
                    
                    else {
                        minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                        totalMinutes += minutes.toMinutes();
                    }

                }
                }
        }
        
        else {return totalMinutes;}

        if (secondPunch.getOriginalTimestamp().getDayOfWeek().toString() != "SATURDAY" && secondPunch.getOriginalTimestamp().getDayOfWeek().toString() != "SUNDAY") {
            
            if (totalMinutes > shift.getLunchthreshold() && secondPunch.getAdjustmenttype() == "Lunch Start") {
                return totalMinutes - shift.getLunchduration().toMinutesPart();
            }
            
            else if (totalMinutes > shift.getLunchthreshold() && (!firstPunch.getAdjustmenttype().equals("Lunch Stop")) && (!secondPunch.getAdjustmenttype().equals("Lunch Start"))) {
                return totalMinutes - shift.getLunchduration().toMinutesPart();
            }

            else {return totalMinutes;}
            }

        else {return totalMinutes;}
        }
}

        
    
    
