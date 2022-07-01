package compose.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowScope

@Composable
fun dialog(title: String, state: MutableState<Boolean>, content: @Composable DialogWindowScope.() -> Unit) {
    Dialog(
        visible = state.value,
        title = title,
        onCloseRequest = { state.value = false },
        content = content
    )
}