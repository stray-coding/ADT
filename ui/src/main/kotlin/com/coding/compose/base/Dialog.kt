package com.coding.compose.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowScope

@Composable
fun Dialog(
    title: String,
    state: MutableState<Boolean>,
    content: @Composable DialogWindowScope.() -> Unit
) {
    Dialog(
        visible = state.value,
        resizable = false,
        title = title,
        onCloseRequest = { state.value = false },
        content = content
    )
}