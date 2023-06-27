package com.coding.compose.ui.adb

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.compose.base.Button
import com.coding.compose.base.CheckBox
import com.coding.compose.base.FileChooser
import com.coding.compose.base.Toast
import com.coding.compose.listener.OnCheckListener
import com.coding.compose.listener.OnSelectListener
import com.coding.compose.mWindow
import com.coding.dec.AdbTool
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@Composable
fun ADBUI() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Row {
            Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val btnLabel = remember { mutableStateOf("choose device") }
                val selectedDevice = remember { mutableStateOf("") }
                val showDevicesList = remember { mutableStateOf(false) }
                DevicesListDialog(showDevicesList, object : OnSelectListener {
                    override fun onSelected(name: String) {
                        selectedDevice.value = name
                        btnLabel.value = name
                        showDevicesList.value = false
                    }
                })
                ClickableText(text = AnnotatedString(btnLabel.value), style = TextStyle(
                        color = Color.Blue, fontSize = 16.sp
                ), onClick = {
                    showDevicesList.value = true
                })
                val debug = remember { mutableStateOf(false) }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CheckBox("debug", debug)
                    Button("install apk") {
                        if (selectedDevice.value.isEmpty()) {
                            Toast.showMsg(mWindow, "Please select the device first.")
                            return@Button
                        }
                        FileChooser.newInstance(mWindow,
                                JFileChooser.FILES_ONLY,
                                "install apk",
                                Suffix.APK,
                                object : FileChooser.OnFileSelectListener {
                                    override fun onSelected(path: String) {
                                        println("install apk:" + AdbTool.installApk(selectedDevice.value, debug.value, path))
                                    }
                                })
                    }
                }
                val pkgNameLabel = remember { mutableStateOf("choose package name") }
                val selectedPkg = remember { mutableStateOf("") }
                val showPkgNamesList = remember { mutableStateOf(false) }
                val mode = remember { mutableStateOf(AdbTool.ONLY_THIRD_APP) }
                AppPkgListDialog(selectedDevice, mode, showPkgNamesList, object : OnSelectListener {
                    override fun onSelected(name: String) {
                        selectedPkg.value = name
                        pkgNameLabel.value = name
                        showPkgNamesList.value = false
                    }
                })

                val only_system = remember { mutableStateOf(false) }
                val only_third = remember { mutableStateOf(true) }
                Row {
                    CheckBox("only system app", only_system, listener = object : OnCheckListener {
                        override fun onCheckedChange(checked: Boolean) {
                            if (checked) {
                                only_third.value = false
                                mode.value = AdbTool.ONLY_SYSTEM_APP
                                return
                            }
                            mode.value = AdbTool.ALL_APP
                        }
                    })

                    CheckBox("only third app", only_third, listener = object : OnCheckListener {
                        override fun onCheckedChange(checked: Boolean) {
                            if (checked) {
                                only_system.value = false
                                mode.value = AdbTool.ONLY_THIRD_APP
                                return
                            }
                            mode.value = AdbTool.ALL_APP
                        }
                    })
                }

                ClickableText(text = AnnotatedString(pkgNameLabel.value), style = TextStyle(
                        color = Color.Blue, fontSize = 16.sp
                ), onClick = {
                    showPkgNamesList.value = true
                })

                Button("extract apk") {
                    if (selectedDevice.value.isEmpty() || selectedPkg.value.isEmpty()) {
                        Toast.showMsg(mWindow, "Please select a device and package name.")
                        return@Button
                    }
                    FileChooser.newInstance(mWindow,
                            JFileChooser.DIRECTORIES_ONLY,
                            "extract apk",
                            object : FileChooser.OnFileSelectListener {
                                override fun onSelected(path: String) {
                                    AdbTool.extractApk(selectedDevice.value, selectedPkg.value, path)
                                }
                            })
                }
            }
        }
    }
}