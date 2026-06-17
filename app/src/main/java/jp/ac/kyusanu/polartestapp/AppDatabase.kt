package jp.ac.kyusanu.polartestapp

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [HeartRateEntity::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heartRateDao(): HeartRateDao
}