package com.patrickcarlile.evaluator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionEvaluator implements IExpressionEvaluator {
    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("(\\d+\\.\\d+|\\d+|[-+]|[A-Za-z]+\\d*)");

    private final List<List<String>> data;
    private final Set<String> calculationSet;

    public ExpressionEvaluator(List<List<String>> data) {
        this.data = data;
        this.calculationSet = new HashSet<>();
    }

    @Override
    public double evaluateCell(int row, int column) throws CircularReferenceException {
        // Cell identifier
        String cellName = getCellName(row, column);

        // Check for and handle circular references
        if (calculationSet.contains(cellName)) {
            throw new CircularReferenceException("Circular reference detected at " + cellName);
        }

        // Track our current cell calculations to detect duplication
        calculationSet.add(cellName);

        try {
            String expression = data.get(row).get(column);
            return parseAndEvaluate(expression);
        } finally {
            // Ensure cleanup
            calculationSet.remove(cellName);
        }
    }

    /**
     * Takes an expression, breaks it into pieces, evaluates them, and combines it
     * @param expression The expression to evaluate
     * @return The result of the expression
     * @throws CircularReferenceException Thrown when a circular reference is detected
     * @throws NumberFormatException Thrown if input is invalid
     */
    private double parseAndEvaluate(String expression) throws CircularReferenceException, NumberFormatException {

        // Process the expression
        double result = 0;
        String operator = "+";

        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);
        while (matcher.find()) {
            String token = matcher.group();
            if (token.equals("+") || token.equals("-")) {
                operator = token;
            } else {
                double value = 0;
                if (Character.isLetter(token.charAt(0))) {
                    // Handle cell reference
                    int refColumn = token.charAt(0) - 'A';
                    int refRow = Integer.parseInt(token.substring(1)) - 1;
                    value = evaluateCell(refRow, refColumn);
                } else {
                    // Handle numeric value
                    value = Double.parseDouble(token);
                }
                result = operator.equals("+") ? result + value : result - value;
            }
        }

        return result;
    }

    private String getCellName(int row, int column) {
        // Convert numeric column value to letter
        char columnLetter = (char) ('A' + column);

        // Convert zero based index to one based
        int rowNumber = row + 1;

        return String.format("%c%d", columnLetter, rowNumber);
    }
}
