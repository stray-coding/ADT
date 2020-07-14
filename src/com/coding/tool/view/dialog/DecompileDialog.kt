package com.coding.tool.view.dialog

import com.coding.tool.util.CMD
import com.coding.tool.util.Suffix
import com.coding.tool.util.ToolUtil
import com.coding.tool.view.FileJfc
import java.awt.Checkbox
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:反编译界面
 */
object DecompileDialog : JDialog() {
    init {
        val pane = JPanel()
        val srcPathTv = JTextField(45)
        val srcPathBtn = JButton("apk path")
        srcPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "选择apk", Suffix.APK, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val outPathTv = JTextField(45)
        val outPathBtn = JButton("out path")
        outPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "选择反编译输出目录", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    outPathTv.text = path
                }
            })
        }

        val ignoreDexCb = Checkbox("ignore dex")
        val ignoreSrcCb = Checkbox("ignore resource")
        val forceCoverCb = Checkbox("force cover", true)

        val submitBtn = JButton("反编译")
        val logTA = JTextArea("日志")
        logTA.rows = 20
        logTA.columns = 50
        val scrollPane = JScrollPane(logTA)
        logTA.lineWrap = true
        submitBtn.addActionListener {
            logTA.text = ""
            val dex = if (ignoreDexCb.state) "-s" else ""
            val src = if (ignoreSrcCb.state) "-r" else ""
            val forceCover = if (forceCoverCb.state) "-f" else ""
            val srcPath = srcPathTv.text
            if (srcPath == "") {
                return@addActionListener
            }
            val outPath = if (outPathTv.text.isEmpty()) {
                srcPath.substring(0, srcPath.length - 4)
            } else
                " ${outPathTv.text}"
            val cmd = "java -jar ${ToolUtil.getApkTool()} d $srcPath $dex $src $forceCover -o $outPath"
            logTA.text = logTA.text + cmd + "\n"
            CMD.CMD(cmd) { msg ->
                logTA.text = logTA.text + msg
            }
        }

        pane.add(srcPathBtn)
        pane.add(srcPathTv)
/*        pane.add(outPathBtn)
        pane.add(outPathTv)*/

        pane.add(ignoreDexCb)
        pane.add(ignoreSrcCb)
        pane.add(forceCoverCb)

        pane.add(submitBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "apk反编译"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}