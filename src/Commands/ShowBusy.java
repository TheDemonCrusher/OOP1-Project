package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.time.LocalDate;
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
     * @param args аргументи на командата - трябва да съдържат 2 валидни дати във формат "dd/mm"
     *
     * @throws IllegalArgumentException ако аргументите са с неправилен брой или форматирани неправилно
     */
    @Override
    public void execute(Calendar calendar, String[] args) {
        if (args.length != 3)
            throw new IllegalArgumentException("ShowBusy takes 2 arguments! (ShowBusy <fromdate> <todate>) [date format: dd/mm]");

        int day, month;
        LocalDate from;
        LocalDate to;

        String[] date = args[1].split("/");
        if (date.length != 2)
            throw new IllegalArgumentException("fromDate must consist of a day and month separated by /");
        try {
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid first argument! fromDate isn't written in numbers!");
        }

        if (month < 1 || month > 12)
            throw new IllegalArgumentException("Invalid month in first argument! Month must be between 1 and 12!");
        if (day < 0 || day > Month.of(month).length(calendar.currentYear.isLeapYear()))
            throw new IllegalArgumentException("Invalid day in first argument! Day cannot be 0 or surpass " + Month.of(month).length(calendar.currentYear.isLeapYear()) + " during " + Month.of(month));
        from = LocalDate.of(calendar.currentYear.getYear(), month, day);

        date = args[2].split("/");
        if (date.length != 2)
            throw new IllegalArgumentException("toDate must consist of a day and month separated by /");
        try {
            day = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid second argument! fromDate isn't written in numbers!");
        }

        if (month < 1 || month > 12)
            throw new IllegalArgumentException("Invalid month in second argument! Month must be between 1 and 12!");
        if (day < 0 || day > Month.of(month).length(calendar.currentYear.isLeapYear()))
            throw new IllegalArgumentException("Invalid day in second argument! Day cannot be 0 or surpass " + Month.of(month).length(calendar.currentYear.isLeapYear()) + " during " + Month.of(month));
        to = LocalDate.of(calendar.currentYear.getYear(), month, day);

        calendar.showBusyDays(from, to);
    }
}
