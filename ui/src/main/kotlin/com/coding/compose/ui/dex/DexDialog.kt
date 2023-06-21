package com.coding.compose.ui.dex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coding.compose.base.Button
import com.coding.compose.base.Dialog
import com.coding.compose.base.FileChooser
import com.coding.compose.base.Toast
import com.coding.dec.DexTool
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@Composable
fun DexDialog(show: MutableState<Boolean>) {
    Dialog(title = "dex", state = show) {
        Column(
                modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Button("dex2smali") {
                    FileChooser.newInstance(
                            window,
                            JFileChooser.FILES_ONLY,
                            "dex2smali",
                            Suffix.DEX,
                            object : FileChooser.OnFileSelectListener {
                                override fun onSelected(path: String) {
                                    DexTool.dex2smali(path)
                                }
                            })
                }

                Button("smali2dex") {
                    FileChooser.newInstance(
                            window,
                            JFileChooser.DIRECTORIES_ONLY,
                            "smali2dex",
                            object : FileChooser.OnFileSelectListener {
                                override fun onSelected(path: String) {
                                    DexTool.smali2dex(path)
                                }
                            })
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {
                Button("dex2jar") {
                    FileChooser.newInstance(
                            window,
                            JFileChooser.FILES_ONLY,
                            "dex2jar",
                            Suffix.DEX,
                            object : FileChooser.OnFileSelectListener {
                                override fun onSelected(path: String) {
                                    DexTool.dex2jar(path)
                                }
                            })
                }
                Button("jar2dex") {
                    FileChooser.newInstance(
                            window,
                            JFileChooser.FILES_ONLY,
                            "jar2dex",
                            Suffix.JAR,
                            object : FileChooser.OnFileSelectListener {
                                override fun onSelected(path: String) {
                                    DexTool.jar2dex(path)
                                }
                            })
                }
            }

            val oldDexPath = remember { mutableStateOf("") }
            Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                        value = oldDexPath.value,
                        onValueChange = { oldDexPath.value = it },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier.width(300.dp).height(50.dp).clickable {
                            FileChooser.newInstance(window,
                                    JFileChooser.FILES_ONLY,
                                    "new dex",
                                    Suffix.DEX,
                                    object : FileChooser.OnFileSelectListener {
                                        override fun onSelected(path: String) {
                                            oldDexPath.value = path
                                        }
                                    })
                        },
                        placeholder = { Text("choose old dex") },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        maxLines = 1,
                )
            }
            val newDexPath = remember { mutableStateOf("") }
            Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                        value = newDexPath.value,
                        onValueChange = { newDexPath.value = it },
                        enabled = false,
                        readOnly = true,
                        modifier = Modifier.width(300.dp).height(50.dp).clickable {
                            FileChooser.newInstance(window,
                                    JFileChooser.FILES_ONLY,
                                    "new dex",
                                    Suffix.DEX,
                                    object : FileChooser.OnFileSelectListener {
                                        override fun onSelected(path: String) {
                                            newDexPath.value = path
                                        }
                                    })
                        },
                        placeholder = { Text("choose new dex") },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        maxLines = 1,
                )
            }

            Button("generate patch") {
                if (oldDexPath.value.isEmpty() || newDexPath.value.isEmpty()) {
                    Toast.showMsg(window, "please complete the dex file path information")
                    return@Button
                }
                FileChooser.newInstance(
                        window,
                        JFileChooser.DIRECTORIES_ONLY,
                        "generate patch",
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                DexTool.generatePatch(
                                        oldDexPath.value, newDexPath.value, path
                                )
                            }
                        })
            }
        }
    }
}