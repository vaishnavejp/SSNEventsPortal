import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.SimpleDateFormat;

class DeleteEventDetails
{
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

	public void deleteEvent(String eventID)
	{
		Connection conn=null;	
		try {
			conn = getConnection();
			Statement stmt = conn.createStatement();
			String sql= "DELETE FROM Events where EID="+eventID;
            stmt.executeUpdate(sql); 
		} 
		catch(SQLException se) {
         se.printStackTrace();
		} 
		catch(Exception e) {
         e.printStackTrace();
		} 
		finally {
		}
	}

	public void deleteEventMedia(String eventID)
    {


        
        Connection conn = null;
        try 
        {
            conn = getConnection();
            Statement stmt = conn.createStatement();
            String sql = "DELETE FROM eventMedia WHERE eventID="+eventID;
            stmt.executeUpdate(sql);         
        } 
        catch (SQLException ex) 
        {
            ex.printStackTrace();

        } 
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally 
        {   if (conn != null) 
            {
                try {conn.close();} 
                catch (SQLException ex) {}

            }
			
        }
    }
}

public class DeleteServlet extends HttpServlet{
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
 
		HttpSession session = request.getSession();
		String type = (String)session.getAttribute("type");
        String eventID=(String)session.getAttribute("eventID");


		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		DeleteEventDetails e=new DeleteEventDetails();
        e.deleteEventMedia(eventID);
		e.deleteEvent(eventID);
    
        RequestDispatcher ds = request.getRequestDispatcher("eventDeleted.html");
        ds.include(request, response);
        
   }
} 
