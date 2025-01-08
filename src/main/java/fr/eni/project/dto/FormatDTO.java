package fr.eni.project.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatDTO {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy à HH:mm");

    // Méthode pour formater une date
    public static String formatDate(LocalDateTime dateTime) {
        if (dateTime == null) {
            return "Date non définie";
        }
        return dateTime.format(formatter);
    }
}
