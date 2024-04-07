package com.coding.dec

import com.coding.dec.bean.SignBean
import com.coding.dec.utils.*
import com.coding.utils.FileUtils
import com.coding.utils.ZipUtils
import net.lingala.zip4j.ZipFile
import java.io.File
import java.util.concurrent.CompletableFuture
import java.util.concurrent.Executors

object BundleTool {
    /**
     * aab file 2 apks
     */
    fun aab2Apks(
        aabPath: String,
        signBean: SignBean,
        outPath: String = "",
        universal: Boolean = false
    ): Boolean {
        if (!aabPath.isFilePathValid(Suffix.AAB)) return false
        val newOutPath = outPath.ifEmpty {
            aabPath.removeSuffix(Suffix.AAB) + ".apks"
        }
        FileUtils.delete(newOutPath)
        val cmd = mutableListOf<String>().put(
            Paths.getJava(), "-jar", Paths.getBundleTool(),
            "build-apks", "--bundle=$aabPath",
            "--output=$newOutPath",
        ).put(
            signBean.path.isNotEmpty(),
            "--ks=${signBean.path}",
            "--ks-pass", "pass:${signBean.pwd}",
            "--ks-key-alias=${signBean.alias}",
            "--key-pass", "pass:${signBean.aliasPwd}"
        )
            .put(universal, "--mode=universal")
        return Terminal.run(cmd)
    }

    /**
     *
     */
    fun apk2AAB(apkPath: String, signBean: SignBean): Boolean {
        if (!apkPath.isFilePathValid(Suffix.APK)) {
            println("apk is not exist,failed")
            return false
        }

        println("-----1. create temp dir-----")
        val tempPath = File(apkPath.substringBeforeLast(File.separator), "work_temp").absolutePath
        FileUtils.delete(tempPath)
        FileUtils.createOrExistsDir(tempPath)

        println("-----2. decompile apk-----")
        val decompilePath = File(tempPath, "decompile").absolutePath
        Terminal.run("${Paths.getJava()} -jar ${Paths.getApkTool()} d $apkPath -f -o $decompilePath -only-main-classes")
        val apkInfo = YmlUtils.getValueFromApkToolYml(decompilePath.join("apktool.yml"))

        println("-----3. compile resource-----")
        val compiledResourcesZipPath = File(tempPath, "compiled_resources.zip").absolutePath
        Terminal.run("${Paths.getAAPT2()} compile --dir ${decompilePath.join("res")} -o $compiledResourcesZipPath")

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
                    "--manifest ${decompilePath.join("AndroidManifest.xml")} " +
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
        val tasks = ArrayList<CompletableFuture<Void>>(7)
        val customThreadPool = Executors.newFixedThreadPool(7)
        tasks.add(CompletableFuture.runAsync({
            println("-----copy AndroidManifest.xml-----")
            FileUtils.move(baseDirPath.join("AndroidManifest.xml"), baseDirPath.join("manifest", "AndroidManifest.xml"))
        }, customThreadPool))

        tasks.add(CompletableFuture.runAsync({
            println("-----copy assets dir-----")
            FileUtils.copy(decompilePath.join("assets"), baseDirPath.join("assets"))
        }, customThreadPool))

        tasks.add(CompletableFuture.runAsync({
            println("-----copy lib dir-----")
            FileUtils.copy(decompilePath.join("lib"), baseDirPath.join("lib"))
        }, customThreadPool))

        tasks.add(CompletableFuture.runAsync({
            println("-----copy unknown dir-----")
            FileUtils.copy(decompilePath.join("unknown"), baseDirPath.join("root"))
        }, customThreadPool))

        tasks.add(CompletableFuture.runAsync({
            println("-----copy kotlin dir-----")
            FileUtils.copy(decompilePath.join("kotlin"), baseDirPath.join("root", "kotlin"))
        }, customThreadPool))

        tasks.add(CompletableFuture.runAsync({
            println("-----copy META-INF dir-----")
            FileUtils.copy(decompilePath.join("original", "META-INF"), baseDirPath.join("root", "META-INF"))
        }, customThreadPool))

        tasks.add(CompletableFuture.runAsync({
            println("-----copy dex files-----")
            FileUtils.createOrExistsDir(baseDirPath.join("dex"))
            for (file in FileUtils.listFilesInDir(decompilePath)) {
                if (file.isDirectory && file.name.contains("smali")) {
                    val array = file.name.split("_")
                    val outDexName = if (array.size > 1) {
                        "${array[1]}.dex"
                    } else {
                        "classes.dex"
                    }
                    Terminal.run(
                        "${Paths.getJava()} -jar ${Paths.getSmaliJar()} " +
                                "assemble ${decompilePath.join(file.name)} " +
                                "-o ${baseDirPath.join("dex", outDexName)}"
                    )
                }
            }
        }, customThreadPool))
        CompletableFuture.allOf(*tasks.toTypedArray()).join()
        customThreadPool.shutdown()


        println("-----7. zip all resource to .zip file-----")
        val baseZipPath = tempPath.join("base.zip")
        zipAllInDir(baseDirPath, baseZipPath)

        if (!FileUtils.isFileExists(baseZipPath)) {
            println("base zip is not exist,failed")
            return false
        }

        println("-----8. create .aab file-----")
        val finalAABPath = "${apkPath.substringBeforeLast(".")}.aab"
        FileUtils.delete(finalAABPath)
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
        FileUtils.delete(tempPath)

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

    /**
     * 压缩某个目录下的所有文件，但不包括自身
     *
     * @param dir
     * @param outZip
     */
    private fun zipAllInDir(dir: String, outZip: String) {
        val zipFile = ZipFile(outZip)
        File(dir).listFiles()?.let {
            for (item in it) {
                if (item.exists()) {
                    if (item.isDirectory) {
                        zipFile.addFolder(item)
                    } else {
                        zipFile.addFile(item)
                    }
                }
            }
        }
    }
}