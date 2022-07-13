package com.coding.compose.ui.adb

import RadioGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
            val list = remember { mutableListOf<String>() }
            list.clear()
            for (item in ADT.getAllDevices()) {
                list.add(item)
            }
            val select = remember { mutableStateOf("choose device") }
            RadioGroup(select, list, listener)
        }
    }
}