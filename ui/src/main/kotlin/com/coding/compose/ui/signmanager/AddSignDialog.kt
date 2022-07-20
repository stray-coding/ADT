package com.coding.compose.ui.signmanager

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.compose.base.*
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import com.coding.dec.utils.Tools
import com.coding.utils.FileUtils
import java.io.File
import javax.swing.JFileChooser

@Composable
fun AddSignDialog(show: MutableState<Boolean>) {
    Dialog(title = "sign manager", state = show) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val pwd = remember { mutableStateOf("") }
            val alias = remember { mutableStateOf("") }
            val alias_pwd = remember { mutableStateOf("") }
            OutlinedTextField("password", pwd)
            OutlinedTextField("alias", alias)
            OutlinedTextField("alias password", alias_pwd)
            Button("sign file path") {
                if (pwd.value.isEmpty() || alias.value.isEmpty() || alias_pwd.value.isEmpty()) {
                    Toast.showMsg(window, "please complete the signature configuration information")
                    return@Button
                }
                FileChooser.newInstance(
                    window,
                    JFileChooser.FILES_ONLY,
                    "choose sign",
                    arrayOf(Suffix.JKS, Suffix.KEY_STORE),
                    object : FileChooser.OnFileSelectListener {
                        override fun onSelected(path: String) {
                            val name = path.substring(path.lastIndexOf(File.separator) + 1, path.length)
                            //保存签名文件的路径
                            val savePath =
                                Tools.getConfigDir() + File.separator + path.substring(path.lastIndexOf(File.separator) + 1)
                            FileUtils.copyFile(path, savePath)
                            val sign = SignUtils.SignBean(name, savePath, pwd.value, alias.value, alias_pwd.value)
                            SignUtils.addSign(sign)
                        }
                    })
            }
        }
    }
}