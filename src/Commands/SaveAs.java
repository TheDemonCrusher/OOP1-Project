package Commands;

import Interfaces.FileCommand;
import Models.Calendar;
import Models.FileController;

import java.util.Scanner;

/**
 * Команда за записване на текущия календар във файл с потребителско име и разширение.
 * <p>
 * Използва се като {@code saveas <име.разширение>}, където потребителят задава
 * името и разширението на файла. Позволява запис във всякакъв формат, но
 * предупреждава, че файлът може да не е съвместим с информацията.
 * </p>
 * <p>
 * Ако календарът е празен, се изисква потвърждение от потребителя преди запис.
 * </p>
 */
public class SaveAs implements FileCommand {

    Calendar calendar = Calendar.getInstance();

    /**
     * Изпълнява командата за записване на календара в избран от потребителя файл.
     *
     * @param fileController обект, който управлява файловите операции
     * @param args аргументи на командата – трябва да съдържат името на файла с разширение
     *
     * @throws IllegalArgumentException ако аргументите не са точно 2 или ако
     *                                  името не съдържа точка и разширение
     */
    @Override
    public void execute(FileController fileController, String[] args)
    {
        if(args.length != 2)
            throw new IllegalArgumentException("Saveas takes 1 argument! (saveas <name.extension>)");
        if(!args[1].contains("."))
            throw new IllegalArgumentException("Name must contain an extension!");

        System.out.println("WARNING: This function will create a file with whatever extension you choose for it, meaning the file you create might not be compatible with the information!");
        if (calendar.getEvents().isEmpty()) {
            Scanner scanner = new Scanner(System.in);
            System.out.println("You are about to write an empty calendar to a file, are you sure you want to continue?" +
                               "\n Write 'CONTINUE' to go on. Write anything else to go back.");
            String answer = scanner.nextLine();
            if (!answer.equalsIgnoreCase("CONTINUE"))
                return;
        }

        fileController.writeToFile(args[1]);
    }
}