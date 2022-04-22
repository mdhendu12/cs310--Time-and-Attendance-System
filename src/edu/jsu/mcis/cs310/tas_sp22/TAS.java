package edu.jsu.mcis.cs310.tas_sp22;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import org.json.simple.*; 

public class TAS {
    
    public static void main(String[] args) {
        
        TASDatabase db;
        db = new TASDatabase();
        
        Punch p = db.getPunch(4943);
        Badge b = p.getBadge();
        Shift s = db.getShift(b);
        
        /* Get Pay Period Punch List */
        
        LocalDateTime ts = p.getOriginalTimestamp();
        ArrayList<Punch> punchlist = db.getPayPeriodPunchList(b, ts.toLocalDate(), s);
        
        for (Punch punch : punchlist) {
            System.out.println(punch.printAdjusted());
        }
        
        
        /* Compute Pay Period Total Absenteeism */
        
        double percentage = TAS.calculateAbsenteeism(punchlist, s);
    

//        Punch p = db.getPunch(1087);
//        Badge b = p.getBadge();
//        Shift s = db.getShift(b);
//        
//        ArrayList<Punch> dailypunchlist = db.getDailyPunchList(b, p.getOriginalTimestamp().toLocalDate());
//        
//        for (Punch punch : dailypunchlist) {
//            punch.adjust(s);
//            System.out.println(punch.printAdjusted());
//        }
//		
//        /* Compute Pay Period Total */
//        
//        int m = TAS.calculateTotalMinutes(dailypunchlist, s);
        
    }
    
    private static boolean clockedOutForLunch (Punch firstPunch, Punch secondPunch) {
        
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
    
    private static boolean punchTypesCorrect (Punch firstPunch, Punch secondPunch) {
        
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

                correct = punchTypesCorrect(firstPunch, secondPunch);

                if (!correct) {
                    firstPunch = secondPunch;
                    clockedOut = true;
                }

                else {
                    clockedOut = clockedOutForLunch(firstPunch, secondPunch);
                    correct = false;
                }
                
                minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                String day = secondPunch.getOriginalTimestamp().getDayOfWeek().toString();
           
                if (clockedOut) {
                    totalMinutes += minutes.toMinutes();
                }

                else if (minutes.toMinutes() > shift.getLunchthreshold())  {
                    totalMinutes += (minutes.toMinutes() - shift.getLunchduration().toMinutes());
                }
                
                else {   
                    totalMinutes += minutes.toMinutes();
                }
            }
        }
        
        return totalMinutes;
        
        }
          
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        
        ArrayList<HashMap<String, String>> jsonData = new ArrayList<HashMap<String, String>>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("E MM/dd/yyyy HH:mm:ss");
        
        for (Punch p : dailypunchlist) {
                    
            HashMap<String, String> punchData = new HashMap<>();
            
            punchData.put("id", String.valueOf(p.getId()));
            punchData.put("badgeid", String.valueOf(p.getBadgeid()));
            punchData.put("terminalid", String.valueOf(p.getTerminalid()));
            punchData.put("punchtype", String.valueOf(p.getPunchtype().toString()));
            punchData.put("adjustmenttype", String.valueOf(p.getAdjustmenttype()));
            punchData.put("originaltimestamp", String.valueOf(p.getOriginalTimestamp().format(dtf).toUpperCase()));
            punchData.put("adjustedtimestamp", String.valueOf(p.getAdjustedTS().format(dtf).toUpperCase()));
            
            jsonData.add(punchData);
        }
        
        String json = JSONValue.toJSONString(jsonData);
        
        return json;
        
    }
    
    public static double calculateAbsenteeism(ArrayList<Punch> p1, Shift s) {
    
        double absenteeism;
        
        int minutesWorked = calculateTotalMinutes(p1, s);
        double totalMinutes;
        int shiftDays = 5; //shiftDays(p1);  FOR NOW, FIVE IS CONSTANT. PERSONALLY I WOULD NOT DO IT THIS WAY, HENCE THE EXTRA FUNCTION BELOW.
        
        totalMinutes = s.getShiftduration().toMinutes() - s.getLunchduration().toMinutes();
        totalMinutes = totalMinutes * shiftDays;
            
        absenteeism = 100 - (minutesWorked/totalMinutes) * 100;
        System.out.println(absenteeism);
        return absenteeism; 
        
    }

    public static int shiftDays (ArrayList<Punch> p1) {
     
     int shiftDays = 0;
     Iterator<Punch> it = p1.iterator();
     String day;
     String previousDay = null;
     boolean weekend = true;
     
     while (it.hasNext()) {
        
        Punch punch = it.next();
        day = punch.getOriginalTimestamp().getDayOfWeek().toString();
        
        if (day != "SATURDAY" && day != "SUNDAY") {weekend = false;}
        
        else {weekend = true;}
            
        
        if (!weekend && day != previousDay) {
            shiftDays += 1;
            previousDay = day;
        }
    }

    return shiftDays;
}   

}