package getterson.insight.utils;

import getterson.insight.exceptions.IllegalDateException;

import java.time.LocalDate;
import java.time.format.*;
import java.time.temporal.ChronoUnit;
import java.util.*;

public class DateUtil {
    public static final String DEFAULT_DATE_PATTERN = "dd/MM/yyyy";
    public static final String ISO8601_DATE_PATTERN = "yyyy-MM-dd";
    private static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
    private static final double DAY_DURATION = ChronoUnit.DAYS.getDuration().getSeconds();

    public static String dateToString(LocalDate date) {
        return date.format(DEFAULT_FORMATTER);
    }

    public static LocalDate stringToDate(String dateStr, String datePattern) throws IllegalDateException {
        if (!isValidDate(dateStr, datePattern)) {
            throw new IllegalDateException("Data inválida: " + dateStr + ". Verifique se o formato está correto e se a data é válida.");
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
        return LocalDate.parse(dateStr, formatter);
    }

    public static boolean isValidDate(String dateStr, String datePattern) {
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(datePattern);
            LocalDate date = LocalDate.parse(dateStr, formatter);
            return date.getMonthValue() != 2 || date.getDayOfMonth() != 29 || date.isLeapYear();
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static List<LocalDate> getDaysBetweenDates(LocalDate startDate, LocalDate endDate) throws IllegalDateException {
        List<LocalDate> daysBetween = new ArrayList<>();

        if (startDate.isAfter(endDate)) {
            throw new IllegalDateException("A data de início deve ser anterior à data de término.");
        }

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            daysBetween.add(date);
        }

        return daysBetween;
    }

    public static boolean validateDifferenceBetweenDate(LocalDate startDate, LocalDate endDate) {
        return endDate.isBefore(startDate);
    }

    public static double calculateDifferenceBetweenDate(LocalDate startDate, LocalDate endDate) throws IllegalDateException {
        if (!validateDifferenceBetweenDate(startDate, endDate)) throw new IllegalDateException("Data de início não pode ser menor que data de fim.");
        return Math.abs(ChronoUnit.DAYS.between(startDate, endDate));
    }
}
