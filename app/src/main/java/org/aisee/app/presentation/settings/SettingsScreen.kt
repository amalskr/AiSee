package org.aisee.app.presentation.settings

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.OpenInNew
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview

private val CardBackground = Color(0xFF1A1A1A)
private val DividerColor = Color(0xFF2A2A2A)
private val SubtextColor = Color(0xFF8A8A8A)
private val SignOutRed = Color(0xFFE85C5C)

@Composable
fun SettingsScreen(
    fullName: String,
    username: String,
    userEmail: String,
    phoneNumber: String,
    onTermsOfUse: () -> Unit,
    onCheckForUpdates: () -> Unit,
    onSignOut: () -> Unit,
    onClose: () -> Unit
) {
    val context = LocalContext.current
    val versionName = remember { getVersionName(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        // Close button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            IconButton(
                onClick = onClose,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF3A3A3A))
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        // Title
        Text(
            text = "Settings",
            style = MaterialTheme.typography.displaySmall,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Card
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(CardBackground, RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 4.dp)
        ) {
            SettingsInfoItem(label = "Full Name", value = fullName)
            SettingsDivider()
            SettingsInfoItem(label = "Username", value = username)
            SettingsDivider()
            SettingsInfoItem(label = "Email", value = userEmail)
            SettingsDivider()
            SettingsInfoItem(label = "Phone Number", value = phoneNumber)
            SettingsDivider()
            SettingsInfoItem(label = "Version", value = versionName)
            SettingsDivider()
            SettingsActionItem(
                label = "Terms of Use",
                subtitle = "View our terms and conditions",
                icon = Icons.Outlined.OpenInNew,
                onClick = onTermsOfUse
            )
            SettingsDivider()
            SettingsActionItem(
                label = "Check for Updates",
                subtitle = "Visit App Store",
                icon = Icons.Outlined.Upload,
                onClick = onCheckForUpdates
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        // Sign Out button
        Button(
            onClick = onSignOut,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = SignOutRed)
        ) {
            Text(
                text = "Sign Out",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun SettingsInfoItem(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleSmall,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            color = SubtextColor
        )
    }
}

@Composable
private fun SettingsActionItem(
    label: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = SubtextColor
            )
        }
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = SubtextColor,
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
private fun SettingsDivider() {
    HorizontalDivider(color = DividerColor, thickness = 1.dp)
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun SettingsScreenPreview() {
    SettingsScreen(
        fullName = "John Doe",
        username = "johndoe",
        userEmail = "john@example.com",
        phoneNumber = "+1234567890",
        onTermsOfUse = {},
        onCheckForUpdates = {},
        onSignOut = {},
        onClose = {}
    )
}

private fun getVersionName(context: Context): String {
    return try {
        val pInfo = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.packageManager.getPackageInfo(context.packageName, PackageManager.PackageInfoFlags.of(0))
        } else {
            @Suppress("DEPRECATION")
            context.packageManager.getPackageInfo(context.packageName, 0)
        }
        val versionCode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode
        } else {
            @Suppress("DEPRECATION")
            pInfo.versionCode.toLong()
        }
        "${pInfo.versionName} ($versionCode)"
    } catch (_: Exception) {
        "1.0"
    }
}
