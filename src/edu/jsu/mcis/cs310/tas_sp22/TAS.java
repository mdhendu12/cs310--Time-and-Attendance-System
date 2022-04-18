package edu.jsu.mcis.cs310.tas_sp22;
import java.util.*;
import java.time.*;
import org.junit.Before;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.json.simple.*; 

public class TAS {
    
    public static void main(String[] args) {
  
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
    
    public static int calculateTotalMinutes(ArrayList<Punch> dailypunchlist, Shift s) {
        
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
                
                String day = secondPunch.getOriginalTimestamp().getDayOfWeek().toString();
        
                if (day != "SATURDAY" && day != "SUNDAY") {
                    
                    if (clockedOut) {
                        minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                        totalMinutes += minutes.toMinutes();
                    }

                    else {
                        minutes = Duration.between(firstPunch.getAdjustedTS(), secondPunch.getAdjustedTS());
                        totalMinutes += (minutes.toMinutes() - s.getLunchduration().toMinutes());
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
          
    public static String getPunchListAsJSON(ArrayList<Punch> dailypunchlist) {
        // Written by Matthew
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
}

        
    
    
