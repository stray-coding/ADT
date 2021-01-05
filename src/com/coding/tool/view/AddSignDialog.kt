package com.coding.tool.view

import com.coding.tool.util.SignCfgUtil
import com.coding.tool.util.Suffix
import java.io.File
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:ui of sign
 */
object AddSignDialog : JDialog() {
    private const val columns = 15

    init {
        var name: String
        val pane = JPanel()

        val jksPwdLabel = JLabel("jks password: ", SwingConstants.LEFT)
        val jksPwdTv = JTextField(columns)

        val keyAliasLabel = JLabel("key alias: ")
        val keyAliasTv = JTextField(columns)

        val keyPwdLabel = JLabel("key password: ", SwingConstants.LEFT)
        val keyPwdTv = JTextField(columns)

        val jksFileBtn = JButton("jks file path")
        jksFileBtn.addActionListener {
            if (jksPwdTv.text.isEmpty() || keyAliasTv.text.isEmpty() || keyPwdTv.text.isEmpty()) {
                Toast.showMsg(this, "please complete the signature configuration information")
                return@addActionListener
            }
            FileChooser.newInstance(this, JFileChooser.FILES_ONLY, "choose sign", Suffix.SIGN, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    name = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf('.'))
                    val sign = SignCfgUtil.SignConfig(name, path, jksPwdTv.text, keyAliasTv.text, keyPwdTv.text)
                    SignCfgUtil.addSign(sign)
                    SignDialog.refreshSignList()
                    isVisible = false
                }
            })
        }

        pane.add(jksPwdLabel)
        pane.add(jksPwdTv)

        pane.add(keyAliasLabel)
        pane.add(keyAliasTv)

        pane.add(keyPwdLabel)
        pane.add(keyPwdTv)

        pane.add(jksFileBtn)

        add(pane)
        setSize(250, 250)

        setLocationRelativeTo(null)
        title = "add sign"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}