package com.example.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.components.RONAAIR_LOGO_URL
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.PrimaryContainer
import com.example.ui.theme.SecondaryContainer

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 2200, easing = FastOutSlowInEasing)
        )
        onSplashFinished()
    }

    // Ripple Infinite Transition
    val infiniteTransition = rememberInfiniteTransition(label = "ripple")
    val ripple1 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "r1"
    )
    val ripple2 by infiniteTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 2.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, delayMillis = 1300, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "r2"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .clickable { onSplashFinished() }
            .testTag("splash_screen"),
        contentAlignment = Alignment.Center
    ) {
        // Water Ripple Background
        Canvas(modifier = Modifier.fillMaxSize()) {
            val centerOffset = center
            val baseRadius = size.minDimension * 0.25f

            // Ripple 1
            val alpha1 = (1f - (ripple1 / 2.2f)).coerceIn(0f, 0.12f)
            drawCircle(
                color = Primary.copy(alpha = alpha1),
                radius = baseRadius * ripple1,
                center = centerOffset
            )

            // Ripple 2
            val alpha2 = (1f - (ripple2 / 2.2f)).coerceIn(0f, 0.08f)
            drawCircle(
                color = SecondaryContainer.copy(alpha = alpha2),
                radius = baseRadius * ripple2,
                center = centerOffset
            )
        }

        // Center Branding Content - Clean Minimalism
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(24.dp)
        ) {
            // Circular Logo Box
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .border(1.dp, BorderSubtle, CircleShape)
                    .background(PrimaryContainer)
                    .padding(8.dp),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(RONAAIR_LOGO_URL)
                        .crossfade(true)
                        .build(),
                    contentDescription = "RonaAir Brand Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "RONAAIR",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp,
                fontSize = 36.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "See the Change. Act Earlier.",
                style = MaterialTheme.typography.titleMedium,
                color = Primary,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Early risk screening for freshwater ponds",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(36.dp))

            // Progress Bar - Minimalist Track
            Box(
                modifier = Modifier
                    .width(48.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
            ) {
                Box(
                    modifier = Modifier
                        .width(48.dp * progress.value)
                        .height(4.dp)
                        .clip(RoundedCornerShape(2.dp))
                        .background(Primary)
                )
            }
        }
    }
}
