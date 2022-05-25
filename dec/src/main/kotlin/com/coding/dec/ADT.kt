package com.coding.dec

import com.coding.dec.utils.PathUtils
import com.coding.dec.utils.Suffix
import com.coding.utils.FileUtils
import com.coding.utils.Terminal
import com.coding.utils.ZipUtils
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
        ignoreSrc: Boolean,
        outPath: String = ""
    ) {
        if (apkPath.isEmpty()) return
        if (!apkPath.endsWith(Suffix.APK)) return
        val dexStr = if (ignoreDex) "-s" else ""
        val srcStr = if (ignoreSrc) "-r" else ""
        val onlyMainClasses = if (ignoreDex) "" else "-only-main-classes"
        val newOutPath = outPath.ifEmpty {
            apkPath.substring(0, apkPath.length - 4)
        }
        val cmd =
            "${PathUtils.getJava()} -jar ${PathUtils.getApkTool()} d $apkPath $dexStr $onlyMainClasses $srcStr -f -o $newOutPath"
        Terminal.run(cmd)
    }

    /**
     * dex2jar
     * convert dex file to jar file
     * */
    fun dex2jar(dexPath: String, outPath: String = "") {
        if (dexPath.isEmpty()) return
        if (!dexPath.endsWith(Suffix.DEX)) return
        if (!Terminal.isWindows()) Terminal.run("chmod u+x ${PathUtils.getDex2jar()}", null)
        val newOutPath = outPath.ifEmpty {
            dexPath.substring(0, dexPath.lastIndexOf('.')) + "_d2j.jar"
        }
        val cmd = "${PathUtils.getDex2jar()} $dexPath -f -o $newOutPath"
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
    fun jar2dex(jarPath: String, outPath: String = "") {
        if (jarPath.isEmpty()) return
        if (!jarPath.endsWith(Suffix.JAR)) return
        if (!Terminal.isWindows()) Terminal.run("chmod u+x ${PathUtils.getJar2dex()}", null)
        val newOutPath = outPath.ifEmpty {
            jarPath.substring(0, jarPath.lastIndexOf('.')) + "_j2d.dex"
        }
        val cmd = "${PathUtils.getJar2dex()} $jarPath -f -o $newOutPath"
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
    fun backToApk(srcDir: String, outPath: String = "") {
        if (srcDir.isEmpty()) return
        val newOutPath = outPath.ifEmpty {
            "${srcDir}_btc.apk"
        }
        val cmd = "${PathUtils.getJava()} -jar ${PathUtils.getApkTool()} b $srcDir -f -o $newOutPath"
        Terminal.run(cmd)
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
        if (apkPath.isEmpty()) return false
        if (!apkPath.endsWith(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.substring(0, apkPath.length - 4) + "_signed.apk"
        }
        val v3EnableStr = if (v3Enable) "--v3-signing-enabled true " else ""
        val v4EnableStr = if (v4Enable) "--v4-signing-enabled true " else ""

        val cmd = "${PathUtils.getJava()} -jar ${PathUtils.getApkSigner()} sign " +
                "--ks ${signConfig.path} " +
                "--ks-key-alias ${signConfig.alias} " +
                "--ks-pass pass:${signConfig.pwd} " +
                "--key-pass pass:${signConfig.aliasPwd} " +
                "--v1-signing-enabled $v1Enable " +
                "--v2-signing-enabled $v2Enable " +
                "$v3EnableStr " +
                "$v4EnableStr " +
                "-v --out $newOutPath $apkPath"
        var success = false
        Terminal.run(cmd, object : Terminal.OnResultListener {
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
    fun alignApk(apkPath: String, outPath: String = ""): Boolean {
        if (apkPath.isEmpty()) return false
        if (!apkPath.endsWith(Suffix.APK)) return false
        val newOutPath = outPath.ifEmpty {
            apkPath.substring(0, apkPath.lastIndexOf('.')) + "_aligned.apk"
        }
        val cmd = "${PathUtils.getZipalign()} -f -v 4 $apkPath $newOutPath"
        var success = false
        Terminal.run(cmd, object : Terminal.OnResultListener {
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

    fun aab2Apks(aabPath: String, sign: SignCfgUtil.SignConfig, outPath: String = "") {
        if (aabPath.isEmpty()) return
        if (!aabPath.endsWith(Suffix.AAB)) return
        val newOutPath = outPath.ifEmpty {
            aabPath.substring(0, aabPath.lastIndexOf('.')) + ".apks"
        }
        val cmd = "java -jar ${PathUtils.getBundleTool()} build-apks " +
                "--bundle=${aabPath} " +
                "--output=${newOutPath} " +
                "--ks=${sign.path} " +
                "--ks-pass pass:${sign.pwd} " +
                "--ks-key-alias=${sign.alias} " +
                "--key-pass pass:${sign.aliasPwd}"
        Terminal.run(cmd, object : Terminal.OnResultListener {
            override fun onStdout(msg: String) {

            }

            override fun onStdErr(err: String) {

            }

        })
        return
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