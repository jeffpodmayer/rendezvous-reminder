package com.coderscampus.rendezvous_reminder.service;

import com.coderscampus.rendezvous_reminder.EmailService;
import com.coderscampus.rendezvous_reminder.domain.AvailabilityDate;
import com.coderscampus.rendezvous_reminder.domain.Hut;
import com.coderscampus.rendezvous_reminder.repository.AvailabilityDateRepository;
import com.coderscampus.rendezvous_reminder.repository.HutRepository;
import com.coderscampus.rendezvous_reminder.service.ReservationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;package com.coderscampus.rendezvous_reminder.service;

import com.coderscampus.rendezvous_reminder.EmailService;
import com.coderscampus.rendezvous_reminder.domain.AvailabilityDate;
import com.coderscampus.rendezvous_reminder.domain.Hut;
import com.coderscampus.rendezvous_reminder.repository.AvailabilityDateRepository;
import com.coderscampus.rendezvous_reminder.repository.HutRepository;
import com.coderscampus.rendezvous_reminder.service.ReservationService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @PostConstruct
    public void init() {
        checkAvailability();
    }

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
                    List<AvailabilityDate> previousDates = availabilityDateRepository.findByHutAndDateBetween(hut, startDate, endDate);

                    List<LocalDate> previousDatesList = previousDates.stream()
                            .map(AvailabilityDate::getDate)
                            .toList();

                    if (!newDates.isEmpty()) {
                        System.out.println(hutName + ":");
                        emailContent.append(hutName).append(":\n");

                        // Track newly added dates
                        for (LocalDate newDate : newDates) {
                            boolean isNew = previousDatesList.stream()
                                    .noneMatch(prevDate -> prevDate.equals(newDate));

                            DayOfWeek dayOfWeek = newDate.getDayOfWeek();
                            String formattedDay = dayOfWeek.toString();

                            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                                formattedDay = formattedDay.toUpperCase();
                            } else {
                                formattedDay = formattedDay.charAt(0) + formattedDay.substring(1).toLowerCase();
                            }

                            // Add information about added dates
                            String formattedDate = "  - ADDED: " + newDate + " (" + formattedDay + ")";
                            System.out.println(formattedDate);
                            emailContent.append(formattedDate).append("\n");

                            // Save new available date if it didn't exist before
                            if (isNew) {
                                AvailabilityDate availabilityDate = new AvailabilityDate();
                                availabilityDate.setDate(newDate);
                                availabilityDate.setHut(hut);

                                availabilityDateRepository.save(availabilityDate);
                            }
                        }

                        // Track removed dates
                        for (AvailabilityDate prevDate : previousDates) {
                            boolean isRemoved = newDates.stream()
                                    .noneMatch(newDate -> newDate.equals(prevDate.getDate()));

                            if (isRemoved) {
                                String removedDateInfo = "  - REMOVED: " + prevDate.getDate() + " (" + prevDate.getDate().getDayOfWeek() + ")";
                                System.out.println(removedDateInfo);
                                emailContent.append(removedDateInfo).append("\n");

                                // Optionally remove the date from the database if it is no longer available
                                availabilityDateRepository.delete(prevDate);
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

    @PostConstruct
    public void init() {
        checkAvailability();
    }

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
                    List<AvailabilityDate> previousDates = availabilityDateRepository.findByHutAndDateBetween(hut, startDate, endDate);

                    List<LocalDate> previousDatesList = previousDates.stream()
                            .map(AvailabilityDate::getDate)
                            .toList();

                    if (!newDates.isEmpty()) {
                        System.out.println(hutName + ":");
                        emailContent.append(hutName).append(":\n");

                        // Track newly added dates
                        for (LocalDate newDate : newDates) {
                            boolean isNew = previousDatesList.stream()
                                    .noneMatch(prevDate -> prevDate.equals(newDate));

                            DayOfWeek dayOfWeek = newDate.getDayOfWeek();
                            String formattedDay = dayOfWeek.toString();

                            if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
                                formattedDay = formattedDay.toUpperCase();
                            } else {
                                formattedDay = formattedDay.charAt(0) + formattedDay.substring(1).toLowerCase();
                            }

                            // Add information about added dates
                            String formattedDate = "  - ADDED: " + newDate + " (" + formattedDay + ")";
                            System.out.println(formattedDate);
                            emailContent.append(formattedDate).append("\n");

                            // Save new available date if it didn't exist before
                            if (isNew) {
                                AvailabilityDate availabilityDate = new AvailabilityDate();
                                availabilityDate.setDate(newDate);
                                availabilityDate.setHut(hut);

                                availabilityDateRepository.save(availabilityDate);
                            }
                        }

                        // Track removed dates
                        for (AvailabilityDate prevDate : previousDates) {
                            boolean isRemoved = newDates.stream()
                                    .noneMatch(newDate -> newDate.equals(prevDate.getDate()));

                            if (isRemoved) {
                                String removedDateInfo = "  - REMOVED: " + prevDate.getDate() + " (" + prevDate.getDate().getDayOfWeek() + ")";
                                System.out.println(removedDateInfo);
                                emailContent.append(removedDateInfo).append("\n");

                                // Optionally remove the date from the database if it is no longer available
                                availabilityDateRepository.delete(prevDate);
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
