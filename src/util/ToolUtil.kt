package util

import java.io.File

/**
 * @author: Coding.He
 * @date: 2020/7/13
 * @emil: 229101253@qq.com
 * @des:
 */
object ToolUtil {
    fun getApkSigner(): String {
        val file = File("libs/apksigner.jar")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getApkTool(): String {
        val file = File("libs/apktool-2.4.1.jar")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }

    fun getJadx(): String {
        val file = File("libs/jadx-gui-1.0.0.exe")
        println("exist:${file.exists()}")
        println(file.absolutePath)
        return file.absolutePath
    }
}