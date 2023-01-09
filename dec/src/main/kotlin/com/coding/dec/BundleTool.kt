package com.coding.dec

import com.coding.dec.utils.Paths
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import com.coding.utils.FileUtils
import com.coding.utils.Terminal
import com.coding.utils.ZipUtils
import com.coding.utils.isFilePathValid
import net.lingala.zip4j.ZipFile
import java.io.File

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
        val cmd = "java -jar ${Paths.getBundleTool()} " +
                "build-apks " + "--bundle=${aabPath} " +
                "--output=${newOutPath} " +
                "--ks=${signBean.path} " +
                "--ks-pass pass:${signBean.pwd} " +
                "--ks-key-alias=${signBean.alias} " +
                "--key-pass pass:${signBean.aliasPwd} " + universalStr

        return Terminal.run(cmd)
    }

    /**
     *
     */
    fun apk2AAB(apkPath: String, signBean: SignUtils.SignBean): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) {
            println("apk is not exist,failed")
            return false
        }

        println("-----1. create temp dir-----")
        val tempPath = File(apkPath.substringBeforeLast(File.separator), "work_temp").absolutePath
        FileUtils.deleteDir(tempPath)
        FileUtils.createOrExistsDir(tempPath)

        println("-----2. decompile apk-----")
        val decompilePath = File(tempPath, "decompile").absolutePath
        Terminal.run("${Paths.getJava()} -jar ${Paths.getApkTool()} d $apkPath -f -o $decompilePath -only-main-classes")
        val apkInfo = getValueFromApkToolYml("$decompilePath${File.separator}apktool.yml")

        println("-----3. compile resource-----")
        val compiledResourcesZipPath = File(tempPath, "compiled_resources.zip").absolutePath
        Terminal.run("${Paths.getAAPT2()} compile --dir $decompilePath${File.separator}res -o $compiledResourcesZipPath")

        println("-----4. link resource-----")
        val linkApk = File(tempPath, "link.apk").absolutePath
        Terminal.run(
            "${Paths.getAAPT2()} link " +
                    "--proto-format " +
                    "-o $linkApk " +
                    "-I ${Paths.getAndroidJar()} " +
                    "--min-sdk-version ${apkInfo.minsdk} " +
                    "--target-sdk-version ${apkInfo.targetsdk} " +
                    "--version-code ${apkInfo.versionCode} " +
                    "--version-name ${apkInfo.versionName} " +
                    "--manifest $decompilePath${File.separator}AndroidManifest.xml " +
                    "-R $compiledResourcesZipPath " +
                    "--auto-add-overlay"
        )
        if (!FileUtils.isFileExists(linkApk)) {
            println("link apk is not exist,failed")
            return false
        }

        println("-----5. unzip apk-----")
        val baseDirPath = File(tempPath, "base").absolutePath
        ZipUtils.unzipFile(linkApk, baseDirPath)

        println("-----6. copy all resource-----")

        println("-----copy AndroidManifest.xml-----")
        FileUtils.moveFile(
            "$baseDirPath${File.separator}AndroidManifest.xml",
            "$baseDirPath${File.separator}manifest${File.separator}AndroidManifest.xml"
        )

        println("-----copy assets dir-----")
        FileUtils.copyDir(
            "$decompilePath${File.separator}assets",
            "$baseDirPath${File.separator}assets"
        )

        println("-----copy lib dir-----")
        FileUtils.copyDir(
            "$decompilePath${File.separator}lib",
            "$baseDirPath${File.separator}lib"
        )

        println("-----copy unknown dir-----")
        FileUtils.copyDir(
            "$decompilePath${File.separator}unknown",
            "$baseDirPath${File.separator}root"
        )

        println("-----copy kotlin dir-----")
        FileUtils.copyDir(
            "$decompilePath${File.separator}kotlin",
            "$baseDirPath${File.separator}root${File.separator}kotlin"
        )

        println("-----copy META-INF dddir-----")
        FileUtils.copyDir(
            "$decompilePath${File.separator}original${File.separator}META-INF",
            "$baseDirPath${File.separator}root${File.separator}META-INF"
        )

        println("-----copy dex files-----")
        FileUtils.createOrExistsDir("$baseDirPath${File.separator}dex")
        for (file in FileUtils.listFilesInDir(decompilePath)) {
            if (file.isDirectory && file.name.contains("smali")) {
                val array = file.name.split("_")
                val outDexName = if (array.size > 1) {
                    "${array[1]}.dex"
                } else {
                    "classes.dex"
                }
                Terminal.run(
                    "${Paths.getJava()} -jar ${Paths.getSmaliJar()} assemble $decompilePath${File.separator}${file.name} " +
                            "-o $baseDirPath${File.separator}dex${File.separator}$outDexName"
                )
            }
        }

        println("-----7. zip all resource to .zip file-----")
        val items = arrayListOf(
            "$baseDirPath${File.separator}assets",
            "$baseDirPath${File.separator}dex",
            "$baseDirPath${File.separator}lib",
            "$baseDirPath${File.separator}manifest",
            "$baseDirPath${File.separator}res",
            "$baseDirPath${File.separator}root",
            "$baseDirPath${File.separator}resources.pb"
        )
        val baseZipPath = "$tempPath${File.separator}base.zip"
        val zipFile = ZipFile(baseZipPath)
        for (item in items) {
            val file = File(item)
            if (file.exists()) {
                if (file.isDirectory) {
                    zipFile.addFolder(File(item))
                } else {
                    zipFile.addFile(file)
                }
            }
        }
        if (!FileUtils.isFileExists(baseZipPath)) {
            println("base zip is not exist,failed")
            return false
        }

        println("-----8. create .aab file-----")
        val finalAABPath = "${apkPath.substringBeforeLast(".")}.aab"
        FileUtils.deleteFile(finalAABPath)
        Terminal.run(
            "${Paths.getJava()} -jar ${Paths.getBundleTool()} build-bundle " +
                    "--modules=$baseZipPath " +
                    "--output=$finalAABPath " +
                    "--config=${Paths.getBundleConfigJson()}"
        )
        if (!FileUtils.isFileExists(finalAABPath)) {
            println("aab is not exist,failed")
            return false
        }
        FileUtils.deleteDir(tempPath)

        println("-----9. sign the aab file-----")
        return Terminal.run(
            "jarsigner " +
                    "-digestalg SHA1 " +
                    "-sigalg SHA1withRSA " +
                    "-keystore ${signBean.path} " +
                    "-storepass ${signBean.pwd} " +
                    "-keypass ${signBean.aliasPwd} " +
                    "$finalAABPath " +
                    signBean.alias
        )
    }
}

