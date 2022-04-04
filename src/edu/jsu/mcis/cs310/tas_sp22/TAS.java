package edu.jsu.mcis.cs310.tas_sp22;
import java.util.*;
import java.time.*;
import org.junit.Before;

public class TAS {
    
    public static void main(String[] args) {
    
        TASDatabase db = new TASDatabase();
        Punch p = db.getPunch(3634);
        Badge b = p.getBadge();
        Shift s = db.getShift(b);
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginalTimestamp().toLocalDate());
        
        for (Punch punch : dailypunchlist) {
            punch.adjust(s);
        }
        
        /* Compute Pay Period Total */
        
        int m = TAS.calculateTotalMinutes(dailypunchlist, s);
        
    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        
        int totalMinutes = 0;
        Punch inPunch = null;
        Punch outPunch = null;
        Punch firstPunch = null;
        Punch secondPunch = null;
        boolean clockout = true;
        Duration minutes;
        Iterator<Punch> it = dailypunchlist.iterator();
        
        if (dailypunchlist.size() > 1) {
            System.out.println(dailypunchlist);
            while (it.hasNext()) {

                if (clockout) {
                    firstPunch = it.next();
                    System.out.println(firstPunch);
                }

                else {
                    clockout = true; 
                    break;
                }

                    secondPunch = it.next();

                if (secondPunch.getPunchtype().toString() == "CLOCK IN") {
                    firstPunch = secondPunch;
                    clockout = false;                
                    break;
                }

                else {

                    if (secondPunch.toString() == "TIME OUT") {break;}
                    
                    System.out.println(firstPunch);
                    minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                    totalMinutes += minutes.toMinutes();
                    System.out.println(totalMinutes);


                }
                }
        }

            if (totalMinutes > shift.getLunchthreshold() && secondPunch.getAdjustedTS().toLocalTime() != shift.getLunchstart()) {
                System.out.println(totalMinutes);
                return totalMinutes - shift.getLunchduration().toMinutesPart();
            }

            else {return totalMinutes;}
        
        }
}

        
    
    
