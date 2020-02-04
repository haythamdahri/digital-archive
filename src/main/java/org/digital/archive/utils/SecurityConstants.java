package org.digital.archive.utils;

import java.util.Arrays;
import java.util.List;

public class SecurityConstants {


    /*
     * Useful properties
     */
    public static final String uploadDirectory = System.getProperty("user.dir") + "/uploads/";
    public static final String usersUploadDirectory = System.getProperty("user.dir") + "/uploads/users/";
    public static final String usersPicturesUploadDirectory = System.getProperty("user.dir") + "/uploads/users/images/";
    public static final String archivesUploadDirectory = System.getProperty("user.dir") + "/uploads/archives/";
    public static final String archivesImagesUploadDirectory = System.getProperty("user.dir") + "/uploads/archives/images/";
    public static final String archivesFilesUploadDirectory = System.getProperty("user.dir") + "/uploads/archives/files/";
    public static final List<String> imageContentTypes = Arrays.asList("image/png", "image/jpeg", "image/gif");

}
