package kr.co.community.util;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class FileUtils {
    public static String getTimeStamp(){
        String rtnStr = null;

        String pattern = "yyyyMMddhhmmssSSS";
        SimpleDateFormat sdf = new SimpleDateFormat(pattern, Locale.KOREA);
        Timestamp ts = new Timestamp(System.currentTimeMillis());

        rtnStr = sdf.format(ts.getTime());
        return rtnStr;
    }

    public static String getExtension(String filename){
        return FilenameUtils.getExtension(filename);
    }

    public static String getStrFilename(String atchFileId, String filename){
        return atchFileId +"_" +getTimeStamp()+"."+getExtension(filename);
    }

    public static boolean deleteFile(String path) throws IOException {
        if(path == null){
            return false;
        }
        org.apache.commons.io.FileUtils.touch(new File(path));
        File fileToDelete = org.apache.commons.io.FileUtils.getFile(path);
        boolean success = org.apache.commons.io.FileUtils.deleteQuietly(fileToDelete);
        return success;
    }
}
