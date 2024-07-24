package com.patrickcarlile.csv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CsvParser implements ICsvParser {

    private final String DELIMITER = ",";

    @Override
    public List<List<String>> read(String fileName) throws IOException {
        List<List<String>> data = new ArrayList<>();
        try (InputStream is = getClass().getClassLoader().getResourceAsStream(fileName);
             BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                List<String> row = new ArrayList<>();
                for (String value : values) {
                    row.add(value.trim());
                }
                data.add(row);
            }
        } catch (NullPointerException e) {
            throw new IOException("File not found in resources: " + fileName, e);
        }
        return data;
    }

    @Override
    public void writeToConsole(List<List<Double>> data) {
        for (List<Double> row : data) {
            String outputLine = row.stream()
                    .map(value -> String.format("%.2f", value))
                    .reduce((a, b) -> a + DELIMITER + b)
                    .orElse(DELIMITER);
            System.out.println(outputLine);
        }
    }
}
