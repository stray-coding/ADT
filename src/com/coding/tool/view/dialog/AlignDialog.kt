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
 * @des:apk 对齐
 */
object AlignDialog : JDialog() {
    init {
        val pane = JPanel()

        val srcPathTv = JTextField(45)
        val srcPathBtn = JButton("apk path")
        srcPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "choose apk", Suffix.APK, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val submitBtn = JButton("align")
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
                val outPath = srcPath.substring(0, srcPath.lastIndexOf('.')) + "_align.apk"
                val cmd = "${ToolUtil.getZipalign()} -f -v 4 $srcPath $outPath"
                logTA.text = logTA.text + cmd + "\n"
                CMD.CMD(cmd) { msg ->
                    logTA.text = logTA.text + msg
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
        title = "apk align"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}