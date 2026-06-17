package jp.ac.kyusanu.polartestapp

class HeartRateRepository(
    private val dao: HeartRateDao
) {
    suspend fun insert(
        heartRate: Int,
        rrInterval: Int
    ) {
        dao.insert(
            HeartRateEntity(//自動でidは入るから入れなくておっけ
                timestamp = System.currentTimeMillis(),
                heartRate = heartRate,
                rrInterval = rrInterval
            )
        )
    }
    suspend fun getAll() = dao.getAll()
}