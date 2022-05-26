package com.coding.dec

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
        if (Terminal.isWindows()) {
            Terminal.run(Tools.getJadx())
        } else {
            Terminal.run("open ${Tools.getJadx()}")
        }
    }

    /**
     * decompile apk file
     * */
    fun decompile(apkPath: String, ignoreDex: Boolean, ignoreRes: Boolean, outPath: String = ""): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val srcStr = if (ignoreDex) "-s" else ""
        val resStr = if (ignoreRes) "-r" else ""
        val omc = if (ignoreDex) "" else "-only-main-classes"
        val finalOutPath = outPath.ifEmpty { apkPath.removeSuffix(Suffix.APK) }
        val cmd =
            "${Tools.getJava()} -jar ${Tools.getApkTool()} d $apkPath $srcStr $omc $resStr -f -o $finalOutPath"
        return Terminal.run(cmd) == 0
    }

    /**
     * dex2jar
     * convert dex file to jar file
     * */
    fun dex2jar(dexPath: String, outPath: String = ""): Boolean {
        if (!dexPath.isFilePathValid(Suffix.DEX)) return false
        if (!Terminal.isWindows()) Terminal.run("chmod u+x ${Tools.getDex2jar()}", null)
        val finalOutPath = outPath.ifEmpty {
            dexPath.substring(0, dexPath.lastIndexOf('.')) + "_d2j.jar"
        }
        val cmd = "${Tools.getDex2jar()} $dexPath -f -o $finalOutPath"
        val code = Terminal.run(cmd)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.contains("-error.zip")) {
                item.delete()
            }
        }
        return code == 0
    }

    /**
     * jar2dex
     * convert jar file to dex file
     * */
    fun jar2dex(jarPath: String, outPath: String = ""): Boolean {
        if (!jarPath.isFilePathValid(Suffix.JAR)) return false
        if (!Terminal.isWindows()) Terminal.run("chmod u+x ${Tools.getJar2dex()}", null)
        val finalOutPath = outPath.ifEmpty {
            jarPath.substring(0, jarPath.indexOf(Suffix.JAR)) + "_j2d.dex"
        }
        val cmd = "${Tools.getJar2dex()} $jarPath -f -o $finalOutPath"
        val code = Terminal.run(cmd, null)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.contains("-error.zip")) {
                item.delete()
            }
        }
        return code == 0
    }

    /**
     * back to apk
     * convert dex and resource to apk file
     * */
    fun backToApk(srcDir: String, outPath: String = ""): Boolean {
        if (!srcDir.isDirPathValid()) return false
        val newOutPath = outPath.ifEmpty { "${srcDir}_btc.apk" }
        val cmd = "${Tools.getJava()} -jar ${Tools.getApkTool()} b $srcDir -f -o $newOutPath"
        return Terminal.run(cmd) == 0
    }

    /**
     * apk sign
     * sign the apk
     * */
    fun apkSign(
        apkPath: String,
        signConfig: SignCfgUtil.SignConfig,
        outPath: String = "",
        v1Enable: Boolean,
        v2Enable: Boolean,
        v3Enable: Boolean = false,
        v4Enable: Boolean = false
    ): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.substring(0, apkPath.length - 4) + "_signed.apk"
        }
        val v3EnableStr = if (v3Enable) "--v3-signing-enabled true " else ""
        val v4EnableStr = if (v4Enable) "--v4-signing-enabled true " else ""

        val cmd = "${Tools.getJava()} -jar ${Tools.getApkSigner()} " +
                "sign " +
                "--ks ${signConfig.path} " +
                "--ks-key-alias ${signConfig.alias} " +
                "--ks-pass pass:${signConfig.pwd} " +
                "--key-pass pass:${signConfig.aliasPwd} " +
                "--v1-signing-enabled $v1Enable " +
                "--v2-signing-enabled $v2Enable " +
                "$v3EnableStr " +
                "$v4EnableStr " +
                "-v --out $newOutPath $apkPath"

        return Terminal.run(cmd) == 0
    }

    /**
     * get apk signature information
     * */
    fun verifyApkSign(apkPath: String): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val cmd = "${Tools.getJava()} -jar ${Tools.getApkSigner()} verify -v $apkPath"
        return Terminal.run(cmd) == 0
    }

    /**
     * perform 4-byte alignment operations on apk
     * */
    fun alignApk(apkPath: String, outPath: String = ""): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.substring(0, apkPath.lastIndexOf('.')) + "_aligned.apk"
        }
        val cmd = "${Tools.getZipalign()} -f -v 4 $apkPath $newOutPath"
        return Terminal.run(cmd) == 0
    }

    fun aab2Apks(aabPath: String, sign: SignCfgUtil.SignConfig, outPath: String = ""): Boolean {
        if (!aabPath.isFilePathValid(Suffix.AAB)) return false
        val newOutPath = outPath.ifEmpty {
            aabPath.substring(0, aabPath.lastIndexOf('.')) + ".apks"
        }
        val cmd = "java -jar ${Tools.getBundleTool()} " +
                "build-apks " +
                "--bundle=${aabPath} " +
                "--output=${newOutPath} " +
                "--ks=${sign.path} " +
                "--ks-pass pass:${sign.pwd} " +
                "--ks-key-alias=${sign.alias} " +
                "--key-pass pass:${sign.aliasPwd}"

        return Terminal.run(cmd) == 0
    }

    /**
     * Generate patch dex based on new and old dex files
     * Perform content verification on the old and new dex files and generate incremental packages and patches
     * */
    fun generatePatch(oldDex: String, newDex: String, outputDir: String) {
        if (oldDex.isEmpty()) return
        if (newDex.isEmpty()) return
        if (outputDir.isEmpty()) return
        val workSpace = "${outputDir}${File.separator}patch_workspace"
        val oldDexWorkspace = "$workSpace${File.separator}old.dex"
        val newDexWorkspace = "$workSpace${File.separator}new.dex"
        println("copy resource dex file.")
        FileUtils.copyFile(oldDex, oldDexWorkspace)
        FileUtils.copyFile(newDex, newDexWorkspace)
        if (File(oldDexWorkspace).readBytes().contentEquals(File(newDexWorkspace).readBytes())) {
            println("The content of the new and old dex is the same, so the incremental package cannot be generated")
            return
        }

        println("convert dex to jar")
        val jarOfOldDexPath = "$workSpace${File.separator}old.jar"
        val jarOfNewDexPath = "$workSpace${File.separator}new.jar"
        Terminal.run("${Tools.getDex2jar()} $oldDexWorkspace -f -o $jarOfOldDexPath")
        Terminal.run("${Tools.getDex2jar()} $newDexWorkspace -f -o $jarOfNewDexPath")

        println("unzip the jar file")
        val jarOfOldDexDir = "$workSpace${File.separator}old"
        val jarOfNewDexDir = "$workSpace${File.separator}new"
        ZipUtils.unzipFile(File(jarOfOldDexPath), File(jarOfOldDexDir))
        ZipUtils.unzipFile(File(jarOfNewDexPath), File(jarOfNewDexDir))

        /**
         * Store the .class files in the patch
         * Old dex contains A B C
         * New dex includes A B+ D
         * The generated patch package should be B+ D
         * So it will delete the duplicate elements in the old and new dex, and keep the changed and new classes
         * */
        println("generating patch...")
        val patchDir = "${workSpace}${File.separator}patch"
        FileUtils.copyDir(jarOfNewDexDir, patchDir)
        for (item in FileUtils.listFilesInDir(patchDir, true)) {
            if (item.isFile) {
                val oldClassFilePath = jarOfOldDexDir + item.absolutePath.replace(patchDir, "")
                println("oldClassFilePath:$oldClassFilePath")
                val oldFile = File(oldClassFilePath)
                if (oldFile.exists() && oldFile.isFile) {
                    if (item.readBytes().contentEquals(oldFile.readBytes())) {
                        println("The content of the new and old .class is the same, delete${item.delete()}")
                    }
                }
            }
        }

        println("convert patch dir to patch.jar.")
        val jarOutPath = "${workSpace}${File.separator}patch.jar"
        val dir2JarCMD = String.format("jar -cvf %s %s", jarOutPath, patchDir)
        Terminal.run(dir2JarCMD, null)

        println("convert patch.jar to patch.dex.")
        val patchOutPath = "$outputDir${File.separator}patch.dex"
        val jar2DexCMD = "${Tools.getJar2dex()} $jarOutPath -f -o $patchOutPath"
        Terminal.run(jar2DexCMD, null)
        FileUtils.deleteDir(workSpace)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.contains("-error.zip")) {
                item.delete()
            }
        }
        println("patch generate success,patch path:$patchOutPath")
    }


}