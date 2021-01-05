package com.coding.tool.view

import com.coding.tool.Proxy
import com.coding.tool.util.Suffix
import javax.swing.*

/**
 * @author: Coding.He
 * @date: 2020/7/14
 * @emil: stray-coding@foxmail.com
 * @des: ui of generate patch
 */
object PatchDialog : JDialog() {
    init {
        val pane = JPanel()

        val oldDexPathTv = JTextField(10)
        val oldDexPathBtn = JButton("old dex")
        oldDexPathBtn.addActionListener {
            FileChooser.newInstance(this, JFileChooser.FILES_ONLY, "old dex", Suffix.DEX, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    oldDexPathTv.text = path
                }
            })
        }

        val newDexPathTv = JTextField(10)
        val newDexPathBtn = JButton("new dex")
        newDexPathBtn.addActionListener {
            FileChooser.newInstance(this, JFileChooser.FILES_ONLY, "new dex", Suffix.DEX, object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    newDexPathTv.text = path
                }
            })
        }

        val generateBtn = JButton("generate patch")
        generateBtn.addActionListener {
            if(oldDexPathTv.text.isEmpty() || newDexPathTv.text.isEmpty()){
                Toast.showMsg(this, "please complete the dex file path information")
                return@addActionListener
            }
            FileChooser.newInstance(this, JFileChooser.DIRECTORIES_ONLY, "generate patch", object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    Proxy.generatePatch(
                        oldDexPathTv.text,
                        newDexPathTv.text,
                        path
                    )
                }
            })
        }

        pane.add(oldDexPathBtn)
        pane.add(oldDexPathTv)

        pane.add(newDexPathBtn)
        pane.add(newDexPathTv)

        pane.add(generateBtn)
        add(pane)
        setSize(250, 250)
        setLocationRelativeTo(null)
        title = "generate patch"
        isVisible = true
        isResizable = false
    }

    fun showDialog() {
        isVisible = true
    }

}