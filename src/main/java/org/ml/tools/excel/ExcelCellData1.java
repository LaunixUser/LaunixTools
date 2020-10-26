/*
 * The MIT License
 *
 * Copyright 2019 Dr. Matthias Laux.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.ml.tools.excel;

import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.ml.tools.logging.LoggerFactory;

/**
 *
 * @author mlaux
 */
public class ExcelCellData1 {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelCellData1.class.getName());
    public static final ExcelCellData1 EMPTY_CELL = new ExcelCellData1();
    public static final String ERROR_CONDITION_NUMBER_FORMAT = "ERROR-NUMBER-FORMAT";
    public static final String ERROR_CONDITION_ILLEGAL_STATE = "ERROR-ILLEGAL-STATE";
    public static final double ERROR_CONDITION_VALUE = Double.NEGATIVE_INFINITY;
    private String rawData = "";
    private String processedData = "";
    private Comparable processedComparableData;
//    private boolean flag = false;
//    private String formula = "";
    private double numberData = 0.0d;
    private boolean emptyCell = true;
//    private CellStyle style = null;
    private DataFormatter dataFormatter = new DataFormatter();

    /**
     *
     * @param cell
     */
    public ExcelCellData1(Cell cell) {
        if (cell == null) {
            throw new NullPointerException("cell may not be null");
        }

        rawData = dataFormatter.formatCellValue(cell).trim();

        try {
            switch (cell.getCellType()) {

                case FORMULA:

                    CellType resultType = cell.getCachedFormulaResultType();
                    switch (resultType) {
                        case STRING:
                            processedData = cell.getStringCellValue();
                            processedComparableData = processedData;
                            break;
                        case NUMERIC:
                            double numberData = cell.getNumericCellValue();
                            processedData = String.valueOf(numberData);
                            processedComparableData = numberData;
                            break;
                        case BOOLEAN:
                            boolean d2 = cell.getBooleanCellValue();
                            processedData = String.valueOf(d2);
                            processedComparableData = d2;
                            break;
                        case ERROR:
                            byte d3 = cell.getErrorCellValue();
                            processedData = String.valueOf(d3);
                            processedComparableData = d3;
                            break;
                        default:
                            throw new UnsupportedOperationException("This should not happen according to the Apache POI docmentation");
                    }
                    break;

                // Numeric cell type (whole numbers, fractional numbers, dates)
                case NUMERIC:

                    String dataFormatString = cell.getCellStyle().getDataFormatString();
                    if (dataFormatString.endsWith("%")) {
                       System.out.println("TTT2 " + cell.getNumericCellValue());
                    }
                    
                    try {

                        numberData = cell.getNumericCellValue();
                        processedData = String.valueOf(numberData);
                        processedComparableData = numberData;

                    } catch (NumberFormatException ex1) {

                        try {

                            boolean d2 = cell.getBooleanCellValue();
                            processedData = String.valueOf(d2);
                            processedComparableData = d2;

                        } catch (NumberFormatException ex2) {

                            try {

                                LocalDateTime dateTime = cell.getLocalDateTimeCellValue();
                                processedData = String.valueOf(dateTime);
                                processedComparableData = dateTime;

                            } catch (NumberFormatException ex3) {

                                processedData = cell.getStringCellValue();
                                processedComparableData = processedData;

                            }
                        }
                    }

                    break;

                case BOOLEAN:

                    boolean flag = cell.getBooleanCellValue();
                    processedData = String.valueOf(flag);
                    processedComparableData = flag;

                    break;

                case STRING:
                case BLANK:
                case ERROR:
                default:

                    processedData = cell.getStringCellValue();
                    processedComparableData = processedData;

            }
            emptyCell = false;

        } catch (NumberFormatException | IllegalStateException ex) {

            LOGGER.log(Level.SEVERE, "Could not parse cell data - raw cell data = {0}", rawData);
            rawData = ERROR_CONDITION_NUMBER_FORMAT;
            processedData = ERROR_CONDITION_NUMBER_FORMAT;

        }
    }

    /**
     *
     */
    private ExcelCellData1() {
    }

    /**
     * @return the rawData
     */
    public String getRawData() {
        return rawData;
    }

    /**
     *
     * @return
     */
    public boolean isEmptyCell() {
        return emptyCell;
    }

    /**
     * @return the processedData
     */
    public String getProcessedData() {
        return processedData;
    }

    /**
     *
     * @return
     */
    public Comparable getComparableProcessedData() {
        return processedComparableData;
    }

    /**
     * @return the formula
     */
//    public String getFormula() {
//        return formula;
//    }
//
//    /**
//     * @return the style
//     */
//    public CellStyle getStyle() {
//        return style;
//    }
    /**
     * @return the numberData
     */
    public double getNumberData() {
        return numberData;
    }

//    /**
//     *
//     * @return
//     */
//    public boolean getFlag() {
//        return flag;
//    }
    /**
     *
     * @return
     */
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder(100);
//        sb.append("Raw data       = ");
//        sb.append(rawData);
//        sb.append('\n');
//        sb.append("Processed data = ");
//        sb.append(processedData);
//        sb.append('\n');
//        sb.append("Formula        = ");
//        sb.append(formula);
//        sb.append('\n');
//        sb.append("Number data    = ");
//        sb.append(numberData);
//        sb.append('\n');
//        sb.append("Flag           = ");
//        sb.append(flag);
//        sb.append('\n');
//        sb.append("Style          = ");
//        sb.append(style);
//        sb.append('\n');
//        return sb.toString();
//    }
}
