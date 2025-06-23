package Interfaces;

import Models.FileController;

/**
 * Интерфейс, дефиниращ командата за работа с обект от тип {@link FileController}.
 * <p>
 * Командите, свързани с файлови операции (напр. запис, четене, обединяване),
 * трябва да имплементират този интерфейс.
 * </p>
 */
public interface FileCommand {

    /**
     * Изпълнява командата върху подадения {@link FileController}, използвайки аргументи от потребителя.
     *
     * @param fileController обектът {@link FileController}, върху който ще се прилага действието
     * @param args           масив с аргументи, подадени от потребителя (например име на файл, формат и т.н.)
     */
    void execute(FileController fileController, String[] args);
}
