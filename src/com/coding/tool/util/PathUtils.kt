package com.coding.tool.util

import java.io.File

/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object PathUtils {
    private const val TOOLS = "tools"
    private const val CONFIG = "config"
    fun getJava(): String {
        return "java"
    }

    fun getToolsDir(): String {
        val dir = File(TOOLS)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.absolutePath
    }

    fun getConfigDir(): String {
        val dir = File(CONFIG)
        if (!dir.exists()) {
            dir.mkdirs()
        }
        return dir.absolutePath
    }

    fun getApkSigner(): String {
        val file = File(TOOLS, "apksigner.jar")
        return file.absolutePath
    }

    fun getApkTool(): String {
        val file = File(TOOLS, "apktool_2.6.1.jar")
        return file.absolutePath
    }

    fun getJadx(): String {
        val file = if (Terminal.isWindows())
            File("${TOOLS}${File.separator}jadx${File.separator}bin${File.separator}jadx-gui.bat")
        else
            File("${TOOLS}${File.separator}jadx${File.separator}bin${File.separator}jadx-gui")
        return file.absolutePath
    }

    fun getZipalign(): String {
        val file = if (Terminal.isWindows())
            File(TOOLS, "zipalign.exe")
        else
            File(TOOLS, "zipalign")
        return file.absolutePath
    }

    fun getJar2dex(): String {
        val file = if (Terminal.isWindows())
            File("${TOOLS}${File.separator}dex2jar-2.0${File.separator}d2j-jar2dex.bat")
        else
            File("${TOOLS}${File.separator}dex2jar-2.0${File.separator}d2j-jar2dex.sh")
        return file.absolutePath
    }

    fun getBundleTool():String {
        val file = File(TOOLS, "bundletool-all-1.10.0.jar")
        return file.absolutePath
    }

    fun getDex2jar(): String {
        val file = if (Terminal.isWindows()) {
            File(TOOLS, "dex2jar-2.0${File.separator}d2j-dex2jar.bat")
        } else {
            File(TOOLS, "dex2jar-2.0${File.separator}d2j-dex2jar.sh")
        }
        return file.absolutePath
    }

    fun getDefaultSignFile(): String {
        val file = File(TOOLS, "stray-coding.jks")
        return file.absolutePath
    }

    fun getSignConfigXml(): String {
        val file = File(TOOLS, "sign_config.xml")
        return file.absolutePath
    }
}