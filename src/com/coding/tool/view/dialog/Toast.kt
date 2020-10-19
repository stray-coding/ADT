package com.coding.tool.view.dialog

import java.awt.Component
import javax.swing.JOptionPane

/**
 * @author: Coding.He
 * @date: 2020/10/19
 * @emil: 229101253@qq.com
 * @des:
 */
object Toast : JOptionPane() {
    fun showMsg(parent: Component, msg: String) {
        showMessageDialog(parent, msg)
    }
}