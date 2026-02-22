package org.aisee.app.navigation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.aisee.app.presentation.main.MainScreen
import org.aisee.app.presentation.splash.SplashScreen

@Composable
fun AiSeeNavHost() {
    val backStack = remember { mutableStateListOf<Any>(SplashRoute) }

    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = { key ->
            when (key) {
                SplashRoute -> NavEntry(key) {
                    SplashScreen(
                        onNavigateToMain = {
                            backStack[0] = MainRoute
                        }
                    )
                }
                MainRoute -> NavEntry(key) {
                    MainScreen()
                }
                else -> NavEntry(Unit) { Text("Unknown") }
            }
        }
    )
}
