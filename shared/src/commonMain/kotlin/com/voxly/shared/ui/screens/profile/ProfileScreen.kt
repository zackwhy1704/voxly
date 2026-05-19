package com.voxly.shared.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.voxly.shared.ui.components.VoxlyBottomNav
import com.voxly.shared.ui.components.VoxlyTab
import com.voxly.shared.ui.screens.auth.LandingScreen
import com.voxly.shared.ui.theme.VoxlyColors

class ProfileScreen : Screen {
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow

        Box(modifier = Modifier.fillMaxSize().background(VoxlyColors.Background)) {
            Column(modifier = Modifier.fillMaxSize().padding(bottom = 68.dp)
                .verticalScroll(rememberScrollState())) {

                // Profile header
                Box(modifier = Modifier.fillMaxWidth().background(VoxlyColors.Surface)
                    .statusBarsPadding().padding(horizontal = 20.dp, vertical = 16.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(52.dp).clip(CircleShape)
                                .background(VoxlyColors.Coral), contentAlignment = Alignment.Center) {
                                Text("ST", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column {
                                Text("Sarah Tan", fontSize = 17.sp, fontWeight = FontWeight.SemiBold,
                                    color = VoxlyColors.TextPrimary)
                                Text("Senior Manager", fontSize = 12.sp, color = VoxlyColors.TextSecondary)
                            }
                        }
                        Text("Edit", fontSize = 14.sp, color = VoxlyColors.Coral,
                            modifier = Modifier.clickable { })
                    }
                }

                Spacer(Modifier.height(20.dp))

                // Settings sections
                SettingsSection(title = "Account") {
                    SettingsRow("Edit profile", "›")
                    SettingsRow("Change email", "›")
                    SettingsRow("Change password", "›")
                }

                SettingsSection(title = "Learning") {
                    SettingsRow("Daily goal", "10 min")
                    SettingsRow("Notification time", "9:00 AM")
                    SettingsRow("Reminder type", "Push")
                }

                SettingsSection(title = "Subscription") {
                    Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(VoxlyColors.Gold10).padding(12.dp)) {
                        Row(horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()) {
                            Text("Free plan", fontSize = 14.sp, color = VoxlyColors.Gold)
                            Text("Upgrade →", fontSize = 13.sp, color = VoxlyColors.Coral,
                                fontWeight = FontWeight.SemiBold)
                        }
                    }
                    SettingsRow("🇸🇬 SkillsFuture info", "›")
                }

                SettingsSection(title = "Language & Region") {
                    SettingsRow("App language", "English")
                    SettingsRow("Region", "Singapore")
                }

                SettingsSection(title = "About") {
                    SettingsRow("Privacy policy", "›")
                    SettingsRow("Terms of service", "›")
                    SettingsRow("Help & support", "›")
                    SettingsRow("Rate Voxly ⭐", "›")
                }

                Spacer(Modifier.height(16.dp))

                // Danger zone
                Box(modifier = Modifier.padding(horizontal = 20.dp).fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp)).background(VoxlyColors.SurfaceElevated)) {
                    Column {
                        Text("Log out", fontSize = 15.sp, color = VoxlyColors.Coral,
                            modifier = Modifier.fillMaxWidth().clickable {
                                navigator.replaceAll(LandingScreen())
                            }.padding(horizontal = 20.dp, vertical = 16.dp))
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(VoxlyColors.White06))
                        Text("Delete account", fontSize = 15.sp, color = VoxlyColors.Error,
                            modifier = Modifier.fillMaxWidth().clickable { }
                                .padding(horizontal = 20.dp, vertical = 16.dp))
                    }
                }

                Spacer(Modifier.height(24.dp))
            }

            VoxlyBottomNav(activeTab = VoxlyTab.PROFILE,
                onTabSelected = { if (it == VoxlyTab.HOME) navigator.pop() },
                modifier = Modifier.align(Alignment.BottomCenter))
        }
    }
}

@Composable
private fun SettingsSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Text(title, fontSize = 12.sp, color = VoxlyColors.TextTertiary,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(vertical = 8.dp))
        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
            .background(VoxlyColors.SurfaceElevated)) {
            Column { content() }
        }
        Spacer(Modifier.height(12.dp))
    }
}

@Composable
private fun SettingsRow(label: String, trailing: String, onClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
        .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Text(label, fontSize = 14.sp, color = VoxlyColors.TextPrimary)
        Text(trailing, fontSize = 14.sp, color = VoxlyColors.TextSecondary)
    }
}
