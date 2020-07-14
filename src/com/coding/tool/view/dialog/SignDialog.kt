package com.coding.tool.view.dialog

import com.coding.tool.util.CMD
import com.coding.tool.util.Suffix
import com.coding.tool.util.ToolUtil
import com.coding.tool.view.FileJfc
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object SignDialog : JDialog() {
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
            FileJfc.newInstance(JFileChooser.DIRECTORIES_ONLY, "签名后的保存目录", object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    outPathTv.text = path
                }
            })
        }

        val storeFileTv = JTextField(45)
        val storeFileBtn = JButton("store file")
        storeFileBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "选择签名文件", Suffix.SIGN, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    storeFileTv.text = path
                }
            })
        }

        val storePasswordLabel = JLabel("storePassword: ", SwingConstants.LEFT)
        val storePasswordTv = JTextField(45)

        val keyAliasLabel = JLabel("keyAlias: ")
        val keyAliasTv = JTextField(45)

        val keyPasswordLabel = JLabel("keyPassword: ", SwingConstants.LEFT)
        val keyPasswordTv = JTextField(45)

        val submitBtn = JButton("签名")
        val logTA = JTextArea("日志")
        logTA.rows = 14
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
                logTA.text = ""
                val finalApkName = srcPath.substring(0, srcPath.length - 4) + "_sign.apk"
                val cmd = "java -jar ${ToolUtil.getApkSigner()} sign " +
                        "--ks ${storeFileTv.text} " +
                        "--ks-key-alias ${keyAliasTv.text} " +
                        "--ks-pass pass:${storePasswordTv.text} " +
                        "--key-pass pass:${keyPasswordTv.text} " +
                        "-v --out $finalApkName $srcPath"
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

        pane.add(storeFileBtn)
        pane.add(storeFileTv)

        pane.add(storePasswordLabel)
        pane.add(storePasswordTv)

        pane.add(keyAliasLabel)
        pane.add(keyAliasTv)

        pane.add(keyPasswordLabel)
        pane.add(keyPasswordTv)

        pane.add(submitBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "apk签名"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}