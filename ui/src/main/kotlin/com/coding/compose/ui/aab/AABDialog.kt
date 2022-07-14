package com.coding.compose.ui.aab

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import com.coding.compose.ui.sign.SignListDialog
import com.coding.dec.ADT
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser

@OptIn(ExperimentalUnitApi::class)
@Composable
fun AABDialog(show: MutableState<Boolean>) {
    Dialog(title = "aab", state = show) {
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
                    println("selectedName:$name")
                }
            })

            ClickableText(text = AnnotatedString(btnLabel.value),
                style = TextStyle(
                    color = Color.Blue,
                    fontSize = TextUnit(16.0f, TextUnitType.Sp)
                ), onClick = {
                    showSignList.value = true
                })

            val universal = remember { mutableStateOf(false) }
            CheckBox("universal", universal)
            Button("aab2apks") {
                FileChooser.newInstance(
                    window,
                    JFileChooser.FILES_ONLY,
                    "choose aab",
                    Suffix.AAB,
                    object : FileChooser.OnFileSelectListener {
                        override fun onSelected(path: String) {
                            ADT.aab2Apks(
                                path,
                                SignUtils.getSign(selectedName.value),
                                universal = universal.value
                            )
                        }
                    })
            }
        }
    }
}
