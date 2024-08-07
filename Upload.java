import java.io.*;
import java.util.*;
import java.time.*;
import java.sql.Time;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.ResultSet;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.servlet.*;
import javax.servlet.http.*;
import java.text.SimpleDateFormat;

@MultipartConfig(maxFileSize = 16177215)
// upload file's size up to 16MB

public class Upload extends HttpServlet{

    private static final int BUFFER_SIZE = 4096;
    private String dbURL = "jdbc:mysql://localhost:3306/SSNEventPortal";
    private String dbUser = "root";
    private String dbPass = "Mysql*07";

    private Connection getConnection() {
        Connection conn = null;
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            conn = DriverManager.getConnection(dbURL, dbUser, dbPass);
        } catch (Exception e) 
        {
            throw new RuntimeException("Failed to obtain database connection.", e);
        }
        return conn;
    }
    public void insertMedia(int eventID,String registerLink,InputStream inputStream,InputStream videoStream,String evType)
    {
        Connection conn = null; // connection to the database
        String message = null; // message will be sent back to client
        try 
        {
            conn = getConnection();

            if(evType.equals("Regular"))
            {
                String sql = "INSERT INTO EventMedia (picture,registerLink,eventID) values (?, ?, ?)";
                PreparedStatement pstmtSave = conn.prepareStatement(sql);
                if (inputStream != null) {

                    pstmtSave.setBlob(1, inputStream);
                    pstmtSave.setString(2, registerLink);
                    pstmtSave.setInt(3, eventID);
                }
                int row = pstmtSave.executeUpdate();
                pstmtSave.close();
            }
            else if(evType.equals("Seminar"))
            {

                String sql = "INSERT INTO EventMedia (picture,video,registerLink,eventID) values (?,?,?,?)";
                // out.println("WAW OVER HERE!2");
                PreparedStatement pstmtSave = conn.prepareStatement(sql);
                pstmtSave.setString(3, registerLink);
                // out.println("WAW OVER HERE3!");
                pstmtSave.setInt(4,eventID);
                // out.println("WAW OVER HERE!");
                if (inputStream != null) {

                    // out.println("DOING!1");
                    pstmtSave.setBlob(1, inputStream);
                }
                if(videoStream!=null)
                {
                    // out.println("DOING!2");
                    pstmtSave.setBlob(2, videoStream);
                }
                int row = pstmtSave.executeUpdate();
            }
        }
        catch (SQLException ex) {
                    ex.printStackTrace();
        } 
        finally 
        {
            if (conn != null) 
            {
                    // closes the database connection
                    try {
                            conn.close();
                    } catch (SQLException ex) 
                    {
                        //silent
                    }
                }
            
        }
        
    }
    public void addEventDetails(String name, java.sql.Date date, Time time, int orgID, String contact, String description, String evTyp, String orgComm )
    {
        Connection conn = null;
		try {

            conn = getConnection();
			PreparedStatement st = conn
                   .prepareStatement("insert into Events(name,date,time,orgID,contact,description,type,org_comm) values(?,?,?,?,?,?,?,?)");
            
            st.setString(1, name);
            st.setDate(2, date);
            st.setTime(3,time);
            st.setInt(4, orgID); //Organizer id is 16
            st.setString(5,contact);
            st.setString(6,description);
            st.setString(7,evTyp);
            st.setString(8,orgComm);
			st.executeUpdate(); //st.executeUpdate()

            st.close();
            conn.close();
        }
        catch(SQLException se){
            se.printStackTrace();
        } 
        catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public int getEventID(String eventName)
    {
        Connection conn = null;
		try {
            conn = getConnection();

            Statement stmt = conn.createStatement();
			String sql;
			sql = "SELECT EID FROM Events WHERE name = '"+eventName+"'";
			ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                int EID=rs.getInt("EID");
                rs.close();
                stmt.close();
                conn.close();
                return EID;
            }
            else{
                rs.close();
                stmt.close();
                conn.close();
                return -1;
            }
		} 
		catch(SQLException se) {
         se.printStackTrace();
		} 
		catch(Exception e) {
         e.printStackTrace();
		} 
		finally {
		}
        return -1;
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
    throws ServletException, IOException {

        try{
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String eventName= request.getParameter("eventName");

        String dateString = request.getParameter("eventDate");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date date = format.parse(dateString);
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        // out.println("Reached this far");

        String timeString=request.getParameter("eventTime");
        Time tme=Time.valueOf(timeString+":00");

        int orgID=17;//MODIFY THIS MODIFY THIS

        HttpSession session = request.getSession();
        orgID = (Integer)session.getAttribute("userID");
        
        String contact=request.getParameter("contact");
        String description= request.getParameter("description");
        String orgComm= request.getParameter("orgCom");


        String evType=request.getParameter("Etype");

        addEventDetails(eventName, sqlDate, tme, orgID, contact, description, evType, orgComm);

        String registerLink=request.getParameter("registerLink");
        int eventID=getEventID(eventName);

        InputStream inputStream = null; 
        InputStream videoStream = null;

        Part filePart = request.getPart("photo");
        if (filePart != null) {
            inputStream = filePart.getInputStream();
        }

        if(evType.equals("Seminar"))
        {
            Part videoPart = request.getPart("video");

            if (videoPart != null) {
                    videoStream = videoPart.getInputStream();
            }
            
        }
        
        insertMedia(eventID, registerLink, inputStream, videoStream, evType);

        RequestDispatcher ds = request.getRequestDispatcher("eventUploaded.html");
        ds.include(request, response);
    }
    catch (Exception e) {
        e.printStackTrace();
    } 
}   
}