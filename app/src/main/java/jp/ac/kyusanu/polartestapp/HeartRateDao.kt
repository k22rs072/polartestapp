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

}