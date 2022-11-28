package com.coding.dec

import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Paths
import com.coding.utils.Terminal
import com.coding.utils.isDirPathValid
import com.coding.utils.isFilePathValid

object ApkTool {
    /**
     * decompile apk file
     * */
    fun decompile(apkPath: String, ignoreDex: Boolean, ignoreRes: Boolean, outPath: String = ""): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val dexStr = if (ignoreDex) "-s" else ""
        val resStr = if (ignoreRes) "-r" else ""
        val omc = if (ignoreDex) "" else "-only-main-classes"
        val finalOutPath = outPath.ifEmpty { apkPath.removeSuffix(Suffix.APK) }
        val cmd = "${Paths.getJava()} -jar ${Paths.getApkTool()} d $apkPath $dexStr $omc $resStr -f -o $finalOutPath"
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