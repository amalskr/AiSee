package org.aisee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import org.aisee.app.navigation.AiSeeNavHost
import org.aisee.app.presentation.theme.AiSeeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AiSeeTheme {
                AiSeeNavHost()
            }
        }
    }
}
