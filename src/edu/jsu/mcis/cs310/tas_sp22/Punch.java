package edu.jsu.mcis.cs310.tas_sp22;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.lang.Math;

public class Punch {
    private int id, terminalid;
    private PunchType eventtypeid;
    private String adjustmenttype, badgeid;
    private LocalDateTime timestamp, adjustedTS;
    private Badge badge;
    
    public Punch(int terminalid, Badge badge, int eventtypeid) {
        this.terminalid = terminalid;
        this.badge = badge;
        this.eventtypeid = PunchType.values()[eventtypeid];
        
        // other fields set to zero or null
        id = 0;
        adjustmenttype = badgeid = null;
        timestamp = adjustedTS = null;
    }
    
    public Punch(HashMap<String, String> params) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        timestamp = LocalDateTime.parse(params.get("timestamp"), dtf);

        id = Integer.valueOf(params.get("id"));
        terminalid = Integer.valueOf(params.get("terminalid"));
        eventtypeid = PunchType.values()[Integer.parseInt(params.get("eventtypeid"))];
        badgeid = params.get("badgeid");
        
        // fields not retrieved by getPunch set to zero or null
        adjustedTS = null;
        badge = null;
        adjustmenttype = null;
    }
    
    public void adjust(Shift s) {
        String day = timestamp.getDayOfWeek().toString();
        
        LocalTime time = timestamp.toLocalTime();
        LocalTime shiftstart = s.getShiftstart();
        LocalTime shiftstop = s.getShiftstop();
        LocalTime lunchstart = s.getLunchstart();
        LocalTime lunchstop = s.getLunchstop();
        LocalTime adjuster = null;
        
        adjustedTS = timestamp;
        
        Boolean inlunchbreak = time.isAfter(lunchstart) && time.isBefore(lunchstop);
        Boolean isntweekend = !"SATURDAY".equals(day) && !"SUNDAY".equals(day);
        
        String eventString = eventtypeid.toString();
        
        int roundinterval = s.getRoundinterval();
        
        if (!"TIME OUT".equals(eventString) && isntweekend) {
                       
            //None Rule
           
            int intervalRound = s.getRoundinterval();
            
            if (timestamp.getMinute() % intervalRound == 0) {
                adjuster = timestamp.toLocalTime().withSecond(0);
                adjustmenttype = "None";
            }
            
            // Shift start rule
            if (time.isBefore(shiftstart) && time.isAfter(shiftstart.minusMinutes(roundinterval))) { 
                adjuster = shiftstart;
                adjustmenttype = "Shift Start";
            }
            else if (time.isAfter(shiftstop) && time.isBefore(shiftstop.plusMinutes(roundinterval))) { 
                adjuster = shiftstop;
                adjustmenttype = "Shift Stop";
            }
            else if (inlunchbreak) {
                if ("CLOCK OUT".equals(eventString)) { 
                    adjuster = lunchstart;
                    adjustmenttype = "Lunch Start";
                }
                else { 
                    adjuster = lunchstop; 
                    adjustmenttype = "Lunch Stop";
                }
            }
            //Grace Period Rule
            
            int graceperiod = s.getGraceperiod();
            
            //Checks if the shift was entered during the grace period for clock-in punches.
            
            if (time.isAfter(shiftstart) && time.isBefore(shiftstart.plusMinutes(graceperiod))) { 
                adjuster = shiftstart;
                adjustmenttype = "Shift Start";
            }
            
            //Checks if the shift was entered during the grace period for clock-out punches.
            
            else if (time.isBefore(shiftstop) && time.isAfter(shiftstop.minusMinutes(graceperiod).minusSeconds(1))) { 
                adjuster = shiftstop;
                adjustmenttype = "Shift Stop";
            }
            
            //Dock Penalty Rule
            
            int dockPenalty = s.getDockpenalty();
            LocalTime inDockInterval = shiftstart.plusMinutes(graceperiod);
            LocalTime endInDockInterval = shiftstart.plusMinutes(dockPenalty);
            LocalTime outDockInterval = shiftstop.minusMinutes(graceperiod);
            LocalTime endOutDockInterval = shiftstop.minusMinutes(dockPenalty);
            
            //This if statement checks if the punch was entered outside the grace period for a clock in.
            
            if (time.isAfter(inDockInterval) && time.isBefore(endInDockInterval)) {
                adjuster = shiftstart.plusMinutes(dockPenalty);
                adjustmenttype = "Shift Dock";
            }
            
            //This if statement checks if the punch was entered outside the grace period for clock out.
            
            else if (time.isBefore(outDockInterval) && time.isAfter(endOutDockInterval.minusSeconds(1))) {
                adjuster = shiftstop.minusMinutes(dockPenalty);
                adjustmenttype = "Shift Dock";
            }
            
            if (adjustmenttype == null) {adjuster = intervalRound(adjuster, s);}
            
            adjustedTS = timestamp;
            
            adjustedTS = adjustedTS.withHour(adjuster.getHour());
            adjustedTS = adjustedTS.withMinute(adjuster.getMinute());
            adjustedTS = adjustedTS.withSecond(adjuster.getSecond());
            
        }
        
        else {
            
              adjuster = intervalRound(adjuster, s);
              adjustedTS = adjustedTS.withHour(adjuster.getHour());
              adjustedTS = adjustedTS.withMinute(adjuster.getMinute());
              adjustedTS = adjustedTS.withSecond(adjuster.getSecond());
                
        }
        
    }
    
    private LocalTime intervalRound(LocalTime adjuster, Shift s) {
                
                int intervalRound = s.getRoundinterval();
                adjustmenttype = "Interval Round";
                int minute = timestamp.getMinute();
                int adjustedminute;
                             
                //Interval Round Rule
                
                if (minute % intervalRound !=0) {
                    
                    if ((minute % intervalRound) < (intervalRound/2)) {
                        adjustedminute = (Math.round(minute/intervalRound) * intervalRound);
                    }
                    
                    else {
                        adjustedminute = (Math.round(minute/intervalRound) * intervalRound) + intervalRound;
                    }
                    
                    if (adjustedminute != 60) {
                        adjuster = timestamp.toLocalTime().withMinute(adjustedminute); 
                    }
                    
                    else {
                        adjuster = timestamp.toLocalTime().plusHours(1).withMinute(0);
                    }
         
                    adjuster = adjuster.withSecond(0);
                    
                }
                
                return adjuster;
                
    }
    
    @Override
    public String toString() {
        return printOriginal();
    }
    
    public String printOriginal() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("#").append(badgeid).append(" ");
        sb.append(eventtypeid);
        sb.append(": ").append(timestamp.getDayOfWeek().toString().substring(0, 3)).append(" ");    // substring is used to shorten day of week string (e.g. "THURSDAY" -> "THU")
        sb.append(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(timestamp));    // format timestamp properly for output
        
        return sb.toString();
    }
    public String printAdjusted() {
        StringBuilder sb = new StringBuilder();
        
        sb.append("#").append(badgeid).append(" ");
        sb.append(eventtypeid);
        sb.append(": ").append(adjustedTS.getDayOfWeek().toString().substring(0, 3)).append(" ");    // substring is used to shorten day of week string (e.g. "THURSDAY" -> "THU")
        sb.append(DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss").format(adjustedTS));    // format timestamp properly for output
        sb.append(" (").append(adjustmenttype).append(")");
        
        return sb.toString();
    }

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public PunchType getEventtypeid() {
        return eventtypeid;
    }

    public String getAdjustmenttype() {
        return adjustmenttype;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public LocalDateTime getAdjustedTS() {
        return adjustedTS;
    }

    public Badge getBadge() {
        return badge;
    }
    
}