package com.debamalya.shopapp;

import android.content.Context;
import java.io.File;

public class CacheCleaner {

    public static boolean clearCache(Context context) {
        boolean result = false;
        try {
            File cacheDir = context.getCacheDir();
            if (cacheDir != null && cacheDir.isDirectory()) {
                result = deleteDir(cacheDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : children) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
}

