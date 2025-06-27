package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

/**
 * Команда за показване на натовареността на календарните дни в даден период.
 * <p>
 * Приема два аргумента - начална и крайна дата във формат "dd/mm".
 * Проверява валидността на подадените дати и извиква метод за показване на заетите дни
 * в зададения интервал.
 * </p>
 */
public class ShowBusy implements Command {

    /**
     * Изпълнява командата за показване на заетите дни в календара в определения период.
     *
     * @param calendar обект на календара, в който се търсят натоварените дни
     * @param args     аргументи на командата - трябва да съдържат 2 валидни дати във формат "dd/mm"
     * @throws IllegalArgumentException ако аргументите са с неправилен брой или форматирани неправилно
     */
    @Override
    public void execute(Calendar calendar, String[] args) {
        if (args.length != 3)
            throw new IllegalArgumentException("ShowBusy takes 2 arguments! (ShowBusy <fromdate> <todate>) [date format: dd/mm]");

        LocalDate from;
        LocalDate to;

        String[] input = args[1].split("/");

        if (input.length != 2)
            throw new IllegalArgumentException("Fromdate must consist of a day and month separated by '/'");
        try {
            from = LocalDate.of(calendar.currentYear.getYear(), Integer.parseInt(input[1]), Integer.parseInt(input[0]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new IllegalArgumentException("Fromdate month and day must be valid numbers and represent a real date");
        }

        input = args[2].split("/");
        if (input.length != 2)
            throw new IllegalArgumentException("Todate must consist of a day and month separated by '/'");
        try {
            to = LocalDate.of(calendar.currentYear.getYear(), Integer.parseInt(input[1]), Integer.parseInt(input[0]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new IllegalArgumentException("Todate month and day must be valid numbers and represent a real date");
        }
        calendar.showBusyDays(from, to);
    }
}
