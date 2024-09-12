package com.coderscampus.rendezvous_reminder;

import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FileService {
    private static final String FILE_PATH = "hutAvailability.txt"; // File path to store results

    // Method to save the availability results to a file
    public static void saveResultsToFile(Map<String, List<LocalDate>> availabilityMap) {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(FILE_PATH))) {
            for (Map.Entry<String, List<LocalDate>> entry : availabilityMap.entrySet()) {
                String hutName = entry.getKey();
                List<LocalDate> dates = entry.getValue();
                writer.write(hutName + ": " + dates.toString());
                writer.newLine();
            }
            System.out.println("Results saved to file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to load the previous results from a file
    public static Map<String, List<LocalDate>> loadPreviousResults() {
        Map<String, List<LocalDate>> previousAvailability = new HashMap<>();
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(": ");
                String hutName = parts[0];
                String[] dateStrings = parts[1].replace("[", "").replace("]", "").split(", ");
                List<LocalDate> dates = new ArrayList<>();
                for (String dateString : dateStrings) {
                    dates.add(LocalDate.parse(dateString));
                }
                previousAvailability.put(hutName, dates);
            }
            System.out.println("Previous results loaded from file.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return previousAvailability;
    }
}

