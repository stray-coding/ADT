package com.coding.compose.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.compose.base.*
import com.coding.compose.listener.OnCloseListener
import com.coding.compose.mWindow
import com.coding.dec.SignTool
import com.coding.dec.utils.Paths
import com.coding.dec.utils.SignUtils
import com.coding.dec.utils.Suffix
import com.coding.utils.FileUtils
import java.io.File
import javax.swing.JFileChooser


enum class Type {
    SIGN_MANAGER,
    ADD_SIGN
}

@Composable
fun SignManagerUI() {
    val list = remember { mutableStateListOf<String>() }
    val curType = remember { mutableStateOf(Type.SIGN_MANAGER) }
    when (curType.value) {
        Type.SIGN_MANAGER -> {
            ManagerUI(list, curType)
        }

        Type.ADD_SIGN -> {
            AddSignUI(object : OnCloseListener {
                override fun onClose() {
                    refreshSignList(list)
                    curType.value = Type.SIGN_MANAGER
                }
            })
        }
    }
}


private fun refreshSignList(list: SnapshotStateList<String>) {
    list.clear()
    for (item in SignUtils.getSignList()) {
        list.add(item.name)
    }
}

@Composable
fun ManagerUI(list: SnapshotStateList<String>, curType: MutableState<Type>) {
    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        refreshSignList(list)
        val select = remember { mutableStateOf("choose sign") }
        val scrollState = rememberScrollState()
        Column(modifier = Modifier.height(250.dp).verticalScroll(scrollState)) {
            RadioGroup(select, list)
        }
        Row {
            Button("delete sign") {
                val signBean = SignUtils.getSign(select.value) ?: return@Button
                if (signBean.name == "adt.jks") {
                    Toast.showMsg(mWindow, "the default signature can't be deleted!")
                    return@Button
                }
                if (signBean.name == "") {
                    return@Button
                }
                SignUtils.deleteSign(signBean)
                list.remove(select.value)
            }

            Button("add sign") {
                curType.value = Type.ADD_SIGN
            }
        }
    }
}

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
                                onCloseListener.onClose()
                            }
                        })
            }
        }
    }
}


