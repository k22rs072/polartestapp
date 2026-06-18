package jp.ac.kyusanu.polartestapp

import kotlinx.serialization.Serializable

@Serializable
data class HeartRateDto(
    val deviceId: String,
    val timestamp: Long,
    val heartRate: Int,
    val rrInterval: Int
)
