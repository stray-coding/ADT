package com.coding.tool.view.dialog

import com.coding.tool.util.CMD
import com.coding.tool.util.ToolUtil
import com.coding.tool.view.FileJfc
import java.awt.Checkbox
import java.awt.Dimension
import java.awt.Toolkit
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:反编译界面
 */
object BackToApkDialog : JDialog() {
    private fun test() {
        val kit: Toolkit = Toolkit.getDefaultToolkit() // 定义工具包
        val screenSize: Dimension = kit.screenSize // 获取屏幕的尺寸
        val screenWidth = screenSize.width / 2 // 获取屏幕的宽
        val screenHeight = screenSize.height / 2 // 获取屏幕的高
        val height = height //对象的高
        val width = width //对象的宽
        setLocation(screenWidth - width / 2, screenHeight - height / 2) //设置对象居中显示
    }

    init {
        val pane = JPanel()
        val srcPathTv = JTextField(45)
        val srcPathBtn = JButton("src dir")
        srcPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "选择资源目录", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val outPathTv = JTextField(45)
        val outPathBtn = JButton("out path")
        outPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "选择apk输出输出目录", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    outPathTv.text = path
                }
            })
        }

        val copyOriginalCb = Checkbox("copy original")
        val forceCoverCb = Checkbox("force cover", true)

        val submitBtn = JButton("回编译")
        val logTA = JTextArea("日志")
        logTA.rows = 20
        logTA.columns = 50
        val scrollPane = JScrollPane(logTA)
        logTA.lineWrap = true
        submitBtn.addActionListener {
            logTA.text = ""
            val origin = if (copyOriginalCb.state) "-c" else ""
            val forceCover = if (forceCoverCb.state) "-f" else ""
            val srcPath = srcPathTv.text
            if (srcPath == "") {
                return@addActionListener
            }
            val outPath = if (outPathTv.text.isEmpty()) {
                "$srcPath.apk"
            } else
                " ${outPathTv.text}"
            val cmd = "java -jar ${ToolUtil.getApkTool()} b $srcPath $origin $forceCover -o $outPath"
            logTA.text = logTA.text + cmd + "\n"
            CMD.CMD(cmd) { msg ->
                logTA.text = logTA.text + msg
            }
        }

        pane.add(srcPathBtn)
        pane.add(srcPathTv)
/*        pane.add(outPathBtn)
        pane.add(outPathTv)*/

        //pane.add(ignoreDexCb)
        pane.add(copyOriginalCb)
        pane.add(forceCoverCb)

        pane.add(submitBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "apk回编译"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}