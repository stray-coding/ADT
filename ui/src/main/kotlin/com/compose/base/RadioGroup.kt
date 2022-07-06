import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RadioGroup(select: MutableState<String>, tags: List<String>) {
    Column {
        tags.forEach {
            Row(verticalAlignment = Alignment.CenterVertically) {
                RadioButton(modifier = Modifier.height(25.dp), selected = it == select.value, onClick = {
                    select.value = it
                })
                Text(text = it, textAlign = TextAlign.Start, maxLines = 1)
            }
            Spacer(modifier = Modifier.height(0.5.dp))
        }
    }
}