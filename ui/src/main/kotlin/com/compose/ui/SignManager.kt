package com.compose.ui

import RadioGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coding.dec.utils.SignUtils
import com.coding.ui.Toast
import com.compose.base.Button
import com.compose.base.Dialog

private var curSelect = ""

@Composable
fun SignManagerDialog(show: MutableState<Boolean>) {
    Dialog(title = "sign manager", state = show) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val list = remember { mutableListOf<String>() }
            list.clear()
            for (item in SignUtils.getSignList()) {
                list.add(item.name)
            }
            val select = remember { mutableStateOf(list[0]) }
            RadioGroup(select, list)
            curSelect = select.value

            val addSign_show = remember { mutableStateOf(false) }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Button("delete sign") {
                    val selectSign = SignUtils.getSign(select.value)
                    if (selectSign.name == "defaultSign") {
                        Toast.showMsg(window, "the default signature can't be deleted！")
                        return@Button
                    }
                    SignUtils.deleteSign(selectSign)
                }

                Button("add sign") {
                    addSign_show.value = true
                }
            }
            AddSignDialog(addSign_show)
        }
    }
}

fun getCurSign(): SignUtils.SignBean {
    return SignUtils.getSign(curSelect)
}