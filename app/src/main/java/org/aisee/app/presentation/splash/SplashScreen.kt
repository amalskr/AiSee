package org.aisee.app.presentation.splash

import android.content.Context
import android.content.Intent
import android.provider.Settings
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
import androidx.compose.runtime.DisposableEffect
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import kotlinx.coroutines.delay
import org.aisee.app.R

private val Purple = Color(0xFF9B87E8)

private fun isVoiceAccessEnabled(context: Context): Boolean {
    val enabledServices = Settings.Secure.getString(
        context.contentResolver,
        Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES
    ) ?: return false
    return enabledServices.contains("com.google.android.apps.accessibility.voiceaccess")
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    val context = LocalContext.current
    val textAlpha = remember { Animatable(0f) }
    val textOffset = remember { Animatable(12f) }
    var showVoiceAccessDialog by remember { mutableStateOf(false) }
    var splashReady by remember { mutableStateOf(false) }
    val lifecycleOwner = LocalLifecycleOwner.current

    // When user returns from settings, check if Voice Access is now enabled
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME && splashReady) {
                if (isVoiceAccessEnabled(context)) {
                    showVoiceAccessDialog = false
                    onFinished()
                }
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
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
        if (isVoiceAccessEnabled(context)) {
            onFinished()
        } else {
            splashReady = true
            showVoiceAccessDialog = true
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

    if (showVoiceAccessDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = {
                Text(
                    text = "Voice Access Required",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Text(
                    text = "AiSee requires Voice Access to be enabled for hands-free control. Please enable Voice Access in your device's Accessibility settings.",
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    context.startActivity(intent)
                }) {
                    Text("Open Settings", color = Purple)
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showVoiceAccessDialog = false
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
