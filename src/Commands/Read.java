package Commands;

import Interfaces.FileCommand;
import Models.FileController;

/**
 * Команда за зареждане на календар от файл.
 * <p>
 * Използва се като {@code read <име>}, където {@code <име>} е името на текстов файл (без разширение),
 * от който се зареждат събития в календара. При успешен прочит, текущият календар се заменя
 * изцяло със съдържанието от файла.
 * </p>
 */
public class Read implements FileCommand {

    /**
     * Изпълнява командата за зареждане на календар от файл.
     *
     * @param fileController обект {@link FileController}, който управлява файловите операции
     * @param args аргументи на командата – трябва да съдържат името на файла без разширение
     *
     * @throws IllegalArgumentException ако броят на аргументите е различен от 2,
     *                                  или ако името на файла съдържа точка (.) или разширение
     */
    @Override
    public void execute(FileController fileController, String[] args)
    {
        if(args.length != 2)
            throw new IllegalArgumentException("Read takes 1 argument! (read <name>) [name must be without an extension like .txt]");
        if(args[1].contains("."))
            throw new IllegalArgumentException("Name cannot contain dots or extensions!");

        fileController.readFile(args[1]);
    }
}
