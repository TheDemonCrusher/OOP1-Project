package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.time.LocalDate;

/**
 * Команда за показване на дневния график (агенда) за конкретна дата.
 * <p>
 * Използва се за визуализация на всички събития в даден ден от текущата година в календара.
 * Приема една дата като аргумент, във формат {@code dd/mm}.
 * </p>
 *
 * <p><b>Пример за използване:</b></p>
 * <pre>
 * agenda 12/06
 * </pre>
 */
public class Agenda implements Command {

    /**
     * Изпълнява командата за показване на дневната агенда.
     *
     * @param calendar обектът {@link Calendar}, от който се извлича и показва информацията
     * @param args     аргументи на командата – очаква масив с точно два елемента,
     *                 където вторият е датата във формат {@code dd/mm}
     *
     * @throws IllegalArgumentException ако аргументите са невалидни или датата не е подадена в правилен формат
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if (args.length != 2)
            throw new IllegalArgumentException("Agenda takes 1 argument! (Agenda <date>) [date format: dd/mm]");

        int day, month;
        LocalDate date;
        String[] input = args[1].split("/");
        if (input.length != 2)
            throw new IllegalArgumentException("fromDate must consist of a day and month separated by /");
        try {
            day = Integer.parseInt(input[0]);
            month = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid first argument! date isn't written in numbers!");
        }

        date = LocalDate.of(calendar.currentYear.getYear(), month, day);
        calendar.agenda(date);
    }
}
