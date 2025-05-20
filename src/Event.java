import java.time.LocalDate;
import java.time.LocalTime;
import java.time.Month;
import java.util.Scanner;

public class Event {
String name;
LocalTime startTime;
LocalTime endTime;
LocalDate chosenDay;
String desc;
boolean holiday;
final LocalDate currentYear = LocalDate.now();

    public Event(Event e)
    {
        startTime = e.startTime;
        endTime = e.endTime;
        name = e.name;
        chosenDay = e.chosenDay;
        desc = e.desc;
        holiday = e.holiday;
    }

    public Event(String name, Month month, int day, int start, int end, String desc) {
        startTime = LocalTime.of(start, 0);
        endTime = LocalTime.of(end, 0);
        this.name = name;
        chosenDay = LocalDate.of(2025, month, day);
        this.desc = desc;
        holiday = false;
    }

    public void setHoliday(boolean holiday) {
        this.holiday = holiday;
    }

    public int getYear(){
        return chosenDay.getYear();
    }
    public Month getMonth() {
        return chosenDay.getMonth();
    }

    public int getDay() {
        return chosenDay.getDayOfMonth();
    }

    public String ShowEvent()
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Event: ");
        sb.append(name);
        sb.append('\n');
        sb.append("Description: ");
        sb.append(desc);
        sb.append('\n');
        sb.append("Scheduled at: ");
        sb.append(chosenDay);
        sb.append(" From ");
        sb.append(startTime);
        sb.append(" To ");
        sb.append(endTime);
        sb.append('\n');
        sb.append("-----------------------------------------------------\n");
        return sb.toString();
    }

    public void change()
    {
        Scanner scanner = new Scanner(System.in);
        String action;
        int day, month, start, end, i = 0;
        String info;

        while(true) {
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
            //All actions must lead back to the main menu at the end!
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

                    setChosenDay(LocalDate.of(currentYear.getYear(), month, day));
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
                    break;
                case "name"://Change the name
                    System.out.println("Enter the name of the event: ");
                    info = scanner.nextLine();
                    System.out.println('\n');

                    setName(info);
                    break;
                case "desc"://Change the desc
                    System.out.println("Enter a description of the event: ");
                    info = scanner.nextLine();
                    System.out.println('\n');

                    setDesc(info);
                    break;
                default:
                    System.out.println("Number out of range! Please choose a valid option.");
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public void setChosenDay(LocalDate chosenDay) {
        this.chosenDay = chosenDay;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public boolean isHoliday() {
        return holiday;
    }
}
