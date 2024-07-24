package com.patrickcarlile.evaluator;

/***
 * Thrown when a circular reference is detected
 */
public class CircularReferenceException extends Exception {
    public CircularReferenceException(String message) {
        super(message);
    }
}
