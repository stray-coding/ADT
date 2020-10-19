package com.coding.tool.view.dialog

import com.coding.tool.util.CMD
import com.coding.tool.util.SignCfgUtil
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
    val signListJCb = JComboBox<String>()

    init {
        val pane = JPanel()

        val srcPathTv = JTextField(48)
        val srcPathBtn = JButton("apk path")
        srcPathBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "choose apk", Suffix.APK, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    srcPathTv.text = path
                }
            })
        }

        val nameList = DefaultComboBoxModel<String>()
        for (item in SignCfgUtil.getSignList()) {
            nameList.addElement(item.name)
        }

        signListJCb.model = nameList

        val deleteSignBtn = JButton("delete sign")
        deleteSignBtn.addActionListener {
            val selectSign = getCurrSelectSign()
            SignCfgUtil.deleteSign(selectSign)
            refreshSignList()
        }
        val addSignBtn = JButton("add sign")
        addSignBtn.addActionListener {
            SignCfgDialog.showDialog()
        }
        val submitBtn = JButton("sign")
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
                logTA.text = ""
                val selectSign = getCurrSelectSign()
                val finalApkName = srcPath.substring(0, srcPath.length - 4) + "_signed.apk"
                val cmd = "java -jar ${ToolUtil.getApkSigner()} sign " +
                        "--ks ${selectSign?.path} " +
                        "--ks-key-alias ${selectSign?.alias} " +
                        "--ks-pass pass:${selectSign?.pwd} " +
                        "--key-pass pass:${selectSign?.aliasPwd} " +
                        "-v --out $finalApkName $srcPath"
                logTA.text = logTA.text + cmd + "\n"
                CMD.CMD(cmd) { msg ->
                    logTA.text = logTA.text + msg
                }
            }
        }

        pane.add(srcPathBtn)
        pane.add(srcPathTv)
        pane.add(signListJCb)
        pane.add(submitBtn)
        pane.add(deleteSignBtn)
        pane.add(addSignBtn)
        pane.add(scrollPane)
        add(pane)
        setSize(640, 480)
        setLocationRelativeTo(null)
        title = "sign"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }

    fun refreshSignList() {
        val nameList = DefaultComboBoxModel<String>()
        for (item in SignCfgUtil.getSignList()) {
            nameList.addElement(item.name)
        }
        signListJCb.model = nameList
    }

    fun getCurrSelectSign(): SignCfgUtil.SignConfig? {
        var selectSign: SignCfgUtil.SignConfig? = null
        for (item in SignCfgUtil.getSignList()) {
            if (signListJCb.selectedItem == item.name) {
                selectSign = item
            }
        }
        return selectSign
    }
}