package com.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.coding.dec.ADT
import com.coding.ui.FileChooser
import com.compose.base.Button
import com.compose.ui.*
import javax.swing.JFileChooser


fun main() = application {
    val dec_show = remember { mutableStateOf(false) }
    val dex_show = remember { mutableStateOf(false) }
    val sign_show = remember { mutableStateOf(false) }
    val aab_show = remember { mutableStateOf(false) }
    val adb_show = remember { mutableStateOf(false) }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ADT",
        state = rememberWindowState(width = 300.dp, height = 450.dp),
        resizable = false
    ) {
        MaterialTheme {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button("jadx") {
                    ADT.openJadx()
                }
                Button("decompile") {
                    //showDialog(DialogType.DEC)
                    dec_show.value = true
                }
                Button("dex") {
                    //showDialog(DialogType.DEX)
                    dex_show.value = true
                }
                Button("backToApk") {
                    FileChooser.newInstance(
                        window,
                        JFileChooser.DIRECTORIES_ONLY,
                        "backToApk",
                        object : FileChooser.OnSelectListener {
                            override fun onSelected(path: String) {
                                ADT.backToApk(path)
                            }
                        })
                }
                Button("sign") {
                    //showDialog(DialogType.SIGN)
                    sign_show.value = true
                }
                Button("aab") {
                    //showDialog(DialogType.AAB)
                    aab_show.value = true
                }

//                Button("adb") {
//                    //showDialog(DialogType.ADB)
//                    adb_show.value = true
//                }
                DecDialog(dec_show)
                DexDialog(dex_show)
                SignDialog(sign_show)
                AABDialog(aab_show)
                ADBDialog(adb_show)
            }
        }
    }
}
