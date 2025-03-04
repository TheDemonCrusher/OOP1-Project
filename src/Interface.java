import java.util.Scanner;

//The class responsible for all menu functions
public class Interface {

    private int action = 0;
    public Interface(){}

    public void mainMenu()
    {
        Scanner scanner = new Scanner(System.in);

        while(true) {
            System.out.println("Please choose an action.\n" +
                    "-------------------------\n" +
                    "1: File functions (Write to/Read from file | Open/Close file) \n" +
                    "2: Book/Remove/Find/Edit events \n" +
                    "3: Need help?");

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
            System.out.println("Please choose an action.\n" +
                    "-------------------------\n" +
                    "0: Return to the main menu \n" +
                    "1: Save the current callender to a .txt file (Write to/Read from file | Open/Close file) \n" +
                    "2: Read a new callender from a .txt file \n" +
                    "3: Save as a custom file format (May cause errors if the type is incopatible, use with caution) \n");

            while(!scanner.hasNextInt()){
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }

            action = scanner.nextInt();
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
            System.out.println("Please choose an action.\n" +
                    "-------------------------\n" +
                    "0: Return to main menu\n" +
                    "1: Add new event to the calendar  \n" +
                    "2: Remove an event\n" +
                    "3: Edit an event" +
                    "4: Mark a day as a holiday" +
                    "5: Search for events\n" +
                    "6: Search for free space in your schedule\n");

            while(!scanner.hasNextInt()){
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }

            action = scanner.nextInt();
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
