package com.coding.ui

import java.awt.Component
import javax.swing.JOptionPane

/**
 * @author: Coding.He
 * @date: 2020/10/19
 * @emil: stray-coding@foxmail.com
 * @des:
 */
object Toast : JOptionPane() {
    fun showMsg(parent: Component, msg: String) {
        showMessageDialog(parent, msg)
    }
}