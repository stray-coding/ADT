package com.coding.dec

import com.coding.dec.utils.Paths
import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Terminal
import com.coding.dec.utils.isDirPathValid
import com.coding.dec.utils.isFilePathValid
import com.coding.dec.utils.put

object ApkTool {
    /**
     * decompile apk file
     * */
    fun decompile(apkPath: String, ignoreDex: Boolean, ignoreRes: Boolean, outPath: String = ""): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val finalOutPath = outPath.ifEmpty { apkPath.removeSuffix(Suffix.APK) }
        val cmd = mutableListOf<String>()
                .put(Paths.getJava(), "-jar", Paths.getApkTool(), "d", apkPath)
                .put(ignoreDex, "-s")
                .put(ignoreRes, "-r")
                .put(!ignoreDex, "-only-main-classes")
                .put("-f", "-o", finalOutPath)
        return Terminal.run(cmd)
    }

    /**
     * back to apk
     * convert dex and resource to apk file
     * */
    fun backToApk(srcDir: String, outPath: String = ""): Boolean {
        if (!srcDir.isDirPathValid()) return false
        val newOutPath = outPath.ifEmpty { "${srcDir}_btc.apk" }
        val cmd = "${Paths.getJava()} -jar ${Paths.getApkTool()} b $srcDir -f -o $newOutPath"
        return Terminal.run(cmd)
    }
}