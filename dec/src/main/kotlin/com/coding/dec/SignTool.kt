package com.coding.dec

import com.coding.dec.bean.SignBean
import com.coding.dec.utils.Paths
import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Terminal
import com.coding.dec.utils.isFilePathValid
import com.coding.utils.FileUtils

object SignTool {

    /**
     * sign the aab file
     *
     * @param aabPath
     * @param signBean
     * @return
     */
    fun signAAB(
        aabPath: String,
        signBean: SignBean
    ): Boolean {
        if (!aabPath.isFilePathValid(Suffix.AAB)) return false
        return Terminal.run(
            "jarsigner " +
                    "-digestalg SHA1 " +
                    "-sigalg SHA1withRSA " +
                    "-keystore ${signBean.path} " +
                    "-storepass ${signBean.pwd} " +
                    "-keypass ${signBean.aliasPwd} " +
                    "$aabPath " +
                    signBean.alias
        )
    }

    /**
     * 1. sign apk
     * 2. align apk
     * 3. jarsigner apk only v1
     */
    fun signAndAlign(
        apkPath: String,
        signBean: SignBean,
        outPath: String = "",
    ): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val signPath = apkPath.removeSuffix(Suffix.APK) + "_signed.apk"
        if (!signApkByJarSigner(apkPath, signBean, signPath)) {
            return false
        }
        val finalOutPath = outPath.ifEmpty { signPath.removeSuffix(Suffix.APK) + "_aligned.apk" }
        return if (alignApk(signPath, finalOutPath)) {
            FileUtils.delete(signPath)
            true
        } else {
            false
        }
    }


    /**
     * 1. align apk
     * 2. sign apk
     */
    fun alignAndSign(
        apkPath: String,
        signBean: SignBean,
        outPath: String = "",
        v1Enable: Boolean,
        v2Enable: Boolean,
        v3Enable: Boolean = false,
        v4Enable: Boolean = false
    ): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val alignPath = apkPath.removeSuffix(Suffix.APK) + "_aligned.apk"
        if (!alignApk(apkPath, alignPath)) {
            println("apk align error")
            return false
        }
        val finalOutPath = outPath.ifEmpty { alignPath.removeSuffix(Suffix.APK) + "_signed.apk" }
        return if (signApkByApkSigner(alignPath, signBean, finalOutPath, v1Enable, v2Enable, v3Enable, v4Enable)) {
            //delete align apk
            FileUtils.delete(alignPath)
            //delete idsig file
            FileUtils.delete("$finalOutPath${Suffix.IDSIG}")
            true
        } else {
            false
        }
    }


    /**
     * jar sign
     * sign the apk  only v1
     * */
    private fun signApkByJarSigner(
        apkPath: String,
        signBean: SignBean,
        outPath: String = "",
    ): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.removeSuffix(Suffix.APK) + "_signed.apk"
        }
        val cmd = "jarsigner -verbose " +
                "-keystore ${signBean.path} " +
                "-storepass ${signBean.pwd} " +
                "-keypass ${signBean.aliasPwd} " +
                "-sigfile CERT " +
                "-signedjar $newOutPath $apkPath ${signBean.alias} " +
                "-digestalg SHA1 " +
                "-sigalg MD5withRSA"
        return Terminal.run(cmd)
    }

    /**
     * apk sign
     * sign the apk
     * */
    private fun signApkByApkSigner(
        apkPath: String,
        signBean: SignBean,
        outPath: String = "",
        v1Enable: Boolean,
        v2Enable: Boolean,
        v3Enable: Boolean = false,
        v4Enable: Boolean = false
    ): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.removeSuffix(Suffix.APK) + "_signed.apk"
        }
        val cmd = "${Paths.getJava()} -jar ${Paths.getApkSigner()} " +
                "sign " + "--ks ${signBean.path} " +
                "--ks-key-alias ${signBean.alias} " +
                "--ks-pass pass:${signBean.pwd} " +
                "--key-pass pass:${signBean.aliasPwd} " +
                "--v1-signing-enabled $v1Enable " +
                "--v2-signing-enabled $v2Enable " +
                "--v3-signing-enabled $v3Enable " +
                "--v4-signing-enabled $v4Enable " +
                "-v --out $newOutPath $apkPath"
        return Terminal.run(cmd)
    }

    /**
     * perform 4-byte alignment operations on apk
     * */
    private fun alignApk(apkPath: String, outPath: String = ""): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.removeSuffix(Suffix.APK) + "_aligned.apk"
        }
        val cmd = "${Paths.getZipalign()} -f -v 4 $apkPath $newOutPath"
        return Terminal.run(cmd)
    }


    /**
     * get apk signature information
     * */
    fun verifyApkSign(apkPath: String): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val cmd = "${Paths.getJava()} -jar ${Paths.getApkSigner()} verify -v $apkPath"
        return Terminal.run(cmd)
    }
}