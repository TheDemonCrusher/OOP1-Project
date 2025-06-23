package Commands;

import Interfaces.Command;
import Models.Calendar;
import Models.Event;

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

        int hours;
        try{
            hours = Integer.parseInt(args[2]);
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid third argument! Hours must be a number!");
        }
        if(hours < 0 || hours > 9)
            throw new IllegalArgumentException("Event cannot be longer than 9hrs or shorter than 1hr]");
        String[] date = args[1].split("/");
        if(date.length != 2)
            throw new IllegalArgumentException("Date must include day and month separated by / in that order");

        int day, mnt;
        Month month;
        try{
            day = Integer.parseInt(date[0]);
            mnt = Integer.parseInt(date[1]);
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid second argument! Invalid date!");
        }
        if(mnt < 0 || mnt > 12)
            throw new IllegalArgumentException("Month cannot be less than 0 or greater than 12");
        month = Month.of(mnt);
        if(day < 0 || day > month.length(calendar.currentYear.isLeapYear()))
            throw new IllegalArgumentException("Invalid day in first argument! Day cannot be 0 or surpass " + month.length(calendar.currentYear.isLeapYear()) + " during " + month);

        List<Event> results = calendar.findSlot(day, mnt, hours);

        if(results != null)
        {
            System.out.println("The available slots are: \n");
            for(Event e : results)
                System.out.println(e.ShowEvent());
        }
    }
}
