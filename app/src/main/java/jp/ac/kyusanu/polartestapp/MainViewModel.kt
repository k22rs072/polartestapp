package jp.ac.kyusanu.polartestapp

import androidx.lifecycle.ViewModel
import com.polar.sdk.api.model.PolarDeviceInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MainViewModel (
    private val polarManager: PolarManager
) : ViewModel() {
    private val _devices = MutableStateFlow<List<PolarDeviceInfo>>(emptyList())

    val devices: StateFlow<List<PolarDeviceInfo>> = _devices
    private val _hr = MutableStateFlow(0)
    val hr: StateFlow<Int> = _hr
    private val _rr = MutableStateFlow(0)
    val rr: StateFlow<Int> = _rr
    private val _connectionState = MutableStateFlow("未接続")
    val connectionState: StateFlow<String> = _connectionState

    init {
        polarManager.onHr = { value ->
            _hr.value = value
        }
        polarManager.onConnectionChanged = { value ->
            _connectionState.value = value
        }
        polarManager.onRr = { value ->
            _rr.value = value
        }
        polarManager.onDeviceFound = { value ->
            _devices.value = (_devices.value + value).distinctBy { it.deviceId }//重複したものは消す
        }
    }


    fun autoConnect() {
        polarManager.autoConnect()
    }
    fun search() {
        _devices.value = emptyList()//前回のが残んないように初期化
        polarManager.search()
    }
    fun connect(deviceId: String) {
        polarManager.connect(deviceId)
    }
    fun disconnect(deviceId: String) {
        polarManager.disconnect(deviceId)
    }
}