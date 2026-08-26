package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AssignmentLate
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.OfflinePin
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Sync
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.data.model.MonitoringRecord
import com.example.ui.MonitorStep
import com.example.ui.theme.Error
import com.example.ui.theme.ErrorContainer
import com.example.ui.theme.OnErrorContainer
import com.example.ui.theme.OnTertiaryContainer
import com.example.ui.theme.Primary
import com.example.ui.theme.Secondary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.StatusNormal
import com.example.ui.theme.StatusNormalText
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.StatusWarningBg
import com.example.ui.theme.StatusWarningText
import com.example.ui.theme.Tertiary
import com.example.ui.theme.TertiaryContainer

const val WATER_SAMPLE_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDVW9z_bAttzMjlJejpuSejFRw7aDmjv9hRPv_yqaJTJti-G8osgHskdWaEzErQgcwWPXvydPY0S-sJ9Zf_UMEJKc8pS92gbBmznqciOd89tfsDcTLZ7TfbIti5I2hvk7HME5HL0txS_HGNv3AaaUoIhyyXjr0srl8d1h8w261fKvNKOjt174eIdY9RHAivTlj1UEKCY62kiontoY0Z-MPW6aUwncqK5wGSrrrJGASmTDoqmSKVm11GBg"
const val VIEWFINDER_BG_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuA29plL2tCEmmU7VR2YRE9BzGeW2_Nba9N9carSZx1qG4bnMVtvx9fo580pXreobtBghNslGobEKg-iDprbhBirNCDUujxeY1w_Rr7PPbLG9inljrt1sKcSkzjNsNpEBy5tcoi4-PhdeUncgW2h_4jnYKUnxVKYXAfjZNm_YGvkfVFWfCMyamTw9-LRzDmpkn5ukJnNrv7QFeRHuv1rY7I9SRfT399oZLQehZ47lArLvToHTilPOfSWVA"
const val PREPARE_IMG_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDHBtIvyvCR10R5W1KlZJ_h-oJkEFfF8cvFE3jfOrlRzCq_UftFM6D_4wCMZkd-zGdalEA5iMUQCb-kr5Gw6qZEOeUxpXNNYEDzkCgscMDQQizAy18e2XOV-zeGTyul_oHazU_4wasACi0C1Wc0rhLvPVspcwtmEIQ4fpxVfBYdF7ADwfkTtMK7iAamICFN77kH3tHWiYhMrTnz8OjXR83wRgTjMeEXUnSZzq6VVS8I8Bc_alSbhVhhsg"
const val SENSOR_SCAN_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuBTKer2I7soUKkFDmSArLYNLMWaM17m4Qsgktrr4lo3hEhhXrzKWmyz1ZNv7Rm0CJd_FB6-z3AyhL_m4oDgZw6mSscmoLHmIHQZKSICDBiCPPz9q6A-R55QKwVqi4471G4C6FC0VifSLCHcnAv5vx1OSRIpLxoNd5_o5UiDPh_-pnJlTxRHcutgWUJNnpG8ZFNukN9UVpUL0taei6__KU5edLsNg2asEvcE-mETQzgoA6sqI3__okU7jA"

@Composable
fun MonitorFlowScreen(
    currentStep: MonitorStep,
    scanProgress: Int,
    latestRecord: MonitoringRecord?,
    onStartGuide: () -> Unit,
    onSkipGuide: () -> Unit,
    onProceedToCamera: () -> Unit,
    onCapturePhoto: () -> Unit,
    onRetakePhoto: () -> Unit,
    onProceedToAnalysis: () -> Unit,
    onViewRecommendations: () -> Unit,
    onFinish: () -> Unit
) {
    when (currentStep) {
        MonitorStep.GUIDE -> CaraMonitoringScreen(onStart = onStartGuide, onSkip = onSkipGuide)
        MonitorStep.PREPARE -> SiapkanPengambilanScreen(onContinue = onProceedToCamera)
        MonitorStep.VIEWFINDER -> CameraViewfinderScreen(onCapture = onCapturePhoto)
        MonitorStep.PHOTO_READY -> FotoSiapDianalisisScreen(onContinue = onProceedToAnalysis, onRetake = onRetakePhoto)
        MonitorStep.SCANNING -> ScanningScreen(scanProgress = scanProgress)
        MonitorStep.RESULT -> HasilMonitoringScreen(
            record = latestRecord,
            onViewActions = onViewRecommendations,
            onRetake = onRetakePhoto
        )
        MonitorStep.RECOMMENDATIONS -> RekomendasiScreen(onFinish = onFinish)
    }
}

