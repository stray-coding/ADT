package com.coding.compose.ui.adb

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
import com.coding.dec.ADT

@Composable
fun DevicesListDialog(show: MutableState<Boolean>, listener: OnSelectListener) {
    Dialog(title = "devices list", state = show) {
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier.verticalScroll(scrollState).fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (show.value) {
                val list = remember { mutableStateListOf<String>() }
                list.clear()
                for (item in ADT.getAllDevices()) {
                    list.add(item)
                }
                val select = remember { mutableStateOf("choose device") }
                RadioGroup(select, list, listener)
            }
        }
    }
}