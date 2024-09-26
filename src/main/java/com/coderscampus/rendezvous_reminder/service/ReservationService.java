package com.coderscampus.rendezvous_reminder.service;

import com.coderscampus.rendezvous_reminder.domain.AvailabilityDate;
import com.coderscampus.rendezvous_reminder.domain.Hut;
import com.coderscampus.rendezvous_reminder.repository.AvailabilityDateRepository;
import com.coderscampus.rendezvous_reminder.repository.HutRepository;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class ReservationService {
    private final String url = "https://methowreservations.com/lodging/huts"; // Hardcoded URL

    @Autowired
    private HutRepository hutRepository;

    @Autowired
    private AvailabilityDateRepository availabilityDateRepository;

    @Autowired
    private ReservationService reservationService;

    public void initialScan() {
        final LocalDate startDate = LocalDate.of(2024, 12, 13);
        final LocalDate endDate = LocalDate.of(2025, 3, 15);

        // Get the current availability scan
        Map<String, List<LocalDate>> currentAvailabilityMap = getAvailableDatesForHuts(startDate, endDate);

        for (Map.Entry<String, List<LocalDate>> entry : currentAvailabilityMap.entrySet()) {
            String hutName = entry.getKey();
            List<LocalDate> availableDates = entry.getValue();

            // Retrieve or create hut entity
            Optional<Hut> hutOpt = hutRepository.findByName(hutName);
            Hut hut = hutOpt.orElseGet(() -> new Hut(hutName));

            hutRepository.save(hut);

            // Save available dates to the database
            for (LocalDate date : availableDates) {
                AvailabilityDate availabilityDate = new AvailabilityDate();
                availabilityDate.setDate(date);
                availabilityDate.setHut(hut);

                availabilityDateRepository.save(availabilityDate);
            }
        }
    }

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

            for (int i = 0; i < hutNames.size(); i++) {
                String hutName = hutNames.get(i);
                WebElement row = rows.get(i + 1); // Skip the first row (header row)
                List<WebElement> cells = row.findElements(By.tagName("td"));
                List<LocalDate> availableDates = new ArrayList<>();

                Optional<Hut> existingHutOpt = hutRepository.findByName(hutName);
                Hut hut = existingHutOpt.orElseGet(() -> {
                    Hut newHut = new Hut();
                    newHut.setName(hutName);
                    return hutRepository.save(newHut);
                });

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

                                AvailabilityDate availabilityDate = new AvailabilityDate();
                                availabilityDate.setDate(date);
                                availabilityDate.setHut(hut);

                                availabilityDateRepository.save(availabilityDate);
                            }
                        }
                    }
                }

                hutAvailability.put(hutName, availableDates);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }

        return hutAvailability;
    }
}
