package com.coding.tool.util

import java.io.File

/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: 229101253@qq.com
 * @des:
 */
object ToolUtil {
    private const val BASE_PATH = "tools/"
    fun getApkSigner(): String {
        val file = File("${BASE_PATH}apksigner.jar")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getApkTool(): String {
        val file = File("${BASE_PATH}apktool-2.4.1.jar")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getJadx(): String {
        val file = File("${BASE_PATH}jadx-gui-1.0.0.exe")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getZipalign(): String {
        val file = File("${BASE_PATH}zipalign.exe")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getJar2dex(): String {
        val file = File("${BASE_PATH}dex2jar-2.0/d2j-jar2dex.bat")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getDex2jar(): String {
        val file = File("${BASE_PATH}dex2jar-2.0/d2j-dex2jar.bat")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getDefaultSignFile():String{
        val file = File("${BASE_PATH}stray-coding.jks")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getSignConfigXml():String{
        val file = File("${BASE_PATH}sign_config.xml")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }
}