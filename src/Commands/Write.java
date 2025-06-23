package Commands;

import Interfaces.FileCommand;
import Models.FileController;

/**
 * Команда за записване на текущия календар във файл с разширение .txt.
 * <p>
 * Приема един аргумент - името на файла без разширение.
 * Извиква метода за записване в текстов файл на обекта FileController.
 * </p>
 */
public class Write implements FileCommand {

    /**
     * Изпълнява командата за запис във файл.
     *
     * @param fileController контролер за файлови операции
     * @param args аргументи на командата - трябва да съдържат името на файла без разширение
     *
     * @throws IllegalArgumentException ако броят на аргументите е грешен или името съдържа разширение
     */
    @Override
    public void execute(FileController fileController, String[] args)
    {
        if (args.length != 2)
            throw new IllegalArgumentException("Write takes 1 argument! (Write <filename>) [name must be without an extension like .txt]");

        if(args[1].contains("."))
            throw new IllegalArgumentException("Name cannot contain dots or extensions!");

        try
        {
            fileController.writeToTxt(args[1]);
        }
        catch (IllegalStateException e)
        {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
