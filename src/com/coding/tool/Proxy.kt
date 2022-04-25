package com.coding.tool

import com.coding.tool.util.*
import java.io.File

/**
 * @author: Coding.He
 * @date: 2021/1/2
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object Proxy {
    /**
     * open jadx-gui
     * */
    fun openJadx() {
        if (Terminal.isWindows()) {
            Terminal.run(PathUtils.getJadx())
        } else {
            Terminal.run("open ${PathUtils.getJadx()}")
        }
    }

    /**
     * decompile apk file
     * */
    fun decompile(
        apkPath: String,
        ignoreDex: Boolean,
        ignoreSrc: Boolean
    ) {
        if (apkPath.isEmpty()) return
        if (!apkPath.endsWith(Suffix.APK)) return
        val dexStr = if (ignoreDex) "-s" else ""
        val srcStr = if (ignoreSrc) "-r" else ""
        val onlyMainClasses = if(ignoreDex) "" else "-only-main-classes"
        val outPath = apkPath.substring(0, apkPath.length - 4)
        val cmd = "${PathUtils.getJava()} -jar ${PathUtils.getApkTool()} d $apkPath $dexStr $onlyMainClasses $srcStr -f -o $outPath"
        Terminal.run(cmd)
    }

    /**
     * dex2jar
     * convert dex file to jar file
     * */
    fun dex2jar(dexPath: String) {
        if (dexPath.isEmpty()) return
        if (!dexPath.endsWith(Suffix.DEX)) return
        if (!Terminal.isWindows()) Terminal.run("chmod u+x ${PathUtils.getDex2jar()}", null)
        val outPath = dexPath.substring(0, dexPath.lastIndexOf('.')) + "_d2j.jar"
        val cmd = "${PathUtils.getDex2jar()} $dexPath -f -o $outPath"
        Terminal.run(cmd, null)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.contains("-error.zip")) {
                item.delete()
            }
        }
    }

    /**
     * jar2dex
     * convert jar file to dex file
     * */
    fun jar2dex(jarPath: String) {
        if (jarPath.isEmpty()) return
        if (!jarPath.endsWith(Suffix.JAR)) return
        if (!Terminal.isWindows()) Terminal.run("chmod u+x ${PathUtils.getJar2dex()}", null)
        val outPath = jarPath.substring(0, jarPath.lastIndexOf('.')) + "_j2d.dex"
        val cmd = "${PathUtils.getJar2dex()} $jarPath -f -o $outPath"
        Terminal.run(cmd, null)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.contains("-error.zip")) {
                item.delete()
            }
        }
    }

    /**
     * back to apk
     * convert dex and resource to apk file
     * */
    fun backToApk(srcDir: String) {
        if (srcDir.isEmpty()) return
        val outPath = "${srcDir}_btc.apk"
        val cmd = "${PathUtils.getJava()} -jar ${PathUtils.getApkTool()} b $srcDir -f -o $outPath"
        Terminal.run(cmd)
    }

    /**
     * apk sign
     * sign the apk
     * */
    fun apkSign(
        apkPath: String,
        signConfig: SignCfgUtil.SignConfig,
        v1Enable: Boolean,
        v2Enable: Boolean
    ): Boolean {
        if (apkPath.isEmpty()) return false
        if (!apkPath.endsWith(Suffix.APK)) return false
        val finalApkName = apkPath.substring(0, apkPath.length - 4) + "_signed.apk"
        val cmd = "${PathUtils.getJava()} -jar ${PathUtils.getApkSigner()} sign " +
                "--ks ${signConfig.path} " +
                "--ks-key-alias ${signConfig.alias} " +
                "--ks-pass pass:${signConfig.pwd} " +
                "--key-pass pass:${signConfig.aliasPwd} " +
                "--v1-signing-enabled $v1Enable " +
                "--v2-signing-enabled $v2Enable " +
                "-v --out $finalApkName $apkPath"
        var success = false
        Terminal.run(cmd, object : Terminal.OnResultCallback {
            override fun onStdout(msg: String) {
                if (msg == "Signed") {
                    success = true
                }
            }

            override fun onStdErr(err: String) {
                success = false
            }

        })
        return success
    }

    /**
     * get apk signature information
     * */
    fun verifyApkSign(apkPath: String) {
        val cmd = "${PathUtils.getJava()} -jar ${PathUtils.getApkSigner()} verify -v $apkPath"
        Terminal.run(cmd)
    }

    /**
     * perform 4-byte alignment operations on apk
     * */
    fun alignApk(apkPath: String): Boolean {
        if (apkPath.isEmpty()) return false
        if (!apkPath.endsWith(Suffix.APK)) return false
        val outPath = apkPath.substring(0, apkPath.lastIndexOf('.')) + "_aligned.apk"
        val cmd = "${PathUtils.getZipalign()} -f -v 4 $apkPath $outPath"
        var success = false
        Terminal.run(cmd, object : Terminal.OnResultCallback {
            override fun onStdout(msg: String) {
                if (msg == ("Verification succesful")) {
                    success = true
                }
            }

            override fun onStdErr(err: String) {
                success = false
            }

        })
        return success
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
        Terminal.run("${PathUtils.getDex2jar()} $oldDexWorkspace -f -o $jarOfOldDexPath")
        Terminal.run("${PathUtils.getDex2jar()} $newDexWorkspace -f -o $jarOfNewDexPath")

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
        val jar2DexCMD = "${PathUtils.getJar2dex()} $jarOutPath -f -o $patchOutPath"
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