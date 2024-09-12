//package com.coderscampus.rendezvous_reminder;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import com.coderscampus.rendezvous_reminder.ReservationService;
//
//import java.time.DayOfWeek;
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Map;
//
//@Component
//public class AvailabilityChecker {
//    @Autowired
//    private ReservationService reservationService;
//
//    @Autowired
//    private EmailService emailService;
//
//    public void checkAvailability() {
//        StringBuilder emailContent = new StringBuilder();  // To store the email content
//
//        System.out.println("Starting availability check...");
//        final LocalDate startDate = LocalDate.of(2024, 12, 13);  // Hardcoded start date
//        final LocalDate endDate = LocalDate.of(2025, 3, 15);  // Hardcoded end date
//
//        // Get the map of available dates grouped by huts
//        Map<String, List<LocalDate>> hutAvailabilityMap = reservationService.getAvailableDatesForHuts(startDate, endDate);
//
//        if (hutAvailabilityMap.isEmpty()) {
//            System.out.println("No spots available within the date range.");
//            emailContent.append("No spots available within the date range.\n");
//        } else {
//            System.out.println("Available spots by hut:");
//            emailContent.append("Available spots by hut:\n");
//
//            for (Map.Entry<String, List<LocalDate>> entry : hutAvailabilityMap.entrySet()) {
//                String hutName = entry.getKey();
//                List<LocalDate> dates = entry.getValue();
//
//                if (!dates.isEmpty()) {
//                    System.out.println(hutName + ":");
//                    emailContent.append(hutName).append(":\n");
//
//                    for (LocalDate date : dates) {
//                        DayOfWeek dayOfWeek = date.getDayOfWeek();
//                        String formattedDay = dayOfWeek.toString();
//
//                        // Capitalize Friday, Saturday, and Sunday
//                        if (dayOfWeek == DayOfWeek.FRIDAY || dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY) {
//                            formattedDay = formattedDay.toUpperCase();
//                        } else {
//                            formattedDay = formattedDay.charAt(0) + formattedDay.substring(1).toLowerCase();
//                        }
//
//                        // Print date with day of the week
//                        String formattedDate = "  - " + date + " (" + formattedDay + ")";
//                        System.out.println(formattedDate);
//                        emailContent.append(formattedDate).append("\n");
//                    }
//                } else {
//                    System.out.println(hutName + ": No available dates");
//                    emailContent.append(hutName).append(": No available dates\n");
//                }
//            }
//        }
//
//        // Send the email with the content
//        emailService.sendEmail("Hut Availability Report", emailContent.toString());
//    }
//}
