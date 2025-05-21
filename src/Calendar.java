import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Calendar {

    private List<Event> events = new ArrayList<>();
    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy");
    final LocalDate currentYear = LocalDate.now();
    //The calendar is a singleton since we only have one active calendar in the app at once
    private static Calendar calendarInstance;

    private Calendar() {
        formatter.format(currentYear);
    }

    public static Calendar getInstance() {
        if (calendarInstance == null)
            calendarInstance = new Calendar();
        return calendarInstance;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }

    public void removeEvent(Event event) {
        int index = events.indexOf(event);
        if (index != -1)
            events.remove(index);
    }

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
        boolean notBusy = true;


        for (Event e : events) {
            if (e.getMonth() == event.getMonth() && e.getDay() == event.getDay()) {
                for (int i = event.startTime.getHour(); i < event.endTime.getHour(); i++) {
                    if (i > e.startTime.getHour() && i < e.endTime.getHour()) {
                        notBusy = false;
                        break;
                    }
                }
            }
        }
        if (notBusy) {
            events.add(event);
            return true;
        }
        return false;
    }

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
                    e.ShowEvent();
                    System.out.println("Do you really want to delete this event?\n");
                    System.out.println("Type 'confirm' continue:\n");

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

    public List<Event> findOverlap(Event event) {
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

    public void change(Event event) {

        Scanner scanner = new Scanner(System.in);
        String action;
        int day, month, start, end, i = 0;
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
        switch (action.toLowerCase()) {
            case "date": //Change date
                for (Event e : events) {
                    i++;
                    if (!e.equals(event))
                        continue;

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

                    if (findOverlap(e).isEmpty())
                        e.setChosenDay(LocalDate.of(currentYear.getYear(), month, day));
                    else
                        System.out.println("Change not possible due to overlapping event/s!");
                    break;
                }
                break;
            case "start": //Change start time
                for (Event e : events) {
                    i++;
                    if (!e.equals(event))
                        continue;

                    do {
                        System.out.println("Enter the starting hour of the event: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Error, invalid number entered!");
                            scanner.nextLine();
                        }
                        start = scanner.nextInt();
                        System.out.println('\n');

                    } while (start < 0 || start >= 24 || start > e.endTime.getHour());

                    if (findOverlap(e).isEmpty())
                        e.setStartTime(LocalTime.of(start, 0));
                    else
                        System.out.println("Change not possible due to overlapping event/s!");
                    break;
                }
                break;
            case "end": //Change end time
                for (Event e : events) {
                    i++;
                    if (!e.equals(event))
                        continue;

                    do {
                        System.out.println("Enter the ending hour of the event: ");
                        while (!scanner.hasNextInt()) {
                            System.out.println("Error, invalid number entered!");
                            scanner.nextLine();
                        }
                        end = scanner.nextInt();
                        System.out.println('\n');

                    } while (end < 0 || end >= 24 || end == e.startTime.getHour() || end < e.startTime.getHour());

                    if (findOverlap(e).isEmpty())
                        e.setEndTime(LocalTime.of(end, 0));
                    else
                        System.out.println("Change not possible due to overlapping event/s!");

                    break;
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
                overlaps = findOverlap(event);
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

    public List<Event> findSlotWith() {
        FileController fileController = new FileController();
        List<Event> avaliableSlots = findSlot();
        List<Event> avaliableCombinedSlots = new ArrayList<>();
        List<Event> result = new ArrayList<>();
        List<Event> fileEvents = fileController.readFileToArray();
        List<Event> overlaps = new ArrayList<>();
        int day, month, hours;

        if (avaliableSlots.isEmpty()) {
            System.out.println("No avaliable slots in the current calendar, no point in checking the file");
            return null;
        }

        month = avaliableSlots.get(0).getMonth().getValue();
        day = avaliableSlots.get(0).getDay();
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

    public record DayBusyness(LocalDate date, double totalHours) {
        @Override
        public String toString() {
            return String.format("%s (%.1f hours)",
                    date.getDayOfWeek(), totalHours);
        }
    }

    public void findEvents(String info) {
        List<Event> temp = new ArrayList<>();
        for (Event e : events) {
            if (e.name.toUpperCase().contains(info.toUpperCase()) || e.desc.toUpperCase().contains(info.toUpperCase()))
                temp.add(e);
        }
        int size = temp.size();
        if (size > 0) {
            Scanner scanner = new Scanner(System.in);
            int i = 0, choice;
            for (Event e : temp) {
                i++;
                e.ShowEvent();
                System.out.println(i + "/" + size);
            }
            System.out.println("Total Matches : " + size);
        } else
            System.out.println("There are no events containing that/those keyword/s");
    }

    public void printEvents() {
        int i = 0;
        for (Event e : events) {
            System.out.println("Index: " + i + '|');
            System.out.println(e.ShowEvent());
            i++;
        }
    }
}