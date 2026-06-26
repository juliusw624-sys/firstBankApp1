package bank.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.Year;

/** Calendar helpers for the Date-of-Birth combo boxes. */
public final class DateUtil {

    private DateUtil() { }

    /** Number of days in the given month/year, leap-year aware. */
    public static int daysInMonth(int year, int month) {
        switch (month) {
            case 1: case 3: case 5: case 7: case 8: case 10: case 12:
                return 31;
            case 4: case 6: case 9: case 11:
                return 30;
            case 2:
                return Year.isLeap(year) ? 29 : 28;
            default:
                throw new IllegalArgumentException("Invalid month: " + month);
        }
    }

    /** Age in whole years on the current system date. */
    public static int calculateAge(LocalDate dateOfBirth) {
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
