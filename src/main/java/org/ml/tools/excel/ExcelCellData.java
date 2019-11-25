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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormatter;

/**
 *
 * @author mlaux
 */
public class ExcelCellData {

    public static final ExcelCellData EMPTY_CELL = new ExcelCellData();
    public static final String ERROR_CONDITION_NUMBER_FORMAT = "ERROR-NUMBER-FORMAT";
    public static final String ERROR_CONDITION_ILLEGAL_STATE = "ERROR-ILLEGAL-STATE";
    public static final double ERROR_CONDITION_VALUE = Double.NEGATIVE_INFINITY;
    private String rawData = "";
    private boolean flag = false;
    private String processedData = "";
    private String formula = "";
    private double numberData = 0.0d;
    private CellStyle style = null;
    private DataFormatter dataFormatter = new DataFormatter();

    /**
     *
     * @param cell
     */
    public ExcelCellData(Cell cell) {
        if (cell != null) {

            rawData = dataFormatter.formatCellValue(cell).trim();

            try {
                switch (cell.getCellType()) {

                    case BLANK:

                        processedData = cell.getStringCellValue();
                        break;

                    case BOOLEAN:

                        flag = cell.getBooleanCellValue();
                        processedData = String.valueOf(flag);
                        break;

                    case ERROR:

                        processedData = cell.getStringCellValue();
                        break;

                    case FORMULA:

                        try {
                            numberData = cell.getNumericCellValue();
                            processedData = String.valueOf(numberData);
                        } catch (IllegalStateException ex1) {

                            try {
                                processedData = cell.getStringCellValue();
                            } catch (IllegalStateException ex2) {

                                try {
                                    flag = cell.getBooleanCellValue();
                                    processedData = String.valueOf(flag);
                                } catch (IllegalStateException ex3) {
                                    System.out.println("How on earth could we get here?");
                                    System.exit(1);
                                }

                            }

                        }

                        formula = cell.getCellFormula();
                        break;

                    case NUMERIC:

                        numberData = cell.getNumericCellValue();
                        processedData = String.valueOf(numberData);
                        break;

                    case STRING:

                        processedData = cell.getStringCellValue();
                        break;

                    default:

                        processedData = cell.getStringCellValue();

                }

                style = cell.getCellStyle();

            } catch (NumberFormatException ex) {

                rawData = ERROR_CONDITION_NUMBER_FORMAT;
                processedData = ERROR_CONDITION_NUMBER_FORMAT;
                formula = ERROR_CONDITION_NUMBER_FORMAT;
                numberData = 0.0d;

            } catch (IllegalStateException ex) {

                rawData = ERROR_CONDITION_ILLEGAL_STATE;
                processedData = ERROR_CONDITION_ILLEGAL_STATE;
                formula = ERROR_CONDITION_ILLEGAL_STATE;
                numberData = 0.0d;

            }

        }
    }

    /**
     *
     */
    private ExcelCellData() {
    }

    /**
     * @return the rawData
     */
    public String getRawData() {
        return rawData;
    }

    /**
     * @return the processedData
     */
    public String getProcessedData() {
        return processedData;
    }

    /**
     * @return the formula
     */
    public String getFormula() {
        return formula;
    }

    /**
     * @return the style
     */
    public CellStyle getStyle() {
        return style;
    }

    /**
     * @return the numberData
     */
    public double getNumberData() {
        return numberData;
    }

    /**
     *
     * @return
     */
    public boolean getFlag() {
        return flag;
    }

    /**
     *
     * @return
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(100);
        sb.append("Raw data       = ");
        sb.append(rawData);
        sb.append('\n');
        sb.append("Processed data = ");
        sb.append(processedData);
        sb.append('\n');
        sb.append("Formula        = ");
        sb.append(formula);
        sb.append('\n');
        sb.append("Number data    = ");
        sb.append(numberData);
        sb.append('\n');
        sb.append("Flag           = ");
        sb.append(flag);
        sb.append('\n');
        sb.append("Style          = ");
        sb.append(style);
        sb.append('\n');
        return sb.toString();
    }
}
