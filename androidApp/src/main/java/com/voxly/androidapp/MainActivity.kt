package com.voxly.androidapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.SlideTransition
import com.voxly.shared.ui.screens.auth.SplashScreen
import com.voxly.shared.ui.theme.VoxlyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            VoxlyTheme {
                Navigator(SplashScreen()) { navigator ->
                    SlideTransition(navigator)
                }
            }
        }
    }
}
