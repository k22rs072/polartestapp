package jp.ac.kyusanu.polartestapp

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.foundation.lazy.items

@Composable
fun MainScreen (modifier: Modifier = Modifier,viewModel: MainViewModel){
    val devices by viewModel.devices.collectAsState()
    val heartRate by viewModel.hr.collectAsState()
    val rrInterval by viewModel.rr.collectAsState()
    val connectionState by viewModel.connectionState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Heart Rate",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$heartRate bpm",
            style = MaterialTheme.typography.displayLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "RR Interval",
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "$rrInterval ms",
            style = MaterialTheme.typography.displayLarge
        )
        Text(
            text = connectionState,
            style = MaterialTheme.typography.headlineMedium
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = {
//                viewModel.connect()
                viewModel.search()
            },
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .height(56.dp)
                .width(180.dp)
        ) {
            Text(
                text = "デバイスを探す",
                style = MaterialTheme.typography.titleMedium
            )
        }
        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "見つかったデバイス",
            style = MaterialTheme.typography.titleLarge
        )

        LazyColumn {
            items(devices) { device ->
                Card(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clickable {
                            viewModel.connect(device.deviceId)
                        }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(device.name)
                        Text(device.deviceId)
                    }
                }
            }
        }
    }
}