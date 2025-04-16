package com.rena.application.service.traceability.helper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class WpOneHelper {
    public static String getSerialNumber(Integer nextBoilerNumber, String article) {
        var manufacture = "EngP3";
        var dateNow = getDateNow();
        var barcodeNumber = getBarcodeNumber(nextBoilerNumber);
        return String.format("%s%s%s%s", manufacture, dateNow, barcodeNumber, article);
    }

    private static String getDateNow() {
        var dateNow = LocalDateTime.now();
        var formatter = DateTimeFormatter.ofPattern("ddMMyyyy");
        return dateNow.format(formatter);
    }

    private static String getBarcodeNumber(Integer number) {
        return String.format("%06d", number);
    }
}
