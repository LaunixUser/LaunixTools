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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ml.tools.FileType;
import static org.ml.tools.FileType.XLSX;
import static org.ml.tools.FileType.XLS;

/**
 * @author osboxes
 */
public class ExcelTools {

    /**
     * @param type
     * @return
     */
    public static Workbook getNewWorkbook(FileType type) {
        if (type == null) {
            throw new NullPointerException("type may not be null");
        }
        switch (type) {
            case XLS:
                return new HSSFWorkbook();
            case XLSX:
                return new XSSFWorkbook();
            default:
                throw new UnsupportedOperationException("Unknown / unsupported file type: " + type.toString());
        }
    }

    /**
     * @param path
     * @return
     */
    public static Workbook getNewWorkbook(Path path) {
        if (path == null) {
            throw new NullPointerException("path may not be null");
        }
        File file = path.toFile();
        Workbook workbook;
        if (file.getName().endsWith(".xls")) {
            workbook = new HSSFWorkbook();
        } else if (file.getName().endsWith(".xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            throw new UnsupportedOperationException("Unknown / unsupported file type: " + file);
        }
        return workbook;
    }

    /**
     * @param path
     * @return
     */
    public static Workbook getWorkbook(Path path) {
        if (path == null) {
            throw new NullPointerException("path may not be null");
        }
        File file = path.toFile();
        Workbook workbook;
        if (file.exists()) {

            try {
                if (file.getName().endsWith(".xls")) {
                    workbook = new HSSFWorkbook(new FileInputStream(file));
                } else if (file.getName().endsWith(".xlsx")) {
                    OPCPackage workbookPackage = OPCPackage.open(file);
                    workbook = new XSSFWorkbook(workbookPackage);
                } else {
                    throw new UnsupportedOperationException("Unknown / unsupported file type: " + file);
                }
            } catch (IOException | InvalidFormatException ex) {
                workbook = getNewWorkbook(path);
            }

        } else {
            workbook = getNewWorkbook(path);

        }
        return workbook;
    }

}
