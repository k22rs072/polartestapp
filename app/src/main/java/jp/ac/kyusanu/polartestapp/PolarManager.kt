package jp.ac.kyusanu.polartestapp

import android.content.Context
import android.util.Log
import com.polar.androidcommunications.api.ble.model.DisInfo
import com.polar.sdk.api.PolarBleApi
import com.polar.sdk.api.PolarBleApiDefaultImpl
import com.polar.sdk.api.PolarBleApiCallback
import com.polar.sdk.api.model.PolarDeviceInfo
import com.polar.sdk.api.model.PolarHealthThermometerData
import com.polar.sdk.api.model.PolarHrData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class PolarManager (context: Context){

    var onDeviceFound: ((PolarDeviceInfo) -> Unit)? = null
    var onHr: ((Int) -> Unit)? = null
    var onRr: ((Int) -> Unit)? = null

    var onConnectionChanged: ((String) -> Unit)? = null
    companion object {
        private const val TAG = "PolarManager"
    }
    private val api: PolarBleApi =
        PolarBleApiDefaultImpl.defaultImplementation(
            context,
            setOf(
                PolarBleApi.PolarBleSdkFeature.FEATURE_HR,
                PolarBleApi.PolarBleSdkFeature.FEATURE_DEVICE_INFO
            )

        )
    init {//コンストラクタ実行時に呼ばれる初期化関数
        api.setApiCallback(object : PolarBleApiCallback() {// Polar SDKのイベントを受け取るコールバックを登録

            override fun deviceConnecting(
                polarDeviceInfo: PolarDeviceInfo
            ){
                Log.d(TAG, "CONNECTING ${polarDeviceInfo.deviceId}")
                onConnectionChanged?.invoke("接続中")
            }
            override fun deviceConnected(//接続できたら呼ばれる
                polarDeviceInfo: PolarDeviceInfo
            ) {
                Log.d(TAG, "CONNECTED ${polarDeviceInfo.deviceId}")
                onConnectionChanged?.invoke("接続済み")
            }
            override fun deviceDisconnected(//接続が切れたら呼ばれる
                polarDeviceInfo: PolarDeviceInfo
            ) {
                Log.d(TAG, "DISCONNECTED ${polarDeviceInfo.deviceId}")
                onConnectionChanged?.invoke("未接続")
            }

            override fun disInformationReceived(//デバイス情報を教えてくれる
                identifier: String,
                disInfo: DisInfo
            ) {
                Log.d(TAG, "DIS INFO: $disInfo")
            }

            override fun htsNotificationReceived(//体温は今回取れないから意味ない
                identifier: String,
                data: PolarHealthThermometerData
            ) {

            }
            override fun hrNotificationReceived(
                identifier: String,
                data: PolarHrData.PolarHrSample
            ) {
            }

            override fun bleSdkFeatureReady(//H10が接続され機能の準備ができたら呼ばれる
                identifier: String,
                feature: PolarBleApi.PolarBleSdkFeature
            ){
                Log.d(TAG, "Feature Ready: $feature")
                if (feature == PolarBleApi.PolarBleSdkFeature.FEATURE_HR)
                    api.startHrStreaming(identifier)
                        .onEach {polarHrData ->
                            polarHrData.samples.forEach { sample ->
//                                Log.d(TAG, "HeartRate : ${sample.hr}")
                                Log.d(TAG, sample.toString())
                                onHr?.invoke(sample.hr)//HeartRate

                                sample.rrsMs.forEach { rr ->
                                    onRr?.invoke(rr)
                                }
                            }}//データが流れてきたら毎回する処理
                        .launchIn(CoroutineScope(Dispatchers.IO))//ここわからん
            }
        })
    }
    fun connect(deviceId: String) {
        Log.d("PolarManager", "connect called: $deviceId")
        api.connectToDevice(deviceId)
    }

    fun search() {
        onConnectionChanged?.invoke("デバイス検索中")
        api.searchForDevice()
            .onEach { device ->
                Log.d(TAG, "Found: ${device.deviceId}")
                Log.d(TAG, "Name: ${device.name}")
                onDeviceFound?.invoke(device)
            }
            .launchIn(CoroutineScope(Dispatchers.IO))
    }
    fun autoConnect() {
        CoroutineScope(Dispatchers.Main).launch {
//            onConnectionChanged?.invoke("デバイス検索中")
            api.autoConnectToDevice(-50, null, null)//自動で繋ぐ
        }
    }
    fun disconnect(deviceId: String) {
        api.disconnectFromDevice(deviceId)
    }
}