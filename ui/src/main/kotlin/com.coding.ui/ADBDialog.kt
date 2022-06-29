package com.coding.ui

import com.coding.dec.ADT
import com.coding.dec.utils.Suffix
import com.coding.tool.constants.Constants
import java.awt.Checkbox
import javax.swing.*


/**
 *  Date 2020/7/13 19:52
 *  author hdl
 *  Description:签名界面
 */
object ADBDialog : JDialog() {
    private val apkJCb = JComboBox<String>()
    private val apkList = DefaultComboBoxModel<String>()

    private val devicesJCb = JComboBox<String>()
    private val devicesList = DefaultComboBoxModel<String>()

    init {
        val extraPane = JPanel()
        val debugCb = Checkbox("debug", false)
        val installApk = JButton("install apk")
        installApk.addActionListener {
            FileChooser.newInstance(JFileChooser.FILES_ONLY,
                "choose apk",
                Suffix.APK,
                object : FileChooser.OnSelectListener {
                    override fun onSelected(path: String) {
                        println("install apk:" + ADT.installApk(getCurDevice(), debugCb.state, path))
                    }
                })
        }


        val extractApk = JButton("extract apk")
        extractApk.addActionListener {
            FileChooser.newInstance(JFileChooser.DIRECTORIES_ONLY, "choose dir", object : FileChooser.OnSelectListener {
                override fun onSelected(path: String) {
                    println("extract apk:" + ADT.extractApk(devicesJCb.selectedItem as String, getChooseApk(), path))
                }
            })
        }

        val refreshBtn = JButton("refresh")
        refreshBtn.addActionListener {
            refresh()
        }

        devicesJCb.addActionListener {
            devicesJCb.selectedItem?.let {
                val apks = ADT.getAllApkPackageNames(devicesJCb.selectedItem as String)
                for (pkg in apks) {
                    apkList.addElement(pkg)
                }
                apkJCb.model = apkList
            }
        }


        extraPane.add(debugCb)
        extraPane.add(devicesJCb)
        extraPane.add(installApk)

        extraPane.add(apkJCb)
        extraPane.add(extractApk)
        extraPane.add(refreshBtn)

        add(extraPane)

        setSize(Constants.Windows_Width, Constants.Window_Height)
        setLocationRelativeTo(null)
        title = "adb"
        isVisible = true
        isResizable = false

        refresh()
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

    private fun refresh() {
        apkList.removeAllElements()
        devicesList.removeAllElements()
        val devices = ADT.getAllDevices()
        for (device in devices) {
            devicesList.addElement(device)
        }
        devicesJCb.model = devicesList

        if (devices.isNotEmpty()) {
            devicesJCb.selectedItem = 0
            val apks = ADT.getAllApkPackageNames(devicesJCb.selectedItem as String)
            for (pkg in apks) {
                apkList.addElement(pkg)
            }
            apkJCb.model = apkList
        }
    }
}