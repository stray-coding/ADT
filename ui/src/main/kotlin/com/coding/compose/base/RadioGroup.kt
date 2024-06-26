package com.coding.compose.base

import androidx.compose.foundation.layout.*
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coding.compose.listener.OnSelectListener

@Composable
fun RadioGroup(
        selected: MutableState<String>,
        list: SnapshotStateList<String>,
        listener: OnSelectListener? = null,
) {
    Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (list.isNotEmpty()) {
            list.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(modifier = Modifier.height(25.dp), selected = it == selected.value, onClick = {
                        selected.value = it
                        listener?.onSelected(it)
                    })
                    Text(modifier = Modifier.width(200.dp), text = it, textAlign = TextAlign.Start, maxLines = 1)
                }
                Spacer(modifier = Modifier.height(1.dp))
            }
        } else {
            Text(
                    modifier = Modifier.fillMaxSize(),
                    text = "数据不见了 -_-!",
                    style = TextStyle(textAlign = TextAlign.Center)
            )
        }
    }
}
