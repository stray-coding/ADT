package com.coding.compose.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coding.compose.base.*
import com.coding.compose.listener.OnSelectListener
import com.coding.compose.mWindow
import com.coding.dec.SignTool
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@Composable
fun SignUI() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val btnLabel = remember { mutableStateOf("choose sign") }
            val selectedName = remember { mutableStateOf("") }
            val showSignList = remember { mutableStateOf(false) }


            val list = remember { mutableStateListOf<String>() }
            list.clear()
            for (item in SignUtils.getSignList()) {
                list.add(item.name)
            }
            ListDialog("sign list",
                    btnLabel,
                    list,
                    showSignList,
                    object : OnSelectListener {
                        override fun onSelected(name: String) {
                            selectedName.value = name
                            btnLabel.value = name
                            showSignList.value = false
                            println("selected sign:$name")
                        }
                    }
            )

            ClickableText(text = AnnotatedString(btnLabel.value), style = TextStyle(
                    color = Color.Blue, fontSize = 16.sp
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
                    println("sign selectedName:" + selectedName.value)
                    val signBean = SignUtils.getSign(selectedName.value)
                    if (signBean == null) {
                        Toast.showMsg(mWindow, "Please select a signature first.")
                        return@Button
                    }
                    FileChooser.newInstance(
                            mWindow,
                            JFileChooser.FILES_ONLY,
                            "sign",
                            arrayOf(Suffix.APK, Suffix.AAB),
                            object : FileChooser.OnFileSelectListener {
                                override fun onSelected(path: String) {
                                    if (path.endsWith(Suffix.AAB)) {
                                        SignTool.signAAB(path, signBean)
                                        return
                                    }
                                    if (v1.value && !v2.value && !v3.value && !v4.value) {
                                        SignTool.signAndAlign(path, signBean)
                                    } else {
                                        SignTool.alignAndSign(
                                                path,
                                                signBean,
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
                    FileChooser.newInstance(mWindow,
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