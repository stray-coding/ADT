package com.coding.dec

import com.coding.dec.utils.Paths
import com.coding.dec.utils.Suffix
import com.coding.utils.*
import java.io.File

object DexTool {

    /**
     * dex2smali
     * convert dex file to smali files
     * */
    fun dex2smali(dexPath: String, outPath: String = ""): Boolean {
        if (!dexPath.isFilePathValid(Suffix.DEX)) return false
        val finalOutPath = outPath.ifEmpty { dexPath.removeSuffix(Suffix.DEX) }
        val cmd = "${Paths.getJava()} -jar ${Paths.getBackSmaliJar()} disassemble $dexPath -o $finalOutPath"
        return Terminal.run(cmd)
    }

    /**
     * smali2dex
     * convert smali files to dex file
     * */
    fun smali2dex(smaliDir: String, outPath: String = ""): Boolean {
        if (!smaliDir.isDirPathValid()) return false
        val finalOutPath = outPath.ifEmpty { "${smaliDir}_back.dex" }
        val cmd = "${Paths.getJava()} -jar ${Paths.getSmaliJar()} assemble $smaliDir -o $finalOutPath"
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
        val cmd = "${Paths.getDex2jar()} $dexPath -f -o $finalOutPath"
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
        val cmd = "${Paths.getJar2dex()} $jarPath -f -o $finalOutPath"
        val isSuc = Terminal.run(cmd)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.endsWith("-error.zip")) {
                item.delete()
            }
        }
        return isSuc
    }

    /**
     * Generate patch dex based on new and old dex files
     * Perform content verification on the old and new dex files and generate incremental packages and patches
     * */
    fun generatePatch(oldDex: String, newDex: String, outDir: String): Boolean {
        if (!oldDex.isFilePathValid(Suffix.DEX)) return false
        if (!newDex.isFilePathValid(Suffix.DEX)) return false
        if (outDir.isEmpty()) return false
        val tempDir = outDir.join("patch_temp")
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
}