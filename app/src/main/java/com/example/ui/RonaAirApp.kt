package com.example.ui

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.RonaAirBottomNav
import com.example.ui.components.RonaAirHeader
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.MonitorFlowScreen
import com.example.ui.screens.OnboardingScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SplashScreen

@Composable
fun RonaAirApp(
    viewModel: RonaAirViewModel = viewModel()
) {
    val appScreen by viewModel.appScreen.collectAsState()
    val currentTab by viewModel.currentTab.collectAsState()
    val monitorStep by viewModel.monitorStep.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val isSyncing by viewModel.isSyncing.collectAsState()
    val historyFilter by viewModel.historyFilter.collectAsState()
    val scanProgress by viewModel.scanProgressStep.collectAsState()
    val latestRecord by viewModel.latestRecord.collectAsState()
    val unsyncedRecords by viewModel.unsyncedRecords.collectAsState()
    val allRecords by viewModel.allRecords.collectAsState()
    val allPonds by viewModel.allPonds.collectAsState()

    Crossfade(targetState = appScreen, label = "appScreenCrossfade") { screen ->
        when (screen) {
            AppScreen.SPLASH -> {
                SplashScreen(
                    onSplashFinished = { viewModel.finishOnboarding() }
                )
            }
            AppScreen.ONBOARDING -> {
                OnboardingScreen(
                    onContinue = { viewModel.finishOnboarding() }
                )
            }
            AppScreen.MAIN_APP -> {
                Scaffold(
                    topBar = {
                        // Don't show header if in camera viewfinder
                        if (currentTab != NavigationTab.MONITOR || monitorStep != MonitorStep.VIEWFINDER) {
                            RonaAirHeader(
                                isOffline = isOffline,
                                onProfileClick = { viewModel.selectTab(NavigationTab.PROFIL) }
                            )
                        }
                    },
                    bottomBar = {
                        // Don't show bottom nav in camera viewfinder or scanning
                        if (currentTab != NavigationTab.MONITOR || (monitorStep != MonitorStep.VIEWFINDER && monitorStep != MonitorStep.SCANNING)) {
                            RonaAirBottomNav(
                                currentTab = currentTab,
                                onTabSelected = { viewModel.selectTab(it) }
                            )
                        }
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                    ) {
                        when (currentTab) {
                            NavigationTab.BERANDA -> {
                                HomeScreen(
                                    isOffline = isOffline,
                                    unsyncedCount = unsyncedRecords.size,
                                    isSyncing = isSyncing,
                                    latestRecord = latestRecord,
                                    onStartMonitoring = { viewModel.startMonitoringFromHome() },
                                    onSyncClick = { viewModel.syncData() },
                                    onViewGuide = { viewModel.startMonitoringFromHome() }
                                )
                            }
                            NavigationTab.MONITOR -> {
                                MonitorFlowScreen(
                                    currentStep = monitorStep,
                                    scanProgress = scanProgress,
                                    latestRecord = latestRecord,
                                    onStartGuide = { viewModel.startGuide() },
                                    onSkipGuide = { viewModel.skipGuide() },
                                    onProceedToCamera = { viewModel.proceedToCamera() },
                                    onCapturePhoto = { viewModel.capturePhoto() },
                                    onRetakePhoto = { viewModel.retakePhoto() },
                                    onProceedToAnalysis = { viewModel.proceedToAnalysis() },
                                    onViewRecommendations = { viewModel.viewRecommendations() },
                                    onFinish = { viewModel.finishMonitoring() }
                                )
                            }
                            NavigationTab.RIWAYAT -> {
                                HistoryScreen(
                                    records = allRecords,
                                    selectedFilter = historyFilter,
                                    onFilterChange = { viewModel.setHistoryFilter(it) }
                                )
                            }
                            NavigationTab.PROFIL -> {
                                ProfileScreen(
                                    ponds = allPonds,
                                    isOffline = isOffline,
                                    onToggleOffline = { viewModel.setOfflineMode(it) },
                                    onSyncNow = { viewModel.syncData() },
                                    onAddPond = { name, fish -> viewModel.addNewPond(name, fish) },
                                    onDeletePond = { viewModel.deletePond(it) },
                                    onViewGuide = { viewModel.startMonitoringFromHome() }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
