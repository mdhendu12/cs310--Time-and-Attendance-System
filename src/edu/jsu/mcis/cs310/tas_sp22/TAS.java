package edu.jsu.mcis.cs310.tas_sp22;
import java.util.*;
import java.time.*;
import org.junit.Before;

public class TAS {
    
    public static void main(String[] args) {
    
        TASDatabase db = new TASDatabase();
        Punch p = db.getPunch(3634);
        //System.out.println(p);
        Badge b = p.getBadge();
        //System.out.println(b);
        Shift s = db.getShift(b);
        //System.out.println(s);
        
        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginalTimestamp().toLocalDate());
        
        for (Punch punch : dailypunchlist) {
            punch.adjust(s);
            System.out.println(punch.printAdjusted());
        }
        System.out.println(dailypunchlist.toString());
        /* Compute Pay Period Total */
        
        int m = TAS.calculateTotalMinutes(dailypunchlist, s);
        System.out.println(m);
    }
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift shift) {
        
        int totalMinutes = 0;
        Iterator<Punch> currentPunch = dailypunchlist.iterator();
        
        while (currentPunch.hasNext()) {
        
            Punch inPunch = currentPunch.next();
            System.out.println(inPunch);
            Punch outPunch = currentPunch.next();
            System.out.println(outPunch);
            Duration minutes = Duration.between(inPunch.getAdjustedTS(), outPunch.getAdjustedTS());
            totalMinutes += minutes.toMinutes();
        
        }

        return totalMinutes;
        
    }
            
}