class ApkInfo {
    var apkName: String = ""
    var minsdk: Int = 0
    var targetsdk: Int = 0
    var versionCode: Int = 0
    var versionName: String = ""
    override fun toString(): String {
        return "ApkInfo(apkName='$apkName', minsdk=$minsdk, targetsdk=$targetsdk, versionCode=$versionCode, versionName='$versionName')"
    }
}

object ApktoolTAG {
    const val MIN_SDK_VERSION = "minSdkVersion"
    const val TARGET_SDK_VERSION = "targetSdkVersion"
    const val VERSION_CODE = "versionCode"
    const val VERSION_NAME = "versionName"
    const val APK_FILE_NAME = "apkFileName"
}

val TAG_LIST = arrayListOf(
    ApktoolTAG.APK_FILE_NAME,
    ApktoolTAG.MIN_SDK_VERSION,
    ApktoolTAG.TARGET_SDK_VERSION,
    ApktoolTAG.VERSION_CODE,
    ApktoolTAG.VERSION_NAME
)

private fun getValueFromApkToolYml(ymlPath: String): ApkInfo {
    val apkInfo = ApkInfo()
    if (!ymlPath.isFilePathValid()) return apkInfo
    try {
        for (line in File(ymlPath).readLines()) {
            for (tag in TAG_LIST) {
                if (line.contains(tag)) {
                    val array = line.split(":")
                    if (array.size >= 2) {
                        val value = array[1].replace("\"", "").replace("'", "").trim()
                        when (tag) {
                            ApktoolTAG.APK_FILE_NAME -> apkInfo.apkName = value
                            ApktoolTAG.MIN_SDK_VERSION -> apkInfo.minsdk = value.toInt()
                            ApktoolTAG.TARGET_SDK_VERSION -> apkInfo.targetsdk = value.toInt()
                            ApktoolTAG.VERSION_CODE -> apkInfo.versionCode = value.toInt()
                            ApktoolTAG.VERSION_NAME -> apkInfo.versionName = value
                        }
                    }
                }
            }
        }
    } catch (_: Exception) {
    }
    return apkInfo
}

fun main() {
    println(
        BundleTool.apk2AAB(
            "C:\\Users\\root\\Desktop\\app-debug.apk",
            SignUtils.getSign("adt.jks")!!
        )
    )
}