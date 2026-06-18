package jp.ac.kyusanu.polartestapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface HeartRateDao {
    @Insert
    suspend fun insert(data: HeartRateEntity)

    @Query("SELECT * FROM heart_rate ORDER BY timestamp DESC")
    fun getAll(): Flow<List<HeartRateEntity>>

    @Query("SELECT * FROM heart_rate WHERE uploaded = 0 AND sending = 0")
    suspend fun getUnsent(): List<HeartRateEntity>//suspendは一回だけ

    @Query("UPDATE heart_rate SET uploaded = 1,sending = 0 WHERE id IN (:ids)")
    suspend fun markAsSent(ids: List<Long>)

    @Query("UPDATE heart_rate SET sending = :sending WHERE id IN (:ids)")
    suspend fun markSending(ids: List<Long>, sending: Boolean)

}