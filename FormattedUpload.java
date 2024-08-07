import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import javax.servlet.http.Part;

public class UploadFull{

    public void uploadEventDetails(String uname, String pwd)
    {
        try {

			Class.forName(JDBC_DRIVER);
            String dateString = request.getParameter("eventDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            out.println("Reached this far");

            String timeString=request.getParameter("eventTime");
            Time tme=Time.valueOf(timeString+":00");

            String eventType=request.getParameter("Etype");
			// Open a connection
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
			// Execute SQL query 
			PreparedStatement st = conn
                   .prepareStatement("insert into Events(name,date,time,orgID,contact,description,mediaID,type) values(?,?,?,?,?,?,?,?)");
            
            st.setString(1, request.getParameter("eventName"));
            st.setDate(2, sqlDate);
            st.setTime(3,tme);
            st.setInt(4, 16); //Organizer id is 16
            st.setString(5, request.getParameter("contact"));
            st.setString(6, request.getParameter("description"));
			st.setInt(7,1);
            st.setString(8,eventType);
			st.executeUpdate(); //st.executeUpdate()
            
  
            // Close all the connections
            st.close();
            conn.close();
  
            // Get a writer pointer 
            // to display the successful result

            out.println("Reached this far2");
            
            out.println("<html><head><link href='styles.css' rel='stylesheet'><style></style></head>"+
            "<body><h1></h1><div class='inputBox'><h3>Record inserted successfully</h3>"+
                "</div><script src='actions.js'></script></body></html>");
        }
        catch (Exception e) {
            e.printStackTrace();
        } 

    }
}

public class Upload extends HttpServlet{
		// JDBC driver name and database URL

	  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
   
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";
		// Set response content type
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
        out.println("Reached this farsdf");

        String photo=request.getParameter("photo");
        out.println("photo"+photo);
        InputStream inputStream = null;
        Part filePart= request.getPart("photo");
        if (filePart != null) {
 
            // Prints out some information
            // for debugging
            System.out.println(filePart.getName());
            System.out.println(filePart.getContentType());
 
            // Obtains input stream of the upload file
            inputStream =filePart.getInputStream();

        }


		/*try {

			Class.forName(JDBC_DRIVER);
            String dateString = request.getParameter("eventDate");
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = format.parse(dateString);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            out.println("Reached this far");

            String timeString=request.getParameter("eventTime");
            Time tme=Time.valueOf(timeString+":00");

            String eventType=request.getParameter("Etype");
			// Open a connection
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
	
			// Execute SQL query 
			PreparedStatement st = conn
                   .prepareStatement("insert into Events(name,date,time,orgID,contact,description,mediaID,type) values(?,?,?,?,?,?,?,?)");
            
            st.setString(1, request.getParameter("eventName"));
            st.setDate(2, sqlDate);
            st.setTime(3,tme);
            st.setInt(4, 16); //Organizer id is 16
            st.setString(5, request.getParameter("contact"));
            st.setString(6, request.getParameter("description"));
			st.setInt(7,1);
            st.setString(8,eventType);
			st.executeUpdate(); //st.executeUpdate()
            
  
            // Close all the connections
            st.close();
            conn.close();
  
            // Get a writer pointer 
            // to display the successful result

            out.println("Reached this far2");
            
            out.println("<html><head><link href='styles.css' rel='stylesheet'><style></style></head>"+
            "<body><h1></h1><div class='inputBox'><h3>Record inserted successfully</h3>"+
                "</div><script src='actions.js'></script></body></html>");
        }
        catch (Exception e) {
            e.printStackTrace();
        } */
    }
} 