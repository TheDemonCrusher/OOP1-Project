import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класът Calendar представлява календар, който съхранява и управлява събития за дадена година.
 * Използва се Singleton шаблон за гарантиране на една инстанция.
 */
public class Calendar {
    /**
     * Списък с всички събития в календара.
     */
    private List<Event> events = new ArrayList<>();

    /**
     * Форматиращ обект за работа с дати.
     */
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");

    /**
     * Текущата дата, използвана като база за текущата година.
     */
    final LocalDate currentYear = LocalDate.now();

    /**
     * Единствената инстанция на календара (Singleton).
     */
    private static Calendar calendarInstance;

    /**
     * Частен конструктор за създаване на инстанцията на календара.
     */
    private Calendar() {
        formatter.format(currentYear);
    }

    /**
     * Връща единствената инстанция на класа Calendar.
     * @return инстанция на Calendar
     */
    public static Calendar getInstance() {
        if (calendarInstance == null)
            calendarInstance = new Calendar();
        return calendarInstance;
    }

    /**
     * Връща списъка със събития.
     * @return списък със събития
     */
    public List<Event> getEvents() {
        return events;
    }

    /**
     * Задава нов списък със събития.
     * @param events новият списък със събития
     */
    public void setEvents(List<Event> events) {
        this.events = events;
    }

    /**
     * Премахва дадено събитие от календара.
     * @param event събитието, което ще бъде премахнато
     */
    public void removeEvent(Event event) {
        int index = events.indexOf(event);
        if (index != -1)
            events.remove(index);
    }

