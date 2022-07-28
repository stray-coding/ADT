package com.coding.compose.ui.sign

import RadioGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.coding.compose.base.Dialog
import com.coding.compose.listener.OnSelectListener
import com.coding.dec.utils.SignUtils

@Composable
fun SignListDialog(show: MutableState<Boolean>, listener: OnSelectListener) {
    Dialog(title = "sign list", state = show) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val list = remember { mutableStateListOf<String>() }
            list.clear()
            for (item in SignUtils.getSignList()) {
                list.add(item.name)
            }
            val select = remember { mutableStateOf("choose sign") }
            RadioGroup(select, list, listener)
        }
    }
}