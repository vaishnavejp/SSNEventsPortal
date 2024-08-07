import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

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
    private java.sql.Date date;
    private String infrastructureID;
    public int[] availability = new int[4];

    public Slot(java.sql.Date date, String infrastructureID){
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
    public java.sql.Date bookingDate;
    public Slot timeSlot;
    public String status;
    public String bookingName;

    public Booking(String bookingID, String OID, String inf_id, java.sql.Date date, String slot, String name, String status){
        this.bookingID = bookingID;
        this.organizerID = OID;
        this.infrastructureID = inf_id;
        this.bookingDate = date;
        Slot slottemp = new Slot(date, inf_id);
        slottemp.fillSlot(slot, bookingID);
        this.timeSlot = slottemp;
        this.status = status;
        this.bookingName = name;
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

class BookingHistory{
    private ArrayList<Booking> bookings = new ArrayList<Booking>();
    private int userID;
    // ArrayList<Booking>

    public ArrayList<Booking> getBookingHistory(int userID){
        this.userID = userID;
        final String DB_URL = "jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";
        final String QUERY = "SELECT * FROM Booking WHERE oid="+userID;

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
                String status = rs.getString("status");
                String name = rs.getString("name");

                Booking b = new Booking(bid, oid, inf_id, date, slot, name, status);
                bookings.add(b);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
        // return null;
    }
}
 
public class ViewBookingHistory extends HttpServlet{ 
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        int userID = (Integer)session.getAttribute("userID");
       
        out.println("<html><head><title>Booking history</title>"+
        "<link rel='stylesheet' href='nav.css'>"+
        "<link rel='stylesheet' href='table.css'>"+
        "<link rel='stylesheet' href='style.css'>"+
        "<style>"+
        "body{"+
        "font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"+
        "text-align: center;"+
        "color: #32303A;"+
        "}"+
        ".rej{"+
        "color: red;}"+
        ".acc{ color: green } </style> </head>"+
        "<body><nav class='navTab'>"+
        "<p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p>"+
        "<div class='rightHalf'>"+
        "<a href='http://localhost:8080/MiniProject/Dashboard' class='navTabText'>Dashboard</a>"+
        "<a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>Past Events</a>"+
        "<a href='Upload.html' class='navTabText'>Upload</a>"+
                "<div class='dropdown'>"+
                    "<a class='navTabText' style='font-weight:bold'>Infrastructure</a>"+
                    "<div class='dropdown-content'>"+
                        "<a href='checkavailability.html'>Book</a>"+
                         "<a href='http://localhost:8080/MiniProject/BookingHistory' style='font-weight:bold'>History</a>"+
                    "</div>"+
                "</div>"+
                "<span class='dot'></span>"+
            "</div>"+
        "</nav><br><br><br><br><h2>Booking history</h1><br>"+
        "<div id='outer'>"+
        "<table id='booking-history'>"+
        "<tr class='titles'>"+
        "<th class='big_col' style='border-radius: 25px 0px 0px 0px;'>Infrastructure</th>"+
        "<th class='small_col'>Date</th>"+
        "<th class='big_col'>Slot</th>"+
        "<th class='small_col' style='border-radius: 0px 25px 0px 0px;'>Status</th></tr>");

        BookingHistory b = new BookingHistory();
        
        ArrayList<Booking> history = b.getBookingHistory(userID);
        // out.print(b.getBookingHistory(userID));
        
        
        for(int i=0; i<history.size(); i++){
            out.print("<tr><td>" + Infrastructure.values()[Integer.valueOf(history.get(i).infrastructureID)-1] + "</td>");
            out.print("<td>" + history.get(i).bookingDate + "</td>");
            out.print("<td>" + history.get(i).timeSlot.getSlotTime(history.get(i).bookingID)+ "</td><td>" + history.get(i).status + "</td></tr>");
        }

        out.println("</table>");

        if(history.size()==0) out.println("<h2 style='padding:15px;'> No recent bookings </h2>");
         
        out.println("</div><br><br><a class='submit' href='checkavailability.html'>New booking</a><br><br></body></html>");
   }
} 