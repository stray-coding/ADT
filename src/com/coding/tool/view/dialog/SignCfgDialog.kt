package com.coding.tool.view.dialog

import com.coding.tool.util.SignCfgUtil
import com.coding.tool.util.Suffix
import com.coding.tool.view.FileJfc
import java.io.File
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object SignCfgDialog : JDialog() {
    private const val columns = 15

    init {

        val pane = JPanel()

        val nameLabel = JLabel("jks name: ")
        val nameTv = JTextField(columns)

        val jksFileTv = JTextField(columns)
        val jksFileBtn = JButton("jks file path")
        jksFileBtn.addActionListener {
            FileJfc.newInstance(JFileChooser.FILES_ONLY, "choose sign", Suffix.SIGN, object : FileJfc.OnSelectListener {
                override fun onSelected(path: String) {
                    jksFileTv.text = path
                    if (nameTv.text.isEmpty()) {
                        nameTv.text = jksFileTv.text.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf('.'))
                    }
                }
            })
        }

        val jksPwdLabel = JLabel("jks password: ", SwingConstants.LEFT)
        val jksPwdTv = JTextField(columns)

        val keyAliasLabel = JLabel("key alias: ")
        val keyAliasTv = JTextField(columns)

        val keyPwdLabel = JLabel("key password: ", SwingConstants.LEFT)
        val keyPwdTv = JTextField(columns)

        val saveBtn = JButton("save")
        saveBtn.addActionListener {
            if (nameTv.text.isEmpty() || jksFileTv.text.isEmpty() || jksPwdTv.text.isEmpty()
                    || keyAliasTv.text.isEmpty() || keyPwdTv.text.isEmpty()) {
                Toast.showMsg(this@SignCfgDialog, "请先完善签名相关配置信息")
                return@addActionListener
            }
            val sign = SignCfgUtil.SignConfig(nameTv.text, jksFileTv.text, jksPwdTv.text, keyAliasTv.text, keyPwdTv.text)
            SignCfgUtil.addSign(sign)
            SignDialog.refreshSignList()
            Toast.showMsg(this@SignCfgDialog, "签名添加成功")
            isVisible = false
        }

        pane.add(nameLabel)
        pane.add(nameTv)

        pane.add(jksFileBtn)
        pane.add(jksFileTv)

        pane.add(jksPwdLabel)
        pane.add(jksPwdTv)

        pane.add(keyAliasLabel)
        pane.add(keyAliasTv)

        pane.add(keyPwdLabel)
        pane.add(keyPwdTv)

        pane.add(saveBtn)
        add(pane)
        setSize(200, 310)
        setLocationRelativeTo(null)
        title = "add sign"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}