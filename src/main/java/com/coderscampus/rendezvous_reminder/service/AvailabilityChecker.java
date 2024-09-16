package com.coderscampus.rendezvous_reminder.service;

import com.coderscampus.rendezvous_reminder.EmailService;
import com.coderscampus.rendezvous_reminder.domain.AvailabilityDate;
import com.coderscampus.rendezvous_reminder.domain.Hut;
import com.coderscampus.rendezvous_reminder.repository.AvailabilityDateRepository;
import com.coderscampus.rendezvous_reminder.repository.HutRepository;
import com.coderscampus.rendezvous_reminder.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class AvailabilityChecker {

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HutRepository hutRepository;

    @Autowired
    private AvailabilityDateRepository availabilityDateRepository;

    public void checkAvailability() {
        StringBuilder emailContent = new StringBuilder();

        System.out.println("Starting availability check...");
        final LocalDate startDate = LocalDate.of(2024, 12, 13);
        final LocalDate endDate = LocalDate.of(2025, 3, 15);

        // Get the current availability scan
        Map<String, List<LocalDate>> currentAvailabilityMap = reservationService.getAvailableDatesForHuts(startDate, endDate);

        if (currentAvailabilityMap.isEmpty()) {
            System.out.println("No spots available within the date range.");
            emailContent.append("No spots available within the date range.\n");
        } else {
            System.out.println("Available spots by hut:");
            emailContent.append("Available spots by hut:\n");

            for (Map.Entry<String, List<LocalDate>> entry : currentAvailabilityMap.entrySet()) {
                String hutName = entry.getKey();
                List<LocalDate> newDates = entry.getValue();

                // Retrieve the hut entity from the database
                Optional<Hut> hutOpt = hutRepository.findByName(hutName);
                if (hutOpt.isPresent()) {
                    Hut hut = hutOpt.get();

                    // Retrieve previously stored availability for this hut
                    List<AvailabilityDate> previousDates = availabilityDateRepository.findByHutAndAvailableDateBetween(hut, startDate, endDate);

                    if (!newDates.isEmpty()) {
                        System.out.println(hutName + ":");
                        emailContent.append(hutName).append(":\n");

                        for (LocalDate newDate : newDates) {
                            // Check if this date is new compared to previous scans
                            boolean isNew = previousDates.stream()
                                    .noneMatch(availabilityDate -> availabilityDate.getDate().equals(newDate));

                            DayOfWeek dayOfWeek = newDate.getDayOfWeek();
                            String formattedDay = dayOfWeek.toString();

                            // Capitalize Friday, Saturday, and Sunday
                            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                                formattedDay = formattedDay.toUpperCase();
                            } else {
                                formattedDay = formattedDay.charAt(0) + formattedDay.substring(1).toLowerCase();
                            }

                            // Print date with day of the week
                            String formattedDate = "  - " + newDate + " (" + formattedDay + ")";
                            System.out.println(formattedDate);
                            emailContent.append(formattedDate).append("\n");

                            // Add the new available date to the DB if it didn't exist before
                            if (isNew) {
                                AvailabilityDate availabilityDate = new AvailabilityDate();
                                availabilityDate.setDate(newDate);
                                availabilityDate.setHut(hut);

                                availabilityDateRepository.save(availabilityDate);
                            }
                        }
                    } else {
                        System.out.println(hutName + ": No available dates");
                        emailContent.append(hutName).append(": No available dates\n");
                    }
                } else {
                    System.out.println("Hut " + hutName + " not found in the database.");
                    emailContent.append("Hut ").append(hutName).append(" not found in the database.\n");
                }
            }
        }

        // Send the email with the content
        emailService.sendEmail("Hut Availability Report", emailContent.toString());
    }
}
