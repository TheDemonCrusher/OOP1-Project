package Commands;

import Interfaces.Command;
import Models.Calendar;
import Models.Event;
import Models.Holidays;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.List;

/**
 * Команда за търсене на свободни времеви слотове в календара, започвайки от определена дата.
 * <p>
 * Командата очаква два аргумента:
 * <ul>
 *     <li><b>Дата на започване</b> във формат {@code dd/mm}</li>
 *     <li><b>Продължителност</b> на желаното събитие в часове (1 до 9)</li>
 * </ul>
 * </p>
 *
 * <p><b>Пример:</b> {@code findslot 12/06 2} – търси свободни слотове за 2 часа, започвайки от 12 юни.</p>
 */
public class FindSlot implements Command {

    /**
     * Изпълнява командата за търсене на свободен времеви интервал в календара.
     *
     * @param calendar обектът {@link Calendar}, в който се търси
     * @param args     аргументи на командата:
     *                 <ul>
     *                     <li>{@code args[1]} – начална дата във формат {@code dd/mm}</li>
     *                     <li>{@code args[2]} – желана продължителност в часове (от 1 до 9)</li>
     *                 </ul>
     *
     * @throws IllegalArgumentException ако:
     * <ul>
     *     <li>броят на аргументите е различен от 3</li>
     *     <li>датата или часовете не са валидни числа</li>
     *     <li>датата не съществува (например 30 февруари)</li>
     *     <li>часовете са извън позволения диапазон</li>
     * </ul>
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 3)
            throw new IllegalArgumentException("FindSlot takes 2 arguments! (findslot <fromdate> <hours>) [fromdate format: dd/mm]");

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

        List<Event> results = calendar.findSlot(date, hours);

        if(results != null)
        {
            System.out.println("The available slots are: \n");
            for(Event e : results)
                System.out.println(e.ShowEvent());
        }
    }
}
