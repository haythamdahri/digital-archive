package org.digital.archive.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

/**
 * @author Haytham DAHRI
 */
@Component
public class ArchiveHelper {

    private static final String ALPHA_NUMERIC_STRING = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    /**
     * Random unique username generator
     *
     * @param count: counter
     * @return SString
     */
    public static String randomAlphaNumeric(int count) {
        StringBuilder builder = new StringBuilder();
        while (count-- != 0) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }
        return builder.toString();
    }

    /**
     * Retrieve file extension from a file name
     *
     * @param filename: File name
     * @return String
     */
    public String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }

}
