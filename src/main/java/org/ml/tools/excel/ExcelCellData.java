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

import java.util.logging.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.ml.tools.logging.LoggerFactory;

/**
 *
 * @author mlaux
 */
public class ExcelCellData {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExcelCellData.class.getName());
    public static final ExcelCellData EMPTY_CELL = new ExcelCellData();
    public static final String ERROR_CONDITION_NUMBER_FORMAT = "ERROR-NUMBER-FORMAT";
    public static final String ERROR_CONDITION_ILLEGAL_STATE = "ERROR-ILLEGAL-STATE";
    public static final double ERROR_CONDITION_VALUE = Double.NEGATIVE_INFINITY;
    private Cell cell;
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
    public ExcelCellData(Cell cell) {
        if (cell == null) {
            throw new NullPointerException("cell may not be null");
        }
        this.cell = cell;
        this.emptyCell = false;
    }

    /**
     * Just for internal use to create an empty cell
     */
    private ExcelCellData() {
    }

    /**
     *
     * @return
     */
    public Comparable getValue() {
        rawData = dataFormatter.formatCellValue(cell).trim();
        return null;
    }

    /**
     *
     * @param targetDataType
     * @return
     */
    public Comparable getValue(DataType targetDataType) {
        if (targetDataType == null) {
            throw new NullPointerException("targetDataType may not be null");
        }

        //.... We have no dtaa, therefore we return the default
        if (isEmptyCell()) {
            return targetDataType.getDefaultValue();
        }

        rawData = dataFormatter.formatCellValue(cell).trim();
        CellType cellType = cell.getCellType();
        if (cell.getCellType().equals(CellType.FORMULA)) {
            cellType = cell.getCachedFormulaResultType();   // One of CellType.NUMERIC, CellType.STRING, CellType.BOOLEAN, CellType.ERROR
        }

        switch (cellType) {
            case NUMERIC:
                switch (targetDataType) {
                    case TypeDouble:
                        return (Double) cell.getNumericCellValue();
                    case TypeDoublePercentage:
                        return (Double) (100.0d * cell.getNumericCellValue());
                    case TypeInteger:
                        return (int) cell.getNumericCellValue();
                    case TypeIntegerPercentage:
                        return 100 * (int) cell.getNumericCellValue();
                    default:
                        throw new UnsupportedOperationException("Can not extract " + targetDataType + " from raw data " + rawData);
                }
            case BOOLEAN:
                switch (targetDataType) {
                    case TypeBoolean:
                        return (Boolean) cell.getBooleanCellValue();
                    case TypeString:
                        return String.valueOf(cell.getBooleanCellValue());
                    case TypeInteger:
                        return cell.getBooleanCellValue() ? 1 : 0;
                    default:
                        throw new UnsupportedOperationException("Can not extract " + targetDataType + " from raw data " + rawData);
                }
            case BLANK:
                return targetDataType.getDefaultValue();
            case STRING:
                String s = cell.getStringCellValue().trim();
                switch (targetDataType) {
                    case TypeDouble:
                        try {
                        return Double.valueOf(s);
                    } catch (NumberFormatException ex) {
                        return targetDataType.getDefaultValue();
                    }
                    case TypeDoublePercentage:
                        try {
                        return 100.d * Double.valueOf(s);
                    } catch (NumberFormatException ex) {
                        return targetDataType.getDefaultValue();
                    }
                    case TypeInteger:
                        try {
                        return Integer.valueOf(s);
                    } catch (NumberFormatException ex) {
                        return targetDataType.getDefaultValue();
                    }
                    case TypeIntegerPercentage:
                                try {
                        return 100 * Integer.valueOf(s);
                    } catch (NumberFormatException ex) {
                        return targetDataType.getDefaultValue();
                    }
                    case TypeBoolean:
                   try {
                        return Boolean.valueOf(s);
                    } catch (NumberFormatException ex) {
                        return targetDataType.getDefaultValue();
                    }
                    case TypeEmail:
                    case TypeString:
                        return s;
                    case TypeUndefined:
                    default:
                        throw new UnsupportedOperationException("Can not extract " + targetDataType + " from raw data " + rawData);
                }
            case ERROR:
            case _NONE:
                throw new UnsupportedOperationException("Unsupported POI cell type: " + cell.getCellType());
            default:
                throw new UnsupportedOperationException("Unknown POI cell type: " + cell.getCellType());
        }
    }

    /**
     *
     * @return
     */
    public boolean isEmptyCell() {
        return emptyCell;
    }
}

