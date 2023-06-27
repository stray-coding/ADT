package com.coding.compose.ui.dex

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coding.compose.base.Button
import com.coding.compose.base.FileChooser
import com.coding.compose.base.Toast
import com.coding.compose.mWindow
import com.coding.dec.DexTool
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@Composable
fun DexUI() {
    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row {
            Button("dex2smali") {
                FileChooser.newInstance(
                        mWindow,
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
                        mWindow,
                        JFileChooser.DIRECTORIES_ONLY,
                        "smali2dex",
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                DexTool.smali2dex(path)
                            }
                        })
            }
        }

        Row {
            Button("dex2jar") {
                FileChooser.newInstance(
                        mWindow,
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
                        mWindow,
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
        OutlinedTextField(
                value = oldDexPath.value,
                onValueChange = { oldDexPath.value = it },
                enabled = false,
                readOnly = true,
                modifier = Modifier.padding(10.dp).size(300.dp, 50.dp).clickable {
                    FileChooser.newInstance(mWindow,
                            JFileChooser.FILES_ONLY,
                            "old dex",
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
                singleLine = true
        )
        val newDexPath = remember { mutableStateOf("") }
        OutlinedTextField(
                value = newDexPath.value,
                onValueChange = { newDexPath.value = it },
                enabled = false,
                readOnly = true,
                modifier = Modifier.padding(10.dp).size(300.dp, 50.dp).clickable {
                    FileChooser.newInstance(mWindow,
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
                singleLine = true
        )

        Button("generate patch") {
            if (oldDexPath.value.isEmpty() || newDexPath.value.isEmpty()) {
                Toast.showMsg(mWindow, "please complete the dex file path information")
                return@Button
            }
            FileChooser.newInstance(
                    mWindow,
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