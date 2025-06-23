package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.util.ArrayList;

/**
 * Команда за промяна на избрано поле от съществуващо събитие в календара.
 * <p>
 * Позволява на потребителя да избере събитие по индекс и да промени конкретно негово поле (име, описание, дата и др.).
 * Приема два аргумента: индекс на събитието и желаното поле за редакция.
 * </p>
 *
 * <p><b>Пример за използване:</b></p>
 * <pre>
 * change 3 date
 * </pre>
 */
public class Change implements Command {

    /**
     * Валидните полета, които могат да бъдат променяни чрез тази команда.
     */
    private static ArrayList<String> actions = new ArrayList<String>();

    /**
     * Конструктор, който инициализира списъка с позволени полета за промяна.
     */
    public Change()
    {
        actions.add("name");
        actions.add("desc");
        actions.add("description");
        actions.add("start");
        actions.add("end");
        actions.add("date");
    }

    /**
     * Изпълнява командата за промяна на събитие в календара.
     *
     * @param calendar обектът {@link Calendar}, съдържащ събитията
     * @param args     аргументи на командата – очакват се точно два:
     *                 <ul>
     *                     <li>{@code args[1]} – индекс на събитието в списъка</li>
     *                     <li>{@code args[2]} – името на полето, което ще бъде променено</li>
     *                 </ul>
     *
     * @throws IllegalArgumentException ако броят на аргументите е грешен, индексът е невалиден,
     *                                  или името на полето не е сред позволените
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 3)
            throw new IllegalArgumentException("Change takes 2 arguments! Requires the index of the event and the desired field as an argument.\nExample: change <index> date | Use showall to see all events and their indexes!");
        int index;
        try{
            index = Integer.parseInt(args[1]);
        }
        catch(NumberFormatException e)
        {
            throw new IllegalArgumentException("Invalid first argument! Index must be a number!");
        }
        if(index < 0 || index > calendar.getEvents().size() - 1)
            throw new IllegalArgumentException("Invalid first argument! Index out of range!");
        if(!actions.contains(args[2].toLowerCase()))
            throw new IllegalArgumentException("Invalid second argument! Valid arguments: 'name', 'desc', 'start', 'end', 'date'");

        if(args[2].equalsIgnoreCase("description"))
            args[2] = "desc";
        calendar.change(calendar.getEvents().get(index), args[2]);
    }
}