package com.coding.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.coding.compose.base.Button
import com.coding.compose.base.FileChooser
import com.coding.compose.ui.aab.AABDialog
import com.coding.compose.ui.adb.ADBDialog
import com.coding.compose.ui.dec.DecDialog
import com.coding.compose.ui.dex.DexDialog
import com.coding.compose.ui.sign.SignDialog
import com.coding.compose.ui.signmanager.SignManagerDialog
import com.coding.dec.ADT
import javax.swing.JFileChooser

@OptIn(ExperimentalUnitApi::class)
fun main() = application {
    val dec_show = remember { mutableStateOf(false) }
    val dex_show = remember { mutableStateOf(false) }
    val sign_manager_show = remember { mutableStateOf(false) }
    val sign_show = remember { mutableStateOf(false) }
    val aab_show = remember { mutableStateOf(false) }
    val adb_show = remember { mutableStateOf(false) }
    DecDialog(dec_show)
    DexDialog(dex_show)
    SignManagerDialog(sign_manager_show)
    SignDialog(sign_show)
    AABDialog(aab_show)
    ADBDialog(adb_show)
    Window(
        onCloseRequest = ::exitApplication,
        title = "ADT",
        state = rememberWindowState(width = 300.dp, height = 520.dp),
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
                    dec_show.value = true
                }
                Button("dex") {
                    dex_show.value = true
                }
                Button("backToApk") {
                    FileChooser.newInstance(window,
                        JFileChooser.DIRECTORIES_ONLY,
                        "backToApk",
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                ADT.backToApk(path)
                            }
                        })
                }
                Button("sign manager") {
                    sign_manager_show.value = true
                }
                Button("sign") {
                    sign_show.value = true
                }
                Button("aab") {
                    aab_show.value = true
                }
                Button("adb") {
                    adb_show.value = true
                }
            }
        }
    }
}
