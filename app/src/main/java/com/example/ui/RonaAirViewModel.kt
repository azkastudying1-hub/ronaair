package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.RonaAirRepository
import com.example.data.model.MonitoringRecord
import com.example.data.model.PondEntity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppScreen {
    SPLASH,
    ONBOARDING,
    MAIN_APP
}

enum class NavigationTab {
    BERANDA,
    MONITOR,
    RIWAYAT,
    PROFIL
}

enum class MonitorStep {
    GUIDE,
    PREPARE,
    VIEWFINDER,
    PHOTO_READY,
    SCANNING,
    RESULT,
    RECOMMENDATIONS
}

class RonaAirViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val repository = RonaAirRepository(database.pondDao(), database.monitoringDao())

    // Navigation & Screen Flow
    private val _appScreen = MutableStateFlow(AppScreen.SPLASH)
    val appScreen: StateFlow<AppScreen> = _appScreen.asStateFlow()

    private val _currentTab = MutableStateFlow(NavigationTab.BERANDA)
    val currentTab: StateFlow<NavigationTab> = _currentTab.asStateFlow()

    private val _monitorStep = MutableStateFlow(MonitorStep.GUIDE)
    val monitorStep: StateFlow<MonitorStep> = _monitorStep.asStateFlow()

    // Mode Offline toggle
    private val _isOffline = MutableStateFlow(true)
    val isOffline: StateFlow<Boolean> = _isOffline.asStateFlow()

    // Sync State
    private val _isSyncing = MutableStateFlow(false)
    val isSyncing: StateFlow<Boolean> = _isSyncing.asStateFlow()

    // History filter
    private val _historyFilter = MutableStateFlow("7 Hari")
    val historyFilter: StateFlow<String> = _historyFilter.asStateFlow()

    // Scanning progress states
    private val _scanProgressStep = MutableStateFlow(1) // 1: Quality check, 2: Visual change, 3: Risk level
    val scanProgressStep: StateFlow<Int> = _scanProgressStep.asStateFlow()

    // Current Analysis Data
    private val _currentRecord = MutableStateFlow<MonitoringRecord?>(null)
    val currentRecord: StateFlow<MonitoringRecord?> = _currentRecord.asStateFlow()

    // Room DB streams
    val allPonds: StateFlow<List<PondEntity>> = repository.allPonds
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRecords: StateFlow<List<MonitoringRecord>> = repository.allRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val unsyncedRecords: StateFlow<List<MonitoringRecord>> = repository.unsyncedRecords
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestRecord: StateFlow<MonitoringRecord?> = repository.latestRecord
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    init {
        // Automatically check database initialization
        viewModelScope.launch {
            // Give a moment for splash animation
            delay(2800)
            _appScreen.value = AppScreen.ONBOARDING
        }
    }

    fun finishOnboarding() {
        _appScreen.value = AppScreen.MAIN_APP
    }

    fun selectTab(tab: NavigationTab) {
        _currentTab.value = tab
        if (tab == NavigationTab.MONITOR) {
            _monitorStep.value = MonitorStep.GUIDE
        }
    }

    fun setOfflineMode(enabled: Boolean) {
        _isOffline.value = enabled
    }

    fun setHistoryFilter(filter: String) {
        _historyFilter.value = filter
    }

    fun startMonitoringFromHome() {
        _currentTab.value = NavigationTab.MONITOR
        _monitorStep.value = MonitorStep.GUIDE
    }

    fun startGuide() {
        _monitorStep.value = MonitorStep.PREPARE
    }

    fun skipGuide() {
        _monitorStep.value = MonitorStep.VIEWFINDER
    }

    fun proceedToCamera() {
        _monitorStep.value = MonitorStep.VIEWFINDER
    }

    fun capturePhoto() {
        _monitorStep.value = MonitorStep.PHOTO_READY
    }

    fun retakePhoto() {
        _monitorStep.value = MonitorStep.VIEWFINDER
    }

    fun proceedToAnalysis() {
        _monitorStep.value = MonitorStep.SCANNING
        _scanProgressStep.value = 1

        viewModelScope.launch {
            delay(1000)
            _scanProgressStep.value = 2
            delay(1200)
            _scanProgressStep.value = 3
            delay(1400)

            // Save new monitoring record into Room database
            val newRecord = MonitoringRecord(
                pondId = 1,
                pondName = "Kolam Utama",
                aqiScore = 112,
                status = "WASPADA",
                statusMessage = "Ada perubahan visual yang perlu diperhatikan pada sampel air.",
                visualNotes = "Warna air tampak lebih pekat dibanding pemantauan sebelumnya.",
                confidencePercent = 78,
                timestamp = System.currentTimeMillis(),
                isSynced = false,
                isOffline = _isOffline.value
            )
            repository.saveMonitoring(newRecord)
            _currentRecord.value = newRecord

            _monitorStep.value = MonitorStep.RESULT
        }
    }

    fun viewRecommendations() {
        _monitorStep.value = MonitorStep.RECOMMENDATIONS
    }

    fun finishMonitoring() {
        _currentTab.value = NavigationTab.BERANDA
    }

    fun syncData() {
        viewModelScope.launch {
            _isSyncing.value = true
            delay(1500) // simulate syncing with server/cloud
            repository.syncAllRecords()
            _isSyncing.value = false
        }
    }

    fun addNewPond(name: String, fishType: String) {
        viewModelScope.launch {
            repository.addPond(name = name, fishType = fishType, status = "Aktif")
        }
    }

    fun deletePond(id: Long) {
        viewModelScope.launch {
            repository.deletePond(id)
        }
    }
}
