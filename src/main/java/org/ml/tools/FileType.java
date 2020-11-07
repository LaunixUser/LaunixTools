package org.ml.tools;

/**
 *
 * @author Dr. Matthias Laux
 */
public enum FileType {

    HTML(".html"), TXT(".txt"), XLSX(".xlsx"), XLS(".xls");

    private String extension = "";

    /**
     *
     * @param extension
     */
    FileType(String extension) {
        this.extension = extension;
    }

    /**
     *
     * @return
     */
    public String getExtension() {
        return extension;
    }
}
