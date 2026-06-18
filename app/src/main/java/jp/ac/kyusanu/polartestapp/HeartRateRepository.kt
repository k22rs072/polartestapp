package jp.ac.kyusanu.polartestapp

class HeartRateRepository(
    private val dao: HeartRateDao
) {
    suspend fun insert(
        heartRate: Int,
        rrInterval: Int,
        deviceId: String
    ) {
        dao.insert(
            HeartRateEntity(//自動でidは入るから入れなくておっけ
                timestamp = System.currentTimeMillis(),
                heartRate = heartRate,
                rrInterval = rrInterval,
                deviceId = deviceId
            )
        )
    }
    fun getAll() = dao.getAll()
    suspend fun getUnsent() = dao.getUnsent()
    suspend fun markAsSent(ids: List<Long>) = dao.markAsSent(ids)
    suspend fun markSending(ids: List<Long>, sending: Boolean) = dao.markSending(ids, sending)
}