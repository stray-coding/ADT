package com.coding.compose.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coding.compose.listener.OnCheckListener

@Composable
fun CheckBox(
    title: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier.width(120.dp),
    listener: OnCheckListener? = null
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (title.isNotEmpty()) {
            Text(text = title, textAlign = TextAlign.Start, modifier = modifier, maxLines = 1)
        }
        Checkbox(checked = state.value, onCheckedChange = {
            state.value = it
            listener?.onCheckedChange(it)
        })
    }
}


