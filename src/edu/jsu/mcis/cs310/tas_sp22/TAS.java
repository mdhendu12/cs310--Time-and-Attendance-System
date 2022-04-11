package edu.jsu.mcis.cs310.tas_sp22;
import java.util.ArrayList;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import org.json.simple.*; 

public class TAS {
    public static double calculateAbseenism(ArrayList<Punch> pl, Shift S)
        {
            return b; 
        }
    
    public static void main(String[] args) {
            
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
}
