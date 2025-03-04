import java.util.Scanner;

//The class responsible for all menu functions
public class Interface {

    private int action = 0;
    public Interface(){}

    public void mainMenu()
    {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("""
                    Please choose an action.
                    -------------------------
                    1: File functions (Write to/Read from file | Open/Close file)\s
                    2: Book/Remove/Find/Edit events\s
                    3: Need help?""");

            while(!scanner.hasNextInt()){
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }

            action = scanner.nextInt();
            switch (action) {
                case 0: //Exit when user input is = 0
                    System.exit(0);
                case 1: //Basic file actions - save/save as
                    fileMenu();
                    break;
                case 2: //Modifying data - book/remove/find
                    bookingsMenu();
                    break;
                case 3://Info for all commands
                    help();
                    break;
                default:
                    System.out.println("Number out of range! Please choose a valid option.");
            }
        }
    }

    public void fileMenu()
    {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("""
                    Please choose an action.
                    -------------------------
                    0: Return to the main menu\s
                    1: Save the current callender to a .txt file (Write to/Read from file | Open/Close file)\s
                    2: Read a new callender from a .txt file\s
                    3: Save as a custom file format (May cause errors if the type is incopatible, use with caution)\s
                    """);

            while(!scanner.hasNextInt()){
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }

            action = scanner.nextInt();
            //All actions must lead back to the main menu at the end!
            switch (action) {
                case 0: //Return to main menu
                    mainMenu();
                case 1: //Basic file actions - open/close/save/save as
                    break;
                case 2: //Basic data actions - book/remove/find
                    break;
                case 3://Data editing actions
                    break;
                default:
                    System.out.println("Number out of range! Please choose a valid option.");
            }
        }
    }

    public void bookingsMenu()
    {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("""
                    Please choose an action.
                    -------------------------
                    0: Return to main menu
                    1: Add new event to the calendar \s
                    2: Remove an event
                    3: Edit an event\
                    4: Mark a day as a holiday\
                    5: Search for events
                    6: Search for free space in your schedule
                    """);

            while(!scanner.hasNextInt()){
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }

            action = scanner.nextInt();
            //All actions must lead back to the main menu at the end!
            switch (action) {
                case 0: //Return to main menu
                    mainMenu();
                case 1: //Add a new event
                    break;
                case 2: //Remove an event
                    break;
                case 3: //Edit a single value of a chosen event
                    break;
                case 4: //Mark a day as a holiday
                    break;
                case 5: //Search for an event either by date or containing a specific string
                    break;
                case 6: //Search for an empty space in the schedule with more specific information
                    break;
                default:
                    System.out.println("Number out of range! Please choose a valid option.");
            }
        }
    }

    public void help()
    {
        System.out.println(); //Will contain all commands with short descriptions for them
    }
}
