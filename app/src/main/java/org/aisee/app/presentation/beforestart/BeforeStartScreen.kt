package org.aisee.app.presentation.beforestart

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import org.aisee.app.R

private val Purple = Color(0xFF9B87E8)
private val SubtextColor = Color(0xFFCCCCCC)

@Composable
fun BeforeStartScreen(onAgreeAndContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(80.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Before you start",
                    fontSize = 28.sp,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White
                )
                Image(
                    painter = painterResource(id = R.drawable.aisee_icon),
                    contentDescription = "AiSee Logo",
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            SectionBlock(
                subtitle = "About the app",
                content = "AiSee uses your camera to scan for bus numbers. It uses your location to identify your bus stop."
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionBlock(
                subtitle = "Your privacy",
                content = "All processing happens on your device. No photos, videos or location data are sent or shared."
            )

            Spacer(modifier = Modifier.height(24.dp))

            SectionBlock(
                subtitle = "Use with care",
                content = "Bus detection may not always be accurate. It can fail due to lighting, distance or movement. Please confirm the bus number with the driver before boarding."
            )

            Spacer(modifier = Modifier.height(24.dp))

            ContentText(
                text = "By continuing, you confirm you understand the app\u2019s limitations and agree to use it safely, staying aware of your surroundings."
            )

            Spacer(modifier = Modifier.height(32.dp))
        }

        Button(
            onClick = onAgreeAndContinue,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 24.dp)
                .height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Purple)
        ) {
            Text(
                text = "Agree and Continue",
                style = MaterialTheme.typography.labelLarge,
                color = Color.White
            )
        }
    }
}

@Composable
private fun SectionBlock(subtitle: String, content: String) {
    SubtitleText(text = subtitle)
    Spacer(modifier = Modifier.height(8.dp))
    ContentText(text = content)
}

@Composable
private fun SubtitleText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleLarge,
        fontSize = 20.sp,
        color = Color.White
    )
}

@Composable
private fun ContentText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        fontSize = 18.sp,
        color = SubtextColor
    )
}

@Preview(showBackground = true, device = "id:pixel_5")
@Composable
private fun BeforeStartScreenPreview() {
    BeforeStartScreen(onAgreeAndContinue = {})
}
