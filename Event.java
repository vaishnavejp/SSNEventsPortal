import java.sql.Date;
import java.sql.Time;

// public class EventMedia{
//     //Use API to represent video
// }
// public class SeminarEvent{
//     EventMedia media=new EventMedia();
// }
public class Event {
    int eventID;
    String Description;
    //Date
    //Time
    String eventHost; //Organizer
    long phone;
    int time; //Slot

    int getEventID(){
        return this.eventID;
    }
    String getEventDetails(){
        return this.Description;
    }
    String getEventHead(){
        return this.eventHost;
    }
    void addToDate(){
        //code to add an event to a specified date
    };

}
public class Upload{
    Event event;
    Date date;
    Time time;

    void addEventDetails()
    {

    }
    void editEventDetails()
    {

    }
    void deleteEvent()
    {
        
    }
}
