package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.NavigationTab
import com.example.ui.theme.BorderSubtle
import com.example.ui.theme.OnSecondaryContainer
import com.example.ui.theme.OnSurfaceVariant
import com.example.ui.theme.Primary
import com.example.ui.theme.SecondaryContainer
import com.example.ui.theme.SurfaceContainer

data class NavItem(
    val tab: NavigationTab,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val NAV_ITEMS = listOf(
    NavItem(NavigationTab.BERANDA, "Beranda", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem(NavigationTab.MONITOR, "Monitor", Icons.Filled.AddAPhoto, Icons.Outlined.AddAPhoto),
    NavItem(NavigationTab.RIWAYAT, "Riwayat", Icons.Filled.History, Icons.Outlined.History),
    NavItem(NavigationTab.PROFIL, "Profil", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
)

@Composable
fun RonaAirBottomNav(
    currentTab: NavigationTab,
    onTabSelected: (NavigationTab) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = SurfaceContainer
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            HorizontalDivider(thickness = 1.dp, color = BorderSubtle)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                NAV_ITEMS.forEach { item ->
                    val isSelected = currentTab == item.tab
                    val interactionSource = remember { MutableInteractionSource() }

                    Column(
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .clip(RoundedCornerShape(16.dp))
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) { onTabSelected(item.tab) }
                            .padding(vertical = 4.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        // Pill container for active item
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(
                                    if (isSelected) SecondaryContainer else androidx.compose.ui.graphics.Color.Transparent
                                )
                                .padding(horizontal = 20.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label,
                                tint = if (isSelected) OnSecondaryContainer else OnSurfaceVariant.copy(alpha = 0.8f),
                                modifier = Modifier.size(22.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = item.label,
                            style = MaterialTheme.typography.labelMedium,
                            color = if (isSelected) OnSecondaryContainer else OnSurfaceVariant.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}
