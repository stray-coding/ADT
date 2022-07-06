package com.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.dec.ADT
import com.coding.dec.utils.Suffix
import com.coding.ui.FileChooser
import com.coding.ui.Toast
import com.compose.base.Button
import com.compose.base.Dialog
import com.compose.base.EditText
import javax.swing.JFileChooser

@Composable
fun DexDialog(show: MutableState<Boolean>) {
    Dialog(title = "dex", state = show) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button("dex2jar") {
                    FileChooser.newInstance(
                        window,
                        JFileChooser.FILES_ONLY,
                        "dex2jar",
                        Suffix.DEX,
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                ADT.dex2jar(path)
                            }
                        })
                }
                Button("jar2dex") {
                    FileChooser.newInstance(
                        window,
                        JFileChooser.FILES_ONLY,
                        "jar2dex",
                        Suffix.JAR,
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                ADT.jar2dex(path)
                            }
                        })
                }
            }


            val oldDexPath = remember { mutableStateOf("") }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Button("old dex") {
                    FileChooser.newInstance(window,
                        JFileChooser.FILES_ONLY,
                        "old dex",
                        Suffix.DEX,
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                oldDexPath.value = path
                            }
                        })
                }
                EditText(oldDexPath)
            }
            val newDexPath = remember { mutableStateOf("") }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically
            ) {
                Button("new dex") {
                    FileChooser.newInstance(window,
                        JFileChooser.FILES_ONLY,
                        "new dex",
                        Suffix.DEX,
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                newDexPath.value = path
                            }
                        })
                }
                EditText(newDexPath)
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
                    object : FileChooser.OnSelectListener {
                        override fun onSelected(path: String) {
                            ADT.generatePatch(
                                oldDexPath.value, newDexPath.value, path
                            )
                        }
                    })
            }
        }
    }
}