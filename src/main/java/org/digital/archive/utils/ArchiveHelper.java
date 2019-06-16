package org.digital.archive.utils;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

@Component
public class ArchiveHelper {

    /*
     * @Retrieve file extension from a file name
     * */
    public String getExtensionByApacheCommonLib(String filename) {
        return FilenameUtils.getExtension(filename);
    }


}
