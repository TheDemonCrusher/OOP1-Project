package Commands;

import Interfaces.Command;
import Models.Calendar;

/**
 * Команда за показване на всички събития от календара.
 * <p>
 * Не приема аргументи.
 * При извикване извежда на екрана пълния списък със събития.
 * </p>
 */
public class ShowAll implements Command {

    /**
     * Изпълнява командата за показване на всички събития.
     *
     * @param calendar обект на календара, от който се извличат събитията
     * @param args аргументи на командата – трябва да е празен масив (само името на командата)
     *
     * @throws IllegalArgumentException ако са подадени аргументи
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 1)
            throw new IllegalArgumentException("ShowAll doesn't take any arguments!");

        calendar.printEvents();
    }
}
