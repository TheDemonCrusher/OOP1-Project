package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.util.Scanner;

/**
 * Команда за показване на помощно меню с описание на всички налични команди и съвети за употреба.
 * <p>
 * Помощното меню включва:
 * <ul>
 *     <li>Команди за работа с файлове</li>
 *     <li>Команди за събития</li>
 *     <li>Съвети за правилна употреба на приложението</li>
 * </ul>
 * </p>
 * Използва се като {@code help} без допълнителни аргументи.
 */
public class Help implements Command {

    /**
     * Извежда помощното меню в конзолата.
     *
     * @param calendar обектът {@link Calendar}, към който принадлежи командата (не се използва тук, но се изисква от интерфейса)
     * @param args     аргументи на командата – не трябва да съдържат нищо освен самата команда
     *
     * @throws IllegalArgumentException ако е подаден допълнителен аргумент към командата
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 1)
            throw new IllegalArgumentException("Help doesn't take any arguments!");

        System.out.println("\n=== HELP MENU ===");

        System.out.println("=== FILE COMMANDS ===");
        System.out.println("write <name> -> Save calendar - Save current calendar to .txt file | expects the name of the file without its extension");
        System.out.println("read <name> -> Load calendar - Replace current calendar with file contents | expects the name of the file without its extension");
        System.out.println("saveas <name.extension> -> Save as custom format - Export to any file format (advanced) | lets the user choose the name and extension of the file");
        System.out.println("merge <name> -> Merge calendars - Combine current calendar with file contents | merges calendars with chosen .txt file by name\n");

        System.out.println("=== EVENT COMMANDS ===");
        System.out.println("book -> Book event - Schedule a new event | expects manual input for each value");
        System.out.println("unbook <date> <starttime> <endtime> -> Remove event - Delete an existing event | expects the exact datetime of the event [dd/mm hh hh]");
        System.out.println("pt.1 change <index> <field>-> Edit event - Modify event details | user chooses what to change and what value to asign");
        System.out.println("pt.2 the index of the event can be checked with the showall command. Allowed fields are 'name' 'desc'/'description' 'start' 'end' 'date'");
        System.out.println("find <keywords>-> Search events - Find events by keyword/s | expects keyword/s as input by user");
        System.out.println("findslot <fromdate> <hours>-> Find free slots - Check available meeting times | expects information about the slot");
        System.out.println("findslotwith <fromdate> <hours> <filename>-> Combined free slots - Find times free in both calendar and file | same as findslot but also searches through a file");
        System.out.println("showall -> Show all events - Display complete schedule | no input expected");
        System.out.println("showbusy <fromdate> <todate>-> Busy days - See which days have most events | expects range of time");
        System.out.println("agenda <date>-> Daily agenda - View schedule for specific day | expects a day as input\n");

        System.out.println("=== USAGE TIPS ===");
        System.out.println("- Universal date format is [dd/mm] there is no need for a year.");
        System.out.println("- Only hours are required for time.");
        System.out.println("- Free slot finder shows times between 8:00-17:00 on workdays only");
        System.out.println("- When merging calendars, you'll be prompted to resolve conflicts");

        System.out.println("Write anything to return...\n");
        new Scanner(System.in).nextLine();
    }
}
