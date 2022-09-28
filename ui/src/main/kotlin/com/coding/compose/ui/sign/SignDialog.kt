package com.coding.compose.ui.sign

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.coding.compose.base.Button
import com.coding.compose.base.CheckBox
import com.coding.compose.base.Dialog
import com.coding.compose.base.FileChooser
import com.coding.compose.listener.OnSelectListener
import com.coding.dec.SignTool
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@ExperimentalUnitApi
@Composable
fun SignDialog(show: MutableState<Boolean>) {
    Dialog(title = "sign apk", state = show) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val btnLabel = remember { mutableStateOf("choose sign") }
            val selectedName = remember { mutableStateOf("") }
            val showSignList = remember { mutableStateOf(false) }
            SignListDialog(showSignList, object : OnSelectListener {
                override fun onSelected(name: String) {
                    selectedName.value = name
                    btnLabel.value = name
                    showSignList.value = false
                    println("selected sign:$name")
                }
            })

            ClickableText(text = AnnotatedString(btnLabel.value), style = TextStyle(
                color = Color.Blue, fontSize = TextUnit(16.0f, TextUnitType.Sp)
            ), onClick = {
                showSignList.value = true
            })
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
            Row(horizontalArrangement = Arrangement.spacedBy(20.dp)) {

                Button("sign") {
                    FileChooser.newInstance(
                        window,
                        JFileChooser.FILES_ONLY,
                        "apk sign",
                        Suffix.APK,
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                println("sign selectedName:" + selectedName.value)
                                if (v1.value && !v2.value && !v3.value && !v4.value) {
                                    SignTool.signAndAlign(path, SignUtils.getSign(selectedName.value))
                                } else {
                                    SignTool.alignAndSign(
                                        path,
                                        SignUtils.getSign(selectedName.value),
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
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                SignTool.verifyApkSign(path)
                            }
                        })
                }
            }

        }
    }
}