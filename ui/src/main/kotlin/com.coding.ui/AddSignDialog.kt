package com.coding.ui

import com.coding.dec.SignCfgUtil
import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Tools
import com.coding.tool.constants.Constants
import com.coding.utils.FileUtils
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

        val jksFileBtn = JButton("sign file path")
        jksFileBtn.addActionListener {
            if (jksPwdTv.text.isEmpty() || keyAliasTv.text.isEmpty() || keyPwdTv.text.isEmpty()) {
                Toast.showMsg(this, "please complete the signature configuration information")
                return@addActionListener
            }
            FileChooser.newInstance(
                this,
                JFileChooser.FILES_ONLY,
                "choose sign",
                arrayOf(Suffix.JKS, Suffix.KEY_STORE),
                object : FileChooser.OnSelectListener {
                    override fun onSelected(path: String) {
                        name = path.substring(path.lastIndexOf(File.separator) + 1, path.lastIndexOf('.'))
                        //保存签名文件的路径
                        val savePath =
                            Tools.getConfigDir() + File.separator + path.substring(path.lastIndexOf(File.separator) + 1)
                        FileUtils.copyFile(path, savePath)
                        val sign = SignCfgUtil.SignConfig(name, savePath, jksPwdTv.text, keyAliasTv.text, keyPwdTv.text)
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
        setSize(Constants.Windows_Width, Constants.Window_Height)

        setLocationRelativeTo(null)
        title = "add sign"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}