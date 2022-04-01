package edu.jsu.mcis.cs310.tas_sp22;

public class Department {
    private int id, terminalid;
    private String description;
    
    public Department (int id, int terminalid, String description) {
        this.id = id;
        this.terminalid = terminalid;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getTerminalid() {
        return terminalid;
    }

    public String getDescription() {
        return description;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append('#').append(id).append(" (").append(description).append("): terminalid: ");
        sb.append(terminalid);
        
        return sb.toString();
    }
}
