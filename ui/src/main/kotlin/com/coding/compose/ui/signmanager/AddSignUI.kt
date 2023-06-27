package com.coding.compose.ui.signmanager

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.compose.base.Button
import com.coding.compose.base.FileChooser
import com.coding.compose.base.OutlinedTextField
import com.coding.compose.base.Toast
import com.coding.compose.listener.OnCloseListener
import com.coding.compose.mWindow
import com.coding.dec.SignTool
import com.coding.dec.utils.Paths
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import com.coding.utils.FileUtils
import java.io.File
import javax.swing.JFileChooser

@Composable
fun AddSignUI(onCloseListener: OnCloseListener) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Icon(
                Icons.Default.ArrowBack,
                "返回",
                modifier = Modifier.padding(5.dp).align(Alignment.TopStart).clickable {
                    onCloseListener.onClose()
                }
        )
        Column(
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
                    Toast.showMsg(mWindow, "please complete the signature configuration information")
                    return@Button
                }
                FileChooser.newInstance(
                        mWindow,
                        JFileChooser.FILES_ONLY,
                        "choose sign",
                        arrayOf(Suffix.JKS, Suffix.KEY_STORE),
                        object : FileChooser.OnFileSelectListener {
                            override fun onSelected(path: String) {
                                val name = path.substring(path.lastIndexOf(File.separator) + 1)
                                //保存签名文件的路径
                                val savePath = Paths.getSignDir() + File.separator + name
                                FileUtils.copyFile(path, savePath)
                                val relativePath = savePath.replace(Paths.getCurDir() + File.separator, "")
                                val sign = SignUtils.SignBean(name, relativePath, pwd.value, alias.value, alias_pwd.value)
                                if (!SignTool.alignAndSign(
                                                Paths.getUnsignedApk(),
                                                sign,
                                                v1Enable = true,
                                                v2Enable = true
                                        )
                                ) {
                                    FileUtils.deleteFile(savePath)
                                    Toast.showMsg(mWindow, "Configuration information does not match signature file.")
                                    return
                                }
                                FileUtils.deleteFile(Paths.getUnsignedApk().replace(".apk", "_aligned_signed.apk"))
                                SignUtils.addSign(sign)
                            }
                        })
            }
        }
    }
}
