import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileController {

    Calendar calendar = Calendar.getInstance();

    public FileController() {
    }

    public void readFile() {
        calendar.setEvents(readFileToArray());
    }

    public List<Event> readFileToArray() {
        try {
            System.out.println("Enter the name of the text file you want to read from (without .txt at the end and without any dots)");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            while (name.indexOf('.') != -1) {
                System.out.println("No dots/file types allowed in the name!");
                name = scanner.nextLine();
            }

            File myObj = new File(name + ".txt");
            Scanner myReader = new Scanner(myObj);
            List<Event> events = new ArrayList<>();
            while (myReader.hasNextLine()) {
                Event e = new Event("", Month.JANUARY, 1, 0, 0, "");

                String line = myReader.nextLine();
                String[] data = line.split("-");
                e.name = data[0];
                e.desc = data[2];
                data = data[1].split(" ");
                int year = Integer.parseInt(data[0]); //year
                Month month = Month.valueOf(data[1]); // month
                int day = Integer.parseInt(data[2]); //day

                e.chosenDay = LocalDate.of(year, month, day);

                e.startTime = LocalTime.parse(data[3]);
                e.endTime = LocalTime.parse(data[4]);

                events.add(e);
            }
            myReader.close();
            if (events.isEmpty()) {
                System.out.println("File was empty, calendar has not been changed!");
            }
            System.out.println("Successfully read from the file.");
            return events;
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
        }
        return null;
    }

    public void writeToTxt() {
        if (calendar.getEvents().isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("You are about to write an empty calendar to a file, are you sure you want to continue?" +
                    "\n Write 'CONTINUE' to go on. Write anything else to go back.");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("continue"))
                return;
        }
        try {
            System.out.println("Enter the name of the text file you want to write to (without .txt at the end and without any dots)");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.nextLine();
            while (path.indexOf('.') != -1) {
                System.out.println("No dots/file types allowed in the name!");
                path = scanner.nextLine();
            }

            File wFile = new File(path + ".txt");
            FileWriter myWriter = new FileWriter(wFile);

            //each event will be written on 1 row, every next element is spaced out, there's a - after the name and before the descriprion
            for (Event e : calendar.getEvents()) {
                myWriter.write(e.name + "-");
                myWriter.write(e.getYear() + " ");
                myWriter.write(e.getMonth() + " ");
                myWriter.write(e.getDay() + " ");
                myWriter.write(e.startTime + " ");
                myWriter.write(e.endTime + "-");
                myWriter.write(e.desc);
                myWriter.write("\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            System.out.println(e.getMessage());
        }
    }

    public void writeToFile() {
        if (calendar.getEvents().isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("You are about to write an empty calendar to a file, are you sure you want to continue?" +
                    "\n Write 'CONTINUE' to go on. Write anything else to go back.");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("CONTINUE"))
                return;
        }
        try {
            System.out.println("Enter the name of the text file you want to write to, include the file type(Example -> .txt | .dat)[File name cannot contain 0]");
            System.out.println("WARNING: This function will create a file with whatever extension you choose for it, meaning the file you create might not be compatible with the information!");
            System.out.println("If you wish to return to the main menu at any time, please type '0' ");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.nextLine();
            if (path.equals("0")) {
                UI ui = new UI();
                ui.mainMenu();
            }
            while (path.indexOf('.') == -1) {
                if (path.equals("0")) {
                    UI ui = new UI();
                    ui.mainMenu();
                }
                System.out.println("No file extension at the end!");
                System.out.println("Enter the name of the text file you want to write to, include the file type( Example -> .txt | .dat)");
                path = scanner.nextLine();
            }

            File wFile = new File(path);
            FileWriter myWriter = new FileWriter(wFile);

            //each event will be written on 1 row, every next element is spaced out, there's a - after the name and before the descriprion
            for (Event e : calendar.getEvents()) {
                myWriter.write(e.name + "-");
                myWriter.write(e.getYear() + " ");
                myWriter.write(e.getMonth() + " ");
                myWriter.write(e.getDay() + " ");
                myWriter.write(e.startTime + " ");
                myWriter.write(e.endTime + "-");
                myWriter.write(e.desc);
                myWriter.write("\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            System.out.println(e.getMessage());
        }
    }

    public void mergeData() {
        List<Event> newEvents = readFileToArray();

        for (Event e : newEvents) {
            System.out.println("Next event: \n");
            System.out.println(e.ShowEvent());
            System.out.println("Trying to add next event... \n");
            List<Event> overlaps = calendar.findOverlap(e);
            if (!overlaps.isEmpty()) {
                boolean repeat = false;
                System.out.println("Old event/s:\n");
                for (Event ev : overlaps)
                    System.out.println(ev.ShowEvent());

                System.out.println("New event:\n");
                System.out.println(e.ShowEvent());

                do {
                    System.out.println("Found overlapping event/s!\n");
                    System.out.println("Please select the next action:\n");
                    System.out.println("0: Keep the old event\n");
                    System.out.println("1: Keep the new event\n");
                    System.out.println("2: Change the old event\n");
                    System.out.println("3: Change the new event\n");
                    System.out.println("-1: Return to menu\n");

                    Scanner scanner = new Scanner(System.in);
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }

                    int action = scanner.nextInt();

                    switch (action) {
                        case 0: //Move to next event
                            continue;
                        case 1: //Replace the old event/s
                            for (Event ev : overlaps)
                                calendar.removeEvent(ev);
                            calendar.book(e);
                            break;
                        case 2: //Move the old event/s
                            for (Event ev : overlaps)
                                calendar.change(ev);
                            repeat = !calendar.book(e);
                            if (repeat)
                                System.out.println("You did not clear the schedule around the new event!");
                            break;
                        case 3://Move the new event
                            e.change();
                            repeat = !calendar.book(e);
                            if (repeat)
                                System.out.println("You did not choose a free time for the new event!");
                            break;
                        case -1:
                            return;
                        default:
                            System.out.println("Number out of range! Please choose a valid option.");
                    }
                } while (repeat);
            } else {
                calendar.book(e);
                System.out.println("Successfully added!\n");
            }
        }

    }
}
