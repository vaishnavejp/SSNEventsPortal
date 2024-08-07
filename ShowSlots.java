import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;

// enum SlotTiming
// {
//     Morn("8am-10am"), ForeN("10:30am-12:30pm"), AfterN("1pm-3pm"), Eve("3:30pm-5:30pm");

//     private final String toString;
//     private SlotTiming(String toString) {
//          this.toString = toString;
//     }
//     public String toString(){
//         return toString;
//     }
// }

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
        return -1;
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

public class ShowSlots extends HttpServlet{
    private boolean[][] slotAvailability = new boolean[5][4];
    String selectedDate;
   
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
        for(int i=0; i<5; i++)
            for(int j=0; j<4; j++)
                slotAvailability[i][j] = true;

        final String DB_URL = "jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";
        final String QUERY = "SELECT * FROM Booking WHERE date='"+request.getParameter("date")+"'";
        selectedDate = request.getParameter("date");
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html><html>"+
        "<head><title>Book Infrastructure</title>"+
        "<link rel='stylesheet' href='nav.css'>"+
        "<link rel='stylesheet' href='table.css'>"+
        "<link rel='stylesheet' href='style.css'>"+
        "<link rel='stylesheet' href='book.css'>"+
        "<style>.button{text-decoration:none; height: 40px;padding: 10px 40px;"+
        "border-radius: 25px;border: 1px #ced1dd solid;"+
        "background-color: #501eda;color: #F5F8FF;text-decoration: none;}"+
        ".selected,.selected:hover{background-color: #501EDA; color: #F5F8FF}"+
        ".non-active:hover{background-color: #b3aef1;}"+
        "</style>"+
        "<script> var slotSelected = null;"+
        "function selectSlot(elem){"+
        "if(slotSelected) document.getElementById(slotSelected).classList.remove('selected');"+
        "elem.classList.add('selected');"+
        "slotSelected = elem.id; document.getElementById('slotDetails').value=slotSelected;}"+
        "</script>"+
        "</head><body>"+
        "<nav class='navTab'>"+
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
        "</nav><br><br><br><br>"+
        "<h2>Book Infrastructure</h2><div id='outer'>"+
        "<table>"+
        "<tr style='font-weight: bold;' class='titles'>"+
        "<td style='border-radius: 25px 0px 0px 0px;'>Venue</td><td>8:00am-10:00am</td><td>10:30am-12:30pm</td><td>1:00pm-3:00pm</td><td style='border-radius: 0px 25px 0px 0px;'>3:30pm-5:30pm</td></tr>");

        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(QUERY);
            
            while (rs.next()) {
                String bid = String.valueOf(rs.getInt("bid"));
                String oid = String.valueOf(rs.getInt("oid"));
                String inf_id = String.valueOf(rs.getInt("inf_id"));
                java.sql.Date Bdate = rs.getDate("date");
                String slot = rs.getString("slot");
                String name = rs.getString("name");
                String status = rs.getString("status");
                
                Booking b = new Booking(bid, oid, inf_id, Bdate, slot, name, status);

                slotAvailability[rs.getInt("inf_id")-1][b.timeSlot.getIdx(slot)] = false;
            }
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for(int i=0; i<5; i++){
            out.print("<tr>");
            out.print("<td><b>");
                String id;
                switch(i){
                    case 0: out.print("Main auditorium");
                    id = "MainAud";
                    break;
                    case 1: out.print("Mini auditorium");
                    id = "MiniAud";
                    break;
                    case 2: out.print("Central Seminar Hall");
                    id = "ESH";
                    break;
                    case 3: out.print("CSE Seminar Hall");
                    id = "CSH";
                    break;
                    case 4: out.print("IT Seminar Hall");
                    id = "ISH";
                    break;
                    default: id = "error";
                }
                out.print("</b></td>");

            for(int j=0; j<4; j++){
                out.print("<td><button id='"+id+"_"+j+"' class ='slot ");
                if(slotAvailability[i][j]==true) out.print("free' onclick=\"selectSlot(this)\">Free");
                    else out.print("booked'>Booked");
                out.print("</button></td>");
            }

        }

        out.print("</table></div><br><br><form action='BookSlot'>"+
        "<input type='hidden' name = 'date' value='"+selectedDate+"'>"+
        "<input type='hidden' name = 'slotDetails' id='slotDetails' value=''>"+
        "<input type='button' class='button non-active' value='Back' style='background-color: #F5F8FF; border:#501eda 1px solid;color:#32303A' onclick='history.back()'></button>"+
        "<input type='submit' class='button' value='Book Slot'></form>"+
        "<br><br></body></html>");
   }
} 