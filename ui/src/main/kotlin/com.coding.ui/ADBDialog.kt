package com.coding.ui

import com.coding.dec.ADT
import com.coding.dec.utils.Suffix
import com.coding.tool.constants.Constants
import com.coding.utils.Terminal
import java.awt.Checkbox
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object ADBDialog : JDialog() {
    private val apkJCb = JComboBox<String>()
    val apkList = DefaultComboBoxModel<String>()

    private val devicesJCb = JComboBox<String>()
    val devicesList = DefaultComboBoxModel<String>()

    init {
        val extraPane = JPanel()
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

        extraPane.add(apkJCb)
        extraPane.add(extractApk)
        add(extraPane)

        val debugCb = Checkbox("debug", false)
        val installApk = JButton("install apk")
        installApk.addActionListener {
            FileChooser.newInstance(
                JFileChooser.FILES_ONLY,
                "choose apk",
                Suffix.APK,
                object : FileChooser.OnSelectListener {
                    override fun onSelected(path: String) {
                        println("install apk:" + ADT.installApk(getCurDevice(), debugCb.state, path))
                    }
                })
        }


        extraPane.add(debugCb)
        extraPane.add(devicesJCb)
        extraPane.add(installApk)

        setSize(Constants.Windows_Width, Constants.Window_Height)
        setLocationRelativeTo(null)
        title = "adb"
        isVisible = true
        isResizable = false
        Terminal.run("adb shell pm list package", listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                if (line.isNotEmpty()) {
                    val pkg = line.replace("package:", "")
                    apkList.addElement(pkg)
                }
            }

        })
        apkJCb.model = apkList

        Terminal.run("adb devices", listener = object : Terminal.OnStdoutListener {
            override fun callback(line: String) {
                if (line.endsWith("device")) {
                    devicesList.addElement(line.removeSuffix("device").trim())
                }
            }

        })
        devicesJCb.model = devicesList
    }

    fun showDialog() {
        isVisible = true
    }

    fun getChooseApk(): String {
        return apkList.selectedItem as String
    }

    fun getCurDevice(): String {
        return devicesList.selectedItem as String
    }
}