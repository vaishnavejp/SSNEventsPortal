import java.sql.*;
import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
 
public class Dashboard extends HttpServlet{
    
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
    static final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
    static final String USER = "root";
    static final String PASS = "Mysql*07";
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {
 
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        String type = (String)session.getAttribute("type");

        out.println("<!DOCTYPE html> <html lang='en'> " + 
            "<meta charset='UTF-8'> " +
            "<head><title>Dashboard</title>" + 
            "<link rel='stylesheet' href='styles.css'>" +
            "<link rel='stylesheet' href='user.css'>" +
            "<link rel='stylesheet' href='index.css'>" +
            "<link rel='stylesheet' href='admin.css'>" +
            "<link rel='stylesheet' href='nav.css'>" +
            "<link rel='stylesheet' href='calendar2.css'>" +
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

        if(type.equals("admin")) {
            // out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='#' class='navTabText'>Dashboard</a><a href='#'' class='navTabText'>Past Events</a><a href='#' class='navTabText' style='font-weight:bold'>Requests</a><a href='#' class='navTabText'>View Users</a><span class='dot'></span></div></nav><br><br><br>");
            out.println("<nav class='navTab'>"+
            "<p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p>"+
            "<div class='rightHalf'>"+
            "<a href='http://localhost:8080/MiniProject/Dashboard' style='font-weight:bold' class='navTabText'>Dashboard</a>"+
            "<a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>Past Events</a>"+
            "<a href='http://localhost:8080/MiniProject/PendingRequests' class='navTabText'>Requests</a>"+
            "<a href='http://localhost:8080/MiniProject/ViewUser' class='navTabText'>View Users</a>"+
            "<span class='dot'></span>"+
            "</div></nav>");
        }

        else if(type.equals("organiser")) {
           // out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='#' class='navTabText'>Dashboard</a><a href='#'' class='navTabText'>Past Events</a><a href='#' class='navTabText' style='font-weight:bold'>Uploads</a><div class='dropdown'><a class='navTabText'>Infrastructure</a><div class='dropdown-content'><a href='#'>Book</a><a href='#'>History</a></div></div><span class='dot'></span></div></nav><br><br><br>");
            out.println("<nav class='navTab'>"+
            "<p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p>"+
            "<div class='rightHalf'>"+
            "<a href='http://localhost:8080/MiniProject/Dashboard' style='font-weight:bold' class='navTabText'>Dashboard</a>"+
            "<a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>Past Events</a>"+
            "<a href='Upload.html' class='navTabText'>Upload</a>"+
                    "<div class='dropdown'>"+
                        "<a class='navTabText'>Infrastructure</a>"+
                        "<div class='dropdown-content'>"+
                            "<a href='checkavailability.html'>Book</a>"+
                             "<a href='http://localhost:8080/MiniProject/BookingHistory' style='font-weight:bold'>History</a>"+
                        "</div>"+
                    "</div>"+
                    "<span class='dot'></span>"+
                "</div>"+
            "</nav>");
        }

        else if(type.equals("student")) {
            out.println("<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'"+
            ">SSN Events Portal</p><div class='rightHalf'><a href='http://localhost:8080/MiniProject/Dashboard' style='font-weight:bold'"+
            " class='navTabText'>Dashboard</a><a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>"+
            "Past Events</a><span class='dot'></span></div></nav><br><br><br>");
        }

        try {
            Class.forName(JDBC_DRIVER);
            Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);

            Statement stmt = conn.createStatement();
            String sql;
            sql = "select * from events where date > '2023-05-09'";
            // sql = "select * from events";
            // sql = "select * from events";
            ResultSet rs = stmt.executeQuery(sql);
            out.println("<h1 style='color:#501EDA; margin-left:2%'> Dashboard </h1>");
            out.println("<div class='bodyContent style='display:grid; grid-template-column:1fr 1fr;'>" +
                "<div class=upcoming' style='display:block'>" );

            while (rs.next()) {
                String name = rs.getString("name");
                java.util.Date date = rs.getDate("date");
                Time time = rs.getTime("time");
                int eid = rs.getInt("eid");
                out.println("<a href='http://localhost:8080/MiniProject/ED?eventID=" + eid + "'>" + "<div class='box'>" +
                "<div style='display: grid; grid-template-columns: 1fr 2fr;'>" +
                    "<img class='rectangleSmall' src='https://www.decolore.net/wp-content/uploads/2019/06/geometric-patterns-cover.png'>" +
                    "<div class='description' style='display:block; margin-inline:20%; color:#501eda'>" +
                        "<h1> " + name + "</h1>" +
                        "<h4>" + date + "</h4>" +
                        "<h4>" + time + "</h4>" +
                    "</div>" +
                "</div>" +
                "</div> <br></a>"  
                );
            //     out.println("<div class='content'>" +
            //     "<a href='http://localhost:5500/MiniProject/ED/" + eid + "'>" +
            //         "<div class='texts'>" + 
            //             "<img src='https://www.decolore.net/wp-content/uploads/2019/06/geometric-patterns-cover.png'>" + 
            //             "<div style='display: block'>" + 
            //             "<h1> " + name + "</h1>" +
            //             "<h4> " + date + "</h4>" +
            //             "<h4> " + time + "</h4>" +
            //         "</div>" + 
            //     "</div>" +
            //     "</a>" + 
            //     "</div>"
            // );
        }

            out.println("</div>");

            // out.println("<div class='calendar'>" +
            // "<h3>Calendar</h3>" +
            // "<img class='rectangle' src='/assets/images/dummyC.jpg'>" +
            // "</div>" +
            // "</div>");
            
            out.println("<div style='display:grid; grid-template-column:1fr'>");

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

        String sql1;
        sql1 = "select * from events where date > '2023-05-06'";
        ResultSet rs1 = stmt.executeQuery(sql1);
        HashMap<Integer, String> hash = new HashMap<Integer, String>();


            while (rs1.next()) {
                String name = rs1.getString("name");
                java.util.Date date = rs1.getDate("date");
                Time time = rs1.getTime("time");
                java.util.Calendar calendar = java.util.Calendar.getInstance();
                calendar.setTime(date);
                int day = calendar.get(java.util.Calendar.DAY_OF_MONTH);

                hash.put(day, name);
            }

            for(int i=1;i<=31;i++) {
                if(hash.containsKey(i)) {
                    out.println("<li style='color:red;'> <p>" + i + "<p>");
                }
                else {
                    out.println("<li style='color:black;'> <p>" + i + "<p>");
                }
                out.println("</li>");
            }

            out.println("</ul></div></div></body></html>");

            rs1.close();


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