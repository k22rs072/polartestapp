package jp.ac.kyusanu.polartestapp

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

class PolarService : Service()  {
    private lateinit var polarManager: PolarManager
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var connectedDeviceId: String? = null
    private var isConnected = false
    private var latestHr: Int = 0
    private var latestRr: Int = 0
    private lateinit var repository: HeartRateRepository
    override fun onCreate() {
        super.onCreate()

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "polar.db"
        ).build()
        repository = HeartRateRepository(db.heartRateDao())
        polarManager = PolarManager(this)
        createChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(1, createNotification())

        val deviceId = intent?.getStringExtra("DEVICE_ID")

        polarManager.onVitalData = { hr, rr ->
            Log.d("GET", "hr=$hr rr=$rr")
            latestHr = hr
            latestRr = rr
        }

//        deviceId?.let {//将来用
//            if (!isConnected || connectedDeviceId != it) {
//                polarManager.connect(it)
//                connectedDeviceId = it
//                isConnected = true
//            }
//        }
        deviceId?.let {
            polarManager.connect(it)
        }

        scope.launch {
            while (true) {
                repository.insert(
                    heartRate = latestHr,
                    rrInterval = latestRr
                )
                val unsentData = repository.getUnsent()
                delay(60000.milliseconds) // 1分ごと
            }

        }
        return START_STICKY
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun createNotification(): Notification {//通知出さなバックグラウンドできんのよ
        return NotificationCompat.Builder(this, "polar_channel")
            .setContentTitle("Polarデータ収集中")
            .setContentText("心拍データを取得しています")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()
    }
    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "polar_channel",
                "Polar Service",
                NotificationManager.IMPORTANCE_LOW
            )
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}