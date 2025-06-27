package Commands;

import Interfaces.Command;
import Models.Calendar;

import java.time.DateTimeException;
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
            throw new IllegalArgumentException("Unbook takes 3 arguments! (Unbook <date> <starttime> <endtime>) [date format: dd/mm] [time format: hh/mm]");

        LocalDate date;
        LocalTime start;
        LocalTime end;

        String[] input = args[1].split("/");

        if (input.length != 2)
            throw new IllegalArgumentException("Date must consist of a day and month separated by '/'");
        try {
            date = LocalDate.of(calendar.currentYear.getYear(), Integer.parseInt(input[1]), Integer.parseInt(input[0]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new IllegalArgumentException("Month and day must be valid numbers and represent a real date");
        }

        input = args[2].split(":");
        if (input.length != 2)
            throw new IllegalArgumentException("Starttime must consist of hours and minutes separated by ':'");
        try{
            start = LocalTime.of(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
        }catch(NumberFormatException | DateTimeException e)
        {
            throw new IllegalArgumentException("Starttime hours and minutes must be valid numbers and represent a real time");
        }

        input = args[3].split(":");
        if (input.length != 2)
            throw new IllegalArgumentException("Endtime must consist of hours and minutes separated by ':'");
        try{
            end = LocalTime.of(Integer.parseInt(input[0]), Integer.parseInt(input[1]));
        }catch(NumberFormatException | DateTimeException e)
        {
            throw new IllegalArgumentException("Endtime hours and minutes must be valid numbers and represent a real time");
        }
        calendar.unbook(date,start,end);

    }
}
