import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.Date;  
 
public class Calendar extends HttpServlet{
    
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
    static final String USER = "root";
    static final String PASS = "Mysql*07";
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        // String username = (String)session.getAttribute("username");

        out.println("<!DOCTYPE html> <html lang='en'> " + 
            "<meta charset='UTF-8'> " +
            "<head><title>Dashboard</title>" + 
            "<link rel='stylesheet' href='styles.css'>" +
            "<link rel='stylesheet' href='user.css'>" +
            "<link rel='stylesheet' href='calendar.css'>" +
                "<style>" + 
                    "div {" + 
                        "display: flex;" + 
                    "}" +
                    "img {" + 
                        "border-radius: 50px;" +
                    "}" +
                "</style>" + 
            "</head>" + 
            "<body>"
        );

        // if(username.isEquals("admin")) {
        //     out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='#' class='navTabText'>Dashboard</a><a href='#'' class='navTabText'>Past Events</a><a href='#' class='navTabText' style='font-weight:bold'>Requests</a><a href='#' class='navTabText'>View Users</a><span class='dot'></span></div></nav><br><br><br>");
        // }

        // else if(username.isEquals("organizer")) {
            // out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='#' class='navTabText'>Dashboard</a><a href='#'' class='navTabText'>Past Events</a><a href='#' class='navTabText' style='font-weight:bold'>Uploads</a><div class='dropdown'><a class='navTabText'>Infrastructure</a><div class='dropdown-content'><a href='#'>Book</a><a href='#'>History</a></div></div><span class='dot'></span></div></nav><br><br><br>");
        // }

        // else if(username.isEquals("student")) {
            out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='#' class='navTabText'>Dashboard</a><a href='#'' class='navTabText'>Past Events</a><span class='dot'></span></div></nav><br><br><br>");
        // }

        out.println("<div class='month'>" + 
        "<h1>May 2023 </h1> </div>" +
        "<ul class='weekdays'>" + 
          "<li>Mo</li>" +
          "<li>Tu</li>" +
          "<li>We</li>" +
          "<li>Th</li>" +
          "<li>Fr</li>" +
          "<li>Sa</li>" +
          "<li>Su</li>" +
        "</ul>" +
        
        "<ul class='days'>" +
        "<li><li>"
        );

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement stmt = conn.createStatement();
            String sql;
            sql = "select * from events";
            // sql = "select * from events";
            ResultSet rs = stmt.executeQuery(sql);
            HashMap<Integer, String> hash = new HashMap<Integer, String>();


            while (rs.next()) {
                String name = rs.getString("name");
                java.util.Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(date);
                int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

                hash.put(day, name);
            }

            for(int i=1;i<=31;i++) {
                out.println("<li> <p>" + i + "<p>");
                if(hash.containsKey(i)) {
                    out.println("<a href='http://localhost:8080/MiniProject/' style='color:#501EDA'>" + hash.get(i) + "</a>");
                }
                out.println("</li>");
            }

            out.println("</ul></div></body></html>");

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