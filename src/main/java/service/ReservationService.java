package service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ReservationService {
    private final String url = "https://methowreservations.com/lodging/huts"; // Hardcoded URL
    private final LocalDate startDate = LocalDate.of(2024, 12, 13);  // Hardcoded start date
    private final LocalDate endDate = LocalDate.of(2025, 3, 15);  // Hardcoded end date

    public boolean spotsAvailableInRange(LocalDate startDate, LocalDate endDate) {
        WebDriver driver = new ChromeDriver();
        try {
            driver.get(url);

            // Locate the dropdown menu and select the desired option
            WebElement dropdown = driver.findElement(By.id("seasonal_year"));
            Select select = new Select(dropdown);
            select.selectByVisibleText("Winter 2024-2025");

            // Wait for the table to update based on the dropdown selection
            Thread.sleep(5000); // Wait for 2 seconds (or more if needed) to allow the table to refresh

            // Locate the table and iterate through dates
            WebElement table = driver.findElement(By.tagName("table")); // Replace with the actual table ID or locator
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                String formattedDate = date.format(formatter);

                boolean spotFound = rows.stream()
                        .flatMap(row -> row.findElements(By.tagName("td")).stream())
                        .anyMatch(td -> {
                            String className = td.getAttribute("class");
                            if ("vacant".equals(className)) {
                                WebElement div = td.findElement(By.tagName("div"));
                                String divId = div.getAttribute("id");
                                return formattedDate.equals(divId);
                            }
                            return false;
                        });

                if (spotFound) {
                    return true; // A spot is available on this date
                }
            }

            return false; // No spots available in the entire range

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            driver.quit(); // Always quit the WebDriver to close the browser
        }
    }
}

