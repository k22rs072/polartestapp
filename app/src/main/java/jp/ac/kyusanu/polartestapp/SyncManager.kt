package jp.ac.kyusanu.polartestapp

import android.util.Log
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType

class SyncManager(private val repository: HeartRateRepository) {

    suspend fun sync() {
        val unsent = repository.getUnsent()

        if (unsent.isEmpty()) return

        val ids = unsent.map { it.id }

        try {
            // ① ロック
            repository.markSending(ids, true)
            // ② DTO化
            val dto = unsent.map { it.toDto() }
            Log.d("SYNC", "SEND")
            // ③ 送信
            val response = ApiClient.client.post("https://carbon25.apps.kyusan-u.ac.jp/vital/save_data.php") {
                contentType(ContentType.Application.Json)
                setBody(dto)
            }
            // ④ 成功時だけ確定
            if (response.status.value in 200..299) {
                repository.markAsSent(ids)
            }
        } catch (e: Exception) {
            Log.e("SYNC", "error", e)
        } finally {
            // ⑤ 失敗でもロック解除
            repository.markSending(ids, false)
        }
    }
}