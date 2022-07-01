package compose.base

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun button(label: String, onClick: () -> Unit) {
    Button(onClick = onClick) {
        Text(label)
    }
}