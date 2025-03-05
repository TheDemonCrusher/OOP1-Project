import java.util.ArrayList;
import java.util.List;
public class Calendar {

    public List<Event> events = new ArrayList<>();
    //The calendar is a singleton since we only have one active calendar in the app at once
    private static Calendar calendarInstance;

    private Calendar(){}

    public static Calendar getInstance()
    {
        if(calendarInstance == null)
            calendarInstance = new Calendar();
        return calendarInstance;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void sortByDate(){}

    public void addEvent(Event event)
    {
        events.add(event);
    }

    public String printEvents()
    {
        StringBuilder sb = new StringBuilder();
        for(Event e : events)
            sb.append("name = " + e.name + "\n");
        return sb.toString();
    }
}
