package com.coding.compose.base

import RadioGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Dialog
import com.coding.compose.listener.OnSelectListener

@Composable
fun ListDialog(
        mTitle: String,
        selected: MutableState<String>,
        list: SnapshotStateList<String>,
        show: MutableState<Boolean>,
        listener: OnSelectListener
) {
    Dialog(
            visible = show.value,
            resizable = false,
            icon = MyIcons.getAppIconPainter(),
            title = mTitle,
            onCloseRequest = {
                show.value = false
            },
            content = {
                Column(
                        modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (show.value) {
                        RadioGroup(selected, list, listener)
                    }
                }
            }
    )
}

