package Commands;

import Interfaces.Command;
import Models.Calendar;
import Models.Event;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

/**
 * Команда за търсене на свободни времеви слотове, които са налични както в текущия календар, така и в друг потребителски файл.
 * <p>
 * Изисква 3 аргумента:
 * <ul>
 *     <li><b>Дата</b> във формат {@code dd/mm}</li>
 *     <li><b>Продължителност</b> в часове (между 1 и 9)</li>
 *     <li><b>Име на файл</b> без разширение</li>
 * </ul>
 * </p>
 * <p><b>Пример:</b> {@code findslotwith 13/07 2 colleague} – търси съвпадащи свободни слотове между текущия календар и файла {@code colleague.txt}</p>
 */
public class FindSlotWith implements Command {

    /**
     * Изпълнява командата за намиране на съвместими свободни времеви интервали между текущия календар и подаден потребителски файл.
     *
     * @param calendar обектът {@link Calendar}, върху който се извършва операцията
     * @param args     аргументи на командата:
     *                 <ul>
     *                     <li>{@code args[1]} – начална дата във формат {@code dd/mm}</li>
     *                     <li>{@code args[2]} – желана продължителност в часове</li>
     *                     <li>{@code args[3]} – име на файл (без разширение)</li>
     *                 </ul>
     *
     * @throws IllegalArgumentException ако:
     * <ul>
     *     <li>аргументите са грешни по брой или формат</li>
     *     <li>датата е невалидна или извън обхвата на месеца</li>
     *     <li>часовете са извън допустимите стойности</li>
     *     <li>името на файла съдържа разширение (напр. ".txt")</li>
     * </ul>
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 4)
            throw new IllegalArgumentException("FindSlot takes 3 arguments! (findslotwith <date> <hours> <filename>) [date format: dd/mm]");

        LocalTime hours;
        LocalDate date;

        String[] input = args[1].split("/");
        if (input.length != 2)
            throw new IllegalArgumentException("Date must consist of a day and month separated by '/'");
        try{
            date = LocalDate.of(calendar.currentYear.getYear(), Integer.parseInt(input[1]), Integer.parseInt(input[0]));
        }
        catch(NumberFormatException | DateTimeException e)
        {
            throw new IllegalArgumentException("Month and day must be valid numbers and represent a real date");
        }

        input = args[2].split(":");
        if (input.length != 2)
            throw new IllegalArgumentException("Hours must consist of hours and minutes separated by ':'");

        try{
            hours = LocalTime.of(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
        }
        catch(DateTimeException e)
        {
            throw new IllegalArgumentException("Invalid third argument! Hours must be valid numbers and represent a real time!");
        }
        if(hours.isAfter(LocalTime.of(9,0)))
            throw new IllegalArgumentException("Event cannot be longer than 9hrs or shorter than 1hr]");

        if(args[3].contains("."))
            throw new IllegalArgumentException("Name cannot contain dots or extensions!");
        String name = args[3];

        List<Event> results = calendar.findSlotWith(date, hours, name);
        if(results != null)
        {
            System.out.println("These slots are both avaliable in the calendar and the file: \n");
            for(Event e : results)
                System.out.println(e.ShowEvent());
        }
    }
}

