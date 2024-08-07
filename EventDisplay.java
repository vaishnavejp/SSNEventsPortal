import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import java.text.SimpleDateFormat;

class EventMedia{
	int mediaID;
	String registerLink;
	String photopath;
    int eventID;
	EventMedia(int mediaID,String registerLink)
	{
		this.mediaID=mediaID;
		this.registerLink=registerLink;
        this.photopath="Photo.jpg";
	}
	String getRegLink()
	{
		return this.registerLink;
	}
	int mediaID()
	{
		return this.mediaID;
	}
    String getPhoto(){
        return this.photopath;
    }

    int getEventID()
    {
        return this.eventID;
    }
}

class Event{
    protected int EID;
	protected String name;
	protected java.sql.Date date;
	protected Time time;
	protected String orgComm;
	protected int orgID;
	protected String contact;
	protected String description;
	protected String type;
	protected EventMedia Media;
	Event(int EID,String name,java.sql.Date date,Time time,int orgID,String contact,String description,String type,String orgComm)
	{
		this.EID=EID;
		this.name=name;
		this.date=date;
		this.time=time;
		this.orgID=orgID;
		this.contact=contact;
		this.description=description;
		this.type=type;
		this.orgComm=orgComm;
	}
	void setMedia(EventMedia Media)
	{
		this.Media=Media;
	}
	int getID()
	{
		return EID;
	}

	String getName()
	{
		return this.name;
	}
	java.sql.Date getDate()
	{
		return this.date;
	}
	Time getTime()
	{
		return this.time;
	}
	int getOID()
	{
		return this.orgID;
	}
	String getContact()
	{
		return this.contact;
	}
	String getDescription()
	{
		return this.description;
	}
	String getOrgComm()
	{
		return this.orgComm;
	}
	String getType()
	{
		return this.type;
	}
    void addToDate(){
        //code to add an event to a specified date
    };

	String htmlString(String navString,int userID)
	{
		String printString="<!DOCTYPE html><html><link href='nav.css' rel='stylesheet'><link href='style1.css' rel='stylesheet'><head><style></style></head>"
		+"<body>"+navString+"<section><p class='headings'style='margin-top: 8%'>"+this.getName();

		if(this.getOID()==userID)
		{
			printString=printString+"<a href='areYouSure.html'> - </a><br>";
		}

		printString=printString+
		"</p><div class='EventDisplay'>"
		+"<div id='EventPicture' ><img width='580' height='540px' src='Photo.jpg' style='border-radius:25px'><img></div>"
		+"<div id='EventDetails'><p class='text24' style='margin-top: 0%;'>Organizing Committee</p>"
		+"<p class='text18'>"+this.getOrgComm()+"</p>"
		+"<p class='text24'>Date</p>"
		+"<p class='text18'>"+this.getDate()+"</p>"
		+ "<p class='text24'>Time</p>"
		+"<p class='text18'>"+this.getTime()+"</p>"
		+"<p class='text24'>Contact</p>"
		+"<p class='text18'>"+this.getContact()+"</p>"
		+"<p class='text24'>Description</p>"
		+"<p class='text18'>"+this.getDescription()+"</p><br>"
		+"<div style='display:flex; align-items:baseline;justify-content: center;'><button id='Register' class='submit'><a style='text-decoration:none; color:white' href='http://"+this.Media.getRegLink()+"'>Register</a></button><div></div>"
		+"</section></div></body></html>";

		return printString;
	}

}

