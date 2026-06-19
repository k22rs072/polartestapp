package jp.ac.kyusanu.polartestapp

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.Int

@Entity(tableName = "heart_rate")
data class HeartRateEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val deviceId: String,
    val timestamp: Long,//時間の形式は要検討
    val heartRate: Int,
    val rrInterval: Int,
    val uploaded: Boolean = false,
    val uploadedAt: Long? = null,
    val sending: Boolean = false
)

fun HeartRateEntity.toDto(): HeartRateDto {
    return HeartRateDto(
        deviceId = deviceId,
        location = "SG01",
        timestamp = timestamp,
        skinTemperature = null,
        heartRate = heartRate.toShort(),
        rrInterval = rrInterval.toShort(),
        spo2 = null,
        build = 1,
        systemVersion = "0.1.0"    // デフォルトでnullをセット
    )
}