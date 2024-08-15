package service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ReservationService {
    private final String url = "https://methowreservations.com/lodging/huts"; // Hardcoded URL
    private final LocalDate startDate = LocalDate.of(2024, 12, 13);  // Hardcoded start date
    private final LocalDate endDate = LocalDate.of(2025, 3, 15);  // Hardcoded end date
}
