package com.coding.tool.view

import com.coding.tool.Proxy
import com.coding.tool.util.Suffix
import java.awt.Checkbox
import javax.swing.*


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
            FileChooser.newInstance(this, JFileChooser.FILES_ONLY, "choose apk", Suffix.APK, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.decompile(path, ignoreDexCb.state, ignoreSrcCb.state)
                }
            })
        }
        pane.add(ignoreDexCb)
        pane.add(ignoreSrcCb)

        pane.add(submitBtn)
        add(pane)
        setSize(250, 250)
        setLocationRelativeTo(null)
        title = "decompile"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }
}