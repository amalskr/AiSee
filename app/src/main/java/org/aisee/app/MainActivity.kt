package org.aisee.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import kotlinx.coroutines.launch
import org.aisee.app.core.common.InAppReviewManager
import org.aisee.app.core.common.InAppUpdateManager
import org.aisee.app.core.data.UserPreferences
import org.aisee.app.navigation.AiSeeNavHost
import org.aisee.app.presentation.theme.AiSeeTheme

class MainActivity : ComponentActivity() {

    private lateinit var inAppUpdateManager: InAppUpdateManager

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Must be created before setContent (registers ActivityResultLauncher)
        inAppUpdateManager = InAppUpdateManager(this)

        // In-app review: show on 3rd app open after signup
        val userPreferences = UserPreferences(this)
        InAppReviewManager(this, userPreferences).tryRequestReview()

        setContent {
            AiSeeTheme {
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Box(modifier = Modifier.fillMaxSize()) {
                    AiSeeNavHost()

                    SnackbarHost(
                        hostState = snackbarHostState,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp)
                    ) { data ->
                        Snackbar(
                            snackbarData = data,
                            containerColor = Color(0xFF1E1E1E),
                            contentColor = Color.White,
                            actionColor = Color(0xFF9B87E8)
                        )
                    }
                }

                // Check for updates after UI is set up
                inAppUpdateManager.checkForUpdate {
                    scope.launch {
                        val result = snackbarHostState.showSnackbar(
                            message = "Update downloaded",
                            actionLabel = "Restart",
                            duration = SnackbarDuration.Indefinite
                        )
                        if (result == SnackbarResult.ActionPerformed) {
                            inAppUpdateManager.completeFlexibleUpdate()
                        }
                    }
                }
            }
        }
    }
}
