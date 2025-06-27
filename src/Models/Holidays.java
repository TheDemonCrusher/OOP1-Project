package Models;

import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

/**
 * Класът {@code Holidays} представлява мениджър за официални почивни дни.
 * Използва се Singleton дизайн шаблон за гарантиране на единствена инстанция.
 * Поддържа колекция от дати, маркирани като почивни дни.
 */
public class Holidays {
    /** Колекция от всички официални почивни дни. */
    private Set<LocalDate> holidays = new HashSet<>();
    /** Единствена инстанция на класа Holidays (Singleton). */
    private static Holidays holidayInstance;
    /**
     * Връща единствената инстанция на класа Holidays.
     * Ако все още не съществува, създава нова.
     *
     * @return инстанцията на Holidays
     */
    public static Holidays getInstance()
    {
        if (holidayInstance == null)
            holidayInstance = new Holidays();
        return holidayInstance;
    }
    /**
     * Връща всички зададени почивни дни.
     *
     * @return множество от дати, които са почивни дни
     */
    public Set<LocalDate> getHolidays() {
        return holidays;
    }
    /**
     * Задава ново множество от почивни дни.
     *
     * @param holidays множество от дати, които да се считат за почивни
     */
    public void setHolidays(Set<LocalDate> holidays) {
        this.holidays = holidays;
    }
    /**
     * Проверява дали дадена дата е почивен ден.
     *
     * @param date датата за проверка
     * @return {@code true}, ако датата е почивен ден; {@code false} в противен случай
     */
    public boolean isHoliday(LocalDate date)
    {
        if(holidays.contains(date))
            return true;
        return false;
    }
    /**
     * Добавя нова дата към списъка с почивни дни.
     *
     * @param date датата, която да бъде добавена като почивен ден
     */
    public void addHoliday(LocalDate date)
    {
        holidays.add(date);
    }
}
