package com.patrickcarlile.csv;

import java.io.IOException;
import java.util.List;

public interface ICsvParser {

    /***
     * Reads CSV file
     * @param fileName
     * @return Returns a two-dimensional of string values
     * @throws IOException
     */
    List<List<String>> read(String fileName) throws IOException;

    /***
     * Writes data to console
     * @param data Two-dimensional array of double values
     */
    void writeToConsole(List<List<Double>> data) ;
}
