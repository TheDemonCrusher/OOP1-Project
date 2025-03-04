public class Calendar {

    private Event[] events;
    //The calendar is a singleton since we only have one active calendar in the app at once
    private static Calendar calendarInstance;

    private Calendar(){}

    public static Calendar getInstance()
    {
        if(calendarInstance == null)
            calendarInstance = new Calendar();
        return calendarInstance;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    public String printEvents()
    {
        StringBuilder sb = new StringBuilder();
        for(Event e : events)
            sb.append("name = " + e.name + "\n");
        return sb.toString();
    }
}
