package jp.ac.kyusanu.polartestapp

import androidx.room.Entity
import androidx.room.PrimaryKey

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
        timestamp = timestamp,
        heartRate = heartRate,
        rrInterval = rrInterval
    )
}