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
            override fun deviceConnected(//接続できたら呼ばれる
                polarDeviceInfo: PolarDeviceInfo
            ) {
                Log.d(TAG, "CONNECTED ${polarDeviceInfo.deviceId}")
            }
            override fun deviceDisconnected(//接続が切れたら呼ばれる
                polarDeviceInfo: PolarDeviceInfo
            ) {
                Log.d(TAG, "DISCONNECTED ${polarDeviceInfo.deviceId}")
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
            override fun bleSdkFeatureReady(//H10が接続され機能の準備ができたら呼ばれる
                identifier: String,
                feature: PolarBleApi.PolarBleSdkFeature
            ){
                Log.d(TAG, "Feature Ready: $feature")
                if (feature == PolarBleApi.PolarBleSdkFeature.FEATURE_HR)
                    api.startHrStreaming(identifier)
                        .onEach { Log.d(TAG, "HR stream: $it") }//データが流れてきたら毎回する処理
                        .launchIn(CoroutineScope(Dispatchers.IO))
            }
            override fun hrNotificationReceived(
                identifier: String,
                data: PolarHrData.PolarHrSample
            ) {
                Log.d(TAG, "HR: ${data.hr}")
            }
        })
    }
//    fun connect(deviceId: String) {
//        Log.d("PolarManager", "connect called: $deviceId")
//        api.connectToDevice(deviceId)
//    }

    fun connect() {
        CoroutineScope(Dispatchers.Main).launch {
            api.autoConnectToDevice(-50, null, null)
        }
    }
    fun disconnect(deviceId: String) {
        api.disconnectFromDevice(deviceId)
    }
    fun search(){
        api.searchForDevice()
    }
}