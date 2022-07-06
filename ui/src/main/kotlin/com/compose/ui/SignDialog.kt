package com.compose.ui

import androidx.compose.foundation.layout.*
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
import com.compose.base.Button
import com.compose.base.CheckBox
import com.compose.base.Dialog
import javax.swing.JFileChooser

@Composable
fun SignDialog(show: MutableState<Boolean>) {
    Dialog(title = "aab", state = show) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            val signDialog = remember { mutableStateOf(false) }
            SignManagerDialog(signDialog)
            Button("sign manager") {
                signDialog.value = true
            }
            val v1 = remember { mutableStateOf(true) }
            val v2 = remember { mutableStateOf(true) }
            val v3 = remember { mutableStateOf(false) }
            val v4 = remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                CheckBox("v1", v1, Modifier.width(20.dp))
                CheckBox("v2", v2, Modifier.width(20.dp))
                CheckBox("v3", v3, Modifier.width(20.dp))
                CheckBox("v4", v4, Modifier.width(20.dp))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button("sign") {
                    FileChooser.newInstance(window,
                        JFileChooser.FILES_ONLY,
                        "apk sign",
                        Suffix.APK,
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                if (v1.value && !v2.value && !v3.value && !v4.value) {
                                    ADT.signAndAlign(path, getCurSign())
                                } else {
                                    ADT.alignAndSign(
                                        path,
                                        getCurSign(),
                                        v1Enable = v1.value,
                                        v2Enable = v2.value,
                                        v3Enable = v3.value,
                                        v4Enable = v4.value
                                    )
                                }
                            }
                        })
                }
                Button("verify sign") {
                    FileChooser.newInstance(window,
                        JFileChooser.FILES_ONLY,
                        "verify sign",
                        Suffix.APK,
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                ADT.verifyApkSign(path)
                            }
                        })
                }
            }

        }
    }
}