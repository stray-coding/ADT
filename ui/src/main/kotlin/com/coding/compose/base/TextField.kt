package com.coding.compose.base

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun TextField(
    label: String = "",
    text: MutableState<String>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (label.isNotEmpty()) {
            Text(label, modifier = Modifier.width(120.dp))
        }
        TextField(
            value = text.value,
            onValueChange = { text.value = it },
            modifier = Modifier.width(200.dp).height(50.dp),
            maxLines = 1,
        )
    }

}