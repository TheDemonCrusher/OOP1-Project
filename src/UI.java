import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

//The class responsible for all menu functions
public class UI {

    private String action;
    public UI(){}

    public void mainMenu()
    {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("""
                    ---------Main Menu---------\s
                    Please choose an action type. (type help to view commands)\s
                    -------------------------\s
                    Base commands: \s
                    files - File function access (Write to/Read from file | Open/Close file)\s
                    events - Event function access (Add/Remove/Find/Modify events)\s
                    help - Need help with commands?\s
                    Exit\s
                    """);

            action = scanner.nextLine();
            switch (action.toLowerCase()) {
                case "exit": //Exit when user input is = 0
                    System.exit(0);
                case "files": //Basic file actions - save/save as
                    fileMenu();
                    break;
                case "events": //Modifying data - book/remove/find
                    bookingsMenu();
                    break;
                case "help"://Info for all commands
                    help();
                    break;
                default:
                    System.out.println("Invalid command!");
            }
        }
    }

    public void fileMenu()
    {
        Scanner scanner = new Scanner(System.in);
        FileController fileController = new FileController();
        while(true) {
            System.out.println("""
                    Please choose an action.
                    -------------------------
                    menu -> Return to the main menu\s
                    write -> Save the current calendar to a .txt file\s
                    read -> Read a new calendar from a .txt file\s
                    saveas -> Save as a custom file format (May cause errors if the type is incompatible, use with caution)\s
                    merge -> Merge the calendar with a file\s
                    Exit
                    """);

            action = scanner.nextLine();
            //All actions must lead back to the main menu at the end!
            switch (action.toLowerCase()) {
                case "menu": //Return to main menu
                    return;
                case "write": //Write the calendar data to a text file ||
                    fileController.writeToTxt();
                    break;
                case "read": //Override the calendar with a text file
                    fileController.readFile();
                    break;
                case "saveas"://Save the calendar as a custom file
                    fileController.writeToFile();
                    break;
                case "merge"://Merge the data from a file with the current calendar data
                    fileController.mergeData();
                    break;
                case "exit":
                    System.exit(0);
                default:
                    System.out.println("Number out of range! Please choose a valid option.");
            }
        }
    }

    public void bookingsMenu()
    {
        Scanner scanner = new Scanner(System.in);
        Calendar calendar = Calendar.getInstance();
        String info;
        int choice = 0;
        List<Event> results = new ArrayList<>();
        while(true) {
            System.out.println("""
                    Please choose an action.
                    -------------------------
                    menu -> Return to main menu\s
                    book -> Add new event to the calendar \s
                    unbook -> Remove an event\s
                    change -> Edit an event\s
                    holiday -> Mark a day as a holiday\s
                    find -> Search for events\s
                    findslot -> Search for free space in your schedule\s
                    findslotwith -> Search for an empty space in this schedule and a file schedule\s
                    showall -> Show all events in the schedule\s
                    showbusy -> Show busy days in range\s
                    showdaily -> Show all events for the chosen day\s
                    Exit\s
                    """);

            action = scanner.nextLine();
            //All actions must lead back to the main menu at the end!
            switch (action.toLowerCase()) {
                case "menu": //Return to main menu
                    return;
                case "book": //Add a new event
                    if(calendar.book(calendar.createEvent()))
                        System.out.println("Successfully added event!");
                    else
                        System.out.println("Event could not be added!");
                    break;
                case "unbook": //Remove an event on given date & time
                    calendar.unbook();
                    break;
                case "change": //Edit a single value of a chosen event

                    calendar.printEvents();
                    System.out.println("Write the index of the event you'd like to change");
                    do{
                        while(!scanner.hasNextInt()){
                            System.out.println("Error, invalid number entered!");
                            scanner.nextLine();
                        }
                        choice = scanner.nextInt();
                    }while(choice < 0 || choice > calendar.getEvents().size() - 1);

                    calendar.change(calendar.getEvents().get(choice));
                    break;
                case "holiday": //Mark a day as a holiday
                    calendar.printEvents();
                    System.out.println("Write the index of the event you'd like to mark as a holiday");
                    do{
                        while(!scanner.hasNextInt()){
                            System.out.println("Error, invalid number entered!");
                            scanner.nextLine();
                        }
                        choice = scanner.nextInt();
                    }while(choice < 0 || choice > calendar.getEvents().size() - 1);
                    calendar.getEvents().get(choice).setHoliday(true);
                    break;
                case "find": //Search for an event either by date or containing a specific string
                    System.out.println("Enter a keyword from the name or description of the event: ");
                    info = scanner.nextLine();
                    System.out.println('\n');
                    calendar.findEvents(info);
                    break;
                case "findslot": //Search for an empty space in this schedule
                    results.clear();
                    results = calendar.findSlot();
                    if(!results.isEmpty())
                    {
                        System.out.println("The avaliable slots are: \n");
                        for(Event e : results)
                            e.ShowEvent();
                    }

                    break;
                case "findslotwith"://Search for an empty space in this schedule and a file schedule
                    results.clear();
                    results = calendar.findSlotWith();
                    if(!results.isEmpty())
                    {
                        System.out.println("These slots are both avaliable in the calendar and the file: \n");
                        for(Event e : results)
                            e.ShowEvent();
                    }
                    break;
                case "showall": //Show all events in the schedule
                    calendar.printEvents();
                    break;
                case "showbusy": //Show busy days in range
                    calendar.showBusyDays();
                    break;
                case "showdaily"://Show all events for the chosen day
                    calendar.agenda();
                    break;
                case "exit": //Exit when user input is = 0
                    System.exit(0);
                default:
                    System.out.println("Invalid command!");
            }
        }
    }

    public void help()
    {
        System.out.println("\n=== HELP MENU ===");
        System.out.println("Commands are organized by their respective menus.\n");

        System.out.println("=== MAIN MENU (Access all functions from here) ===");
        System.out.println("file -> File functions - Save/load calendar data");
        System.out.println("events -> Event management - Book/edit/remove events");
        System.out.println("help -> This help menu");
        System.out.println("exit -> Exit the application\n");

        System.out.println("=== FILE MENU (Press 1 in Main Menu) ===");
        System.out.println("write -> Save calendar - Save current calendar to .txt file | expects the name of the file without its extension");
        System.out.println("save -> Load calendar - Replace current calendar with file contents | expects the name of the file without its extension");
        System.out.println("saveas -> Save as custom format - Export to any file format (advanced) | lets the user choose the name and extension of the file");
        System.out.println("merge -> Merge calendars - Combine current calendar with file contents | merges calendars with chosen .txt file by name");
        System.out.println("menu -> Return to main menu\n");

        System.out.println("=== EVENT MENU (Press 2 in Main Menu) ===");
        System.out.println("book -> Book event - Schedule a new event | expects manual input for each value");
        System.out.println("unbook -> Remove event - Delete an existing event | expects manual input for the exact datetime of the event");
        System.out.println("change -> Edit event - Modify event details | user chooses what to change and what value to asign");
        System.out.println("holiday -> Mark holiday - Flag an event as a holiday | event is chosen by user");
        System.out.println("find -> Search events - Find events by keyword | expects keyword as input by user");
        System.out.println("findslot -> Find free slots - Check available meeting times | expects information about the slot");
        System.out.println("findslotwith -> Combined free slots - Find times free in both calendar and file | same as findslot but also searches through a file");
        System.out.println("showall -> Show all events - Display complete schedule | no input expected");
        System.out.println("showbusy -> Busy days - See which days have most events | expects range of time");
        System.out.println("showdaily -> Daily agenda - View schedule for specific day | expects a day as input");
        System.out.println("menu -> Return to main menu\n");

        System.out.println("=== USAGE TIPS ===");
        System.out.println("- Dates/times must be entered as numbers (e.g., month '6' for June)");
        System.out.println("- Free slot finder shows times between 8:00-17:00 on workdays only");
        System.out.println("- When merging calendars, you'll be prompted to resolve conflicts");
        System.out.println("- Holidays are marked separately from regular events\n");

        System.out.println("Press any key to return...");
        new Scanner(System.in).nextLine();
    }
}
