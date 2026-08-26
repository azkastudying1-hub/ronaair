package com.example.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.MonitoringDao
import com.example.data.dao.PondDao
import com.example.data.model.MonitoringRecord
import com.example.data.model.PondEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [PondEntity::class, MonitoringRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun pondDao(): PondDao
    abstract fun monitoringDao(): MonitoringDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ronaair_database.db"
                ).addCallback(DatabaseCallback())
                 .fallbackToDestructiveMigration()
                 .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database)
                    }
                }
            }

            suspend fun seedDatabase(database: AppDatabase) {
                val pondDao = database.pondDao()
                val monitoringDao = database.monitoringDao()

                // Initial ponds
                val ponds = listOf(
                    PondEntity(id = 1, name = "Kolam A1", fishType = "Nila Merah", status = "Aktif"),
                    PondEntity(id = 2, name = "Kolam B2", fishType = "Lele Sangkuriang", status = "Persiapan")
                )
                pondDao.insertAll(ponds)

                val now = System.currentTimeMillis()
                val oneDay = 24L * 60 * 60 * 1000
                val records = listOf(
                    MonitoringRecord(
                        id = 1,
                        pondId = 1,
                        pondName = "Kolam Utama",
                        aqiScore = 112,
                        status = "WASPADA",
                        statusMessage = "Ada perubahan visual yang perlu diperhatikan pada sampel air.",
                        visualNotes = "Warna air tampak lebih pekat dibanding pemantauan sebelumnya.",
                        confidencePercent = 78,
                        timestamp = now - (15 * 60 * 1000), // today 15 mins ago
                        isSynced = false,
                        isOffline = true
                    ),
                    MonitoringRecord(
                        id = 2,
                        pondId = 1,
                        pondName = "Kolam Utama",
                        aqiScore = 45,
                        status = "NORMAL",
                        statusMessage = "Kondisi air kolam jernih dan stabil.",
                        visualNotes = "Parameter visual dalam rentang optimal.",
                        confidencePercent = 92,
                        timestamp = now - oneDay,
                        isSynced = true,
                        isOffline = false
                    ),
                    MonitoringRecord(
                        id = 3,
                        pondId = 1,
                        pondName = "Kolam Utama",
                        aqiScore = 42,
                        status = "NORMAL",
                        statusMessage = "Kondisi air kolam jernih dan stabil.",
                        visualNotes = "Parameter visual dalam rentang optimal.",
                        confidencePercent = 94,
                        timestamp = now - (2 * oneDay),
                        isSynced = true,
                        isOffline = false
                    )
                )
                monitoringDao.insertAll(records)
            }
        }
    }
}
