package compose.base

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

@Composable
fun checkBox(title: String, state: MutableState<Boolean>) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (title.isNotEmpty()) {
            Text(text = title, textAlign = TextAlign.Start, modifier = Modifier.width(120.dp), maxLines = 1)
        }
        Checkbox(checked = state.value, onCheckedChange = { state.value = !state.value })
    }
}
