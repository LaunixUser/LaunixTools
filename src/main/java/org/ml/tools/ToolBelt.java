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
package org.ml.tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

/**
 *
 * @author Dr. Matthias Laux
 */
public class ToolBelt {

    public static final Charset DEFAULT_CHARSET = StandardCharsets.ISO_8859_1;
    private static final Pattern MAP_PATTERN = Pattern.compile("\\(\\s*?\"(.+?)\"\\s*,\\s*\"(.+?)\"\\s*\\)");

    /**
     *
     */
    public enum XML {

        include
    }

    /**
     *
     * @param fileName
     * @param charSet
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedWriter getBufferedWriter(String fileName, Charset charSet) throws FileNotFoundException {
        if (fileName == null) {
            throw new IllegalArgumentException("fileName may not be null");
        }
        return getBufferedWriter(new File(fileName), charSet);
    }

    /**
     *
     * @param file
     * @param charSet
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedWriter getBufferedWriter(File file, Charset charSet) throws FileNotFoundException {
        if (file == null) {
            throw new IllegalArgumentException("file may not be null");
        }
        FileOutputStream stream = new FileOutputStream(file);
        Charset set = DEFAULT_CHARSET;
        if (charSet != null) {
            set = charSet;
        }
        OutputStreamWriter streamWriter = new OutputStreamWriter(stream, set);
        return new BufferedWriter(streamWriter);
    }

    /**
     *
     * @param fileName
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedWriter getBufferedWriter(String fileName) throws FileNotFoundException {
        return getBufferedWriter(fileName, null);
    }

    /**
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static BufferedWriter getBufferedWriter(File file) throws FileNotFoundException {
        return getBufferedWriter(file, null);
    }

    /**
     *
     * @param propertiesFile
     * @return
     * @throws IOException
     */
    public static Properties loadProperties(File propertiesFile) throws IOException {
        if (propertiesFile == null) {
            throw new IllegalArgumentException("propertiesFile may not be null");
        }
        Properties properties = new Properties();
        if (propertiesFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(propertiesFile))) {
                properties.load(reader);
            }
        }
        return properties;
    }

    /**
     *
     * @param properties
     * @param propertiesFile
     * @throws IOException
     */
    public static void saveProperties(Properties properties, File propertiesFile) throws IOException {
        if (properties == null) {
            throw new IllegalArgumentException("properties may not be null");
        }
        if (propertiesFile == null) {
            throw new IllegalArgumentException("propertiesFile may not be null");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(propertiesFile))) {
            properties.store(writer, null);
        }
    }

    /**
     *
     * @param properties
     * @param propertiesFileName
     * @throws IOException
     */
    public static void saveProperties(Properties properties, String propertiesFileName) throws IOException {
        if (propertiesFileName == null) {
            throw new IllegalArgumentException("propertiesFileName may not be null");
        }
        saveProperties(properties, new File(propertiesFileName));
    }

    /**
     *
     * @param sourceElement
     * @return
     * @throws JDOMException
     * @throws IOException
     */
    public static Element resolveIncludes(Element sourceElement) throws JDOMException, IOException {
        if (sourceElement == null) {
            throw new IllegalArgumentException("sourceElement may not be null");
        }
        SAXBuilder builder = new SAXBuilder();
        if (sourceElement.getChildren(XML.include.toString()) != null) {
            List<Element> includeContent = new ArrayList<>();

            //.... Collect the content to include
            for (Element includeElement : sourceElement.getChildren(XML.include.toString())) {
                Document doc = builder.build(new BufferedReader(new FileReader(includeElement.getTextTrim())));
                includeContent.add(doc.getRootElement());
            }

            //.... Perform the include - this retains the original <include> tags
            //     This needs to be separate to avoid a ConcurrentModificationException
            for (Element include : includeContent) {
                include.getParent().removeContent(include);
                sourceElement.addContent(include);
            }
        }
        return sourceElement;
    }

    /**
     *
     * @param inputString
     * @param chars
     * @return
     */
    public static boolean containsChars(String inputString, String chars) {
        if (inputString == null) {
            throw new IllegalArgumentException("inputString may not be null");
        }
        if (chars == null) {
            throw new IllegalArgumentException("chars may not be null");
        }
        for (int i = 0; i < chars.length(); i++) {
            char c = chars.charAt(i);
            for (int j = 0; j < inputString.length(); j++) {
                if (c == inputString.charAt(j)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Extract a map from a property string
     *
     * Input format looks like this:
     *
     * &lt;property name="productMap"&gt; ("SOO" , "Product 1");
     * ("Wc2000", "Product 2"); ("SEP", "Product 3")
     * &lt;/property&gt;
     *
     * @param data The input string compliant with the format described above
     * @return
     */
    public static Map<String, String> extractMap(String data) {
        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }

        Map<String, String> map = new TreeMap<>();
        String[] mapElements = data.split(";");

        for (String mapElement : mapElements) {
            Matcher matcher = MAP_PATTERN.matcher(mapElement.trim());
            if (matcher.matches()) {
                map.put(matcher.group(1), matcher.group(2));
            }
        }
        return map;
    }

}