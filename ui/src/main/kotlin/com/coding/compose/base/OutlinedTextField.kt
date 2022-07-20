package com.coding.compose.base

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun OutlinedTextField(
    label: String = "",
    text: MutableState<String>,
) {
    OutlinedTextField(
        value = text.value,
        onValueChange = { text.value = it },
        modifier = Modifier.width(250.dp).height(60.dp),
        label = { if (label.isNotEmpty()) Text(label, modifier = Modifier.width(120.dp)) },
        textStyle = TextStyle(textAlign = TextAlign.Start),
        maxLines = 1,
    )
}