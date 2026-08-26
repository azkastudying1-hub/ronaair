package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MonitoringRecord
import com.example.data.model.PondEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PondDao {
    @Query("SELECT * FROM ponds ORDER BY id ASC")
    fun getAllPonds(): Flow<List<PondEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPond(pond: PondEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(ponds: List<PondEntity>)

    @Query("DELETE FROM ponds WHERE id = :id")
    suspend fun deletePond(id: Long)
}

@Dao
interface MonitoringDao {
    @Query("SELECT * FROM monitoring_records ORDER BY timestamp DESC")
    fun getAllRecords(): Flow<List<MonitoringRecord>>

    @Query("SELECT * FROM monitoring_records WHERE isSynced = 0")
    fun getUnsyncedRecords(): Flow<List<MonitoringRecord>>

    @Query("SELECT * FROM monitoring_records ORDER BY timestamp DESC LIMIT 1")
    fun getLatestRecord(): Flow<MonitoringRecord?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: MonitoringRecord): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(records: List<MonitoringRecord>)

    @Update
    suspend fun updateRecord(record: MonitoringRecord)

    @Query("UPDATE monitoring_records SET isSynced = 1 WHERE isSynced = 0")
    suspend fun markAllAsSynced()

    @Query("DELETE FROM monitoring_records WHERE id = :id")
    suspend fun deleteRecord(id: Long)
}
