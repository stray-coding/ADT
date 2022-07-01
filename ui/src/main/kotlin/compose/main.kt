import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import com.coding.dec.ADT


fun main() = application {
    val show = remember {
        mutableStateOf(true)
    }

    Window(
        onCloseRequest = ::exitApplication,
        title = "ADT",
        state = rememberWindowState(width = 300.dp, height = 400.dp)
    ) {
        MaterialTheme {
            Column(Modifier.fillMaxSize(), Arrangement.spacedBy(10.dp)) {


                Button(modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp, 50.dp),
                    onClick = {
                        ADT.openJadx()
                    }) {
                    Text("jadx")
                }
                Button(
                    modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp, 50.dp),
                    onClick = { }
                ) {
                    Text("decompile")
                }
                Button(modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp, 50.dp),
                    onClick = {

                    }) {
                    Text("dex")
                }
                Button(modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp, 50.dp),
                    onClick = {
                        show.value = !(show.value)
                    }) {
                    Text("backToApk")
                }
                Button(modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp, 50.dp),
                    onClick = {

                    }) {
                    Text("sign")
                }
                Button(modifier = Modifier.align(Alignment.CenterHorizontally).size(150.dp, 50.dp),
                    onClick = {

                    }) {
                    Text("aab")
                }

                decDialog(show)
            }
        }
    }
}

//private fun click() {
//    show.value = !(show.value)
//}