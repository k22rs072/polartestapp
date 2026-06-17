package jp.ac.kyusanu.polartestapp

import androidx.lifecycle.ViewModel

class MainViewModel (
    private val polarManager: PolarManager
) : ViewModel() {
    fun connect(deviceId: String) {
        polarManager.connect()
    }
    fun disconnect(deviceId: String) {
        polarManager.disconnect(deviceId)
    }
}