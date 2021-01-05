package com.coding.tool.util

import java.io.File

/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object ToolFilePath {
    private const val BASE_PATH = "tools"
    fun getApkSigner(): String {
        val file = File("${BASE_PATH}${File.separator}apksigner.jar")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getApkTool(): String {
        val file = File("${BASE_PATH}${File.separator}apktool-2.4.1.jar")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getJadx(): String {
        val file = if (Terminal.isWindows())
            File("${BASE_PATH}${File.separator}jadx${File.separator}bin${File.separator}jadx-gui.bat")
        else
            File("${BASE_PATH}${File.separator}jadx${File.separator}bin${File.separator}jadx-gui")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getZipalign(): String {
        val file = if (Terminal.isWindows())
            File("${BASE_PATH}${File.separator}zipalign.exe")
        else
            File("${BASE_PATH}${File.separator}zipalign")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getJar2dex(): String {
        val file = if (Terminal.isWindows())
            File("${BASE_PATH}${File.separator}dex2jar-2.0${File.separator}d2j-jar2dex.bat")
        else
            File("${BASE_PATH}${File.separator}dex2jar-2.0${File.separator}d2j-jar2dex.sh")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getDex2jar(): String {
        val file = if (Terminal.isWindows()) {
            File("${BASE_PATH}${File.separator}dex2jar-2.0${File.separator}d2j-dex2jar.bat")
        } else {
            File("${BASE_PATH}${File.separator}dex2jar-2.0${File.separator}d2j-dex2jar.sh")
        }
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getDefaultSignFile(): String {
        val file = File("${BASE_PATH}${File.separator}stray-coding.jks")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getSignConfigXml(): String {
        val file = File("${BASE_PATH}${File.separator}sign_config.xml")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }
}