package com.patrickcarlile;

import com.patrickcarlile.csv.CsvParser;
import com.patrickcarlile.csv.ICsvParser;
import com.patrickcarlile.evaluator.CircularReferenceException;
import com.patrickcarlile.evaluator.ExpressionEvaluator;
import com.patrickcarlile.evaluator.IExpressionEvaluator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.InputStream;
import java.util.Properties;

public class Spreadsheet {

    private final static String CONFIG_FILE = "config.properties";

    private String inputFile;

    private final ICsvParser csvParser;

    public Spreadsheet(ICsvParser csvParser) {
        this.csvParser = csvParser;
    }

    private void loadConfigurations() {
        Properties prop = new Properties();
        try (InputStream input = Spreadsheet.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            prop.load(input);
            this.inputFile = prop.getProperty("inputFile");
            if (this.inputFile == null) {
                System.out.println("inputFile not found in " + CONFIG_FILE);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void processSpreadsheet()
    {
        loadConfigurations();

        List<List<String>> data = null;

        try {
            data = csvParser.read(inputFile);
        } catch (IOException e) {
            System.out.println("Error handling file: " + e.getMessage());
        }
        if (data != null) {
            IExpressionEvaluator expressionEvaluator = new ExpressionEvaluator(data);

            List<List<Double>> results = new ArrayList<>();

            for (int row = 0; row < data.size(); row++) {
                List<Double> rowResults = new ArrayList<>();
                for (int column = 0; column < data.get(0).size(); column++) {
                    try {
                        rowResults.add(expressionEvaluator.evaluateCell(row, column));
                    } catch (CircularReferenceException e) {
                        rowResults.add(null);
                        System.out.println("Found circular reference: " + e.getMessage());
                    }
                }
                results.add(rowResults);
            }

            csvParser.writeToConsole(results);
        }
    }

    public static void main(String[] args) {
        ICsvParser parser = new CsvParser();

        Spreadsheet spreadsheet = new Spreadsheet(parser);
        spreadsheet.processSpreadsheet();
    }
}
