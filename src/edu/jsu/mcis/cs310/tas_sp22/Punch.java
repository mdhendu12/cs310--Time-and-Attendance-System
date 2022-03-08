package edu.jsu.mcis.cs310.tas_sp22;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

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

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public PunchType getPunchtype() {
        return eventtypeid;
    }

    public String getAdjustmenttype() {
        return adjustmenttype;
    }

    public String getBadgeid() {
        return badgeid;
    }

    public LocalDateTime getOriginalTimestamp() {
        return timestamp;
    }

    public LocalDateTime getAdjustedTS() {
        return adjustedTS;
    }

    public Badge getBadge() {
        return badge;
    }
    
}