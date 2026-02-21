package org.aisee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import org.aisee.app.ui.theme.AiSeeTheme

data object SplashRoute
data object MainRoute

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiSeeTheme {
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
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Hello AiSee!",
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
}
