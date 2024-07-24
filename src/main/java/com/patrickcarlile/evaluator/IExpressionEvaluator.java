package com.patrickcarlile.evaluator;

public interface IExpressionEvaluator {

    /**
     * Evaluates a cell given the row/column combination
     * @param row The row identifier
     * @param column The column identifier
     * @return Returns the calculated value
     * @throws CircularReferenceException Thrown if there's a circular reference detected
     */
    double evaluateCell(int row, int column) throws CircularReferenceException;
}