//        try {
//            switch (cell.getCellType()) {
//
//            case FORMULA:
//
//                CellType resultType = cell.getCachedFormulaResultType();
//                switch (resultType) {
//                    case STRING:
//                        processedData = cell.getStringCellValue();
//                        processedComparableData = processedData;
//                        break;
//                    case NUMERIC:
//                        double numberData = cell.getNumericCellValue();
//                        processedData = String.valueOf(numberData);
//                        processedComparableData = numberData;
//                        break;
//                    case BOOLEAN:
//                        boolean d2 = cell.getBooleanCellValue();
//                        processedData = String.valueOf(d2);
//                        processedComparableData = d2;
//                        break;
//                    case ERROR:
//                        byte d3 = cell.getErrorCellValue();
//                        processedData = String.valueOf(d3);
//                        processedComparableData = d3;
//                        break;
//                    default:
//                        throw new UnsupportedOperationException("This should not happen according to the Apache POI docmentation");
//                }
//                break;
//
//            // Numeric cell type (whole numbers, fractional numbers, dates)
//            case NUMERIC:
//
//                String dataFormatString = cell.getCellStyle().getDataFormatString();
//                if (dataFormatString.endsWith("%")) {
//                    System.out.println("TTT2 " + cell.getNumericCellValue());
//                }
//
//                try {
//
//                    numberData = cell.getNumericCellValue();
//                    processedData = String.valueOf(numberData);
//                    processedComparableData = numberData;
//
//                } catch (NumberFormatException ex1) {
//
//                    try {
//
//                        boolean d2 = cell.getBooleanCellValue();
//                        processedData = String.valueOf(d2);
//                        processedComparableData = d2;
//
//                    } catch (NumberFormatException ex2) {
//
//                        try {
//
//                            LocalDateTime dateTime = cell.getLocalDateTimeCellValue();
//                            processedData = String.valueOf(dateTime);
//                            processedComparableData = dateTime;
//
//                        } catch (NumberFormatException ex3) {
//
//                            processedData = cell.getStringCellValue();
//                            processedComparableData = processedData;
//
//                        }
//                    }
//                }
//
//                break;
//
//            case BOOLEAN:
//
//                boolean flag = cell.getBooleanCellValue();
//                processedData = String.valueOf(flag);
//                processedComparableData = flag;
//
//                break;
//
//            case STRING:
//            case BLANK:
//            case ERROR:
//            default:
//
//                processedData = cell.getStringCellValue();
//                processedComparableData = processedData;
//
//        }
//        emptyCell = false;
//
//    }
//    catch (NumberFormatException | IllegalStateException ex
//
//    
//        ) {
//
//            LOGGER.log(Level.SEVERE, "Could not parse cell data - raw cell data = {0}", rawData);
//        rawData = ERROR_CONDITION_NUMBER_FORMAT;
//        processedData = ERROR_CONDITION_NUMBER_FORMAT;
/**
 * @return the rawData
 */
//public String getRawData() {
//        return rawData;
//    }
/**
 *
 * @return
 */
//    /**
//     * @return the processedData
//     */
//    public String getProcessedData() {
//        return processedData;
//    }
//
//    /**
//     *
//     * @return
//     */
//    public Comparable getComparableProcessedData() {
//        return processedComparableData;
//    }
//
//    /**
//     * @return the formula
//     */
////    public String getFormula() {
////        return formula;
////    }
////
////    /**
////     * @return the style
////     */
////    public CellStyle getStyle() {
////        return style;
////    }
//    /**
//     * @return the numberData
//     */
//    public double getNumberData() {
//        return numberData;
//    }
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

