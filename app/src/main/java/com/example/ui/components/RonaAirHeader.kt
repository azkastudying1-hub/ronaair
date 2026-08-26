package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.ui.theme.AccentPurpleDark
import com.example.ui.theme.AccentPurpleLight
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.Error
import com.example.ui.theme.OutlineVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.StatusNormal
import com.example.ui.theme.SurfaceContainerHigh

const val RONAAIR_LOGO_URL = "https://lh3.googleusercontent.com/aida-public/AB6AXuDEPi52XL5tuvxkKV8H4FO8PpHIXXBaGxiqPFapD_AKyaFKXpz6SObIB4PnnSAvyo7o4e2A3W0ZP8_witkkmAB3vWTTz-qXaaQ2QchZbtmmjh80ofAkbCP8AzPdOcR9yX6CMSDDjM7Sg5eqVrx-rE5-z4tWeWb8iV9tl8aYooIEvUyhoslRJ-JRodBpP-bmLNWFNZt6Mim5okkiYiqZx_MKgnRUjez2hEx1CLNLs9MLmS7C7vc9R_XYDA"

@Composable
fun RonaAirHeader(
    isOffline: Boolean = true,
    onProfileClick: () -> Unit = {}
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surface
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .height(56.dp)
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Offline/Online Status Pill
                Row(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(SurfaceContainerHigh)
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .clip(CircleShape)
                            .background(if (isOffline) Error else StatusNormal)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isOffline) "OFFLINE" else "ONLINE",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Centered Brand Title
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(RONAAIR_LOGO_URL)
                            .crossfade(true)
                            .build(),
                        contentDescription = "RonaAir Logo",
                        modifier = Modifier.size(26.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "RonaAir",
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Clean Profile Avatar with gradient & outline
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(1.dp, OutlineVariant, CircleShape)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(Primary, AccentPurpleLight)
                            )
                        )
                        .clickable { onProfileClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Profile",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
        }
    }
}
