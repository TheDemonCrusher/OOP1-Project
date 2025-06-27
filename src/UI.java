import Commands.*;
import Interfaces.Command;
import Interfaces.FileCommand;
import Models.Calendar;
import Models.FileController;

import java.util.*;

/**
 * Класът UI управлява потребителския интерфейс на приложението.
 * Той обработва въвеждането на команди от потребителя,
 * съхранява наличните команди и файлови команди и ги изпълнява.
 * <p>
 * Поддържа основен цикъл за въвеждане на команди и извежда съответните съобщения и резултати.
 * Предоставя възможност за изход с опция за записване на текущото състояние на календара.
 * </p>
 */
public class UI {

    private Map<String, Command> commands;
    private Map<String, FileCommand> fileCommands;
    private FileController fileController = new FileController();
    private Calendar calendar = Calendar.getInstance();
    boolean exit = false;

    /**
     * Създава нов обект UI и инициализира картите с наличните команди и файлови команди.
     * Регистрира всички поддържани команди, които потребителят може да използва.
     */
    public UI() {
        commands = new HashMap<>();
        fileCommands = new HashMap<>();

        commands.put("agenda", new Agenda());
        commands.put("book", new Book());
        commands.put("change", new Change());
        commands.put("find", new Find());
        commands.put("findslot", new FindSlot());
        commands.put("findslotwith", new FindSlotWith());
        commands.put("help", new Help());
        commands.put("showall", new ShowAll());
        commands.put("showbusy", new ShowBusy());
        commands.put("unbook", new Unbook());

        fileCommands.put("holiday", new Holiday());
        fileCommands.put("merge", new Merge());
        fileCommands.put("read", new Read());
        fileCommands.put("saveas", new SaveAs());
        fileCommands.put("write", new Write());
        fileController.readHolidays();
    }

    /**
     * Стартира основния цикъл на потребителския интерфейс.
     * Изчаква въвеждане на команди от потребителя, обработва ги и извежда резултатите.
     * Позволява изход чрез команда "exit" с възможност за запазване на календара.
     */
    public void mainLoop() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Please enter a command!");
        System.out.println("[Type 'help' for a list of commands.]");

        while (!exit) {
            System.out.print("> ");
            String inputLine = scanner.nextLine();

            if (inputLine.trim().equalsIgnoreCase("exit")) {
                String save = fileController.getLastSaved();// Обработка на изхода с потвърждение
                if (save != null && !save.isEmpty()) {
                    System.out.print("Write 'yes' if you would you like to save the calendar to the last .txt file you wrote to before exiting?");
                    System.out.print("( " + save + ".txt )\n");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("yes")) {
                        FileCommand fileCommand = fileCommands.get("write");
                        fileCommand.execute(fileController, new String[]{"write", save});
                    }
                    exit = true;
                } else {
                    System.out.print("Calendar hasn't been saved to a .txt file before, are you sure you want to exit?");
                    System.out.print("Write 'confirm' to exit.\n");
                    String answer = scanner.nextLine();
                    if (answer.equalsIgnoreCase("confirm"))
                        exit = true;
                }
                continue;
            }

            handleCommand(inputLine);
        }

        scanner.close();
        System.out.println("Exiting...");
    }

    /**
     * Обработва въведена от потребителя команда.
     * Определя дали командата е файлов тип или обикновена, и съответно я изпълнява.
     * При грешки в изпълнението извежда подходящи съобщения.
     *
     * @param inputLine Входният низ с командата и аргументите
     */
    private void handleCommand(String inputLine) {
        String[] parts = inputLine.trim().split("\\s+");
        if (parts.length == 0 || parts[0].isEmpty()) {
            return; // Игнорира празни редове
        }

        String commandName = parts[0].toLowerCase();
        FileCommand fCommand = fileCommands.get(commandName);
        if (fCommand != null) {
            try {
                fCommand.execute(fileController, parts); // Изпълнява командата
            } catch (IllegalArgumentException | IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Unexpected error: " + e.getMessage());
            }
        } else {
            Command command = commands.get(commandName); // Взема командата от регистрираната карта

            if (command != null) {
                try {
                    if (commandName.equalsIgnoreCase("find"))
                        parts = inputLine.split(" ", 2);
                    command.execute(calendar, parts); // Изпълнява командата
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println("Error: " + e.getMessage());
                } catch (Exception e) {
                    System.out.println("Unexpected error: " + e.getMessage());
                }
            } else {
                System.out.println("Unknown command: '" + commandName + "'. Feel free to use 'help' to check all valid commands.");
            }
        }
    }
}
