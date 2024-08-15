package service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;

@Service
public class ReservationService {
    private final String url = "https://methowreservations.com/lodging/huts"; // Hardcoded URL
    private final LocalDate startDate = LocalDate.of(2024, 12, 13);  // Hardcoded start date
    private final LocalDate endDate = LocalDate.of(2025, 3, 15);  // Hardcoded end date

    // This method scrapes the webpage.
    private boolean spotIsAvailable(LocalDate date) {
        try {
            Document doc = Jsoup.connect(url).get();
            // Example: Assume that available dates have a specific CSS class
            Elements availableDates = doc.select(".available-date[data-date='" + date + "']");
            return !availableDates.isEmpty();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
