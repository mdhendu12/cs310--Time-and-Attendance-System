package edu.jsu.mcis.cs310.tas_sp22;
import java.sql.*;
import java.util.HashMap;
import java.time.*;
import java.util.ArrayList;

public class TASDatabase {
    
    private final Connection connection;
    
    public TASDatabase() {
        
        this("tasuser", "c310e1", "localhost");    // call to overload constructor
        
    }
    
    public TASDatabase(String username, String password, String address) {
        
        this.connection = openConnection(username, password, address);  // establish connection to sql server
        
    }
    
    public Punch getPunch(int punchID) {
        Punch punch = null;
        String query = "SELECT * FROM event e WHERE id=?";
        boolean hasresults;
        ResultSet resultset = null;
        ResultSetMetaData rsmd = null;
        HashMap<String, String> hm = new HashMap<String, String>();
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(query);
            pstmt.setInt(1, punchID);
            
            hasresults = pstmt.execute();
            if (hasresults) {
                resultset = pstmt.getResultSet();
                resultset.next();
                rsmd = resultset.getMetaData();
                for (int i = 1; i <= 5; i++) {
                    hm.put(rsmd.getColumnName(i), resultset.getString(i));  // key = table column header; value is row result
                }
                punch = new Punch(hm);  // new Punch object created with hashmap
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        
        return punch;
    }
    
    private Connection openConnection(String u, String p, String a) {
        
        Connection c = null;
        
        if (a.equals("") || u.equals("") || p.equals(""))
            
            System.err.println("*** ERROR: MUST SPECIFY ADDRESS/USERNAME/PASSWORD BEFORE OPENING DATABASE CONNECTION ***");
        
        else {

            try {

                String url = "jdbc:mysql://" + a + "/tas_sp22_v1?autoReconnect=true&useSSL=false&zeroDateTimeBehavior=EXCEPTION&serverTimezone=America/Chicago";

                c = DriverManager.getConnection(url, u, p);

            }
            catch (Exception e) { e.printStackTrace(); }
        
        }
        
        return c;
        
    }
    
    public void close() {
        
        try {
            
            this.connection.close();    // try to close connection
            
        }
        catch (Exception e) { e.printStackTrace(); }
        
    }
    
    public Badge getBadge(String id)
    {
        Badge outputBadge = null; 
        
        String description = null; 
        
        String query = null; 
        ResultSet resultset = null; 
        boolean hasresult;
        
        try 
        {
            if (connection.isValid(0))
            {
                query = "SELECT * FROM badge WHERE id=?"; 
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setString(1, id);
                hasresult = pstmt.execute(); 
                   
                if (hasresult) 
                {
                    resultset = pstmt.getResultSet(); 
                    resultset.first(); 
                    
                    id = resultset.getString("id"); 
                    description = resultset.getString("description"); 
                    
                    HashMap<String, String> params = new HashMap<>(); 
                    params.put ("id",id); 
                    params.put ("description", description);
                    
                    outputBadge = new Badge(params); 
                }
            }
        }
        catch (Exception e) { e.printStackTrace(); }
        return outputBadge; 
    }
   
    public Shift getShift(int id) {
        
        Shift shift = null;
        
        String description = null;
        int roundinterval, graceperiod, dockpenalty;
        LocalTime shiftstart, shiftstop, lunchstart, lunchstop = null;
        
        String query = null;
        ResultSet resultset = null;
        boolean hasresults;
        
        /* Queries the database, receives a resultset and organizes the data in the resultset into different hashmaps before creating a shift object */
        
        try {
            
            if ( connection.isValid(0) ) {
                
                query = "SELECT * FROM shift WHERE id=?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, id);
                hasresults = pstmt.execute();
                
                if ( hasresults ) {
               
                    resultset = pstmt.getResultSet();
                    resultset.first();
                    
                    id = resultset.getInt("id");
                    description = resultset.getString("description");
                    shiftstart = resultset.getTimestamp("shiftstart").toLocalDateTime().toLocalTime();
                    shiftstop = resultset.getTimestamp("shiftstop").toLocalDateTime().toLocalTime();
                    roundinterval = resultset.getInt("roundinterval");
                    graceperiod = resultset.getInt("graceperiod");
                    dockpenalty = resultset.getInt("dockpenalty");
                    lunchstart = resultset.getTimestamp("lunchstart").toLocalDateTime().toLocalTime();
                    lunchstop = resultset.getTimestamp("lunchstop").toLocalDateTime().toLocalTime();
                    
                    HashMap<String, Integer> integers = new HashMap<>();
                    integers.put("id", id);
                    integers.put("roundinterval", roundinterval);
                    integers.put("graceperiod", graceperiod);
                    integers.put("dockpenalty", dockpenalty);
                    
                    HashMap<String, LocalTime> localtimes = new HashMap<>();
                    localtimes.put("shiftstart", shiftstart);
                    localtimes.put("shiftstop", shiftstop);
                    localtimes.put("lunchstart", lunchstart);
                    localtimes.put("lunchstop", lunchstop);
                    
                    shift = new Shift(description, integers, localtimes);
                                        
                }
                
            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        return shift; 
    
    }
    
        public Shift getShift(Badge badgeID) {
        
        /* Declaration/initialization of variables, shift object, and resultset. */    
            
        Shift shift = null;
        String ID = badgeID.getId();
        int shiftID;
        String query = null;
        ResultSet resultset = null;
        PreparedStatement pstmt = null;
        boolean hasresults;
        
        /* Queries the database, receives the resultset, then calls getShift() (above) to create a shift object. */
        
        try{
            if (connection.isValid(0)) {
                
                query = "SELECT * FROM employee WHERE badgeid = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, ID);
                hasresults = pstmt.execute();
                
                if (hasresults) {
                    
                    resultset = pstmt.getResultSet();
                    
                    while(resultset.next()) {
                        
                        shiftID = resultset.getInt("shiftid");
                        shift = getShift(shiftID);
                        
                    }
                }  
            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        return shift;
        
    }
        
    public Employee getEmployee(int id) {
        
        Employee employee = null;
        
        String badgeid, firstname, lastname, middlename;
        LocalDate active, inactive; 
        int employeetypeid, departmentid, shiftid; 
        String query = null;
        ResultSet resultset = null;
        boolean hasresults;
        
        try {
            
            if ( connection.isValid(0) ) {
                
                query = "SELECT * FROM employee WHERE id=?";
                PreparedStatement pstmt = connection.prepareStatement(query);
                pstmt.setInt(1, id);
                hasresults = pstmt.execute();
                
                if ( hasresults ) {
               
                    resultset = pstmt.getResultSet();
                    resultset.first();
                    
                    id = resultset.getInt("id");
                    employeetypeid = resultset.getInt("employeetypeid"); 
                    departmentid = resultset.getInt("departmentid"); 
                    shiftid = resultset.getInt("shiftid"); 
                    active = resultset.getTimestamp("active").toLocalDateTime().toLocalDate();
                    badgeid = resultset.getString("badgeid"); 
                    firstname = resultset.getString("firstname"); 
                    lastname = resultset.getString("lastname"); 
                    middlename= resultset.getString("middlename");
                    try
                    {
                         inactive = resultset.getTimestamp("inactive").toLocalDateTime().toLocalDate();
                    }
                    catch(Exception e) {inactive = null;} 
                    
                    HashMap<String, String> strings = new HashMap<>(); 
                    strings.put("badgeid",badgeid);
                    strings.put("firstname", firstname); 
                    strings.put("lastname", lastname); 
                    strings.put("middlename", middlename); 
                    
                    HashMap<String, Integer> integers = new HashMap<>(); 
                    integers.put("employeetypeid", employeetypeid); 
                    integers.put("departmentid", departmentid); 
                    integers.put("shiftid", shiftid); 
                    integers.put("id", id);
                    
                    HashMap<String, LocalDate> time = new HashMap<>(); 
                    time.put("active", active);
                    time.put("inactive", inactive); 
                    
                    employee = new Employee(strings, time, integers);
                                        
                }
                
            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        return employee; 
    
    }
    
        public Employee getEmployee(Badge badgeID) {
        
        Employee employee = null;
        String ID = badgeID.getId();
        int employeeID;
        String query = null;
        ResultSet resultset = null;
        PreparedStatement pstmt = null;
        boolean hasresults;
        
        try{
            if (connection.isValid(0)) {
                
                query = "SELECT * FROM employee WHERE badgeid = ?";
                pstmt = connection.prepareStatement(query);
                pstmt.setString(1, ID);
                hasresults = pstmt.execute();
                
                if (hasresults) {
                    
                    resultset = pstmt.getResultSet();
                    
                    while(resultset.next()) {
                        
                        employeeID = resultset.getInt("id");
                        employee = getEmployee(employeeID);
                        
                    }
                }  
            }
        }
        
        catch (Exception e) { e.printStackTrace(); }
        return employee;
        
    }
        
        public ArrayList<Punch> getDailyPunchList(Badge badge, LocalDate tsdate) {
            
            ArrayList punchlist = new ArrayList<Punch>();
            Punch punch;
            String query = null;
            ResultSet resultset = null;
            PreparedStatement pstmt = null;
            
            boolean hasresults;
            String badgeID = badge.getId();
            int punchID;
            
            
            try{
                
                if (connection.isValid(0)) {
                    query = "SELECT *, DATE(`timestamp`) AS tsdate FROM event "
                            + "WHERE badgeid = ? HAVING tsdate = ? ORDER BY `timestamp`;";
                    pstmt = connection.prepareStatement(query);
                    pstmt.setString(1, badgeID);
                    pstmt.setString(2, tsdate.toString());
                    
                    hasresults = pstmt.execute();
                    
                    if (hasresults) {
                    
                        resultset = pstmt.getResultSet();

                        while(resultset.next()) {
                            
                            punchID = resultset.getInt("id");
                            punch = getPunch(punchID);
                            punchlist.add(punch);
                            
                        }
                    }

                }
            }
            
            catch (Exception e) { e.printStackTrace(); }
            
            return punchlist;
            
        }              
}