class SeminarEvent extends Event{
	String videopath;
    SeminarEvent(Event e,EventMedia media)
    {
		super(e.EID,e.name,e.date,e.time,e.orgID,e.contact,e.description,e.type,e.orgComm);
		super.setMedia(media);
		this.videopath="Video.mp4";
		
    }
    String getVideo(){
        return this.videopath;
    }
	String htmlString(String navString,int userID)
	{
		String printString="<!DOCTYPE html><html><head><link href='nav.css' rel='stylesheet'><link href='style2.css' rel='stylesheet'></head><body>"+navString
		+"<section><p class='headings' style='margin-left: 3%; margin-top: 8%'>"+this.getName();

		if(this.getOID()==userID)
		{
			printString=printString+"<a href='areYouSure.html'> - </a><br>";
		}
		
		printString=printString+"</p><div class='EventDisplay'><div id='EventVideo'><video style='border-radius: 25px;' width='870' height='440' controls><source src='Video.mp4' type=video/mp4></video>"
		+"</div><div id='EventDetails'>"
		+"<p class='text24' style='margin-top: 0%;'>Organizing Committee</p>"
		+"<p class='text18'>"+this.getOrgComm()+"</p>"
		+"<p class='text24'>Date</p><p class='text18'>"+this.getDate()+"</p>"
		+"<p class='text24'>Time</p>"
		+"<p class='text18'>"+this.getTime()+"</p>"
		+"<p class='text24'>Contact</p>"
		+"<p class='text18'>"+this.getContact()+"</p>"
		+"<button id='Register' class='submit'><a style='text-decoration:none; color:white' href='http://"+this.Media.getRegLink()+"'>Register</a></button>"
		+"</div></div><div style='display: block;margin-left: 3%;'><p class='text24'>Description</p>"
		+"<p class='text18'>"+this.getDescription()+"</p> "
		+"</div></section></div></body></html>";

		return printString;
	}
}
class EventDetails
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

	public Event getEvent(String eventID)
	{
		final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";  
        final String DB_URL="jdbc:mysql://localhost:3306/SSNEventPortal";
        final String USER = "root";
        final String PASS = "Mysql*07";


		String title = "Database Result";
		String docType =
         "<!doctype html public \"-//w3c//dtd html 4.0 " + "transitional//en\">\n";
		try {

			Class.forName(JDBC_DRIVER);
			Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
			Statement stmt = conn.createStatement();
			String sql;
			sql = "SELECT * FROM Events where EID="+eventID;
			ResultSet rs = stmt.executeQuery(sql);

			while(rs.next()){

				int id  = rs.getInt("EID");
				String name=rs.getString("name");
				java.sql.Date date=rs.getDate("date");
				java.sql.Time time=rs.getTime("time");
				int orgID=rs.getInt("orgID");
				String contact=rs.getString("contact");
				String description=rs.getString("description");
				String type=rs.getString("type");
				String orgComm=rs.getString("org_comm");

				Event e=new Event(id,name,date,time,orgID,contact,description,type,orgComm);
				rs.close();
				stmt.close();
				conn.close();
				return e;
			}
			
			// Clean-up environment
			rs.close();
			stmt.close();
			conn.close();
			return null;
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

	public EventMedia getEventMedia(String eventID)
    {
        // String filepath="/Users/shruthidhanvanth/Desktop/Files/Photo.jpg";
        // String videopath="/Users/shruthidhanvanth/Desktop/Files/Video.mp4";

		String filepath="/Library/Tomcat/webapps/MiniProject/Photo.jpg";
        String videopath="/Library/Tomcat/webapps/MiniProject/Video.mp4";

		EventMedia evmd=null;

        final int BUFFER_SIZE = 4096;
        
        Connection conn = null;
        try 
        {
            conn = getConnection();
            InputStream inputStream1=null;
            OutputStream outputStream=null;
            String sql1 = "SELECT mediaID,picture,video,registerLink FROM eventMedia WHERE eventID="+eventID;
            PreparedStatement pstmtSelect = conn.prepareStatement(sql1);

            ResultSet result = pstmtSelect.executeQuery();
            if (result.next()) 
            {
                //Adding photos
				int mediaID=result.getInt("mediaID");
                String registerLink=result.getString("registerLink");
                if(registerLink!=null)
                {
                    // out.println(registerLink);
                }
                Blob blob = result.getBlob("picture");
                if(blob!=null)
                {
                    inputStream1 = blob.getBinaryStream();
                    outputStream = new FileOutputStream(filepath);
                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream1.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                    System.out.println("File saved");
					evmd=new EventMedia(mediaID, registerLink);
                }

                blob = result.getBlob("video");
                if(blob!=null)
                {
                    inputStream1 = blob.getBinaryStream();
                    outputStream = new FileOutputStream(videopath);
                    int bytesRead = -1;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    while ((bytesRead = inputStream1.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                    }
                }
            }
			
            inputStream1.close();
            outputStream.close();
			return evmd;
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
		return evmd;
    }
}

public class EventDisplay extends HttpServlet{

	String orgNavString= "<nav class='navTab'>"+
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
	"</nav>"; //Organiser

	String adminNavString="<nav class='navTab'>"+
	"<p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p>"+
	"<div class='rightHalf'>"+
	"<a href='http://localhost:8080/MiniProject/Dashboard' class='navTabText'>Dashboard</a>"+
	"<a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>Past Events</a>"+
	"<a href='http://localhost:8080/MiniProject/PendingRequests' class='navTabText' style='font-weight:bold'>Requests</a>"+
	"<a href='http://localhost:8080/MiniProject/ViewUser' class='navTabText'>View Users</a>"+
	"<span class='dot'></span>"+
	"</div></nav>";
	String studentNavString="<nav class='navTab'><p class='navTabText' style='font-size: 24px; font-weight: bold;'>SSN Events Portal</p><div class='rightHalf'><a href='http://localhost:8080/MiniProject/Dashboard' style='font-weight:bold' class='navTabText'>Dashboard</a><a href='http://localhost:8080/MiniProject/PastEvents' class='navTabText'>Past Events</a><span class='dot'></span></div></nav><br><br><br>";

	String navString="";
	  public void doGet(HttpServletRequest request, HttpServletResponse response)
      throws ServletException, IOException {
 
		HttpSession session = request.getSession();
		String type = (String)session.getAttribute("type");
		int userID=(Integer)session.getAttribute("userID");
		if(type.equals("admin")) {
			navString=adminNavString;
		}
		else if(type.equals("organiser")) {
			navString=orgNavString;
		}
		else if(type.equals("student")) {
			navString=studentNavString;
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();

		String eventID=request.getParameter("eventID");
		out.println(eventID);
		session.setAttribute("eventID",eventID);

		EventDetails e=new EventDetails();
		Event event=e.getEvent(eventID);

		if(event.getType().equals("Regular"))
		{

			EventMedia media=e.getEventMedia(eventID);
			event.setMedia(media);
			String output=event.htmlString(navString,userID);
			out.println(output);

		 }
		 else if(event.getType().equals("Seminar"))
		 {
			EventMedia media=e.getEventMedia(eventID);
			SeminarEvent semEvent= new SeminarEvent(event, media);
			String output=semEvent.htmlString(navString,userID);
			out.println(output);

		 }
		 


   }
} 