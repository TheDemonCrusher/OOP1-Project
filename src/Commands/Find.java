package Commands;

import Interfaces.Command;
import Models.Calendar;

/**
 * Команда за търсене на събития в календара по ключова дума.
 * <p>
 * Ключовата дума може да съвпада с част от заглавието или описанието на събитие.
 * Командата приема един аргумент – текстът за търсене.
 * </p>
 *
 * <p><b>Пример за използване:</b></p>
 * <pre>
 * find Meeting
 * </pre>
 */
public class Find implements Command {

    /**
     * Изпълнява командата за търсене на събития по ключова дума.
     * Търсенето се извършва в заглавието и описанието на всички събития в календара.
     *
     * @param calendar обектът {@link Calendar}, съдържащ събитията
     * @param args     аргументи на командата – очаква се само един аргумент:
     *                 <ul>
     *                     <li>{@code args[1]} – текст за търсене</li>
     *                 </ul>
     *
     * @throws IllegalArgumentException ако броят на аргументите е различен от 2
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 2)
            throw new IllegalArgumentException("Find takes 1 argument! (find <info>)");

        String info = args[1];
        calendar.findEvents(info);
    }
}
