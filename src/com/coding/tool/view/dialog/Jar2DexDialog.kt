package com.coding.tool.view.dialog

import com.coding.tool.util.CMD
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
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "选择jar", Suffix.JAR, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val outPathTv = JTextField(45)
        outPathTv.isEnabled = false
        val outPathBtn = JButton("out path")
        outPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "选择保存目录", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    outPathTv.text = path
                }
            })
        }

        val submitBtn = JButton("jar2dex")
        val logTA = JTextArea("日志")
        logTA.rows = 23
        logTA.columns = 55
        val scrollPane = JScrollPane(logTA)
        logTA.lineWrap = true
        submitBtn.addActionListener {
            logTA.text = ""
            val srcPath = srcPathTv.text
            if (srcPath.isEmpty()) {
                logTA.text = "源文件路径不能为空"
                return@addActionListener
            } else {
                val outPath = if (outPathTv.text.isEmpty()) {
                    val lastIndex = srcPath.lastIndexOf('.')
                    srcPath.substring(0, lastIndex) + ".dex"
                } else
                    " ${outPathTv.text}"
                val cmd = "${ToolUtil.getJar2dex()} $srcPath -f -o $outPath"
                logTA.text = logTA.text + cmd + "\n"
                CMD.CMD(cmd) { msg ->
                    logTA.text = logTA.text + msg
                }
            }
        }

        pane.add(srcPathBtn)
        pane.add(srcPathTv)

/*        pane.add(outPathBtn)
        pane.add(outPathTv)*/

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