// ---------------- 1. CARA MONITORING SCREEN ----------------
@Composable
fun CaraMonitoringScreen(
    onStart: () -> Unit,
    onSkip: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("cara_monitoring_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Icon Header
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.School,
                    contentDescription = "Education",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Cara Monitoring",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Monitoring pertama Anda akan dipandu langkah demi langkah.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Step List with Connecting Line
            val steps = listOf(
                Pair("01", Pair("Siapkan kolam", "Pastikan pencahayaan cukup dan permukaan air tenang.")),
                Pair("02", Pair("Ambil foto", "Arahkan kamera ke area yang representatif.")),
                Pair("03", Pair("Lihat hasil", "AI kami akan menganalisis kualitas air secara instan.")),
                Pair("04", Pair("Tentukan langkah", "Dapatkan rekomendasi tindakan yang tepat."))
            )

            steps.forEachIndexed { index, (num, content) ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    // Step Number Pill
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(if (index == 0) Primary else MaterialTheme.colorScheme.surfaceContainerHigh)
                            .border(
                                1.dp,
                                if (index == 0) Color.Transparent else MaterialTheme.colorScheme.outlineVariant,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = num,
                            style = MaterialTheme.typography.titleMedium,
                            color = if (index == 0) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = content.first,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = content.second,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            Button(
                onClick = onStart,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("start_guide_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Mulai Panduan", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(18.dp))
            }

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = onSkip,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("skip_guide_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Primary
                ),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text("Lewati", fontWeight = FontWeight.Bold, color = Primary)
            }
        }
    }
}

// ---------------- 2. SIAPKAN PENGAMBILAN SCREEN ----------------
@Composable
fun SiapkanPengambilanScreen(
    onContinue: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .testTag("siapkan_pengambilan_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Drag handle top pill
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Phone Illustration Container
            Box(
                modifier = Modifier
                    .size(240.dp)
                    .clip(CircleShape)
                    .background(Primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(180.dp)
                        .clip(CircleShape)
                        .background(Primary.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(PREPARE_IMG_URL)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Preparation scan guide",
                        modifier = Modifier
                            .size(150.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                }

                // Floating smartphone icon
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .offset(x = 10.dp)
                        .size(40.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Smartphone,
                        contentDescription = "Phone",
                        tint = Primary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Floating water drop icon
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .offset(x = 10.dp, y = (-10).dp)
                        .size(36.dp)
                        .shadow(4.dp, CircleShape)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WaterDrop,
                        contentDescription = "Water drop",
                        tint = Tertiary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Siapkan Pengambilan",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Tempatkan perangkat sesuai panduan. Pastikan permukaan air terlihat jelas tanpa pantulan matahari langsung.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Notice Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(ErrorContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Important Warning",
                            tint = OnErrorContainer,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Column {
                        Text(
                            text = "Penting",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Jaga jarak perangkat sekitar 30cm dari permukaan air untuk hasil optimal.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(top = 16.dp)
                .testTag("prepare_continue_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Lanjut", fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(8.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next", modifier = Modifier.size(18.dp))
        }
    }
}

// ---------------- 3. CAMERA VIEWFINDER SCREEN ----------------
@Composable
fun CameraViewfinderScreen(
    onCapture: () -> Unit
) {
    var isFlashVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("camera_viewfinder_screen")
    ) {
        // Camera Viewfinder Background Image
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(VIEWFINDER_BG_URL)
                .crossfade(true)
                .build(),
            contentDescription = "Camera Viewfinder",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Vignette Dark Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.25f))
        )

        // Top Status & Instructions Overlay
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Arahkan ke permukaan air",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = TertiaryContainer,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = "Valid Position",
                        tint = OnTertiaryContainer,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "POSISI BAIK",
                        style = MaterialTheme.typography.labelMedium,
                        color = OnTertiaryContainer,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Targeting Grid Frame with Brackets
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.Center)
                .border(2.dp, Color.White.copy(alpha = 0.5f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            // Center Crosshair
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Crosshair",
                tint = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.size(36.dp)
            )

            // Custom corner brackets
            Canvas(modifier = Modifier.fillMaxSize()) {
                val bracketLen = 30.dp.toPx()
                val bracketStroke = 4.dp.toPx()
                val color = Primary

                // Top Left
                drawLine(color, Offset(0f, 0f), Offset(bracketLen, 0f), bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(0f, 0f), Offset(0f, bracketLen), bracketStroke, cap = StrokeCap.Round)

                // Top Right
                drawLine(color, Offset(size.width, 0f), Offset(size.width - bracketLen, 0f), bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width, 0f), Offset(size.width, bracketLen), bracketStroke, cap = StrokeCap.Round)

                // Bottom Left
                drawLine(color, Offset(0f, size.height), Offset(bracketLen, size.height), bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(0f, size.height), Offset(0f, size.height - bracketLen), bracketStroke, cap = StrokeCap.Round)

                // Bottom Right
                drawLine(color, Offset(size.width, size.height), Offset(size.width - bracketLen, size.height), bracketStroke, cap = StrokeCap.Round)
                drawLine(color, Offset(size.width, size.height), Offset(size.width, size.height - bracketLen), bracketStroke, cap = StrokeCap.Round)
            }
        }

        // Bottom Capture Controls
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 32.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Gallery Button
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.25f))
                    .clickable { onCapture() },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.PhotoLibrary,
                    contentDescription = "Gallery",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Big Shutter Button
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .border(4.dp, Color.White, CircleShape)
                    .padding(5.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .clickable {
                        isFlashVisible = true
                        onCapture()
                    }
                    .testTag("shutter_button"),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(CircleShape)
                        .background(Color.White)
                )
            }

            // Spacer for symmetry
            Spacer(modifier = Modifier.size(48.dp))
        }

        // Shutter Flash Animation
        AnimatedVisibility(visible = isFlashVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )
        }
    }
}

