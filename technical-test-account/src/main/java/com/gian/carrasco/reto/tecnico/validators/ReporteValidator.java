package com.gian.carrasco.reto.tecnico.validators;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ReporteValidator {
    private static final String DATE_PATTERN = "[0-9]{1,4}-[0-9]{1,2}-[0-9]{1,2}";
    private static final String DATE_FORMAT = "yyyy-MM-dd";

    public static boolean isInvalidDateFormat(String date) {
        return date == null || !date.matches(DATE_PATTERN);
    }

    public static LocalDate toDate(String date) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(DATE_FORMAT);
        return LocalDate.parse(date, dtf);
    }
}