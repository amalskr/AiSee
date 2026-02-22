package org.aisee.app.presentation.terms

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.aisee.app.R

private val Purple = Color(0xFF9B87E8)
private val SubtextColor = Color(0xFFCCCCCC)
private val DisabledGray = Color(0xFF3A3A3A)
private val DisabledTextGray = Color(0xFF6A6A6A)
private val TrackOff = Color(0xFF3A3A3A)

@Composable
fun PrivacyAndTermsScreen(onNext: () -> Unit) {
    var agreed by remember { mutableStateOf(false) }
    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()

    val isAtBottom by remember {
        derivedStateOf {
            scrollState.value >= scrollState.maxValue - 10
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Fixed title bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 32.dp, end = 32.dp, top = 48.dp, bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Privacy and Terms",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Image(
                painter = painterResource(id = R.drawable.aisee_icon),
                contentDescription = "AiSee Logo",
                modifier = Modifier.size(40.dp)
            )
        }

        // Scrollable content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Quick overview",
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Safety
            Text(
                text = "Safety",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Do not use AiSee to cross streets, as a substitute mobility aid, or for medical or emergency advice. Do not share sensitive personal or financial info.",
                fontSize = 14.sp,
                color = SubtextColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Data collection and use
            Text(
                text = "Data collection and use",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "We collect your account and device info, photos, videos, audio, cookies, and analytics. Third-party AI providers may process this data for visual assistance.",
                fontSize = 14.sp,
                color = SubtextColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "We use this data to provide you with our Services, and improve them. We do not sell your data to third parties.",
                fontSize = 14.sp,
                color = SubtextColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Your rights
            Text(
                text = "Your rights",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "You can access, correct, or delete your personal data at any time through your account settings. You may also request a copy of the data we hold about you.",
                fontSize = 14.sp,
                color = SubtextColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Changes to terms
            Text(
                text = "Changes to terms",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "We may update these terms from time to time. If we make significant changes, we will notify you through the app or via email before the changes take effect.",
                fontSize = 14.sp,
                color = SubtextColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "This is not a legally binding overview. Please read the Terms of Service linked below to understand the rules and responsibilities that apply when using AiSee.",
                fontSize = 14.sp,
                color = SubtextColor,
                lineHeight = 21.sp
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Bottom section
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .padding(bottom = 24.dp)
        ) {
            // Read Full Terms of Service button
            val readEnabled = !isAtBottom
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .border(
                        width = 1.5.dp,
                        color = if (readEnabled) Purple else DisabledGray,
                        shape = RoundedCornerShape(26.dp)
                    )
                    .clip(RoundedCornerShape(26.dp))
                    .then(
                        if (readEnabled) {
                            Modifier.clickable {
                                coroutineScope.launch {
                                    scrollState.animateScrollTo(scrollState.maxValue)
                                }
                            }
                        } else {
                            Modifier
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Read Full Terms of Service",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (readEnabled) Purple else DisabledTextGray
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Agree toggle row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "I agree to the Terms of Service",
                    fontSize = 14.sp,
                    color = Color.White
                )
                TermsSwitch(
                    checked = agreed,
                    onCheckedChange = { agreed = it }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Next button
            Button(
                onClick = onNext,
                enabled = agreed,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(26.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Purple,
                    disabledContainerColor = DisabledGray,
                    disabledContentColor = DisabledTextGray
                )
            ) {
                Text(
                    text = "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun TermsSwitch(checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    val trackColor by animateColorAsState(
        targetValue = if (checked) Purple else TrackOff,
        label = "trackColor"
    )

    Box(
        modifier = Modifier
            .width(52.dp)
            .height(28.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(trackColor)
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 3.dp),
        contentAlignment = if (checked) Alignment.CenterEnd else Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .size(22.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (checked) {
                Text(
                    text = "I",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Purple,
                    modifier = Modifier.offset(y = (-1).dp)
                )
            }
        }
    }
}
