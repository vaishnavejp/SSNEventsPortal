import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
// import .Infrastructure;

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
 
public class BookSlot extends HttpServlet{
    
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
    static final String USER = "root";
    static final String PASS = "Mysql*07";

    public void doPost(HttpServletRequest request, HttpServletResponse response) 
    throws IOException, ServletException 
    { 
            doGet(request, response); 
    } 
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            HttpSession session=request.getSession();
            int userID = (Integer)session.getAttribute("userID");

            PreparedStatement st = conn.prepareStatement("INSERT INTO  Booking VALUES(NULL,?,?,?,?,\"\",DEFAULT)");

            String Sid= request.getParameter("slotDetails");
            String[] arrOfStr = Sid.split("_");

            st.setInt(1, userID);
            st.setInt(2, Infrastructure.valueOf(arrOfStr[0]).ordinal()+1);
            st.setString(3, request.getParameter("date"));
            String slot;
            switch(Integer.valueOf(arrOfStr[1])){
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
            st.setString(4, slot);

            st.executeUpdate();
            
            st.close();
            conn.close();
        } 
        catch(SQLException se) {
         //Handle errors for JDBC
         se.printStackTrace();
		} 
		catch(Exception e) {
         //Handle errors for Class.forName
         e.printStackTrace();
		} 

        out.println("<html><head>"+
        "<title>Booking history</title>"+
        "<link rel='stylesheet' href='nav.css'>"+
        "<link rel='stylesheet' href='style.css'>"+
        "<style>body{"+
        "text-align: center;display: flex;height: 100vh;"+
        "justify-content: center;align-items: center;flex-direction: column;"+
        "font-family:'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;"+
        "background-color: #F5F8FF;}"+
        ".button{height: 40px;"+
        "padding: 10px 40px;border-radius: 25px;"+
        "background-color: #501eda;"+
        "color: #F5F8FF;text-decoration: none;}"+
        ".outer{width: 40%;margin: 0%;padding:50px;border-radius: 25px;"+
        "background-color: #b3aef1;box-shadow: #ced1dd 0px 4px 8px 0px;}"+
        "</style></head><body><nav class='navTab'>"+
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
        "<div class='outer'><h2>Booking Request Sent</h2><br><br>"+
        "<a href='http://localhost:8080/MiniProject/BookingHistory' class='button'>Booking History</a>"+
        "</div></body></html>");
    }
    
} 