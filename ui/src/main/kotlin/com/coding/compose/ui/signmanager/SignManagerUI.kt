package com.coding.compose.ui.signmanager

import RadioGroup
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.compose.base.Button
import com.coding.compose.base.Toast
import com.coding.compose.listener.OnCloseListener
import com.coding.compose.mWindow
import com.coding.dec.utils.SignUtils


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

        Type.ADD_SIGN -> AddSignUI(object : OnCloseListener {
            override fun onClose() {
                refreshSignList(list)
                curType.value = Type.SIGN_MANAGER
            }

        })
    }

}

private fun refreshSignList(list: SnapshotStateList<String>) {
    list.clear()
    for (item in SignUtils.getSignList()) {
        list.add(item.name)
    }
}


