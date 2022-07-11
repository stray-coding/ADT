package com.coding.dec

import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Tools
import com.coding.utils.*
import java.io.File

/**
 * @author: Coding.He
 * @date: 2021/1/2
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object ADT {
    /**
     * open jadx-gui
     * */
    fun openJadx() {
        Terminal.run(Tools.getJadx())
    }

    /**
     * decompile apk file
     * */
    fun decompile(apkPath: String, ignoreDex: Boolean, ignoreRes: Boolean, outPath: String = ""): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val dexStr = if (ignoreDex) "-s" else ""
        val resStr = if (ignoreRes) "-r" else ""
        val omc = if (ignoreDex) "" else "-only-main-classes"
        val finalOutPath = outPath.ifEmpty { apkPath.removeSuffix(Suffix.APK) }
        val cmd = "${Tools.getJava()} -jar ${Tools.getApkTool()} d $apkPath $dexStr $omc $resStr -f -o $finalOutPath"
        return Terminal.run(cmd)
    }

    /**
     * dex2jar
     * convert dex file to jar file
     * */
    fun dex2jar(dexPath: String, outPath: String = ""): Boolean {
        if (!dexPath.isFilePathValid(Suffix.DEX)) return false
        val finalOutPath = outPath.ifEmpty {
            dexPath.removeSuffix(Suffix.DEX) + "_d2j.jar"
        }
        val cmd = "${Tools.getDex2jar()} $dexPath -f -o $finalOutPath"
        val isSuc = Terminal.run(cmd)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.endsWith("-error.zip")) {
                item.delete()
            }
        }
        return isSuc
    }

    /**
     * jar2dex
     * convert jar file to dex file
     * */
    fun jar2dex(jarPath: String, outPath: String = ""): Boolean {
        if (!jarPath.isFilePathValid(Suffix.JAR)) return false
        val finalOutPath = outPath.ifEmpty {
            jarPath.removeSuffix(Suffix.JAR) + "_j2d.dex"
        }
        val cmd = "${Tools.getJar2dex()} $jarPath -f -o $finalOutPath"
        val isSuc = Terminal.run(cmd)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.endsWith("-error.zip")) {
                item.delete()
            }
        }
        return isSuc
    }

    /**
     * back to apk
     * convert dex and resource to apk file
     * */
    fun backToApk(srcDir: String, outPath: String = ""): Boolean {
        if (!srcDir.isDirPathValid()) return false
        val newOutPath = outPath.ifEmpty { "${srcDir}_btc.apk" }
        val cmd = "${Tools.getJava()} -jar ${Tools.getApkTool()} b $srcDir -f -o $newOutPath"
        return Terminal.run(cmd)
    }


    /**
     * jar sign
     * sign the apk  only v1
     * */
    private fun signApkByJarSigner(
        apkPath: String,
        signBean: SignUtils.SignBean,
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
        signBean: SignUtils.SignBean,
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
        val v3EnableStr = if (v3Enable) "--v3-signing-enabled true " else ""
        val v4EnableStr = if (v4Enable) "--v4-signing-enabled true " else ""

        val cmd = "${Tools.getJava()} -jar ${Tools.getApkSigner()} " +
                "sign " + "--ks ${signBean.path} " +
                "--ks-key-alias ${signBean.alias} " +
                "--ks-pass pass:${signBean.pwd} " +
                "--key-pass pass:${signBean.aliasPwd} " +
                "--v1-signing-enabled $v1Enable " +
                "--v2-signing-enabled $v2Enable " +
                "$v3EnableStr " +
                "$v4EnableStr " +
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
        val cmd = "${Tools.getZipalign()} -f -v 4 $apkPath $newOutPath"
        return Terminal.run(cmd)
    }

    /**
     * 1. sign apk
     * 2. align apk
     * 3. jarsigner apk only v1
     */
    fun signAndAlign(
        apkPath: String,
        signBean: SignUtils.SignBean,
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
        signBean: SignUtils.SignBean,
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
            true
        } else {
            false
        }
    }


    /**
     * get apk signature information
     * */
    fun verifyApkSign(apkPath: String): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val cmd = "${Tools.getJava()} -jar ${Tools.getApkSigner()} verify -v $apkPath"
        return Terminal.run(cmd)
    }

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

    /**
     * Generate patch dex based on new and old dex files
     * Perform content verification on the old and new dex files and generate incremental packages and patches
     * */
    fun generatePatch(oldDex: String, newDex: String, outDir: String): Boolean {
        if (!oldDex.isFilePathValid(Suffix.DEX)) return false
        if (!newDex.isFilePathValid(Suffix.DEX)) return false
        if (outDir.isEmpty()) return false
        val tempDir = "${outDir}${File.separator}patch_temp"
        val oldDexTemp = File(tempDir, "old.dex")
        val newDexTemp = File(tempDir, "new.dex")
        println("copy resource dex file.")
        FileUtils.copyFile(oldDex, oldDexTemp.absolutePath)
        FileUtils.copyFile(newDex, newDexTemp.absolutePath)
        if (oldDexTemp.readBytes().contentEquals(newDexTemp.readBytes())) {
            println("The content of the new and old dex is the same, so the incremental package cannot be generated.")
            return true
        }

        println("convert dex to jar")
        val oldJar = File(tempDir, "old.jar")
        if (!dex2jar(oldDexTemp.absolutePath, oldJar.absolutePath)) {
            println("oldDex dex2jar cause error.")
            return false
        }

        val newJar = File(tempDir, "new.jar")
        if (!dex2jar(newDexTemp.absolutePath, newJar.absolutePath)) {
            println("newDex dex2jar cause error.")
            return false
        }

        println("unzip the jar file")
        val oldDir = File(tempDir, "old")
        ZipUtils.unzipFile(oldJar, oldDir)
        val newDir = File(tempDir, "new")
        ZipUtils.unzipFile(newJar, newDir)

        /**
         * Store the .class files in the patch
         * Old dex contains A B C
         * New dex includes A B+ D
         * The generated patch package should be B+ D
         * So it will delete the duplicate elements in the old and new dex, and keep the changed and new classes
         * */
        println("generating patch...")
        val patchDir = File(tempDir, "patch")
        FileUtils.copyDir(newDir, patchDir)
        //traverse the patch folder
        for (item in FileUtils.listFilesInDir(patchDir, true)) {
            if (item.isFile) {
                val oldClass = File(oldDir.absolutePath, item.absolutePath.replace(patchDir.absolutePath, ""))
                if (oldClass.exists() && oldClass.isFile) {
                    if (item.readBytes().contentEquals(oldClass.readBytes())) {
                        println("The content of the new and old .class is the same, delete flag:${item.delete()}")
                    }
                }
            }
        }

        println("convert patch dir to patch.jar.")
        val jarOutPath = File(tempDir, "patch.jar")
        val dir2JarCMD = "jar -cvf ${jarOutPath.absolutePath} ${patchDir.absolutePath}"
        if (!Terminal.run(dir2JarCMD)) {
            println("convert dir2jar cause error.")
            return false
        }

        println("convert patch.jar to patch.dex.")
        val patchOutPath = File(outDir, "patch.dex")
        if (!jar2dex(jarOutPath.absolutePath, patchOutPath.absolutePath)) {
            println("convert patch jar2dex cause error.")
            return false
        }
        FileUtils.deleteDir(tempDir)
        println("patch generate success,patch path:$patchOutPath")
        return true
    }

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
        return list
    }

    /**
     * get all apk's package names in android devices
     */
    fun getAllApkPackageNames(device: String): List<String> {
        val list = mutableListOf<String>()
        Terminal.run("adb -s $device shell pm list package", listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                if (line.isNotEmpty()) {
                    val pkg = line.replace("package:", "")
                    list.add(pkg)
                }
            }
        })
        return list
    }


    /**
     * install apk
     */
    fun installApk(device: String, debug: Boolean = false, apkPath: String): Boolean {
        if (!apkPath.isFilePathValid()) return false
        val debugStr = if (debug) "-t" else ""
        return Terminal.run("adb -s $device $debugStr install -r $apkPath")
    }

    /**
     * extract apk
     */
    fun extractApk(device: String, pkgName: String, outDir: String): Boolean {
        if (pkgName.isEmpty()) return false
        if (!outDir.isDirPathValid()) return false
        var apkPath = ""
        Terminal.run("adb -s $device shell pm path $pkgName", listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                apkPath = line.replace("package:", "")
            }
        })
        if (apkPath.isEmpty()) return false
        val time = System.currentTimeMillis()
        val outPath = File(outDir, "${pkgName}_${time}.apk").absolutePath

        return Terminal.run("adb -s $device pull $apkPath $outPath")
    }
}