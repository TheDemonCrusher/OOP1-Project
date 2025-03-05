import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.FileNotFoundException;
import java.io.IOException;

public class FileController {

    Calendar calendar = Calendar.getInstance();

    public FileController(){}

    public void readFile()
    {
        try {
            System.out.println("Enter the name of the text file you want to read from (without .txt at the end and without any dots)");
            Scanner scanner = new Scanner(System.in);
            String name = scanner.nextLine();
            while(name.indexOf('.') != -1){
                System.out.println("No dots/file types allowed in the name!");
                name = scanner.nextLine();
            }

            File myObj = new File(name + ".txt");
            Scanner myReader = new Scanner(myObj);
            List<Event> events = new ArrayList<>();
            while (myReader.hasNextLine()) {
                Event e = new Event("", Month.JAN, 0, 0, 0, "");

                String line = myReader.nextLine();
                String[] data = line.split("-");
                e.name = data[0];
                e.desc = data[2];
                data = data[1].split(" ");
                e.month = Month.valueOf(data[0]);
                e.day = Integer.parseInt(data[1]);
                e.start = Integer.parseInt(data[2]);
                e.end = Integer.parseInt(data[3]);

                events.add(e);
            }
            myReader.close();
            if(events.isEmpty()) {
                System.out.println("File was empty, calendar has not been changed!");
                UI ui = new UI();
                ui.mainMenu();
            }
            calendar.setEvents(events);
            System.out.println("Successfully read from the file.");
            UI ui = new UI();
            ui.mainMenu();
        } catch (FileNotFoundException e) {
            System.out.println("File doesn't exist!");
            UI ui = new UI();
            ui.mainMenu();
        }
    }

    public void writeToTxt()
    {
        try {
            System.out.println("Enter the name of the text file you want to write to (without .txt at the end and without any dots)");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.nextLine();
            while(path.indexOf('.') != -1){
                System.out.println("No dots/file types allowed in the name!");
                path = scanner.nextLine();
            }

            File wFile = new File(path +".txt");
            FileWriter myWriter = new FileWriter(wFile);

            //each event will be written on 1 row, every next element is spaced out, there's a - after the name and before the descriprion
            for(Event e : calendar.events)
            {
                myWriter.write(e.name + "-");
                myWriter.write(e.month + " ");
                myWriter.write(e.day + " ");
                myWriter.write(e.start + " ");
                myWriter.write(e.end + "-");
                myWriter.write(e.desc);
                myWriter.write("\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            UI ui = new UI();
            ui.mainMenu();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            System.out.println(e.getMessage());
            UI ui = new UI();
            ui.mainMenu();
        }
    }

    public void writeToFile()
    {
        try {
            System.out.println("Enter the name of the text file you want to write to, include the file type(Example -> .txt | .dat)[File name cannot contain 0]");
            System.out.println("WARNING: This function will create a file with whatever extension you choose for it, meaning the file you create might not be compatible with the information!");
            System.out.println("If you wish to return to the main menu at any time, please type '0' ");
            Scanner scanner = new Scanner(System.in);
            String path = scanner.nextLine();
            if(path.equals("0"))
            {
                UI ui = new UI();
                ui.mainMenu();
            }
            while(path.indexOf('.') == -1){
                if(path.equals("0"))
                {
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
            for(Event e : calendar.events)
            {
                myWriter.write(e.name + "-");
                myWriter.write(e.month + " ");
                myWriter.write(e.day + " ");
                myWriter.write(e.start + " ");
                myWriter.write(e.end + "-");
                myWriter.write(e.desc);
                myWriter.write("\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");
            UI ui = new UI();
            ui.mainMenu();

        } catch (IOException e) {
            System.out.println("An error occurred.");
            System.out.println(e.getMessage());
            UI ui = new UI();
            ui.mainMenu();
        }
    }

    public void mergeData(){}
}
