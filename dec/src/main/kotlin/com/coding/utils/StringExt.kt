package com.coding.utils

import java.io.File

//检测文件路径是否有效，并且文件是否存在
fun String?.isFilePathValid(suffix: String = ""): Boolean {
    if (this.isNullOrEmpty()) return false
    if (suffix.isNotEmpty()) {
        if (!this.endsWith(suffix)) return false
    }
    val file = File(this)
    return file.exists() && file.isFile
}

//检测文件夹路径是否有效，并且文件夹是否存在
fun String?.isDirPathValid(): Boolean {
    if (this.isNullOrEmpty()) return false
    val file = File(this)
    return file.exists() && file.isDirectory
}