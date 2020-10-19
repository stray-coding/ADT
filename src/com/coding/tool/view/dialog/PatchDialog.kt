package com.coding.tool.view.dialog

import com.coding.tool.util.*
import com.coding.tool.view.FileJfc
import java.io.File
import javax.swing.*
import javax.swing.filechooser.FileSystemView

/**
 * @author: Coding.He
 * @date: 2020/7/14
 * @emil: 229101253@qq.com
 * @des:补丁包生成类
 */
object PatchDialog : JDialog() {
    init {
        val pane = JPanel()

        val oldDexPathTv = JTextField(45)
        val oldDexPathBtn = JButton("oldDex")
        oldDexPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "choose dex", Suffix.DEX, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    oldDexPathTv.text = path
                }
            })
        }

        val newDexPathTv = JTextField(45)
        val newDexPathBtn = JButton("newDex")
        newDexPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "choose dex", Suffix.DEX, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    newDexPathTv.text = path
                }
            })
        }

        val outPathTv = JTextField(45)
        outPathTv.text = FileSystemView.getFileSystemView().homeDirectory.absolutePath
        val outPathBtn = JButton("patch out path")
        outPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "out path", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    outPathTv.text = path
                }
            })
        }

        val submitBtn = JButton("generate patch")
        val logTA = JTextArea("log")
        logTA.rows = 19
        logTA.columns = 55
        val scrollPane = JScrollPane(logTA)
        logTA.lineWrap = true

        pane.add(oldDexPathBtn)
        pane.add(oldDexPathTv)

        pane.add(newDexPathBtn)
        pane.add(newDexPathTv)

        pane.add(outPathBtn)
        pane.add(outPathTv)

        pane.add(submitBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "generate patch"
        isVisible = true
        isResizable = false
        submitBtn.addActionListener {
            logTA.text = "the patch is being generated，please wait··· \n"
            Thread {
                submitBtn.isEnabled = false
                val patchPath = generatePatch(oldDexPathTv.text, newDexPathTv.text, outPathTv.text)
                logTA.text += "the patch is generated successfully，the path is：$patchPath \n"
                submitBtn.isEnabled = true
            }.start()
        }
    }


    fun showDialog() {
        isVisible = true
    }

    /**
     * 根据新旧dex文件，生成补丁dex
     * */
    private fun generatePatch(oldDex: String, newDex: String, outputDir: String): String {
        val workSpace = "${outputDir}\\gene_patch_work"
        /*将新旧dex转化为jar*/
        val jarOfOldDexPath = "$workSpace\\old-classes.jar"
        val jarOfNewDexPath = "$workSpace\\new-classes.jar"
        CMD.CMDSync("${ToolUtil.getDex2jar()} $oldDex -f -o $jarOfOldDexPath", null)
        CMD.CMDSync("${ToolUtil.getDex2jar()} $newDex -f -o $jarOfNewDexPath", null)

        /*将jar解压*/
        val jarOfOldDexDir = "$workSpace\\old-classes"
        val jarOfNewDexDir = "$workSpace\\new-classes"
        ZipUtils.unzipFile(File(jarOfOldDexPath), File(jarOfOldDexDir))
        ZipUtils.unzipFile(File(jarOfNewDexPath), File(jarOfNewDexDir))

        /**
         * 存放patch中的.class文件
         * 老dex包含 A B C
         * 新dex包含 A B+ D
         * 则生成的补丁包应该为B+ D
         * 所以应该删除新旧dex中重复的元素，保留变更、新增的,class
         * */
        val patchDir = "${workSpace}\\patch-classes"
        FileUtils.copyDir(jarOfNewDexDir, patchDir)
        for (item in FileUtils.listFilesInDir(patchDir, true)) {
            if (item.isFile) {
                val oldClassFilePath = jarOfOldDexDir + item.absolutePath.replace(patchDir, "")
                println("oldClassFilePath:$oldClassFilePath")
                val oldFile = File(oldClassFilePath)
                if (oldFile.exists() && oldFile.isFile) {
                    if (item.readBytes().contentEquals(oldFile.readBytes())) {
                        println("因新旧.class一致，故删除${item.delete()}")
                    }
                }
            }
        }

        /*将dir转化为patch.jar文件*/
        val jarOutPath = "${workSpace}\\patch.jar"
        val dir2JarCMD = String.format("jar -cvf %s %s", jarOutPath, patchDir)
        CMD.CMDSync(dir2JarCMD, null)

        /*最后将patch.jar转化为dex*/
        val patchOutPath = "$outputDir\\auto_patch.dex"
        val jar2DexCMD = "${ToolUtil.getJar2dex()} $jarOutPath -f -o $patchOutPath"
        CMD.CMDSync(jar2DexCMD, null)
        FileUtils.deleteDir(workSpace)
        for (item in FileUtils.listFilesInDir("./")) {
            if (item.name.contains("-error.zip")) {
                item.delete()
            }
        }
        return patchOutPath
    }

}