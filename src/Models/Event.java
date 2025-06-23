package Models;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Scanner;

/**
 * Клас, представящ събитие с информация за дата, време, описание и дали е празник.
 */
public class Event {
    /**
     * Име на събитието
     */
    private String name;
    /**
     * Начален час на събитието
     */
    private LocalTime startTime;
    /**
     * Краен час на събитието
     */
    private LocalTime endTime;
    /**
     * Дата, на която ще се проведе събитието
     */
    private LocalDate chosenDate;
    /**
     * Описание на събитието
     */
    private String desc;
    /**
     * Дали събитието е празник
     */
    private boolean holiday;
    /**
     * Текуща година за референция при промени по дата
     */
    private final LocalDate currentYear = LocalDate.now();

    /**
     * Копиращ конструктор.
     * Създава нов обект Models.Event чрез копиране на друг.
     *
     * @param e съществуващ обект Models.Event
     */
    public Event(Event e) {
        startTime = e.getStartTime();
        endTime = e.getEndTime();
        name = e.getName();
        chosenDate = e.getDate();
        desc = e.desc;
        holiday = e.holiday;
    }

    /**
     * Конструктор с параметри.
     *
     * @param name  име на събитието
     * @param month месец на провеждане
     * @param day   ден на провеждане
     * @param start начален час (цяло число от 0 до 23)
     * @param end   краен час (цяло число от 0 до 23)
     * @param desc  описание на събитието
     */
    public Event(String name, Month month, int day, int start, int end, String desc) {
        startTime = LocalTime.of(start, 0);
        endTime = LocalTime.of(end, 0);
        this.name = name;
        chosenDate = LocalDate.of(2025, month, day);
        this.desc = desc;
        holiday = false;
    }

    /**
     * Извежда информация за събитието под формата на низ.
     *
     * @return форматирана информация за събитието
     */
    public String ShowEvent() {
        StringBuilder sb = new StringBuilder();
        sb.append("Event: ");
        sb.append(name);
        sb.append('\n');
        sb.append("Description: ");
        sb.append(desc);
        sb.append('\n');
        sb.append("Scheduled at: ");
        sb.append(chosenDate);
        sb.append(" From ");
        sb.append(startTime);
        sb.append(" To ");
        sb.append(endTime);
        sb.append('\n');
        sb.append("-----------------------------------------------------\n");
        return sb.toString();
    }

    /**
     * Метод за промяна на съществуващо събитие.
     * Позволява редакция на дата, час, име и описание чрез конзолни команди.
     */
    public void change(String action) {
        Scanner scanner = new Scanner(System.in);
        int day, month, start, end;
        switch (action.toLowerCase()) {
            case "date": //Change the date
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

                setDate(LocalDate.of(currentYear.getYear(), month, day));
                System.out.println("Date changed");

                break;
            case "start": //Change the starting hour
                do {
                    System.out.println("Enter the starting hour of the event: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }
                    start = scanner.nextInt();
                    System.out.println('\n');

                } while (start < 0 || start >= 24 || start > endTime.getHour());

                setStartTime(LocalTime.of(start, 0));
                System.out.println("Start time changed!");
                break;
            case "end": //Change the ending hour
                do {
                    System.out.println("Enter the ending hour of the event: ");
                    while (!scanner.hasNextInt()) {
                        System.out.println("Error, invalid number entered!");
                        scanner.nextLine();
                    }
                    end = scanner.nextInt();
                    System.out.println('\n');

                } while (end < 0 || end >= 24 || end == startTime.getHour() || end < startTime.getHour());

                setEndTime(LocalTime.of(end, 0));
                System.out.println("End time changed!");
                break;
            default:
                System.out.println("Number out of range! Please choose a valid option.");
        }
    }

    public String getName() {
        return name;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public String getDesc() {
        return desc;
    }

    public LocalDate getDate(){
        return chosenDate;
    }

    /**
     * Връща годината на провеждане на събитието.
     *
     * @return година като цяло число
     */
    public int getYear() {
        return chosenDate.getYear();
    }

    /**
     * Връща месеца на събитието.
     *
     * @return месец от тип {@link Month}
     */
    public Month getMonth() {
        return chosenDate.getMonth();
    }

    /**
     * Връща деня на събитието.
     *
     * @return ден от месеца
     */
    public int getDay() {
        return chosenDate.getDayOfMonth();
    }

    /**
     * Задава името на събитието.
     *
     * @param name ново име
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Задава началния час на събитието.
     *
     * @param startTime нов начален час
     */
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Задава крайния час на събитието.
     *
     * @param endTime нов краен час
     */
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    /**
     * Задава нова дата за събитието.
     *
     * @param chosenDate нова дата
     */
    public void setDate(LocalDate chosenDate) {
        this.chosenDate = chosenDate;
    }

    /**
     * Задава описанието на събитието.
     *
     * @param desc ново описание
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Задава дали събитието е празник.
     *
     * @param holiday true ако е празник, false в противен случай
     */
    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    /**
     * Проверява дали събитието е маркирано като празник.
     *
     * @return true ако е празник, false в противен случай
     */
    public boolean isHoliday() {
        return holiday;
    }
}
