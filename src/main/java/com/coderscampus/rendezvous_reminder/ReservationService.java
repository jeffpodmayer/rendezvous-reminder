package com.coderscampus.rendezvous_reminder;

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
import java.util.ArrayList;
import java.util.List;

@Service
public class ReservationService {
    private final String url = "https://methowreservations.com/lodging/huts"; // Hardcoded URL

    public List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate) {
        WebDriver driver = new ChromeDriver();
        List<LocalDate> availableDates = new ArrayList<>();

        try {
            driver.get(url);

            // Locate the dropdown menu and select the desired option
            WebElement dropdown = driver.findElement(By.id("seasonal_year"));
            Select select = new Select(dropdown);
            select.selectByVisibleText("Winter 2024-2025");

            // Wait for the table to update based on the dropdown selection
            Thread.sleep(5000); // Wait for 5 seconds (or adjust as needed) to allow the table to refresh

            WebElement matrixScrollDiv = driver.findElement(By.id("matrixScroll"));
            WebElement dateTable = matrixScrollDiv.findElement(By.tagName("table"));
            List<WebElement> dateCells = dateTable.findElements(By.tagName("td"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("M/d");

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                String formattedDate = date.format(formatter);

                boolean spotFound = dateCells.stream()
                        .anyMatch(td -> {
                            WebElement span = td.findElement(By.tagName("span"));
                            String cellDate = span.getText().trim();
                            return cellDate.equals(formattedDate) && td.getAttribute("class").contains("vacant");
                        });

                if (spotFound) {
                    availableDates.add(date);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // Always quit the WebDriver to close the browser
        }

        return availableDates; // Return the list of available dates
    }
}