    /**
     * Създава ново събитие чрез въвеждане от потребителя.
     * @return новосъздаденото събитие
     */
    public Event createEvent() {
        Scanner scanner = new Scanner(System.in);
        String name, desc;
        Month month;
        int mnt, day, start, end;

        System.out.println("Enter the name of the event: ");
        name = scanner.nextLine();
        System.out.println('\n');

        System.out.println("Enter a description of the event: ");
        desc = scanner.nextLine();
        System.out.println('\n');

        do {
            System.out.println("Enter the month of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            mnt = scanner.nextInt();
            System.out.println('\n');

        } while (mnt < 1 || mnt > 12);
        month = Month.of(mnt);

        do {
            System.out.println("Enter the day of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            day = scanner.nextInt();
            System.out.println('\n');

        } while (day < 0 || day > month.length(currentYear.isLeapYear()));

        do {
            System.out.println("Enter the starting hour of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            start = scanner.nextInt();
            System.out.println('\n');

        } while (start < 0 || start >= 24);

        do {
            System.out.println("Enter the ending hour of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            end = scanner.nextInt();
            System.out.println('\n');

        } while (end < 0 || end >= 24 || end == start);

        return new Event(name, month, day, start, end, desc);
    }

    /**
     * Запазва събитие в календара, ако не се припокрива с друго.
     * Ако събитието преминава през полунощ, го разделя на две части.
     * @param event събитието за запазване
     * @return true ако събитието е успешно записано, иначе false
     */
    public boolean book(Event event) {
        if (event.startTime.isAfter(event.endTime)) {
            Event day1 = new Event(event);
            Event day2 = new Event(event);
            day1.name += " pt.1";
            day2.name += " pt.2";
            day1.endTime = LocalTime.of(23, 59);

            if (event.getMonth().length(currentYear.isLeapYear()) > day1.getDay())
                day2.chosenDay = day2.chosenDay.plusDays(1);
            else {
                day2.chosenDay = day2.chosenDay.withDayOfMonth(1);
                day2.chosenDay = day2.chosenDay.plusMonths(1);
            }

            day2.startTime = LocalTime.of(0, 0);

            if (book(day1)) {
                if (book(day2))
                    return true;
                else
                    removeEvent(day1);
            }
            return false;
        }
        if (events.isEmpty()) {
            events.add(event);
            return true;
        }

        List<Event> overlaps = findOverlap(event, events);
        if (overlaps.isEmpty()) {
            events.add(event);
            return true;
        } else
            System.out.println("The event overlaps with other/s!");
        return false;
    }

    /**
     * Премахва събитие чрез въвеждане от потребителя.
     * Изисква потвърждение преди изтриване.
     */
    public void unbook() {
        Scanner scanner = new Scanner(System.in);
        int month, day, start, end, i = 0;
        String choice = "";

        LocalDate date;
        LocalTime startTime;
        LocalTime endTime;

        do {
            System.out.println("Enter the month of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            month = scanner.nextInt();
            System.out.println('\n');

        } while (month < 1 || month > 12);

        do {
            System.out.println("Enter the day of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            day = scanner.nextInt();
            System.out.println('\n');

        } while (day < 0 || day > Month.of(month).length(currentYear.isLeapYear()));

        do {
            System.out.println("Enter the starting hour of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            start = scanner.nextInt();
            System.out.println('\n');

        } while (start < 0 || start >= 24);

        do {
            System.out.println("Enter the ending hour of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            end = scanner.nextInt();
            System.out.println('\n');

        } while (end < 0 || end >= 24 || end == start);

        date = LocalDate.of(currentYear.getYear(), Month.of(month), day);
        startTime = LocalTime.of(start, 0);
        endTime = LocalTime.of(end, 0);

        for (Event e : events) {
            if (e.getMonth() == date.getMonth() && e.getDay() == date.getDayOfMonth()) {
                if (e.startTime == startTime && e.endTime == endTime) {
                    i++;
                    System.out.println(e.ShowEvent());
                    System.out.println("Do you really want to delete this event?\n");
                    System.out.println("Type 'confirm' continue:\n");

                    choice = scanner.nextLine();
                    choice = scanner.nextLine();

                    if (choice.equalsIgnoreCase("confirm")) {
                        removeEvent(e);
                        System.out.println("Event has been removed.");
                        return;
                    }

                }
            }
        }
        if (i == 0 && choice.isEmpty())
            System.out.println("There are no events matching your description.");
        else
            System.out.println("The event has not been removed.");
    }

    /**
     * Намира събития, които се припокриват с дадено събитие.
     * @param event събитието, за което търсим припокривания
     * @param events списък със съществуващи събития
     * @return списък с припокриващи се събития
     */
    public List<Event> findOverlap(Event event, List<Event> events) {
        List<Event> overlaps = new ArrayList<>();

        for (Event e : events) {
            if (e.getMonth() == event.getMonth() && e.getDay() == event.getDay()) {
                for (int i = event.startTime.getHour(); i <= event.endTime.getHour(); i++) {
                    if (i != event.endTime.getHour() && i == e.startTime.getHour()) {
                        overlaps.add(e);
                        break;
                    }
                    if (i == event.startTime.getHour()) {
                        if (i > e.startTime.getHour() && i < e.endTime.getHour()) {
                            overlaps.add(e);
                            break;
                        }
                    } else if (i > e.startTime.getHour() && i <= e.endTime.getHour()) {
                        overlaps.add(e);
                        break;
                    }
                }
            }
        }
        return overlaps;
    }

    /**
     * Показва всички събития за избрана дата, въведена от потребителя.
     */
    public void agenda() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Which day's schedule would you like to see?.\n");
        int month, day, i = 0;
        LocalDate date;
        do {
            System.out.println("Enter the month of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            month = scanner.nextInt();
            System.out.println('\n');

        } while (month < 1 || month > 12);

        do {
            System.out.println("Enter the day of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            day = scanner.nextInt();
            System.out.println('\n');

        } while (day < 0 || day > Month.of(month).length(currentYear.isLeapYear()));

        date = LocalDate.of(currentYear.getYear(), Month.of(month), day);

        for (Event e : events) {
            if (e.getMonth() == date.getMonth() && e.getDay() == date.getDayOfMonth()) {
                i++;
                System.out.println(i + ": ");
                System.out.println(e.ShowEvent());
            }
        }
        if (i == 0)
            System.out.println("You are free on that day!");
    }

    /**
     * Позволява промяна на дадено събитие чрез меню, което избира какво да се промени.
     * Промените се проверяват за припокриване с други събития.
     * @param event събитието, което ще бъде променено
     */
    public void change(Event event) {

        Scanner scanner = new Scanner(System.in);
        String action;
        int day, month, start, end, i = 1;
        String info;

        System.out.println("""
                Choose what to change about the event.
                -------------------------
                date -> Change date\s
                start -> Change start time\s
                end -> Change end time\s
                name -> Change name\s
                desc -> Change description
                """);

        action = scanner.nextLine();
        List<Event> others = new ArrayList<>();
        switch (action.toLowerCase()) {
            case "date": //Change date
            {
                for (Event e : events) {
                    if (!e.equals(event))
                        others.add(e);
                }
                do {
                    System.out.println("Enter the month of the event: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }
                    month = scanner.nextInt();
                    System.out.println('\n');

                } while (month < 1 || month > 12);

                do {
                    System.out.println("Enter the day of the event: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }
                    day = scanner.nextInt();
                    System.out.println('\n');

                } while (day < 0 || day > Month.of(month).length(currentYear.isLeapYear()));

                Event temp = new Event(event);
                temp.setChosenDay(LocalDate.of(currentYear.getYear(), month, day));
                if (findOverlap(temp, others).isEmpty()) {
                    event.setChosenDay(LocalDate.of(currentYear.getYear(), month, day));
                    System.out.println("Change was successful");
                } else
                    System.out.println("Change not possible due to overlapping event/s!");
            }
            break;
            case "start": //Change start time
            {
                for (Event e : events) {
                    if (!e.equals(event))
                        others.add(e);
                }
                do {
                    System.out.println("Enter the starting hour of the event: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }
                    start = scanner.nextInt();
                    System.out.println('\n');

                } while (start < 0 || start >= 24 || start > event.endTime.getHour());

                Event temp = new Event(event);
                temp.setStartTime(LocalTime.of(start, 0));
                if (findOverlap(temp, others).isEmpty()) {
                    event.setStartTime(LocalTime.of(start, 0));
                    System.out.println("Change successful!");
                } else
                    System.out.println("Change not possible due to overlapping event/s!");
            }
            break;
            case "end": //Change end time
            {
                for (Event e : events) {
                    if (!e.equals(event))
                        others.add(e);
                }
                do {
                    System.out.println("Enter the ending hour of the event: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }
                    end = scanner.nextInt();
                    System.out.println('\n');

                } while (end < 0 || end >= 24 || end == event.startTime.getHour() || end < event.startTime.getHour());

                Event temp = new Event(event);
                temp.setEndTime(LocalTime.of(end, 0));
                if (findOverlap(temp, others).isEmpty()) {
                    event.setEndTime(LocalTime.of(end, 0));
                    System.out.println("Change successful!");
                } else
                    System.out.println("Change not possible due to overlapping event/s!");
            }
            break;
            case "name"://Change name
                for (Event e : events) {
                    i++;
                    if (!e.equals(event))
                        continue;
                    System.out.println("Enter the name of the event: ");
                    info = scanner.nextLine();
                    e.setName(info);
                    System.out.println("Name changed.");
                    break;
                }
                break;
            case "desc"://Change description
                for (Event e : events) {
                    i++;
                    if (!e.equals(event))
                        continue;
                    System.out.println("Enter a description of the event: ");
                    info = scanner.nextLine();

                    e.setDesc(info);
                    System.out.println("Description changed.");
                    break;
                }
                break;
            default:
                System.out.println("Invalid command.");
        }
    }

    /**
     * Намира всички възможни свободни времеви интервали за събитие
     * в конкретен ден, базирано на вече съществуващите събития в календара.
     *
     * Въвежда се:
     * <ul>
     *     <li>Месец на събитието</li>
     *     <li>Ден от месеца</li>
     *     <li>Продължителност в часове (от 1 до 9)</li>
     * </ul>
     *
     * @return Списък с {@code Event} обекти, представляващи свободни интервали,
     *         или {@code null}, ако няма събития в календара.
     */
    public List<Event> findSlot() {
        if (events.isEmpty()) {
            System.out.println("No events in the schedule, of course you're free at that time!");
            return null;
        }
        Scanner scanner = new Scanner(System.in);
        int hours, month, day;

        do {
            System.out.println("Enter the month of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            month = scanner.nextInt();
            System.out.println('\n');

        } while (month < 1 || month > 12);

        do {
            System.out.println("Enter the day of the event: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Error, invalid number entered!");
                scanner.nextLine();
            }
            day = scanner.nextInt();
            System.out.println('\n');

        } while (day < 0 || day > Month.of(month).length(currentYear.isLeapYear()));

        do {
            System.out.println("Enter the duration of the event in hours: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Meeting duration must be between 1-9 hours");
                scanner.nextLine();
            }
            hours = scanner.nextInt();
            System.out.println('\n');

        } while (hours < 0 || hours > 9);

        List<Event> overlaps = new ArrayList<>();
        List<Event> avaliableSlots = new ArrayList<>();

        int currentHour = 8;
        while (currentHour <= 17 - hours) {
            for (int i = 0; i < 17 - hours; i++) {
                Event event = new Event("Slot", Month.of(month), day, currentHour, currentHour + hours, "empty slot");
                overlaps = findOverlap(event, events);
                if (overlaps.isEmpty())
                    avaliableSlots.add(event);
                currentHour++;
                if (currentHour >= 17 - hours)
                    break;
                overlaps.clear();
            }
        }

        return avaliableSlots;
    }
    /**
     * Намира общи свободни интервали между текущия календар и външен файл с други събития.
     * Използва се {@code findSlot()}, за да се получат свободни интервали от текущия календар,
     * след което се филтрират тези, които не се припокриват със събития от файл.
     *
     * <p>Подходящо за намиране на общо време за среща между потребители.</p>
     *
     * @return Списък от {@code Event} обекти с валидни интервали, съвпадащи и с календара,
     *         и с външния източник; или {@code null}, ако няма свободни интервали.
     */
    public List<Event> findSlotWith() {
        FileController fileController = new FileController();
        List<Event> avaliableSlots = findSlot();
        List<Event> avaliableCombinedSlots = new ArrayList<>();
        List<Event> fileEvents = fileController.readFileToArray();
        List<Event> overlaps = new ArrayList<>();
        int hours;

        if (avaliableSlots.isEmpty()) {
            System.out.println("No avaliable slots in the current calendar, no point in checking the file");
            return null;
        }

        hours = avaliableSlots.get(0).endTime.getHour() - avaliableSlots.get(0).startTime.getHour();

        int currentHour = 8;
        for (Event event : avaliableSlots) { //only check through the avaliable slots in the calendar since they need to match anyway

            for (Event e : fileEvents) {
                if (e.getMonth() == event.getMonth() && e.getDay() == event.getDay()) {
                    for (int j = event.startTime.getHour(); j <= event.endTime.getHour(); j++) {
                        if (j != event.endTime.getHour() && j == e.startTime.getHour()) {
                            overlaps.add(e);
                            break;
                        }
                        if (j == event.startTime.getHour()) {
                            if (j > e.startTime.getHour() && j < e.endTime.getHour()) {
                                overlaps.add(e);
                                break;
                            }
                        } else if (j > e.startTime.getHour() && j <= e.endTime.getHour()) {
                            overlaps.add(e);
                            break;
                        }
                    }
                }

            }

            if (overlaps.isEmpty())
                avaliableCombinedSlots.add(event);
            currentHour++;
            if (currentHour >= 17 - hours)
                break;
            overlaps.clear();
        }

        return avaliableCombinedSlots;
    }

    /**
     * Позволява на потребителя да въведе период (начална и крайна дата),
     * в който да се търсят най-натоварените дни от календара.
     *
     * <p>Извежда на екрана списък с обекти от тип {@code DayBusyness},
     * съдържащи дата и общ брой заети часове в рамките на този ден.</p>
     */
    public void showBusyDays() {
        Scanner scanner = new Scanner(System.in);
        int day, month;
        LocalDate from = LocalDate.of(1, 1, 1);
        LocalDate to = LocalDate.of(1, 1, 1);

        for (int i = 0; i < 2; i++) {
            if (i == 0)
                System.out.println("Enter the begining of the range for busy days (Month and day)\n");
            else
                System.out.println("Enter the end of the range for busy days (Month and day)\n");

            do {
                System.out.println("Enter the month of the event: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Error, invalid number entered!");
                    scanner.nextLine();
                }
                month = scanner.nextInt();
                System.out.println('\n');

            } while (month < 1 || month > 12);

            do {
                System.out.println("Enter the day of the event: ");
                while (!scanner.hasNextInt()) {
                    System.out.println("Error, invalid number entered!");
                    scanner.nextLine();
                }
                day = scanner.nextInt();
                System.out.println('\n');

            } while (day < 0 || day > Month.of(month).length(currentYear.isLeapYear()));

            if (i == 0)
                from = LocalDate.of(currentYear.getYear(), month, day);
            else
                to = LocalDate.of(currentYear.getYear(), month, day);
        }

        List<DayBusyness> results = busyDays(from, to);
        results.forEach(System.out::println);
    }

    /**
     * Изчислява и връща списък с най-натоварените дни в даден интервал от време.
     * Събитията се групират по дати и се изчислява общият брой заети часове за всяка дата.
     *
     * <p>Ако начален период е след края – извиква се изключение.</p>
     * <p>Празници могат да бъдат изключени от изчислението.</p>
     *
     * @param from Начална дата на интервала
     * @param to Крайна дата на интервала
     * @return Списък от {@code DayBusyness}, подреден по най-много заети часове в деня
     * @throws IllegalArgumentException ако {@code from} е след {@code to}
     */
    private List<DayBusyness> busyDays(LocalDate from, LocalDate to) {
        // Validate date range
        if (from.isAfter(to)) {
            throw new IllegalArgumentException("'From' date must be before 'To' date");
        }

        // Group events by day and calculate total hours per day
        Map<LocalDate, Double> dayHoursMap = events.stream()
                .filter(e -> !e.isHoliday()) // Optional: exclude holidays
                .filter(e -> !e.chosenDay.isBefore(from) && !e.chosenDay.isAfter(to))
                .collect(Collectors.groupingBy(
                        e -> e.chosenDay,
                        Collectors.summingDouble(e -> Duration.between(e.startTime, e.endTime).toHours())
                ));

        // Convert to sortable list
        return dayHoursMap.entrySet().stream()
                .map(entry -> new DayBusyness(entry.getKey(), entry.getValue()))
                .sorted(Comparator.comparingDouble(DayBusyness::totalHours).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Представлява запис за заетостта на конкретна дата.
     * Използва се за извеждане на обобщена информация за натоварени дни.
     *
     * @param date Датата на заетия ден
     * @param totalHours Общ брой заети часове в този ден
     */
    public record DayBusyness(LocalDate date, double totalHours) {
        /**
         * Връща текстово представяне на деня и общите заети часове
         *
         * @return Стринг във формат: {@code Ден от седмицата (брой часове)}
         */
        @Override
        public String toString() {
            return String.format("%s (%.1f hours)",
                    date.getDayOfWeek(), totalHours);
        }
    }

    /**
     * Търси събития в календара, чиито име или описание съдържат
     * зададен низ (case-insensitive).
     *
     * <p>Извежда съвпадащите събития на екрана, заедно с броя на намерените резултати.</p>
     *
     * @param info Текстов низ, който се търси в името или описанието на събитията
     */
    public void findEvents(String info) {
        if (events.isEmpty()) {
            System.out.println("The list of events is empty!");
            return;
        }

        List<Event> temp = new ArrayList<>();
        for (Event e : events) {
            if (e.name.toUpperCase().contains(info.toUpperCase()) || e.desc.toUpperCase().contains(info.toUpperCase()))
                temp.add(e);
        }
        int size = temp.size();
        if (size > 0) {
            int i = 0;
            for (Event e : temp) {
                i++;
                System.out.print(i + "/" + size);
                System.out.println(e.ShowEvent());
            }
            System.out.println("Total Matches : " + size);
        } else
            System.out.println("There are no events containing that/those keyword/s");
    }

    /**
     * Отпечатва всички събития в календара със съответния им индекс.
     * Подходящо за визуален преглед или избор на събитие по индекс.
     */
    public void printEvents() {
        int i = 0;
        for (Event e : events) {
            System.out.println("Index: " + i + '|');
            System.out.println(e.ShowEvent());
            i++;
        }
    }
}