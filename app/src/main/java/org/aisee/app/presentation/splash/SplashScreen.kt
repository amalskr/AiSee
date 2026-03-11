package org.aisee.app.presentation.splash

import android.content.Context
import android.content.Intent
import android.provider.Settings
import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.activity.ComponentActivity
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import kotlinx.coroutines.delay
import org.aisee.app.R

private val Purple = Color(0xFF9B87E8)

private const val TAG = "AiSee_Accessibility"

private fun getEnabledAccessibilityServices(context: Context): String {
    val services = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: ""
    Log.d(TAG, "Enabled accessibility services: $services")
    return services
}

private fun isTalkBackEnabled(context: Context): Boolean {
    val services = getEnabledAccessibilityServices(context)
    // Check all known TalkBack package names across different device manufacturers
    val enabled = services.contains("com.google.android.marvin.talkback") ||
            services.contains("com.samsung.android.accessibility.talkback") ||
            services.contains("com.google.android.accessibility.talkback")
    Log.d(TAG, "TalkBack enabled: $enabled")
    return enabled
}

private const val TALKBACK_MESSAGE = "AiSee requires TalkBack to be enabled. Please go to Accessibility settings and turn on TalkBack under the Screen reader section."

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val textAlpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(12f) }
    var showTalkBackDialog by remember { mutableStateOf(false) }
    var splashReady by remember { mutableStateOf(false) }
    val activity = context as ComponentActivity

    // When user returns from settings, re-check if TalkBack is now enabled
    DisposableEffect(activity) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && splashReady) {
                if (isTalkBackEnabled(context)) {
                    showTalkBackDialog = false
                    onFinished()
                }
            }
        }
        activity.lifecycle.addObserver(observer)
        onDispose { activity.lifecycle.removeObserver(observer) }
    }

    LaunchedEffect(Unit) {
        delay(300)
        textAlpha.animateTo(1f, animationSpec = tween(600))
    }

    LaunchedEffect(Unit) {
        delay(300)
        textOffset.animateTo(0f, animationSpec = tween(600))
    }

    LaunchedEffect(Unit) {
        delay(1500)
        if (isTalkBackEnabled(context)) {
            onFinished()
        } else {
            splashReady = true
            showTalkBackDialog = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.aisee_icon),
                contentDescription = "AiSee Logo",
                modifier = Modifier.size(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "AiSee",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.White,
                modifier = Modifier
                    .alpha(textAlpha.value)
                    .offset(y = textOffset.value.dp)
            )
        }

        if (!splashReady) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 48.dp)
                    .size(24.dp),
                strokeWidth = 2.dp,
                color = Color.White
            )
        }
    }

    if (showTalkBackDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Enable TalkBack",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = TALKBACK_MESSAGE,
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    context.startActivity(Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS))
                }) {
                    Text("Open Settings", color = Purple)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showTalkBackDialog = false
                    onFinished()
                }) {
                    Text("Skip", color = Color(0xFFCCCCCC))
                }
            },
            containerColor = Color(0xFF1E1E1E),
            titleContentColor = Color.White,
            textContentColor = Color(0xFFCCCCCC)
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun SplashScreenPreview() {
    SplashScreen(onFinished = {})
}
