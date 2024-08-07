import java.io.*;
import java.rmi.server.UID;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

class Account
{
    String username;
    String password;
    String type;

    Account(String username,String password,String type)
    {
        this.username=username;
        this.password=password;
        this.type=type;
    }
    String getPassword()
    {
        return this.password;
    }
    String getType()
    {
        return this.type;
    }
}
class Login{
    String usernameEntered;
    String password;
    Login(String username,String password)
    {
        this.usernameEntered=username;
        this.password=password;
    }
    public Account verifyCredentials()
    {

        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";

		try {

			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Account WHERE username = '"+this.usernameEntered+"' and password= '"+this.password+"'";
			ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                String username=rs.getString("username");
                String password=rs.getString("password");
                String type=rs.getString("type");
                Account a=new Account(username,password,type);
                rs.close();
                stmt.close();
                conn.close();
                return a;
            }
            else{
                rs.close();
                stmt.close();
                conn.close();
                return null;
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
        return null;
    }
    public int getUserID(String username)
    {
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";
		try {

			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement stmt = conn.createStatement();
			String sql;
			sql = "SELECT UID FROM User WHERE username = '"+username+"'";
			ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()) {
                int userID=rs.getInt("UID");
                rs.close();
                stmt.close();
                conn.close();
                return userID;
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
}


public class AccountImpl extends HttpServlet{

	  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		String title = "Database Result";
		String docType =
         "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";


        String uname=request.getParameter("username");
        String pwd= request.getParameter("password");
        Login a=new Login(uname,pwd);
        Account acnt=a.verifyCredentials();
        

        if(acnt==null)
        {
            
            RequestDispatcher ds = request.getRequestDispatcher("login.html");
            ds.include(request, response);
            out.println("<br>Either username or password is invalid!");
            
            // response.sendRedirect("index.html");
        }
        else{
            int UID=a.getUserID(uname);
            HttpSession session=request.getSession();
            session.setAttribute("userID",UID);
            session.setAttribute("type",acnt.type);
            // out.println("Logged In");
            RequestDispatcher ds = request.getRequestDispatcher("Dashboard");
            ds.forward(request, response);
            // response.sendRedirect("http://localhost/MiniProject/Dashboard"); 
        }

   }    
} 


