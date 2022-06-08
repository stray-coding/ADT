package com.coding.ui

import com.coding.dec.ADT
import com.coding.tool.constants.Constants
import com.coding.utils.Terminal
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object ExtractApkDialog : JDialog() {
    private val apkJCb = JComboBox<String>()
    val apkList = DefaultComboBoxModel<String>()

    init {
        val pane = JPanel()
        val extractApk = JButton("extract apk")
        extractApk.addActionListener {
            FileChooser.newInstance(
                JFileChooser.DIRECTORIES_ONLY,
                "choose dir",
                object : FileChooser.OnSelectListener {
                    override fun onSelected(path: String) {
                        println("extract apk:" + ADT.extractApk(getChooseApk(), path))
                    }
                })
        }

        pane.add(apkJCb)
        pane.add(extractApk)
        add(pane)

        setSize(Constants.Windows_Width, Constants.Window_Height)
        setLocationRelativeTo(null)
        title = "extract apk"
        isVisible = true
        isResizable = false
        Terminal.run("adb shell pm list package", object : Terminal.OnResultListener {
            override fun onStdout(msg: String) {
                if (msg.isNotEmpty()) {
                    apkList.addElement(msg.replace("package:", ""))
                }
            }

            override fun onStdErr(err: String) {
            }

        })
        apkJCb.model = apkList
    }

    fun showDialog() {
        isVisible = true
    }

    fun getChooseApk(): String {
        return apkList.selectedItem as String
    }
}