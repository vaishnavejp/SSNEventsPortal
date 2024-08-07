import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
 
public class AcceptBooking extends HttpServlet{
    
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
    static final String USER = "root";
    static final String PASS = "Mysql*07";
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            PreparedStatement st = conn.prepareStatement("UPDATE Booking set status='Accepted' where bid=?");
            st.setInt(1, Integer.valueOf(request.getParameter("id")));
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
        "<a href='http://localhost:8080/MiniProject/PendingRequests' class='navTabText' style='font-weight:bold'>Requests</a>"+
        "<a href='http://localhost:8080/MiniProject/ViewUser' class='navTabText'>View Users</a>"+
        "<span class='dot'></span>"+
        "</div></nav><br><br><br><br>"+
        "<div class='outer'><h2>Booking Request Accepted</h2><br><br>"+
        "<a href='http://localhost:8080/MiniProject/PendingRequests' class='button'>View More Requests</a>"+
        "</div></body></html>");
   }
} 