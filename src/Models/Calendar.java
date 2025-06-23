package Models;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Класът Models.Calendar представлява календар, който съхранява и управлява събития за дадена година.
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
    public final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");

    /**
     * Текущата дата, използвана като база за текущата година.
     */
    public final LocalDate currentYear = LocalDate.now();

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
     * Връща единствената инстанция на класа Models.Calendar.
     * @return инстанция на Models.Calendar
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
        if (event.getStartTime().isAfter(event.getEndTime())) {
            Event day1 = new Event(event);
            Event day2 = new Event(event);
            day1.setName(day1.getName() + " pt.1");
            day2.setName(day2.getName() + " pt.2");
            day1.setEndTime(LocalTime.of(23, 59));

            if (event.getMonth().length(currentYear.isLeapYear()) > day1.getDay())
                day2.setDate(day2.getDate().plusDays(1));
            else {
                day2.setDate(day2.getDate().withDayOfMonth(1));
                day2.setDate(day2.getDate().plusMonths(1));
            }

            day2.setStartTime(LocalTime.of(0, 0));

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
            System.out.println("Event added successfully");
            return true;
        }

        List<Event> overlaps = findOverlap(event, events);
        if (overlaps.isEmpty()) {
            events.add(event);
            System.out.println("Event added successfully");
            return true;
        } else
            System.out.println("The event overlaps with other/s!");
        return false;
    }

    /**
     * Премахва събитие чрез въвеждане от потребителя.
     * Изисква потвърждение преди изтриване.
     */
    public void unbook(LocalDate date, LocalTime startTime, LocalTime endTime) {
        Scanner scanner = new Scanner(System.in);
        int i = 0;
        String choice = "";

        for (Event e : events) {
            if (e.getMonth() == date.getMonth() && e.getDay() == date.getDayOfMonth()) {
                if (e.getStartTime() == startTime && e.getEndTime() == endTime) {
                    i++;
                    System.out.println(e.ShowEvent());
                    System.out.println("Do you really want to delete this event?\n");
                    System.out.println("Type 'confirm' continue:\n");

                    choice = scanner.nextLine();

                    if (choice.equalsIgnoreCase("confirm")) {
                        removeEvent(e);
                        System.out.println("Models.Event has been removed.");
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
                for (int i = event.getStartTime().getHour(); i <= event.getEndTime().getHour(); i++) {
                    if (i != event.getEndTime().getHour() && i == e.getStartTime().getHour()) {
                        overlaps.add(e);
                        break;
                    }
                    if (i == event.getStartTime().getHour()) {
                        if (i > e.getStartTime().getHour() && i < e.getEndTime().getHour()) {
                            overlaps.add(e);
                            break;
                        }
                    } else if (i > e.getStartTime().getHour() && i <= e.getEndTime().getHour()) {
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
    public void agenda(LocalDate date) {
        int i = 0;
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
    public void change(Event event, String action) {
        System.out.println(event.ShowEvent());
        System.out.println("Is this the event you'd like to edit?");
        System.out.println("Type 'yes' to continue");
        Scanner scanner = new Scanner(System.in);
        String info = scanner.nextLine();
        if(!info.equalsIgnoreCase("yes"))
        {
            System.out.println("Event wasn't changed");
            return;
        }
        int day, month, start, end, i = 1, choice;



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
                temp.setDate(LocalDate.of(currentYear.getYear(), month, day));
                if (findOverlap(temp, others).isEmpty()) {
                    event.setDate(LocalDate.of(currentYear.getYear(), month, day));
                    System.out.println("Date changed");
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

                } while (start < 0 || start >= 24 || start > event.getEndTime().getHour());

                Event temp = new Event(event);
                temp.setStartTime(LocalTime.of(start, 0));
                if (findOverlap(temp, others).isEmpty()) {
                    event.setStartTime(LocalTime.of(start, 0));
                    System.out.println("Start time changed!");
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

                } while (end < 0 || end >= 24 || end == event.getStartTime().getHour() || end < event.getStartTime().getHour());

                Event temp = new Event(event);
                temp.setEndTime(LocalTime.of(end, 0));
                if (findOverlap(temp, others).isEmpty()) {
                    event.setEndTime(LocalTime.of(end, 0));
                    System.out.println("End time changed!");
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
     * @return Списък с {@code Models.Event} обекти, представляващи свободни интервали,
     *         или {@code null}, ако няма събития в календара.
     */
    public List<Event> findSlot(int day, int month, int hours) {
        if (events.isEmpty()) {
            System.out.println("No events in the schedule, of course you're free at that time!");
            return null;
        }

        List<Event> overlaps;
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

        if(avaliableSlots.isEmpty())
            System.out.println("There are no available slots at the chosen time\n");
        return avaliableSlots;
    }
    /**
     * Намира общи свободни интервали между текущия календар и външен файл с други събития.
     * Използва се {@code findSlot()}, за да се получат свободни интервали от текущия календар,
     * след което се филтрират тези, които не се припокриват със събития от файл.
     *
     * <p>Подходящо за намиране на общо време за среща между потребители.</p>
     *
     * @return Списък от {@code Models.Event} обекти с валидни интервали, съвпадащи и с календара,
     *         и с външния източник; или {@code null}, ако няма свободни интервали.
     */
    public List<Event> findSlotWith(int day, int month, int hours, String name) {
        FileController fileController = new FileController();
        List<Event> avaliableSlots = findSlot(day, month, hours);
        List<Event> avaliableCombinedSlots = new ArrayList<>();
        List<Event> fileEvents = fileController.readFileToArray(name);
        List<Event> overlaps = new ArrayList<>();

        if (avaliableSlots.isEmpty()) {
            System.out.println("No avaliable slots in the current calendar, no point in checking the file");
            return null;
        }

        hours = avaliableSlots.get(0).getEndTime().getHour() - avaliableSlots.get(0).getStartTime().getHour();

        int currentHour = 8;
        for (Event event : avaliableSlots) { //only check through the avaliable slots in the calendar since they need to match anyway

            for (Event e : fileEvents) {
                if (e.getMonth() == event.getMonth() && e.getDay() == event.getDay()) {
                    for (int j = event.getStartTime().getHour(); j <= event.getEndTime().getHour(); j++) {
                        if (j != event.getEndTime().getHour() && j == e.getStartTime().getHour()) {
                            overlaps.add(e);
                            break;
                        }
                        if (j == event.getStartTime().getHour()) {
                            if (j > e.getStartTime().getHour() && j < e.getEndTime().getHour()) {
                                overlaps.add(e);
                                break;
                            }
                        } else if (j > e.getStartTime().getHour() && j <= e.getEndTime().getHour()) {
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
    public void showBusyDays(LocalDate from, LocalDate to) {
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
                .filter(e -> !e.getDate().isBefore(from) && !e.getDate().isAfter(to))
                .collect(Collectors.groupingBy(
                        Event::getDate,
                        Collectors.summingDouble(e -> Duration.between(e.getStartTime(), e.getEndTime()).toHours())
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
            if (e.getName().toUpperCase().contains(info.toUpperCase()) || e.getDesc().toUpperCase().contains(info.toUpperCase()))
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