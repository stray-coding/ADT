package com.coding.dec

import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Tools
import com.coding.utils.FileUtils
import com.coding.utils.Terminal
import com.coding.utils.isFilePathValid

object BundleTool {
    /**
     * aab file 2 apks
     */
    fun aab2Apks(
        aabPath: String,
        signBean: SignUtils.SignBean,
        outPath: String = "",
        universal: Boolean = false
    ): Boolean {
        if (!aabPath.isFilePathValid(Suffix.AAB)) return false
        val newOutPath = outPath.ifEmpty {
            aabPath.removeSuffix(Suffix.AAB) + ".apks"
        }
        FileUtils.delete(newOutPath)
        val universalStr = if (universal) "--mode=universal" else ""
        val cmd = "java -jar ${Tools.getBundleTool()} " +
                "build-apks " + "--bundle=${aabPath} " +
                "--output=${newOutPath} " +
                "--ks=${signBean.path} " +
                "--ks-pass pass:${signBean.pwd} " +
                "--ks-key-alias=${signBean.alias} " +
                "--key-pass pass:${signBean.aliasPwd} " + universalStr

        return Terminal.run(cmd)
    }
}