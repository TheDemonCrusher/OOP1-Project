package Commands;

import Interfaces.FileCommand;
import Models.Calendar;
import Models.FileController;
import Models.Holidays;

import java.time.DateTimeException;
import java.time.LocalDate;

/**
 * Класът {@code Holiday} имплементира командата за добавяне на почивен ден към календара.
 * Използва формат на датата [dd/mm] и записва актуализираните почивни дни чрез {@link FileController}.
 */
public class Holiday implements FileCommand {
    /**
     * Изпълнява командата за добавяне на нова дата като почивен ден.
     * Очаква точно един аргумент с формат "dd/mm".
     * Ако датата е валидна, тя се добавя в системата и се записва във файл.
     *
     * @param fileController контролер за файлови операции
     * @param args аргументи на командата (["holiday", "dd/mm"])
     * @throws IllegalArgumentException ако броят на аргументите е грешен или датата е невалидна
     */
    @Override
    public void execute(FileController fileController, String[] args)
    {
        if(args.length != 2)
            throw new IllegalArgumentException("Holiday takes 1 argument! holiday <date> [dd/mm]");

        LocalDate date;

        String[] input = args[1].split("/");

        if (input.length != 2)
            throw new IllegalArgumentException("Date must consist of a day and month separated by '/'");
        try {
            Calendar calendar = Calendar.getInstance();
            date = LocalDate.of(calendar.currentYear.getYear(), Integer.parseInt(input[1]), Integer.parseInt(input[0]));
        } catch (NumberFormatException | DateTimeException e) {
            throw new IllegalArgumentException("Month and day must be valid numbers and represent a real date");
        }

        Holidays holidays = Holidays.getInstance();
        holidays.addHoliday(date);
        fileController.saveHolidays();
    }
}
