package com.coding.compose.base

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.asComposeImageBitmap
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogWindowScope
import com.coding.compose.listener.OnDialogCloseListener
import com.coding.dec.utils.Paths
import org.jetbrains.skiko.toBitmap
import java.io.File
import javax.imageio.ImageIO

@Composable
fun Dialog(
        title: String,
        state: MutableState<Boolean>,
        onCloseRequest: OnDialogCloseListener? = null,
        content: @Composable DialogWindowScope.() -> Unit
) {
    val buffer = ImageIO.read(File(Paths.getIcon()))
    val painter = BitmapPainter(buffer.toBitmap().asComposeImageBitmap())
    Dialog(
            visible = state.value,
            resizable = false,
            icon = painter,
            title = title,
            onCloseRequest = {
                state.value = false
                onCloseRequest?.onClose()
            },
            content = content
    )
}