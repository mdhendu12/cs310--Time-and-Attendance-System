package edu.jsu.mcis.cs310.tas_sp22;

import java.time.*;
import java.util.HashMap;

public class Shift {
    
    private String description;
    private int roundinterval, graceperiod, dockpenalty, lunchthreshold;
    private LocalTime shiftstart, shiftstop, lunchstart, lunchstop;
    private Duration shiftduration, lunchduration;
  
    public Shift(String description, HashMap<String, Integer> integers, HashMap<String, LocalTime> localtimes) {
     
        this.description = description; 
        this.shiftstart = localtimes.get("shiftstart");
        this.shiftstop = localtimes.get("shiftstop");
        this.shiftduration = Duration.between(shiftstart, shiftstop);
        this.roundinterval = integers.get("roundinterval");
        this.graceperiod = integers.get("graceperiod");
        this.dockpenalty = integers.get("dockpenalty");
        this.lunchstart = localtimes.get("lunchstart");
        this.lunchstop = localtimes.get("lunchstop");
        this.lunchduration = Duration.between(lunchstart, lunchstop);
        this.lunchthreshold = integers.get("lunchthreshold");
      
    }

    public String getDescription() {
        return description;
    }

    public LocalTime getShiftstart() {
        return shiftstart;
    }

    public LocalTime getShiftstop() {
        return shiftstop;
    }

    public Duration getShiftduration() {
        return shiftduration;
    }

    public int getRoundinterval() {
        return roundinterval;
    }

    public int getGraceperiod() {
        return graceperiod;
    }

    public int getDockpenalty() {
        return dockpenalty;
    }

    public LocalTime getLunchstart() {
        return lunchstart;
    }

    public LocalTime getLunchstop() {
        return lunchstop;
    }

    public Duration getLunchduration() {
        return lunchduration;
    }

    public int getLunchthreshold() {
        return lunchthreshold;
    }

    @Override
    public String toString() {
        
        StringBuilder sb = new StringBuilder();
        
        sb.append(description).append(": ");
        sb.append(shiftstart).append(" - ").append(shiftstop).append(" ");
        sb.append("(").append(shiftduration.toMinutes()).append(" minutes").append(")").append("; Lunch: ");
        sb.append(lunchstart).append(" - ").append(lunchstop).append(" (");
        sb.append(lunchduration.toMinutes()).append(" minutes").append(")");
       
        return sb.toString();
        
    }
}