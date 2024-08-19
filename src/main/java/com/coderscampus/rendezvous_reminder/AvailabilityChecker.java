package com.coderscampus.rendezvous_reminder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.coderscampus.rendezvous_reminder.ReservationService;

import java.time.LocalDate;
import java.util.List;

@Component
public class AvailabilityChecker {
    @Autowired
    private ReservationService reservationService;

    public void checkAvailability() {
        System.out.println("Starting availability check...");
        final LocalDate startDate = LocalDate.of(2024, 12, 13);  // Hardcoded start date
        final LocalDate endDate = LocalDate.of(2025, 3, 15);  // Hardcoded end date


        List<LocalDate> availableDates = reservationService.getAvailableDatesInRange(startDate, endDate);

        if (availableDates.isEmpty()) {
            System.out.println("No spots available within the date range.");
        } else {
            System.out.println("Available spots on the following dates:");
            for (LocalDate date : availableDates) {
                System.out.println(date);
            }
        }
    }
}
