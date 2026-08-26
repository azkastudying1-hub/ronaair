package com.example.data

import com.example.data.dao.MonitoringDao
import com.example.data.dao.PondDao
import com.example.data.model.MonitoringRecord
import com.example.data.model.PondEntity
import kotlinx.coroutines.flow.Flow

class RonaAirRepository(
    private val pondDao: PondDao,
    private val monitoringDao: MonitoringDao
) {
    val allPonds: Flow<List<PondEntity>> = pondDao.getAllPonds()
    val allRecords: Flow<List<MonitoringRecord>> = monitoringDao.getAllRecords()
    val unsyncedRecords: Flow<List<MonitoringRecord>> = monitoringDao.getUnsyncedRecords()
    val latestRecord: Flow<MonitoringRecord?> = monitoringDao.getLatestRecord()

    suspend fun addPond(name: String, fishType: String, status: String = "Aktif"): Long {
        return pondDao.insertPond(PondEntity(name = name, fishType = fishType, status = status))
    }

    suspend fun saveMonitoring(record: MonitoringRecord): Long {
        return monitoringDao.insertRecord(record)
    }

    suspend fun syncAllRecords() {
        monitoringDao.markAllAsSynced()
    }

    suspend fun deletePond(id: Long) {
        pondDao.deletePond(id)
    }
}
