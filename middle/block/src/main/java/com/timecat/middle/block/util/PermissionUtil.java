package com.timecat.middle.block.util;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.content.ContextCompat;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/12/13
 * @description null
 * @usage null
 */
public class PermissionUtil {

    public static boolean hasStoragePermission(Context packageContext) {
        return ContextCompat.checkSelfPermission(
                packageContext, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }

    public interface PermissionCallback {

        void onGranted();

        void onDenied();
    }

}
