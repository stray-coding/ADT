package com.coding.tool.view

import java.awt.Component
import javax.swing.JOptionPane

/**
 * @author: Coding.He
 * @date: 2020/10/19
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object Toast : JOptionPane() {
    fun showMsg(msg: String) {
        showMessageDialog(MainWindow.getInstance(), msg)
    }

    fun showMsg(parent: Component, msg: String) {
        showMessageDialog(parent, msg)
    }
}