package jp.ac.kyusanu.polartestapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen (modifier: Modifier = Modifier, polarManager: PolarManager){
    val deviceId = "13309134"
    Column(modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        Button(
            onClick = {
                polarManager.connect()
            }
        ) {
            Text("Connect")
        }
    }
}