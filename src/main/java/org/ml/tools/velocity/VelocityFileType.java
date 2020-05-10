package org.ml.tools.velocity;

/**
 *
 * @author Dr. Matthias Laux
 */
public enum VelocityFileType {

    html(".html"), txt(".txt");

    private String extension = "";

    /**
     *
     * @param extension
     */
    VelocityFileType(String extension) {
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
