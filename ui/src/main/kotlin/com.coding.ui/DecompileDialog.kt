package com.coding.ui

import com.coding.dec.ADT
import com.coding.dec.utils.Suffix
import com.coding.tool.constants.Constants
import java.awt.Checkbox
import javax.swing.JButton
import javax.swing.JDialog
import javax.swing.JFileChooser
import javax.swing.JPanel


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:ui of decompile
 */
object DecompileDialog : JDialog() {
    init {
        val pane = JPanel()

        val ignoreDexCb = Checkbox("ignore dex")
        val ignoreSrcCb = Checkbox("ignore resource")
        val submitBtn = JButton("decompile")
        submitBtn.addActionListener {
            FileChooser.newInstance(
                this,
                JFileChooser.FILES_ONLY,
                "choose apk",
                Suffix.APK,
                object : FileChooser.OnSelectListener {
                    override fun onSelected(path: String) {
                        ADT.decompile(path, ignoreDexCb.state, ignoreSrcCb.state)
                    }
                })
        }
        pane.add(ignoreDexCb)
        pane.add(ignoreSrcCb)

        pane.add(submitBtn)
        add(pane)
        setSize(Constants.Windows_Width, Constants.Window_Height)
        setLocationRelativeTo(null)
        title = "decompile"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}