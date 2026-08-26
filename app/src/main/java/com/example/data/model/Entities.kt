package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ponds")
data class PondEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val fishType: String,
    val status: String = "Aktif", // "Aktif", "Persiapan", "Panen"
    val areaM2: Double = 250.0,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "monitoring_records")
data class MonitoringRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val pondId: Long = 1,
    val pondName: String = "Kolam Utama",
    val aqiScore: Int = 112,
    val status: String = "WASPADA", // "NORMAL", "WASPADA", "BAHAYA"
    val statusMessage: String = "Ada perubahan visual yang perlu diperhatikan pada sampel air.",
    val visualNotes: String = "Warna air tampak lebih pekat dibanding pemantauan sebelumnya.",
    val confidencePercent: Int = 78,
    val timestamp: Long = System.currentTimeMillis(),
    val isSynced: Boolean = false,
    val isOffline: Boolean = true,
    val imageUrl: String = "https://lh3.googleusercontent.com/aida-public/AB6AXuDVW9z_bAttzMjlJejpuSejFRw7aDmjv9hRPv_yqaJTJti-G8osgHskdWaEzErQgcwWPXvydPY0S-sJ9Zf_UMEJKc8pS92gbBmznqciOd89tfsDcTLZ7TfbIti5I2hvk7HME5HL0txS_HGNv3AaaUoIhyyXjr0srl8d1h8w261fKvNKOjt174eIdY9RHAivTlj1UEKCY62kiontoY0Z-MPW6aUwncqK5wGSrrrJGASmTDoqmSKVm11GBg"
)
