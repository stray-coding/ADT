package com.coding.dec.bean

class ApkInfo {
    var apkName: String = ""
    var minsdk: Int = 0
    var targetsdk: Int = 0
    var versionCode: Int = 0
    var versionName: String = ""
    override fun toString(): String {
        return "ApkInfo(apkName='$apkName', minsdk=$minsdk, targetsdk=$targetsdk, versionCode=$versionCode, versionName='$versionName')"
    }
}