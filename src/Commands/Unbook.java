package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;

/**
 * Команда за премахване (отказване) на събитие от календара.
 * <p>
 * Приема три аргумента: дата (формат "dd/mm"), начален час и краен час.
 * Проверява валидността на подадените параметри и премахва събитието,
 * което съвпада с посочения времеви интервал в календара.
 * </p>
 */
public class Unbook implements Command {

    /**
     * Изпълнява командата за премахване на събитие.
     *
     * @param calendar обект на календара, от който ще се премахне събитието
     * @param args аргументи на командата - трябва да съдържат дата, начален и краен час
     *
     * @throws IllegalArgumentException при неправилен брой аргументи или неправилно форматирани входни данни
     */
    @Override
    public void execute(Calendar calendar, String[] args)
    {
        if (args.length != 4)
            throw new IllegalArgumentException("Unbook takes 3 arguments! (Unbook <date> <starttime> <endtime>) [date format: dd/mm]");

        int month, day, start, end, i = 0;
        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;

        String[] input = args[1].split("/");

        if (input.length != 2)
            throw new IllegalArgumentException("Date must consist of a day and month separated by /");
        try {
            day = Integer.parseInt(input[0]);
            month = Integer.parseInt(input[1]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid first argument! Date must be written in numbers!");
        }

        if (month < 1 || month > 12)
            throw new IllegalArgumentException("Invalid month in first argument! Month must be between 1 and 12!");
        if (day < 0 || day > Month.of(month).length(calendar.currentYear.isLeapYear()))
            throw new IllegalArgumentException("Invalid day in first argument! Day cannot be 0 or surpass " + Month.of(month).length(calendar.currentYear.isLeapYear()) + " during " + Month.of(month));
        date = LocalDate.of(calendar.currentYear.getYear(), month, day);

        try {
            start = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid first argument! fromDate isn't written in numbers!");
        }
        if(start < 0 || start >= 24)
            throw new IllegalArgumentException("Starttime out of range! Starttime must be between 0 and 23");

        try {
            end = Integer.parseInt(args[3]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid first argument! fromDate isn't written in numbers!");
        }
        if(end < 0 || end >= 24 || end == start)
            throw new IllegalArgumentException("Endtime out of range! Endtime must be between 0 and 23 and cannot be the same as the starttime");
        startTime = LocalTime.of(start, 0);
        endTime = LocalTime.of(end, 0);
        calendar.unbook(date,startTime,endTime);

    }
}
