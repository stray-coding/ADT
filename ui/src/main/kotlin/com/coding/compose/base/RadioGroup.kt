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
    select: MutableState<String>,
    array: SnapshotStateList<String>,
    listener: OnSelectListener? = null,
    tips: String = "数据不见了 -_-!"
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (array.isNotEmpty()) {
            array.forEach {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(modifier = Modifier.height(25.dp), selected = it == select.value, onClick = {
                        select.value = it
                        listener?.onSelected(it)
                    })
                    Text(modifier = Modifier.width(200.dp), text = it, textAlign = TextAlign.Start, maxLines = 1)
                }
                Spacer(modifier = Modifier.height(1.dp))
            }
        } else {
            Text(
                modifier = Modifier.fillMaxSize(),
                text = tips,
                style = TextStyle(textAlign = TextAlign.Center)
            )
        }
    }
}
