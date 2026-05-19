package com.voxly.shared.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.voxly.shared.ui.theme.VoxlyColors

enum class VoxlyTab(val label: String, val icon: String) {
    HOME("Home",     "⌂"),
    LEARN("Learn",   "📖"),
    PRACTICE("Practice", "🎙"),
    PROGRESS("Progress", "📊"),
    PROFILE("Profile",   "👤"),
}

@Composable
fun VoxlyBottomNav(
    activeTab: VoxlyTab,
    onTabSelected: (VoxlyTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .background(VoxlyColors.Surface)
            .navigationBarsPadding(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VoxlyTab.entries.forEach { tab ->
            NavTabItem(
                tab = tab,
                isActive = tab == activeTab,
                onTap = { onTabSelected(tab) },
            )
        }
    }
}

@Composable
private fun NavTabItem(
    tab: VoxlyTab,
    isActive: Boolean,
    onTap: () -> Unit,
) {
    val iconColor by animateColorAsState(
        if (isActive) VoxlyColors.Coral else VoxlyColors.TextTertiary,
        animationSpec = tween(200),
        label = "tabColor",
    )

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onTap)
            .padding(horizontal = 12.dp, vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        if (isActive) {
            Box(
                modifier = Modifier
                    .width(56.dp)
                    .height(30.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(VoxlyColors.Coral.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center,
            ) {
                Text(text = tab.icon, fontSize = 18.sp, color = iconColor)
            }
        } else {
            Text(
                text = tab.icon,
                fontSize = 18.sp,
                color = iconColor,
                modifier = Modifier
                    .width(56.dp)
                    .height(30.dp)
                    .wrapContentSize(Alignment.Center),
            )
        }
        Text(
            text = tab.label,
            fontSize = 10.sp,
            fontWeight = if (isActive) FontWeight.SemiBold else FontWeight.Normal,
            color = iconColor,
        )
    }
}
