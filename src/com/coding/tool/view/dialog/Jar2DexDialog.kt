package com.coding.tool.view.dialog

import com.coding.tool.util.CMD
import com.coding.tool.util.FileUtils
import com.coding.tool.util.Suffix
import com.coding.tool.util.ToolUtil
import com.coding.tool.view.FileJfc
import javax.swing.*

/**
 * @author: Coding.He
 * @date: 2020/7/14
 * @emil: 229101253@qq.com
 * @des:jar文件转换为dex文件
 */
object Jar2DexDialog : JDialog() {
    init {
        val pane = JPanel()

        val srcPathTv = JTextField(45)
        srcPathTv.isEnabled = false
        val srcPathBtn = JButton("jar path")
        srcPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "choose jar", Suffix.JAR, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val submitBtn = JButton("jar2dex")
        val logTA = JTextArea("log")
        logTA.rows = 23
        logTA.columns = 55
        val scrollPane = JScrollPane(logTA)
        logTA.lineWrap = true
        submitBtn.addActionListener {
            logTA.text = ""
            val srcPath = srcPathTv.text
            if (srcPath.isEmpty()) {
                logTA.text = "src path cannot be empty"
                return@addActionListener
            } else {
                val outPath = srcPath.substring(0, srcPath.lastIndexOf('.')) + ".dex"
                val cmd = "${ToolUtil.getJar2dex()} $srcPath -f -o $outPath"
                logTA.text = logTA.text + cmd + "\n"
                CMD.CMD(cmd) { msg ->
                    logTA.text = logTA.text + msg
                }
                for (item in FileUtils.listFilesInDir("./")) {
                    if (item.name.contains("-error.zip")) {
                        item.delete()
                    }
                }
            }
        }

        pane.add(srcPathBtn)
        pane.add(srcPathTv)

        pane.add(submitBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "jar2dex"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}