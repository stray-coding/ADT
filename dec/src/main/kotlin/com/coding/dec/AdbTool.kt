package com.coding.dec

import com.coding.utils.FileUtils
import com.coding.utils.Terminal
import com.coding.utils.isDirPathValid
import com.coding.utils.isFilePathValid
import java.io.File

/**
 * adb 命令
 */
object AdbTool {
    const val ONLY_SYSTEM_APP = "-s"
    const val ONLY_THIRD_APP = "-3"
    const val ALL_APP = ""

    /**
     * get all connect android devices
     */
    fun getAllDevices(): List<String> {
        val list = mutableListOf<String>()
        Terminal.run("adb devices", listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                if (line.endsWith("device")) {
                    list.add(line.removeSuffix("device").trim())
                }
            }
        })
        list.sort()
        return list
    }

    /**
     * get all apk's package names in android devices
     */
    fun getAllApkPackageNames(device: String = "", filter: String = ""): List<String> {
        val list = mutableListOf<String>()
        val cmd = if (device.isNotEmpty()) {
            "adb -s $device shell pm list packages $filter"
        } else {
            "adb shell pm list packages $filter"
        }
        Terminal.run(cmd, listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                if (line.isNotEmpty() && line.contains("package:")) {
                    val pkg = line.replace("package:", "")
                    list.add(pkg)
                }
            }
        })
        list.sortBy { it.lowercase() }
        return list
    }


    /**
     * install apk
     */
    fun installApk(device: String = "", debug: Boolean = false, apkPath: String): Boolean {
        if (!apkPath.isFilePathValid()) return false
        val debugStr = if (debug) "-t" else ""
        val cmd = if (device.isNotEmpty()) {
            "adb -s $device $debugStr install -r $apkPath"
        } else {
            "adb $debugStr install -r $apkPath"
        }
        return Terminal.run(cmd)
    }

    /**
     * extract apk
     */
    fun extractApk(device: String = "", pkgName: String, outDir: String): Boolean {
        if (pkgName.isEmpty()) return false
        if (!outDir.isDirPathValid()) return false
        val list = arrayListOf<String>()
        val cmd = if (device.isNotEmpty()) {
            "adb -s $device shell pm path $pkgName"
        } else {
            "adb shell pm path $pkgName"
        }
        Terminal.run(cmd, listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                list.add(line.replace("package:", ""))
            }
        })
        if (list.isEmpty()) return false
        if (list.size == 1) {
            val time = System.currentTimeMillis()
            val outPath = File(outDir, "${pkgName}_${time}.apk").absolutePath
            val pullCmd = if (device.isNotEmpty()) {
                "adb -s $device pull ${list[0]} $outPath"
            } else {
                "adb pull ${list[0]} $outPath"
            }
            return Terminal.run(pullCmd)
        } else {
            val time = System.currentTimeMillis()
            val apksDir = outDir + File.separator + pkgName + "_" + time
            FileUtils.createOrExistsDir(apksDir)
            for (item in list) {
                val outPath = File(apksDir, item.substring(item.lastIndexOf("/"))).absolutePath
                val pullCmd = if (device.isNotEmpty()) {
                    "adb -s $device pull $item $outPath"
                } else {
                    "adb pull $item $outPath"
                }
                Terminal.run(pullCmd)
            }
            return true
        }
    }
}