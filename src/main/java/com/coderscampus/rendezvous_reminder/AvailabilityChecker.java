package com.coderscampus.rendezvous_reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.coderscampus.rendezvous_reminder.ReservationService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
public class AvailabilityChecker {
    @Autowired
    private ReservationService reservationService;

    public void checkAvailability() {
        System.out.println("Starting availability check...");
        final LocalDate startDate = LocalDate.of(2024, 12, 13);  // Hardcoded start date
        final LocalDate endDate = LocalDate.of(2025, 3, 15);  // Hardcoded end date

        // Get the map of available dates grouped by huts
        Map<String, List<LocalDate>> hutAvailabilityMap = reservationService.getAvailableDatesForHuts(startDate, endDate);

        if (hutAvailabilityMap.isEmpty()) {
            System.out.println("No spots available within the date range.");
        } else {
            System.out.println("Available spots by hut:");
            for (Map.Entry<String, List<LocalDate>> entry : hutAvailabilityMap.entrySet()) {
                String hutName = entry.getKey();
                List<LocalDate> dates = entry.getValue();

                if (!dates.isEmpty()) {
                    System.out.println(hutName + ":");
                    for (LocalDate date : dates) {
                        DayOfWeek dayOfWeek = date.getDayOfWeek();
                        String formattedDay = dayOfWeek.toString();

                        // Capitalize Friday, Saturday, and Sunday
                        if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                            formattedDay = formattedDay.toUpperCase();  // Explicitly capitalize (it's already upper, but for clarity)
                        } else {
                            formattedDay = formattedDay.charAt(0) + formattedDay.substring(1).toLowerCase();  // Capitalize first letter only
                        }

                        // Print date with day of the week
                        System.out.println(formattedDay + " - " + date );
                    }
                } else {
                    System.out.println(hutName + ": No available dates");
                }
            }
        }
    }
}
