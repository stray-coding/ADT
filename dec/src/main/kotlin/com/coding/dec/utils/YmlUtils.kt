package com.coding.dec.utils

import com.coding.dec.bean.ApkInfo
import java.io.File

object YmlUtils {
    private const val TAG_MIN_SDK_VERSION = "minSdkVersion"
    private const val TAG_TARGET_SDK_VERSION = "targetSdkVersion"
    private const val TAG_VERSION_CODE = "versionCode"
    private const val TAG_VERSION_NAME = "versionName"
    private const val TAG_APK_FILE_NAME = "apkFileName"


    fun getValueFromApkToolYml(ymlPath: String): ApkInfo {
        val tags = arrayListOf(
            TAG_APK_FILE_NAME,
            TAG_MIN_SDK_VERSION,
            TAG_TARGET_SDK_VERSION,
            TAG_VERSION_CODE,
            TAG_VERSION_NAME
        )
        val apkInfo = ApkInfo()
        if (!ymlPath.isFilePathValid()) return apkInfo
        try {
            for (line in File(ymlPath).readLines()) {
                for (tag in tags) {
                    if (line.contains(tag)) {
                        val array = line.split(":")
                        if (array.size >= 2) {
                            val value = array[1].replace("\"", "").replace("'", "").trim()
                            when (tag) {
                                TAG_APK_FILE_NAME -> apkInfo.apkName = value
                                TAG_MIN_SDK_VERSION -> apkInfo.minsdk = value.toInt()
                                TAG_TARGET_SDK_VERSION -> apkInfo.targetsdk = value.toInt()
                                TAG_VERSION_CODE -> apkInfo.versionCode = value.toInt()
                                TAG_VERSION_NAME -> apkInfo.versionName = value
                            }
                        }
                    }
                }
            }
        } catch (_: Exception) {
        }
        return apkInfo
    }
}