// ---------------- 4. FOTO SIAP DIANALISIS SCREEN ----------------
@Composable
fun FotoSiapDianalisisScreen(
    onContinue: () -> Unit,
    onRetake: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 20.dp, vertical = 20.dp)
            .testTag("foto_siap_screen"),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(10.dp))

            // Big Check Icon
            Box(
                modifier = Modifier
                    .size(110.dp)
                    .shadow(4.dp, CircleShape)
                    .clip(CircleShape)
                    .background(TertiaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Ready Check",
                    tint = OnTertiaryContainer,
                    modifier = Modifier.size(64.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Foto Siap Dianalisis",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Kondisi gambar cukup baik untuk skrining. Anda dapat melanjutkan ke tahap berikutnya.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Water sample preview card
            Surface(
                modifier = Modifier
                    .size(280.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .border(1.dp, MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(16.dp)),
                shadowElevation = 2.dp
            ) {
                Box {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(WATER_SAMPLE_URL)
                            .crossfade(true)
                            .build(),
                        contentDescription = "Water Sample Validated",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )

                    // Valid chip top right
                    Surface(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(12.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.92f)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Valid",
                                tint = Tertiary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Valid",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
            Button(
                onClick = onContinue,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("analyze_continue_button"),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = "Lanjutkan",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedButton(
                onClick = onRetake,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("retake_photo_button"),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.Refresh, contentDescription = "Retake", tint = Primary, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Ambil Ulang Foto", style = MaterialTheme.typography.labelLarge, color = Primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// ---------------- 5. SCANNING / ANALYZING SCREEN ----------------
@Composable
fun ScanningScreen(scanProgress: Int) {
    val infiniteTransition = rememberInfiniteTransition(label = "scan_laser")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "laser"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .testTag("scanning_screen")
    ) {
        // Scanning image with animated laser
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .clip(RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp))
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(SENSOR_SCAN_URL)
                    .crossfade(true)
                    .build(),
                contentDescription = "Sensor Scanning",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )

            // Scanning Laser Line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(3.dp)
                    .offset(y = (240 * laserY).dp)
                    .background(Primary)
                    .shadow(12.dp, CircleShape, ambientColor = Primary, spotColor = Primary)
            )

            // Live tag chips
            Surface(
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(16.dp),
                shape = RoundedCornerShape(6.dp),
                color = Tertiary.copy(alpha = 0.85f)
            ) {
                Text(
                    text = "VIS: OK",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Surface(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(16.dp),
                shape = RoundedCornerShape(6.dp),
                color = Primary.copy(alpha = 0.85f)
            ) {
                Text(
                    text = "PM: EST",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
        }

        // Stepper Progress Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Menganalisis kondisi visual...",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Sistem memproses gambar untuk mendeteksi anomali partikel udara.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Checklist Steps Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    // Step 1
                    ProgressStepItem(
                        title = "Memeriksa kualitas gambar",
                        subtitle = "Resolusi dan pencahayaan optimal",
                        isCompleted = scanProgress >= 1,
                        isInProgress = scanProgress == 1
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 2
                    ProgressStepItem(
                        title = "Menganalisis perubahan visual",
                        subtitle = "Mendeteksi kekeruhan atmosfer",
                        isCompleted = scanProgress >= 2,
                        isInProgress = scanProgress == 2
                    )
                    Spacer(modifier = Modifier.height(14.dp))

                    // Step 3
                    ProgressStepItem(
                        title = "Menyusun tingkat risiko",
                        subtitle = if (scanProgress >= 3) "Estimasi risiko selesai." else "Menghitung probabilitas kontaminasi...",
                        isCompleted = scanProgress >= 3,
                        isInProgress = scanProgress == 3
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Offline Indicator Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(10.dp),
                color = SecondaryContainer
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.OfflinePin,
                        contentDescription = "Offline Secure",
                        tint = MaterialTheme.colorScheme.onSecondaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "Tidak memerlukan koneksi internet.",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Analisis utama dilakukan dengan aman di perangkat ini.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.85f),
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ProgressStepItem(
    title: String,
    subtitle: String,
    isCompleted: Boolean,
    isInProgress: Boolean
) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(
                    if (isCompleted) Primary
                    else MaterialTheme.colorScheme.surfaceContainerHighest
                ),
            contentAlignment = Alignment.Center
        ) {
            if (isCompleted) {
                Icon(Icons.Default.Check, contentDescription = "Done", tint = Color.White, modifier = Modifier.size(20.dp))
            } else if (isInProgress) {
                CircularProgressIndicator(
                    color = Primary,
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            } else {
                Icon(Icons.Default.Sync, contentDescription = "Waiting", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }
    }
}

// ---------------- 6. HASIL MONITORING SCREEN ----------------
@Composable
fun HasilMonitoringScreen(
    record: MonitoringRecord?,
    onViewActions: () -> Unit,
    onRetake: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .padding(horizontal = 16.dp, vertical = 14.dp)
            .testTag("hasil_monitoring_screen"),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column {
            Text(
                text = "Hasil Monitoring",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Analisis visual terbaru dari lokasi pemantauan.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Big Warning Amber Banner
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = StatusWarning,
            shadowElevation = 2.dp
        ) {
            Box(modifier = Modifier.padding(16.dp)) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Warning",
                            tint = MaterialTheme.colorScheme.onBackground,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = record?.status ?: "WASPADA",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = record?.statusMessage ?: "Ada perubahan visual yang perlu diperhatikan.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.9f),
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Detail Analisis Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceContainer
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Detail Analisis",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                AnalysisDetailItem(
                    icon = Icons.Default.WaterDrop,
                    text = "Warna air tampak lebih pekat dibanding pemantauan sebelumnya."
                )
                Spacer(modifier = Modifier.height(10.dp))

                AnalysisDetailItem(
                    icon = Icons.Default.TrendingUp,
                    text = "Terdapat perubahan signifikan dibanding baseline historis."
                )
                Spacer(modifier = Modifier.height(10.dp))

                AnalysisDetailItem(
                    icon = Icons.Default.AssignmentLate,
                    text = "Pemeriksaan ulang fisik sangat disarankan.",
                    isBold = true
                )
            }
        }

        // Keyakinan Model AI with radial gauge
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "KEYAKINAN MODEL AI",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "Sedang",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "(78%)",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Gauge Circle
                Box(
                    modifier = Modifier.size(56.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        drawArc(
                            color = Color(0xFFE0E3E5),
                            startAngle = -90f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                        drawArc(
                            color = Primary,
                            startAngle = -90f,
                            sweepAngle = 360f * 0.78f,
                            useCenter = false,
                            style = Stroke(width = 6.dp.toPx(), cap = StrokeCap.Round)
                        )
                    }
                }
            }
        }

        // Offline storage badge
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = SecondaryContainer
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.OfflinePin,
                    contentDescription = "Saved offline",
                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Hasil tersedia offline di perangkat.",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        // Buttons
        Button(
            onClick = onViewActions,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("see_actions_button"),
            colors = ButtonDefaults.buttonColors(containerColor = Primary),
            shape = RoundedCornerShape(10.dp)
        ) {
            Text("Lihat Tindakan", fontWeight = FontWeight.Bold)
        }

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable { onViewActions() }
                .testTag("see_sensor_detail_button"),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "Lihat Detail Sensor",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Disclaimer footnote
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Disclaimer",
                tint = MaterialTheme.colorScheme.outline,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "RonaAir merupakan alat skrining awal untuk mendeteksi anomali visual. Hasil ini bukan pengganti analisis sampel air di laboratorium resmi.",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.outline,
                fontSize = 11.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun AnalysisDetailItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    text: String,
    isBold: Boolean = false
) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal
        )
    }
}

// ---------------- 7. REKOMENDASI SCREEN ----------------
@Composable
fun RekomendasiScreen(
    onFinish: () -> Unit
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
            .verticalScroll(scrollState)
            .testTag("rekomendasi_screen")
    ) {
        // Warning Header Box
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp),
            color = MaterialTheme.colorScheme.surfaceContainer,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Recommendations",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Rekomendasi",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Status Waspada Pill
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = StatusWarningBg
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(StatusWarning)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "STATUS WASPADA",
                            style = MaterialTheme.typography.labelMedium,
                            color = StatusWarningText,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Kualitas air saat ini menunjukkan indikasi yang perlu diperhatikan. Berikut adalah langkah-langkah yang harus Anda ambil untuk menjaga kesehatan tambak.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 22.sp
                )
            }
        }

        // Action Timeline Items
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Action 1: Sekarang
            RecommendationItem(
                icon = Icons.Default.Timer,
                iconBg = Primary,
                title = "Sekarang",
                description = "Lakukan pemantauan ulang sesuai interval yang disarankan pada log sensor."
            )

            // Action 2: Periksa Fisik
            RecommendationItem(
                icon = Icons.Default.WaterDrop,
                iconBg = Secondary,
                title = "Periksa Fisik",
                description = "Pastikan kondisi air secara visual dan pergerakan ikan tetap normal di permukaan."
            )

            // Action 3: Perhatikan Gejala
            RecommendationItem(
                icon = Icons.Default.Warning,
                iconBg = Error,
                title = "Perhatikan Gejala",
                description = "Jika ikan menunjukkan gejala stres (mengapung, pasif), segera konsultasikan dengan penyuluh."
            )

            // Educational Tips Lapangan Box (Teal)
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = TertiaryContainer,
                shadowElevation = 2.dp
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Field Tips",
                        tint = OnTertiaryContainer,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Tips Lapangan",
                            style = MaterialTheme.typography.labelLarge,
                            color = OnTertiaryContainer,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Kondisi \"Waspada\" seringkali bersifat sementara akibat perubahan cuaca mendadak. Pantau ketat selama 6 jam ke depan.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = OnTertiaryContainer.copy(alpha = 0.9f),
                            fontSize = 13.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Action Buttons
            Button(
                onClick = onFinish,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("schedule_re_monitoring_button"),
                colors = ButtonDefaults.buttonColors(containerColor = Primary),
                shape = RoundedCornerShape(10.dp)
            ) {
                Icon(Icons.Default.CalendarMonth, contentDescription = "Calendar", modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Jadwalkan Monitoring Ulang", fontWeight = FontWeight.Bold)
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable { onFinish() }
                    .testTag("finish_recommendation_button"),
                color = MaterialTheme.colorScheme.surfaceContainer
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Analytics, contentDescription = "Analytics", tint = MaterialTheme.colorScheme.onSurface, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Lihat Detail Sensor",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun RecommendationItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconBg: Color,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .shadow(2.dp, CircleShape)
                .clip(CircleShape)
                .background(iconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Color.White,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Surface(
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(12.dp),
            color = MaterialTheme.colorScheme.surfaceContainerLow,
            shadowElevation = 1.dp
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
