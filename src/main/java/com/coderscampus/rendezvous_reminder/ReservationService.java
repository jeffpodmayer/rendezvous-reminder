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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Map<String, List<LocalDate>> getHutAvailabilityInRange(LocalDate startDate, LocalDate endDate) {
        WebDriver driver = new ChromeDriver();
        Map<String, List<LocalDate>> hutAvailabilityMap = new HashMap<>();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Wait up to 10 seconds

        try {
            driver.get(url);

            WebElement dropdown = driver.findElement(By.id("seasonal_year"));
            Select select = new Select(dropdown);
            select.selectByVisibleText("Winter 2024-2025");

            // Wait for the table to update
            Thread.sleep(5000); // Adjust if necessary

            // Find the table
            WebElement table = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("table")));

            // Find the <span> containing all hut names
            WebElement span = table.findElement(By.cssSelector("span.rooms"));

            // Find all hut name divs within the <span>
            List<WebElement> hutNameElements = span.findElements(By.cssSelector("div.name"));

            // Debugging: Print all hut names found
            System.out.println("Hut Names Found:");
            for (WebElement hutNameElement : hutNameElements) {
                String hutName = hutNameElement.getText().trim();
                System.out.println("  - " + hutName);
                hutAvailabilityMap.putIfAbsent(hutName, new ArrayList<>());
            }

            // Find all rows in the table
            List<WebElement> rows = table.findElements(By.tagName("tr"));

            // Iterate through the rows to find hut availability
            for (WebElement row : rows) {
                // Find all cells in the row
                List<WebElement> cells = row.findElements(By.tagName("td"));

                if (cells.size() > 1) { // Ensure there's more than one cell in the row
                    // Process each cell starting from index 1 (to skip the hut name cell)
                    for (int j = 1; j < cells.size(); j++) {
                        WebElement cell = cells.get(j);

                        if (cell.getAttribute("class").contains("vacant")) {
                            WebElement div = cell.findElement(By.tagName("div"));
                            WebElement input = div.findElement(By.tagName("input"));
                            String inputId = input.getAttribute("id");

                            // Extract date from inputId and parse it
                            String[] parts = inputId.split("_");
                            if (parts.length >= 2) {
                                String extractedDate = parts[0] + "_" + parts[1];
                                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMMd_yyyy");
                                LocalDate date = LocalDate.parse(extractedDate, formatter);

                                if (!date.isBefore(startDate) && !date.isAfter(endDate)) {
                                    // Find hut names in this cell's context and add dates
                                    for (WebElement hutNameElement : hutNameElements) {
                                        String hutName = hutNameElement.getText().trim();
                                        if (hutAvailabilityMap.containsKey(hutName)) {
                                            hutAvailabilityMap.get(hutName).add(date);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return hutAvailabilityMap;
    }
}

