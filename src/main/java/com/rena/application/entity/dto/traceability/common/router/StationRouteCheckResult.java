package com.rena.application.entity.dto.traceability.common.router;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StationRouteCheckResult {
    private Boolean isOk;
    private String message;
    private String[] notPassedStations;
    private String[] failedStations;

    public String formatErrorStations() {
        String stationsInfo = Stream.concat(
                        Arrays.stream(notPassedStations != null ? notPassedStations : new String[0])
                                .map(station -> "- " + station + " (не пройдена)"),
                        Arrays.stream(failedStations != null ? failedStations : new String[0])
                                .map(station -> "- " + station + " (NOK)")
                )
                .sorted()
                .collect(Collectors.joining("\n"));

        // Если информации о станциях нет, вернуть основное сообщение
        return stationsInfo.isEmpty() ? message : stationsInfo;
    }

    public static StationRouteCheckResult of(boolean isOk, String message, String[] notPassed, String[] failed) {
        return new StationRouteCheckResult(
                isOk,
                message,
                notPassed != null ? notPassed : new String[0],
                failed != null ? failed : new String[0]
        );
    }

    public static StationRouteCheckResult success(String message) {
        return of(true, message, null, null);
    }

    public static StationRouteCheckResult failure(String message, String[] notPassed, String[] failed) {
        return of(false, message, notPassed, failed);
    }

    public static StationRouteCheckResult failure(String message) {
        return of(false, message, null, null);
    }
}