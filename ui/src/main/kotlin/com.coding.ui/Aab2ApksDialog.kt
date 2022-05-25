package com.coding.ui

import com.coding.dec.ADT
import com.coding.dec.SignCfgUtil
import com.coding.dec.utils.Suffix
import com.coding.tool.constants.Constants
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object Aab2ApksDialog : JDialog() {
    private val signListJCb = JComboBox<String>()

    init {
        val pane = JPanel()

        val nameList = DefaultComboBoxModel<String>()
        for (item in SignCfgUtil.getSignList()) {
            nameList.addElement(item.name)
        }

        signListJCb.model = nameList

        val aab2apks = JButton("aab2apks")
        aab2apks.addActionListener {
            FileChooser.newInstance(
                JFileChooser.FILES_ONLY,
                "align aab",
                Suffix.AAB,
                object : FileChooser.OnSelectListener {
                    override fun onSelected(path: String) {
                        ADT.aab2Apks(path, getCurrSelectSign())
                    }
                })
        }

        pane.add(signListJCb)
        pane.add(aab2apks)
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

    fun getCurrSelectSign(): SignCfgUtil.SignConfig {
        for (item in SignCfgUtil.getSignList()) {
            if (signListJCb.selectedItem == item.name) {
                return item
            }
        }
        return SignCfgUtil.SignConfig()
    }
}