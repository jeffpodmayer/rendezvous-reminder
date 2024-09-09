package com.coderscampus.rendezvous_reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.coderscampus.rendezvous_reminder.ReservationService;

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
        Map<String, List<LocalDate>> hutAvailabilityMap = reservationService.getHutAvailabilityInRange(startDate, endDate);

        if (hutAvailabilityMap.isEmpty()) {
            System.out.println("No spots available within the date range.");
        } else {
            System.out.println("Available huts:");
            for (String hutName : hutAvailabilityMap.keySet()) {
                System.out.println(hutName);
            }
        }
    }
}
