package com.coding.dec.utils

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

fun String.join(vararg subPaths: String): String {
    val path = StringBuilder()
    path.append(this)
    if (!this.endsWith(File.separator)) path.append(File.separator)
    for (sub in subPaths) {
        if (sub.isEmpty() || sub == File.separator) continue
        path.append(sub)
        path.append(File.separator)
    }
    path.deleteCharAt(path.length - 1)
    return path.toString()
}
