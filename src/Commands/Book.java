package Commands;

import Interfaces.Command;
import Models.Calendar;

/**
 * Команда за създаване и добавяне на ново събитие в календара.
 * <p>
 * Използва се за ръчно въвеждане на събитие от потребителя чрез конзолен вход.
 * Не приема аргументи – информацията за събитието се събира интерактивно.
 * </p>
 *
 * <p><b>Пример за използване:</b></p>
 * <pre>
 * book
 * </pre>
 */
public class Book implements Command {

    /**
     * Изпълнява командата за добавяне на ново събитие.
     * Извиква метода {@code createEvent()} за създаване на събитие и го добавя в календара.
     *
     * @param calendar обектът {@link Calendar}, в който ще се добави новото събитие
     * @param args     масив с аргументи – не трябва да съдържа нищо освен името на командата
     *
     * @throws IllegalArgumentException ако са подадени аргументи (командата не приема такива)
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if(args.length != 1)
            throw new IllegalArgumentException("Book doesn't take any arguments!");

        calendar.book(calendar.createEvent());
    }
}
