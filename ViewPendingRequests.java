// package project;
import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.File;
import java.io.FileNotFoundException;
// import infras.Infrastructure;

enum Infrastructure
{
    MainAud("Main Auditorium"), MiniAud("Mini Auditorium"), ESH("Central Seminar Hall"), CSH("CSE Seminar Hall"), ISH("IT Seminar Hall");

    private final String toString;
    private Infrastructure(String toString) {
         this.toString = toString;
    }
    public String toString(){
        return toString;
    }
}

class Slot{
    private java.util.Date date;
    private String infrastructureID;
    public int[] availability = new int[4];

    public Slot(java.util.Date date, String infrastructureID){
        this.date = date;
        this.infrastructureID = infrastructureID;
        for(int i=0; i<4; i++){
            availability[i] = 0;
        }
    }

    public int[] getSlot(){
        return availability;
    }

    public int getIdx(String slotTime){
        if(slotTime.equals("8am-10am"))
            return 0;
        else if(slotTime.equals("10:30am-12:30pm"))
            return 1;
        else if(slotTime.equals("1pm-3pm"))
            return 2;
        else if(slotTime.equals("3:30pm-5:30pm"))
            return 3;
        return 0;
    }

    public String getSlotTime(String bid){
        int idx=-1;
        for(int i=0; i<4; i++)
            if(availability[i]==Integer.valueOf(bid)) idx=i;
        String slot;
       
        switch(idx){
            case 0: slot="8am-10am";
            break;
            case 1: slot="10:30am-12:30am";
            break;
            case 2: slot="1pm-3pm";
            break;
            case 3: slot="3:30pm-5:30pm";
            break;
            default: slot=null;
        }
        return slot;
    }

    public void freeSlot(String slotTime){
        availability[getIdx(slotTime)] = 0;
    }

    public void fillSlot(String slotTime, String bid){
        availability[getIdx(slotTime)] = Integer.valueOf(bid);
    }
}

class Booking{
    public String bookingID;
    public String organizerID;
    public String infrastructureID;
    public java.util.Date bookingDate;
    public Slot timeSlot;
    public String status;
    public String bookingName;

    Booking(String bookingID, String OID, String inf_id, java.util.Date date, String slot, String name, String status){
        this.bookingID = bookingID;
        organizerID = OID;
        infrastructureID = inf_id;
        bookingDate = date;
        Slot slottemp = new Slot(date, inf_id);
        slottemp.fillSlot(slot, bookingID);
        timeSlot = slottemp;
        this.status = status;
        bookingName = name;
    }

    public String getBooking(){
        return "Booking ID: "+bookingID+"\nOrganizer ID: "+organizerID+"\nInfrastructure ID: "+
        infrastructureID+"\nBooking Date: "+bookingDate+"\nTime slot: "+timeSlot;
    }

    public String getStatus(){
        return status;
    }

    public void setStatus(String newstatus){
        status = newstatus;
    }
}

class PendingBookingHistory{
    private ArrayList<Booking> bookings = new ArrayList<Booking>();

    public ArrayList<Booking> getPendingBookingHistory(){
        final String DB_URL = "jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";
        final String QUERY = "SELECT a.bid, a.oid, a.inf_id, a.date, a.slot, b.firstname, b.lastname FROM Booking AS a, User AS b WHERE a.status= 'pending' AND a.oid = b.uid";

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            
            while (rs.next()) {
                String bid = String.valueOf(rs.getInt("bid"));
                String oid = String.valueOf(rs.getInt("oid"));
                String inf_id = String.valueOf(rs.getInt("inf_id"));
                java.sql.Date date = rs.getDate("date");
                String slot = rs.getString("slot");
                String name = rs.getString("firstname")+" "+rs.getString("lastname");

                Booking b = new Booking(bid, oid, inf_id, date, slot, name, "pending");
                bookings.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return bookings;
    }
}
 
public class ViewPendingRequests extends HttpServlet{ 
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.print("<html><head><title>Booking history</title>"+
        "<link rel='stylesheet' href='nav.css'>"+
        "<link rel='stylesheet' href='table.css'>"+
        "<link rel='stylesheet' href='style.css'>"+
        "<style>body{font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"+
        "text-align: center;color: #32303A;}"+
        ".button{height: 40px;padding: 10px 40px;"+
        "border-radius: 25px;border: 1px #ced1dd solid;"+
        "background-color: #F5F8FF;color: #32303A;}"+
        ".acc{background-color: #b3aef1;}"+
        ".rej:hover{background-color: rgb(205, 61, 61);"+
        "color: #F5F8FF;}"+
        ".acc:hover{background-color: rgb(32, 151, 38);color: #F5F8FF;}"+
        "</style><script type='text/javascript'>"+
        "function callAcceptServlet(id) {"+
        "document.forms[0].action = 'Accept?id='+id;"+
        "document.forms[0].submit();}"+
        "function callRejectServlet(id) {"+
        "document.forms[0].action = 'Reject?id='+id;"+
        "document.forms[0].submit();}"+
        "</script></head>"+
        "<body><nav class='navTab'>"+
        "<p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p>"+
        "<div class='rightHalf'>"+
        "<a href='http://localhost:8080/MiniProject/Dashboard' class='navTabText'>Dashboard</a>"+
        "<a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>Past Events</a>"+
        "<a href='http://localhost:8080/MiniProject/PendingRequests' class='navTabText' style='font-weight:bold'>Requests</a>"+
        "<a href='http://localhost:8080/MiniProject/ViewUser' class='navTabText'>View Users</a>"+
        "<span class='dot'></span>"+
        "</div></nav><br><br><br><br>"+
        "<h2>Pending booking requests</h1><br>"+
        "<div id='outer'>"+
        "<table id='booking-history'>"+
        "<tr class='titles'>"+
        "<th style='border-radius: 25px 0px 0px 0px;'>Organizer</th>"+
        "<th>Infrastructure</th>"+
        "<th>Date</th><th>Slot</th>"+
        "<th class='big_col' style='border-radius: 0px 25px 0px 0px;'>Status</th></tr>");

        PendingBookingHistory b = new PendingBookingHistory();
        
        ArrayList<Booking> history = b.getPendingBookingHistory();
        // out.print(history);
        
        for(int i=0; i<history.size(); i++){
            out.print("<tr><td>" + history.get(i).bookingName + "</td>");
            out.print("<td>" + Infrastructure.values()[Integer.valueOf(history.get(i).infrastructureID)-1] + "</td>");
            out.print("<td>" + history.get(i).bookingDate + "</td>");
            out.print("<td>" + history.get(i).timeSlot.getSlotTime(history.get(i).bookingID)+ "</td>");
            out.print("<td><form action='Accept' style='float:left'><input type='hidden' name='id' value='"+ history.get(i).bookingID+"'><input type='submit' class='acc button' value='Accept'></form>");
            out.print("<form action='Reject'><input type='hidden' name='id' value='"+ history.get(i).bookingID+"'><input type='submit' class='rej button' value='Reject'></form>");

            // out.print("<button class='rej' onclick='callRejectServlet("+ history.get(i).bookingID+ ",)'>Reject</button></form></td></tr>");
        }

        out.println("</table>");
        if(history.size()==0) out.println("<h2 style='padding:15px;'> No recent bookings </h2>");
        out.println("</div><br><br></body></html>");
   }
} 