package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Water
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.MonitoringRecord
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.OnPrimaryContainer
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.StatusNormal
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.StatusWarningText
import com.example.ui.theme.SurfaceContainerHigh

@Composable
fun HomeScreen(
    isOffline: Boolean,
    unsyncedCount: Int,
    isSyncing: Boolean,
    latestRecord: MonitoringRecord?,
    onStartMonitoring: () -> Unit,
    onSyncClick: () -> Unit,
    onViewGuide: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .testTag("home_screen"),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Welcome Header - Clean Minimalism Style
        Column(modifier = Modifier.padding(top = 4.dp)) {
            Text(
                text = "Kamis, 24 Oktober",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Selamat pagi, Pak Budi",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Water,
                    contentDescription = "Pond",
                    tint = Primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Kolam Utama • Budidaya Ikan Nila",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Mode Offline Banner - Pill Container
        if (isOffline) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = SurfaceContainerHigh
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CloudOff,
                            contentDescription = "Cloud Off",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Mode Offline Aktif",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Semua hasil monitoring tersimpan aman di perangkat",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Clean Minimalism 2-Grid Quick Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Card 1: Monitoring Cepat (PrimaryContainer / Lavender)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { onStartMonitoring() },
                shape = RoundedCornerShape(24.dp),
                color = PrimaryContainer
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(118.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(OnPrimaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bolt,
                            contentDescription = "Quick Action",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Cek Kualitas Air",
                            style = MaterialTheme.typography.labelLarge,
                            color = OnPrimaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Ambil sampel foto",
                            style = MaterialTheme.typography.labelMedium,
                            color = OnPrimaryContainer.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // Card 2: Sinkronisasi Data (AccentPurpleLight / Soft Purple)
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(24.dp))
                    .clickable { if (unsyncedCount > 0 && !isSyncing) onSyncClick() },
                shape = RoundedCornerShape(24.dp),
                color = AccentPurpleLight
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .height(118.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(AccentPurpleDark),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isSyncing) {
                            CircularProgressIndicator(
                                color = Color.White,
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.Sync,
                                contentDescription = "Sync Data",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "Sinkronisasi",
                            style = MaterialTheme.typography.labelLarge,
                            color = AccentPurpleDark,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (unsyncedCount > 0) "$unsyncedCount siap unggah" else "Semua sinkron",
                            style = MaterialTheme.typography.labelMedium,
                            color = AccentPurpleDark.copy(alpha = 0.75f),
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // Section Title: STATUS KOLAM
        Text(
            text = "KONDISI TERKINI",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )

        // Status Kolam Card - Minimalist Clean Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            shadowElevation = 0.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(StatusWarningBg),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Warning,
                                contentDescription = "Warning",
                                tint = StatusWarningText,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = latestRecord?.status ?: "WASPADA",
                                style = MaterialTheme.typography.titleMedium,
                                color = StatusWarningText,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Skor Indeks AQI: ${latestRecord?.aqiScore ?: 112}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Surface(
                        shape = CircleShape,
                        color = StatusWarningBg
                    ) {
                        Text(
                            text = "Perlu Pantau",
                            color = StatusWarningText,
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = latestRecord?.statusMessage
                        ?: "Ada perubahan visual kejernihan air yang perlu diperhatikan pada sampel kolam.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Time",
                            tint = MaterialTheme.colorScheme.outline,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Terakhir dipantau",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }

                    Text(
                        text = "Hari ini, 07.45 WIB",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // Monitoring Baru Button (Clean Minimalist Floating Pill)
        Button(
            onClick = onStartMonitoring,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("new_monitoring_button"),
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AddAPhoto,
                contentDescription = "New Monitoring",
                tint = Color.White,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Mulai Monitoring Baru",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        // Section Title: TREND 7 HARI
        Text(
            text = "RIWAYAT & TREN",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )

        // Trend 7 Hari Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLowest,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle),
            shadowElevation = 0.dp
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Tren Kualitas Air",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "7 Hari Terakhir",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive trend chart
                TrendChart(modifier = Modifier.fillMaxWidth().height(110.dp))
            }
        }

        // Educational Callout Banner (Clean Lavender Theme)
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = SecondaryContainer,
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(OnSecondaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Education",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = "Panduan Sampling RonaAir",
                            style = MaterialTheme.typography.labelLarge,
                            color = OnSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Pelajari teknik foto sampel air yang presisi.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnSecondaryContainer.copy(alpha = 0.8f),
                            fontSize = 13.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = onViewGuide,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("view_guide_button"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OnSecondaryContainer,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MenuBook,
                        contentDescription = "Guide",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lihat Panduan Lengkap",
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun TrendChart(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height - 24f

            val p1 = Offset(width * 0.12f, height * 0.85f)
            val p2 = Offset(width * 0.38f, height * 0.85f)
            val p3 = Offset(width * 0.65f, height * 0.40f)
            val p4 = Offset(width * 0.90f, height * 0.40f)

            // Gradient Area Under Curve
            val fillPath = Path().apply {
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                lineTo(p3.x, p3.y)
                lineTo(p4.x, p4.y)
                lineTo(p4.x, height + 8f)
                lineTo(p1.x, height + 8f)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Primary.copy(alpha = 0.20f),
                        Primary.copy(alpha = 0.02f)
                    ),
                    startY = p3.y,
                    endY = height + 8f
                )
            )

            // Trend line
            val strokePath = Path().apply {
                moveTo(p1.x, p1.y)
                lineTo(p2.x, p2.y)
                lineTo(p3.x, p3.y)
                lineTo(p4.x, p4.y)
            }

            drawPath(
                path = strokePath,
                color = Primary,
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Draw Dots
            // 15/4 - Green
            drawCircle(Color.White, radius = 6.dp.toPx(), center = p1)
            drawCircle(StatusNormal, radius = 4.dp.toPx(), center = p1)

            // 17/4 - Green
            drawCircle(Color.White, radius = 6.dp.toPx(), center = p2)
            drawCircle(StatusNormal, radius = 4.dp.toPx(), center = p2)

            // 19/4 - Yellow
            drawCircle(Color.White, radius = 6.dp.toPx(), center = p3)
            drawCircle(StatusWarning, radius = 4.dp.toPx(), center = p3)

            // Hr Ini - Yellow with subtle ring
            drawCircle(Primary.copy(alpha = 0.25f), radius = 8.dp.toPx(), center = p4)
            drawCircle(Color.White, radius = 6.dp.toPx(), center = p4)
            drawCircle(StatusWarning, radius = 4.dp.toPx(), center = p4)
        }

        // Bottom Date Labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("15/4", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Text("17/4", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Text("19/4", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 11.sp)
            Text("Hr Ini", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Bold, fontSize = 11.sp)
        }
    }
}
