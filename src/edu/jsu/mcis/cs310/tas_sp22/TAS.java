package edu.jsu.mcis.cs310.tas_sp22;
import java.util.*;
import java.time.*;
import org.junit.Before;

public class TAS {
    
    public static void main(String[] args) {
  
    }
    
    protected static boolean clockedOutForLunch (Punch firstPunch, Punch secondPunch, Shift shift) {
        
        boolean clockedOut;
        
        if (secondPunch.getAdjustmenttype().equals("Lunch Start")) {
            clockedOut = true;
        }
            
        else if (firstPunch.getAdjustmenttype().equals("Lunch Stop")) {
            clockedOut = true;
        }

        else {clockedOut = false;}
        
        return clockedOut;

}
    
    protected static boolean punchTypesCorrect (Punch firstPunch, Punch secondPunch, Shift shift) {
        
        boolean correct = false;
        String first = firstPunch.getPunchtype().toString();
        String second = secondPunch.getPunchtype().toString();
        
        if (first != "CLOCK IN" || second != "CLOCK OUT") {
                    correct = false;
                }
        
        else {correct = true;}
        
        return correct;
        
    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        
        int totalMinutes = 0;
        Punch firstPunch = null;
        Punch secondPunch = null;
        boolean correct = false;
        boolean clockedOut;
        Duration minutes;
        Iterator<Punch> it = dailypunchlist.iterator();
        
        if (dailypunchlist.size() > 1) {
            
            while (it.hasNext()) {
                
                if (firstPunch == null && secondPunch == null) {
                    firstPunch = it.next();
                    secondPunch = it.next();
                }
                
                else if ((firstPunch == secondPunch) && it.hasNext()) {secondPunch = it.next();}

                else {
                    if (it.hasNext()) {firstPunch = it.next();}
                    if (it.hasNext()) {secondPunch = it.next();}
                    else {break;}
                }

                correct = punchTypesCorrect(firstPunch, secondPunch, shift);

                if (!correct) {
                    firstPunch = secondPunch;
                    clockedOut = true;
                }

                else {
                    clockedOut = clockedOutForLunch(firstPunch, secondPunch, shift);
                    correct = false;
                }
                
                String day = secondPunch.getOriginalTimestamp().getDayOfWeek().toString();
        
                if (day != "SATURDAY" && day != "SUNDAY") {
                    
                    if (clockedOut) {
                        minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                        totalMinutes += minutes.toMinutes();
                    }

                    else {
                        minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                        totalMinutes += (minutes.toMinutes() - shift.getLunchduration().toMinutes());
                    }

                }
                
                else {
                    minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                    totalMinutes += minutes.toMinutes();
                }
            }
        }
        
        return totalMinutes;
        
        }
}

        
    
    
