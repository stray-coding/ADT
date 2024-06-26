package com.coding.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.coding.dec.BundleTool
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@Composable
fun AABUI() {
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
                    color = Color.Blue, fontSize = 16.0.sp
            ), onClick = {
                showSignList.value = true
            })

            val universal = remember { mutableStateOf(false) }
            CheckBox("universal", universal)
            Button("aab2apks") {
                val signBean = SignUtils.getSign(selectedName.value)
                if (signBean == null) {
                    Toast.showMsg(mWindow, "Please select a signature first.")
                    return@Button
                }
                FileChooser.newInstance(mWindow,
                        JFileChooser.FILES_ONLY,
                        "aab2apks",
                        Suffix.AAB,
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                BundleTool.aab2Apks(
                                        path,
                                        signBean,
                                        universal = universal.value
                                )
                            }
                        })
            }

            Button("apk2aab") {
                val signBean = SignUtils.getSign(selectedName.value)
                if (signBean == null) {
                    Toast.showMsg(mWindow, "Please select a signature first.")
                    return@Button
                }
                FileChooser.newInstance(mWindow,
                        JFileChooser.FILES_ONLY,
                        "apk2aab",
                        Suffix.APK,
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                BundleTool.apk2AAB(path, signBean)
                            }
                        })
            }
        }
    }
}
