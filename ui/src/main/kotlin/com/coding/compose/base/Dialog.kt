package com.coding.compose.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowScope
import com.coding.compose.listener.OnCloseListener

@Composable
fun Dialog(
        title: String,
        state: MutableState<Boolean>,
        onCloseRequest: OnCloseListener? = null,
        content: @Composable DialogWindowScope.() -> Unit
) {
    Dialog(
            visible = state.value,
            resizable = false,
            icon = MyIcons.getAppIconPainter(),
            title = title,
            onCloseRequest = {
                state.value = false
                onCloseRequest?.onClose()
            },
            content = content
    )
}