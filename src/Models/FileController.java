package Models;

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

/**
 * Отговаря за четене, запис и сливане на събития от и към файлове.
 * Използва вътрешен екземпляр от {@link Calendar} за управление на събитията.
 */
public class FileController {
    /**
     * Инстанция на класа {@link Calendar}, използвана за управление на събитията.
     * Инициализира се чрез фабричния метод {@code Models.Calendar.getInstance()}.
     */
    private Calendar calendar = Calendar.getInstance();

    private String lastSaved;

    /**
     * Конструктор по подразбиране на класа {@code Models.FileController}.
     * Не извършва специфична инициализация, освен създаване на обекта.
     */
    public FileController() {
    }

    public String getLastSaved() {
        return lastSaved;
    }

    /**
     * Чете събития от файл, избран от потребителя, и ги записва в календара.
     */
    public void readFile(String name) {
        calendar.setEvents(readFileToArray(name));
    }

    /**
     * Чете събития от текстов файл и ги преобразува в списък от {@link Event} обекти.
     *
     * <p>Файлът трябва да има формат: име-на-събитие - дата-във-формат (година месец ден часНачало часКрай) - описание.</p>
     *
     * @return Списък със събития или {@code null}, ако файлът не е намерен
     */
    public List<Event> readFileToArray(String name) {
        try {
            File myObj = new File(name + ".txt");
            Scanner myReader = new Scanner(myObj);
            List<Event> events = new ArrayList<>();
            while (myReader.hasNextLine()) {
                Event e = new Event("", Month.JANUARY, 1, 0, 0, "");

                String line = myReader.nextLine();
                String[] data = line.split("-");
                e.setName(data[0]);
                e.setDesc(data[2]);
                data = data[1].split(" ");
                int year = Integer.parseInt(data[0]); //year
                Month month = Month.valueOf(data[1]); // month
                int day = Integer.parseInt(data[2]); //day

                e.setDate(LocalDate.of(year, month, day));

                e.setStartTime(LocalTime.parse(data[3]));
                e.setEndTime(LocalTime.parse(data[4]));

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

    /**
     * Записва текущите събития в календара в текстов файл (.txt).
     * Ако календарът е празен, потребителят трябва да потвърди действието.
     *
     * <p>Формат на записа: всяко събитие е на нов ред с полетата, разделени с „-“.</p>
     */
    public void writeToTxt(String name) {
        if (calendar.getEvents().isEmpty() || calendar.getEvents() == null) {

            Scanner scanner = new Scanner(System.in);
            System.out.println("You are about to write an empty calendar to a file, are you sure you want to continue?" +
                    "\n Write 'CONTINUE' to go on. Write anything else to go back.");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("continue"))
                return;
            else
            {
                try{
                    File emptyFile = new File(name + ".txt");
                    FileWriter empty = new FileWriter(emptyFile);
                    System.out.println("File created successfully");
                    this.lastSaved = name;
                }catch(IOException e)
                {
                    System.out.println("Error when writting to file: \" + e.getMessage()");
                    System.out.println(e.getMessage());
                }
                return;
            }
        }
        try {
            File wFile = new File(name + ".txt");
            FileWriter myWriter = new FileWriter(wFile);
            this.lastSaved = name;

            //each event will be written on 1 row, every next element is spaced out, there's a - after the name and before the descriprion
            for (Event e : calendar.getEvents()) {
                myWriter.write(e.getName() + "-");
                myWriter.write(e.getYear() + " ");
                myWriter.write(e.getMonth() + " ");
                myWriter.write(e.getDay() + " ");
                myWriter.write(e.getStartTime() + " ");
                myWriter.write(e.getEndTime() + "-");
                myWriter.write(e.getDesc());
                myWriter.write("\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("Error when writting to file: \" + e.getMessage()");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Записва събитията от календара във файл с разширение, зададено от потребителя (напр. .txt, .dat).
     * Потребителят е предупреден, че неправилно разширение може да направи файла несъвместим.
     *
     * <p>Ако потребителят въведе „0“, се връща към главното меню.</p>
     */
    public void writeToFile(String name) {
        try {
            File wFile = new File(name);
            FileWriter myWriter = new FileWriter(wFile);

            //each event will be written on 1 row, every next element is spaced out, there's a - after the name and before the descriprion
            for (Event e : calendar.getEvents()) {
                myWriter.write(e.getName() + "-");
                myWriter.write(e.getYear() + " ");
                myWriter.write(e.getMonth() + " ");
                myWriter.write(e.getDay() + " ");
                myWriter.write(e.getStartTime() + " ");
                myWriter.write(e.getEndTime() + "-");
                myWriter.write(e.getDesc());
                myWriter.write("\n");
            }

            myWriter.close();
            System.out.println("Successfully wrote to the file.");

        } catch (IOException e) {
            System.out.println("An error occurred.");
            System.out.println(e.getMessage());
        }
    }

    /**
     * Слива събития от файл с текущите събития в календара.
     * При наличие на конфликти (припокривания), потребителят избира как да постъпи:
     * <ul>
     *     <li>Да запази старото събитие</li>
     *     <li>Да замени със новото</li>
     *     <li>Да промени старото или новото събитие</li>
     * </ul>
     *
     * <p>Новите събития се визуализират едно по едно заедно с всички съществуващи припокриващи се.</p>
     */
    public void mergeData(String name) {

        List<Event> newEvents = readFileToArray(name);
        if (newEvents == null) return;
        for (Event e : newEvents) {
            System.out.println("Next event: \n");
            System.out.println(e.ShowEvent());
            System.out.println("Trying to add next event... \n");
            List<Event> overlaps = calendar.findOverlap(e, calendar.getEvents());
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
                        {
                            for (Event ev : overlaps)
                            {
                                ev.ShowEvent();
                                System.out.println("What would you like to change for this event?");
                                System.out.println("Valid choices: 'date', 'start', 'end'");
                                String choice;
                                do{
                                    choice = scanner.nextLine();
                                }while (!choice.equalsIgnoreCase("date") && !choice.equalsIgnoreCase("start") && !choice.equalsIgnoreCase("end"));

                                calendar.change(ev, choice);
                            }

                            repeat = !calendar.book(e);
                            if (repeat)
                                System.out.println("You did not clear the schedule around the new event!");
                        }
                            break;
                        case 3://Move the new event
                            System.out.println("What would you like to change for the new event?");
                            System.out.println("Valid choices: 'date', 'start', 'end'");
                            String choice;
                            do{
                                choice = scanner.nextLine();
                            }while (!choice.equalsIgnoreCase("date") && !choice.equalsIgnoreCase("start") && !choice.equalsIgnoreCase("end"));

                            e.change(choice);
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
