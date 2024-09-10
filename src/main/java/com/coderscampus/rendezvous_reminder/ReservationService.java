package com.coderscampus.rendezvous_reminder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class ReservationService {
    private final String url = "https://methowreservations.com/lodging/huts"; // Hardcoded URL

//    public List<LocalDate> getAvailableDatesInRange(LocalDate startDate, LocalDate endDate) {
//        WebDriver driver = new ChromeDriver();
//        List<LocalDate> availableDates = new ArrayList<>();
//
//        try {
//            driver.get(url);
//
//            // Locate the dropdown menu and select the desired option
//            WebElement dropdown = driver.findElement(By.id("seasonal_year"));
//            Select select = new Select(dropdown);
//            select.selectByVisibleText("Winter 2024-2025");
//
//            // Wait for the table to update based on the dropdown selection
//            Thread.sleep(5000); // Wait for 5 seconds (or adjust as needed) to allow the table to refresh
//
//            // Locate the main table containing the rows of interest
//            WebElement matrixScrollDiv = driver.findElement(By.id("matrixScroll"));
//            WebElement dateTable = matrixScrollDiv.findElement(By.tagName("table"));
//            List<WebElement> rows = dateTable.findElements(By.tagName("tr"));
//
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMMd_yyyy");
//
//            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
//                String formattedDate = date.format(formatter);
//
//                for (int i = 1; i < rows.size(); i++) { // Start at 1 to skip the first row
//                    WebElement row = rows.get(i);
//                    List<WebElement> cells = row.findElements(By.tagName("td"));
//
//                    for (WebElement cell : cells) {
//                        if (cell.getAttribute("class").contains("vacant")) {
//                            WebElement div = cell.findElement(By.tagName("div"));
//                            WebElement input = div.findElement(By.tagName("input"));
//                            String inputId = input.getAttribute("id");
//
//                            // Extract and format the date from the inputId
//                            String extractedDate = inputId.split("_")[0] + "_" + inputId.split("_")[1];
//
//                            // If the extractedDate matches the formatted date, add the date to the list
//                            if (formattedDate.equals(extractedDate)) {
//                                availableDates.add(date);
//                                break;
//                            }
//                        }
//                    }
//                }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            driver.quit(); // Always quit the WebDriver to close the browser
//        }
//
//        return availableDates; // Return the list of available dates
//    }

    public Map<String, List<LocalDate>> getAvailableDatesForHuts(LocalDate startDate, LocalDate endDate) {
        WebDriver driver = new ChromeDriver();
        Map<String, List<LocalDate>> hutAvailability = new HashMap<>();

        // Predefined list of hut names
        List<String> hutNames = Arrays.asList("Heifer Hut", "Rendezvous Hut", "Gardner Hut", "Cassal Hut", "Grizzly Hut");

        try {
            driver.get(url);

            // Select the desired option in the dropdown
            WebElement dropdown = driver.findElement(By.id("seasonal_year"));
            Select select = new Select(dropdown);
            select.selectByVisibleText("Winter 2024-2025");

            // Wait for the table to update
            Thread.sleep(5000);

            // Locate the table containing the availability data
            WebElement matrixScrollDiv = driver.findElement(By.id("matrixScroll"));
            WebElement dateTable = matrixScrollDiv.findElement(By.tagName("table"));
            List<WebElement> rows = dateTable.findElements(By.tagName("tr"));

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMMd_yyyy");

            // Iterate over each row corresponding to each hut
            for (int i = 0; i < hutNames.size(); i++) {
                String hutName = hutNames.get(i);
                WebElement row = rows.get(i + 1); // Skip the first row (header row)
                List<WebElement> cells = row.findElements(By.tagName("td"));
                List<LocalDate> availableDates = new ArrayList<>();

                // Iterate over each cell in the row to find vacancies
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    String formattedDate = date.format(formatter);

                    for (WebElement cell : cells) {
                        if (cell.getAttribute("class").contains("vacant")) {
                            WebElement div = cell.findElement(By.tagName("div"));
                            WebElement input = div.findElement(By.tagName("input"));
                            String inputId = input.getAttribute("id");

                            // Extract and format the date from the inputId
                            String extractedDate = inputId.split("_")[0] + "_" + inputId.split("_")[1];

                            // If the extractedDate matches the formatted date, add the date to the list
                            if (formattedDate.equals(extractedDate)) {
                                availableDates.add(date);
                            }
                        }
                    }
                }

                hutAvailability.put(hutName, availableDates);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit(); // Always quit the WebDriver to close the browser
        }

        return hutAvailability;
    }
}


