package com.coding.tool.view

import com.coding.tool.Proxy
import com.coding.tool.constants.Constants
import com.coding.tool.util.SignCfgUtil
import com.coding.tool.util.Suffix
import java.awt.Checkbox
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object SignDialog : JDialog() {
    private val signListJCb = JComboBox<String>()

    init {
        val pane = JPanel()

        val v1Cb = Checkbox("v1", true)
        val v2Cb = Checkbox("v2", true)

        val nameList = DefaultComboBoxModel<String>()
        for (item in SignCfgUtil.getSignList()) {
            nameList.addElement(item.name)
        }

        signListJCb.model = nameList


        val deleteSignBtn = JButton("delete sign")
        deleteSignBtn.addActionListener {
            val selectSign = getCurrSelectSign()
            if (selectSign.name == "defaultSign") {
                Toast.showMsg(this, "the default signature can't be deleted！")
                return@addActionListener
            }
            SignCfgUtil.deleteSign(selectSign)
            refreshSignList()
        }
        val addSignBtn = JButton("add sign")
        addSignBtn.addActionListener {
            AddSignDialog.showDialog()
        }
        val signBtn = JButton("sign")
        signBtn.addActionListener {
            FileChooser.newInstance(this, JFileChooser.FILES_ONLY, "apk sign", Suffix.APK, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.apkSign(path, getCurrSelectSign(), v1Enable = v1Cb.state, v2Enable = v2Cb.state)
                }
            })
        }

        val signCheckBtn = JButton("verify sign")
        signCheckBtn.addActionListener {
            FileChooser.newInstance(this, JFileChooser.FILES_ONLY, "verify sign", Suffix.APK, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.verifyApkSign(path)
                }
            })
        }

        pane.add(signListJCb)

        pane.add(deleteSignBtn)
        pane.add(addSignBtn)

        pane.add(v1Cb)
        pane.add(v2Cb)
        pane.add(signBtn)
        pane.add(signCheckBtn)
        add(pane)

        setSize(Constants.Windows_Width, Constants.Window_Height)
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

    fun getCurrSelectSign(): SignCfgUtil.SignConfig {
        for (item in SignCfgUtil.getSignList()) {
            if (signListJCb.selectedItem == item.name) {
                return item
            }
        }
        return SignCfgUtil.SignConfig()
    }
}