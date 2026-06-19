package jp.ac.kyusanu.polartestapp

import kotlinx.serialization.Serializable

@Serializable
data class HeartRateDto(
    val deviceId: String,
    val location: String?,        // デフォルトでnullをセット
    val timestamp: Long,
    val skinTemperature: Float?,         // デフォルトでnullをセット
    val heartRate: Short,         // DBのsmallintに合わせてShortに（Intのままでも可）
    val rrInterval: Short,        // DBのsmallintに合わせてShortに（Intのままでも可）
    val spo2: Int?,               // デフォルトでnullをセット
    val build: Int?,              // デフォルトでnullをセット
    val systemVersion: String?    // デフォルトでnullをセット
)
