package com.coding.compose.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.coding.compose.base.Button
import com.coding.compose.base.CheckBox
import com.coding.compose.base.FileChooser
import com.coding.compose.mWindow
import com.coding.dec.ApkTool
import com.coding.dec.utils.Suffix
import javax.swing.JFileChooser


@Composable
fun ApktoolUI() {
    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val ignoreDex = remember { mutableStateOf(false) }
        val ignoreResource = remember { mutableStateOf(false) }
        CheckBox("ignore dex", ignoreDex)
        CheckBox("ignore resource", ignoreResource)
        Button("decompile") {
            FileChooser.newInstance(
                    mWindow,
                    JFileChooser.FILES_ONLY,
                    "decompile",
                    Suffix.APK,
                    object : FileChooser.OnFileSelectListener {
                        override fun onSelected(path: String) {
                            ApkTool.decompile(path, ignoreDex.value, ignoreResource.value)
                        }
                    })
        }
        Button("back to apk") {
            FileChooser.newInstance(
                    mWindow,
                    JFileChooser.DIRECTORIES_ONLY,
                    "back to apk",
                    object : FileChooser.OnFileSelectListener {
                        override fun onSelected(path: String) {
                            ApkTool.backToApk(path)
                        }
                    })
        }
    }
}
