package Commands;

import Interfaces.FileCommand;
import Models.FileController;

/**
 * Команда за обединяване на текущия календар с данни от друг файл.
 * <p>
 * Използва се като {@code merge <име>}, където {@code <име>} е името на текстов файл (без разширение),
 * съдържащ календарни събития. Събитията от този файл ще бъдат добавени към текущия календар.
 * В случай на конфликт потребителят ще бъде подканен да избере как да продължи.
 * </p>
 */
public class Merge implements FileCommand {

    /**
     * Изпълнява командата за обединяване на календарите.
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
            throw new IllegalArgumentException("Merge takes 1 argument! (merge <name>) [name must be without an extension like .txt]");
        if(args[1].contains("."))
            throw new IllegalArgumentException("Name cannot contain dots or extensions!");

        fileController.mergeData(args[1]);
    }

}