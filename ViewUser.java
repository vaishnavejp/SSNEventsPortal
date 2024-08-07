import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class ViewUser extends HttpServlet{
    
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
    static final String USER = "root";
    static final String PASS = "Mysql*07";
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html> <html lang='en'> " + 
            "<meta charset='UTF-8'> " +
            "<head><title>ViewUser</title>" + 
            "<link rel='stylesheet' href='view.css'>" +
            "<link rel='stylesheet' href='nav.css'>" + 
            "<link rel='stylesheet' href='index.css'>" +
            "</head>" + 
            "<body>"
        );

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement stmt = conn.createStatement();
            String sql;
            // sql = "select * from events where date > '2023-05-06'";
            sql = "select * from user";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='' class='navTabText'>Dashboard</a><a href=''' class='navTabText'>Past Events</a><a href='' class='navTabText' style='font-weight:bold'>Requests</a><span class='dot'></span></div></nav><br><br><br>");
            out.println("<div class='bodyContent'>" + 
                "<div class='titles'>" + 
                "<h4>First Name</h4>" +
                "<h4>Last Name</h4>" +
                "<h4>DOB</h4> <br>" +
                "</div>");

            while (rs.next()) {
                String firstname = rs.getString("firstname");
                String lastname = rs.getString("lastname");
                java.util.Date dob = rs.getDate("dob");
                out.println("<div class='content'>" + 
                    "<div class='firstName'>" +
                    "<p>" + firstname + "</p> <br>" +
                    "</div>" +
                    "<div class='lastName'>" +
                    "<p>" + lastname + "</p> <br>" +
                    "</div>" +
                    "<div class='dob'>" +
                    "<p>" + dob +"</p> <br>" +
                    "</div>" +
                "</div>");

        }

            out.println("</body></html>");

            // Clean-up environment
            rs.close();
            stmt.close();
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
   }
} 